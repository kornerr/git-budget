func bakeCString(_ s: String, into p: UnsafeMutablePointer<CChar>) {
    let bytes = s.utf8CString
    for i in bytes.indices { p[i] = bytes[i] }
}

func fillFromCString(_ src: UnsafePointer<CChar>, into dst: UnsafeMutablePointer<CChar>, capacity: Int) {
    var i = 0
    while i < capacity - 1 {
        dst[i] = src[i]
        if src[i] == 0 { break }
        i += 1
    }
    dst[capacity - 1] = 0
}

@MainActor
final class DataContext {
    static let shared = DataContext()

    let strBuf: UnsafeMutablePointer<CChar> = {
        let p = UnsafeMutablePointer<CChar>.allocate(capacity: 256)
        let s = "Hello, world"
        bakeCString(s, into: p)
        return p
    }()

    let scratch = UnsafeMutablePointer<CChar>.allocate(capacity: 256)
    var intVal: Int32 = 153

    func setString(from ptr: UnsafePointer<CChar>) {
        fillFromCString(ptr, into: strBuf, capacity: 256)
    }

    private init() {}
}

@MainActor @_expose(wasm, "get_string")
func get_string() -> UnsafePointer<CChar> { UnsafePointer(DataContext.shared.strBuf) }

@MainActor @_expose(wasm, "get_int")
func get_int() -> Int32 { DataContext.shared.intVal }

@MainActor @_expose(wasm, "set_int")
func set_int(_ value: Int32) { DataContext.shared.intVal = value }

@MainActor @_expose(wasm, "set_string")
func set_string(_ ptr: UnsafePointer<CChar>) { DataContext.shared.setString(from: ptr) }

@MainActor @_expose(wasm, "get_scratch")
func get_scratch() -> UnsafeMutablePointer<CChar> { DataContext.shared.scratch }
