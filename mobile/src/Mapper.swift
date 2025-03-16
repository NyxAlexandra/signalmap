import SwiftUI
import ARKit
import RealityKit

struct Mapper: View {
    /// The current mode.
    @State
    private var mode: Mode
    
    /// Manages camera and world state.
    @State
    private var world: World
    /// The mesh of the current area.
    @State
    private var mesh: Mesh
    /// The measured readings.
    @State
    private var readings: [Reading]
    
    private enum Mode {
        case mapping
        case reading
    }

    init() {
        mode = .mapping
        
        let world = World()
        
        self.world = world
        mesh = Mesh(world)
        readings = []
    }
    
    func addPoint() {
        if let target = world.target {
            mesh.append(target)
        }
    }
    
    func next() {
        mode = .reading
    }
    
    func test() {
        Task {
            guard let reading =
                try await Reading.take(at: world.location.translation)
            else {
                return
            }
            
            readings.append(reading)
        }
    }

    var body: some View {
        ZStack {
            WorldView(world)
                .edgesIgnoringSafeArea(.all)
                .onTapGesture(perform: addPoint)
            VStack {
                Spacer()
                
                let button = switch mode {
                    case .mapping:
                        Button(action: next) {
                            Text("Next")
                                .padding()
                        }
                    case .reading:
                        Button(action: test) {
                            Text("Test")
                                .padding()
                        }
                }
                
                button
                    .disabled(!mesh.formsArea)
                    .buttonStyle(.borderedProminent)
                    .frame(alignment: .center)
                    .padding()
            }
                .font(.system(size: 18.0))
        }
    }
}
