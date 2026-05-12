@MainActor
enum DataContext {
    static let strBuf: UnsafeMutablePointer<CChar> = {
        let p = UnsafeMutablePointer<CChar>.allocate(capacity: 256)
        p[0] = 72;  p[1] = 101; p[2] = 108; p[3] = 108; p[4] = 111
        p[5] = 44;  p[6] = 32;  p[7] = 119; p[8] = 111; p[9] = 114
        p[10] = 108; p[11] = 100; p[12] = 0
        return p
    }()

    static let scratch = UnsafeMutablePointer<CChar>.allocate(capacity: 256)
    static var intVal: Int32 = 153

    static func setString(from ptr: UnsafePointer<CChar>) {
        var i = 0
        while i < 255 {
            strBuf[i] = ptr[i]
            if ptr[i] == 0 { break }
            i += 1
        }
        strBuf[255] = 0
    }
}

@MainActor @_expose(wasm, "get_string")
func get_string() -> UnsafePointer<CChar> { UnsafePointer(DataContext.strBuf) }

@MainActor @_expose(wasm, "get_int")
func get_int() -> Int32 { DataContext.intVal }

@MainActor @_expose(wasm, "set_int")
func set_int(_ value: Int32) { DataContext.intVal = value }

@MainActor @_expose(wasm, "set_string")
func set_string(_ ptr: UnsafePointer<CChar>) { DataContext.setString(from: ptr) }

@MainActor @_expose(wasm, "get_scratch")
func get_scratch() -> UnsafeMutablePointer<CChar> { DataContext.scratch }
