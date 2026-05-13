// Buffer for passing strings between JS and WASM
@MainActor
private let cStringBuffer = UnsafeMutablePointer<CChar>.allocate(capacity: 1024)
@_expose(wasm, "strBuf")
@MainActor
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

// Singleton-like context
nonisolated(unsafe) var currentDataContext = DataContext()

// didLaunch
@_expose(wasm, "DataContext_didLaunch")
@MainActor
func DataContext_didLaunch() -> Bool {
    currentDataContext.didLaunch
}

// selectedId
@_expose(wasm, "DataContext_selectedId")
@MainActor
func DataContext_selectedId() -> Int32 {
    currentDataContext.selectedId
}

// url
@_expose(wasm, "DataContext_url")
@MainActor
func DataContext_url() -> UnsafePointer<CChar> {
    bakeCString(currentDataContext.url, into: cStringBuffer)
    return UnsafePointer(cStringBuffer)
}

// recentField (read-only)
@_expose(wasm, "DataContext_recentField")
@MainActor
func DataContext_recentField() -> UnsafePointer<CChar> {
    bakeCString(currentDataContext.recentField, into: cStringBuffer)
    return UnsafePointer(cStringBuffer)
}
