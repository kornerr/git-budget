private let cStringBuffer = UnsafeMutablePointer<CChar>.allocate(capacity: 1024)
@MainActor @_expose(wasm, "strBuf")
func strBuf() -> UnsafeMutablePointer<CChar> { cStringBuffer }

func bakeCString(_ s: String, into p: UnsafeMutablePointer<CChar>) {
    let bytes = s.utf8CString
    for i in bytes.indices {
        p[i] = bytes[i]
    }
}

struct DataContext {
    var didLaunch = false
    var selectedId: Int32 = 0
    var url = ""

    var recentField = ""
}

// Singleton-like context.
nonisolated(unsafe) var currentDataContext = DataContext()

// selectedId
@_expose(wasm, "DataContext_selectedId")
@MainActor
func DataContext_selectedId() -> Int32 {
    currentDataContext.selectedId
}
@_expose(wasm, "DataContext_setSelectedId")
@MainActor
func DataContext_setSelectedId(_ value: Int32) {
    currentDataContext.selectedId = value
}

// url
@_expose(wasm, "DataContext_setURL")
@MainActor
func DataContext_setURL(_ ptr: UnsafePointer<CChar>) {
    currentDataContext.url = String(cString: ptr)
}
@_expose(wasm, "DataContext_url")
@MainActor
func DataContext_url() -> UnsafePointer<CChar> {
    bakeCString(currentDataContext.url, into: cStringBuffer)
    return UnsafePointer(cStringBuffer)
}
