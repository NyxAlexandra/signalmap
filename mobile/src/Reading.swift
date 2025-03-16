import Foundation

/// A network speed reading.
struct Reading {
    /// The location the reading was taken at.
    var location: SIMD3<Float>
    /// Download speed in KiB/s.
    var download: Double

    // Honestly I have no idea why they let you access this programmatically given how
    // expensive their official API is, why would anyone use this for anything other
    // than free API access?
    private static let CLOSEST_SERVERS_URL =
        URL(string: "http://speedtest.net/speedtest-servers.php")!
    
    /// Attempt to take a reading.
    public static func take(at location: SIMD3<Float>) async throws -> Self? {
        // TODO: Select closest server

        // Apple API moment...
        class Delegate: NSObject, XMLParserDelegate {
            var servers: [String] = []
            
            func parser(
                _ parser: XMLParser,
                didStartElement elementName: String,
                namespaceURI: String?,
                qualifiedName qName: String?,
                attributes attributeDict: [String : String] = [:]
            ) {
                if elementName == "server" {
                    let url = attributeDict["url"]!
                    
                    servers.append(url)
                }
            }
        }
        
        let (xml, _) =
            try await URLSession.shared.data(from: CLOSEST_SERVERS_URL)
        let parser = XMLParser(data: xml)
        let delegate = Delegate()
        
        parser.delegate = delegate
        parser.parse()
        
        let server = delegate.servers.first!
        let payload = server + "/speedtest/random4000x4000.jpg"
        var request = URLRequest(url: URL(string: payload)!)
        
        request.setValue(
            "Mozilla/5.0 (macOS-15.3.1-arm64-arm-64bit-Mach-O; U; 64bit; en-us)",
            forHTTPHeaderField: "User-Agent"
        )
        
        let start = Date.now
        let (blob, _) = try await URLSession.shared.data(for: request)
        
        let end = Date.now
        let elapsed = start.distance(to: end)
        let bytes = blob.count
        let kilobytes = Double(bytes / 1024)
        
        return Self(
            location: location,
            download: kilobytes / elapsed
        )
    }
}
