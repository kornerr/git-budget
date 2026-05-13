func bakeCString(_ s: String, into p: UnsafeMutablePointer<CChar>) {
    let bytes = s.utf8CString
    for i in bytes.indices { p[i] = bytes[i] }
}

private let cStringBuffer = UnsafeMutablePointer<CChar>.allocate(capacity: 256)

@MainActor
final class DataContext {
    static let shared = DataContext()
    var stringValue = "Hello, world"
    var intVal: Int32 = 153
    private init() {}
}

@MainActor @_expose(wasm, "get_string")
func get_string() -> UnsafePointer<CChar> {
    bakeCString(DataContext.shared.stringValue, into: cStringBuffer)
    return UnsafePointer(cStringBuffer)
}

@MainActor @_expose(wasm, "get_int")
func get_int() -> Int32 { DataContext.shared.intVal }

@MainActor @_expose(wasm, "set_int")
func set_int(_ value: Int32) { DataContext.shared.intVal = value }

@MainActor @_expose(wasm, "set_string")
func set_string(_ ptr: UnsafePointer<CChar>) {
    DataContext.shared.stringValue = String(cString: ptr)
}

@MainActor @_expose(wasm, "get_scratch")
func get_scratch() -> UnsafeMutablePointer<CChar> { cStringBuffer }
