private let _greeting: [CChar] = {
    let str = "Hello from Swift WebAssembly!"
    return str.utf8.map { CChar(bitPattern: $0) } + [0]
}()

@_expose(wasm, "get_message")
func get_message() -> UnsafePointer<CChar> {
    _greeting.withUnsafeBufferPointer { $0.baseAddress! }
}

@_expose(wasm, "add")
func add(_ a: Int, _ b: Int) -> Int {
    a + b
}

@main
struct hwcli {
    static func main() {
        print("Hello, world!")
    }
}
