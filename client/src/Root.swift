import SwiftUI
import Network
import os.log

let logger: Logger = .init()

struct Root: View {
    @State
    var model: Model = .init()
    @State
    var compose: String = ""
    
    var body: some View {
        VStack {
            List(model.uploads) { upload in
                VStack {
                    upload.view()
                }
            }
            TextField("Message to send", text: $compose)
                .onSubmit {
                    model.send(compose)
                }
        }
    }

}

class Model: Equatable {
    // I love commiting non-functioning in-progress code
    var desktop: URL = .init(string: "insert actual ip here")!
    var uploads: [Upload] = []

    func send(_ message: String) {
        var request = URLRequest(url: desktop)
        
        request.httpMethod = "POST"
        request.httpBody = message.data(using: .utf8)
        
        URLSession.shared.dataTask(with: request) { [weak self]
            data, response, error in
            guard let self else {
                logger.log("upload task polled after model dropped")
                
                return
            }
            
            logger.log("response received :D")
            
            if let error {
                self.uploads.append(.init(result: .err(error)))
            } else if let response = response as? HTTPURLResponse {
                self.uploads.append(.init(result: .ok(Response(
                    status: response.statusCode,
                    body: String(data: data!, encoding: .utf8)!
                ))))
            }
        }
            .resume()
    }
    
    // ---
    
    static func == (lhs: Model, rhs: Model) -> Bool {
        lhs.desktop == rhs.desktop && lhs.uploads == rhs.uploads
    }
}

struct Upload: Equatable, Identifiable {
    let id: UUID = .init()
    let result: Result
    
    func view() -> some View {
        switch self.result {
            case .ok(let response):
                Text("RESPONSE: \(response.body)")
            case .badResponse:
                Text("BAD RESPONSE")
            case .err(let error):
                Text("ERROR: \(error)")
        }
    }
}

enum Result: Equatable {
    case ok(Response)
    case badResponse
    case err(any Error)
    
    static func == (lhs: Self, rhs: Self) -> Bool {
        switch (lhs, rhs) {
            case (.ok(let lhs), .ok(let rhs)):
                return lhs == rhs
            case (.badResponse, .badResponse):
                return true
            case (.err, .err):
                return true
            default:
                return false
        }
    }
}

struct Response: Equatable {
    let status: Int
    let body: String
}

#Preview {
    Root()
}
