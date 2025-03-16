import SwiftUI

struct Server: View {
    @State
    var ip: String = ""
    @State
    var port: String = ""
    
    var body: some View {
        VStack {
            Form {
                Section(header: Text("Server")) {
                    TextField("IP address", text: $ip)
                        .keyboardType(.decimalPad)
                        .autocorrectionDisabled()
                    TextField("Port", text: $port)
                        .keyboardType(.numberPad)
                        .autocorrectionDisabled()
                }
                Button("Connect") {
                    print("man")
                }
                .disabled(ip.isEmpty || port.isEmpty)
            }
        }
    }
}
