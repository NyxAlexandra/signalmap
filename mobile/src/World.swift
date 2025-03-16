import SwiftUI
import ARKit
import RealityKit
import FocusEntity

class World: Observable {
    fileprivate let inner = ARView()
    fileprivate var focus: FocusEntity?
    
    /// The current location of the camera in the scene.
    @Published
    var location: Transform
    /// The transform of the location the camera is pointed at.
    @Published
    var target: Transform?
    
    init() {
        location = inner.cameraTransform
        
        let overlay = ARCoachingOverlayView()
        
        overlay.autoresizingMask = [.flexibleWidth, .flexibleHeight]
        overlay.session = inner.session
        overlay.goal = .horizontalPlane
        
        inner.addSubview(overlay)
    }
    
    /// Spawns a node in the world where the camera is focused. Returns the spawned entity.
    ///
    /// Returns a null value if the value failed to be placed.
    func create(_ at: Transform? = nil) -> Entity? {
        guard let transform = focus?.transform else {
            return nil
        }
        
        let anchor = AnchorEntity()
        let entity = ModelEntity(
            mesh: MeshResource.generateSphere(radius: 0.03),
            materials: [UnlitMaterial(color: .white)]
        )
        
        entity.position = transform.translation
        entity.scale = transform.scale
        entity.transform.rotation = transform.rotation
        
        inner.scene.addAnchor(anchor)
        anchor.addChild(entity)
        
        return entity
    }
    
    /// Removes an entity from the world.
    func destroy(_ entity: Entity) -> ()? {
        entity.parent?.removeFromParent()
    }
    
}

/// A series of connected entities that form an area.
struct Mesh: Observable {
    private let world: World
    
    private let anchor = AnchorEntity()
    private var points: [Entity] = []
    private var edges: [Entity] = []
    
    /// The amount of points in the mesh.
    public var count: Int { points.count }
    /// `true` if the mesh contains no points.
    public var isEmpty: Bool { count == 0 }
    /// `true` if the mesh forms a 2D area.
    public var formsArea: Bool { count >= 3 }
    
    /// Creates a new empty mesh.
    init(_ world: World) {
        self.world = world
        
        world.inner.scene.addAnchor(anchor)
    }
    
    /// Adds a new point to the mesh.
    mutating func append(_ transform: Transform) -> ()? {
        guard let end = world.create(transform) else { return nil }
        
        if let start = points.last {
            let edge = makeEdge(start, end)
            
            edges.append(edge)
        }
    
        anchor.addChild(end)
        points.append(end)
        
        return ()
    }
    
    private func makeEdge(_ start: Entity, _ end: Entity) -> Entity {
        let midPoint = (start.position + end.position) / 2
        let direction = start.position - end.position
        let distance = simd_distance(start.position, end.position)
        let rotation = simd_quatf(from: [0, 1, 0], to: normalize(direction))
        
        let edge = ModelEntity(
            mesh: .generateCylinder(height: distance, radius: 0.01),
            materials: [UnlitMaterial(color: .white)]
        )
        
        edge.transform = Transform(
            rotation: rotation,
            translation: midPoint
        )
        anchor.addChild(edge)
        
        return edge
    }
    
    /// Removes all points from the mesh.
    mutating func removeAll() {
        for point in points {
            anchor.removeChild(point)
        }
        
        for edge in edges {
            anchor.removeChild(edge)
        }
        
        points.removeAll()
        edges.removeAll()
        
        
    }
}

/// A view that displays a world.
struct WorldView: UIViewRepresentable {
    @State
    private var world: World
    
    init(_ world: World) {
        self.world = world
    }
    
    func makeUIView(context: Context) -> some UIView {
        context.coordinator.world = self.world
         
        let config = ARWorldTrackingConfiguration()
        
        config.planeDetection = [.horizontal, .vertical]
        
        world.inner.session.delegate = context.coordinator
        world.inner.session.run(config)
        
        return world.inner
    }
    
    func updateUIView(_ uiView: UIViewType, context: Context) {}
    
    func makeCoordinator() -> Coordinator {
        Coordinator(world)
    }
    
    class Coordinator: NSObject, ARSessionDelegate {
        weak var world: World?
        
        init(_ world: World) {
            self.world = world
        }
        
        func makeFocus(_ world: World) -> FocusEntity {
            return FocusEntity(
                on: world.inner,
                style: .classic(color: .white)
            )
        }
        
        // MARK: - ARSessionDelegate
        
        func session(_ session: ARSession, didUpdate frame: ARFrame) {
            guard let world else { return }
            let focus = world.focus ?? makeFocus(world)
            
            world.focus = focus
            
            world.location = Transform(matrix: frame.camera.transform)
            world.target = focus.transform
        }
    }
}
