nonisolated(unsafe) var contextDidChangeCallback: (() -> Void)?

func sendAny(key: String, value: Any) {
    currentDataContext.recentField = key
    switch key {
    case "didLaunch":
        if let v = value as? Bool { currentDataContext.didLaunch = v }
    case "selectedId":
        if let v = value as? Int32 { currentDataContext.selectedId = v }
    case "url":
        if let v = value as? String { currentDataContext.url = v }
    default:
        break
    }
    contextDidChangeCallback?()
}

@_expose(wasm, "sendAnyString")
@MainActor
func sendAnyString(_ keyPtr: UnsafePointer<CChar>, _ valuePtr: UnsafePointer<CChar>) {
    let key = String(cString: keyPtr)
    let value = String(cString: valuePtr)
    sendAny(key: key, value: value)
}

@_expose(wasm, "sendAnyInt")
@MainActor
func sendAnyInt(_ keyPtr: UnsafePointer<CChar>, _ value: Int32) {
    let key = String(cString: keyPtr)
    sendAny(key: key, value: value)
}

@_expose(wasm, "sendAnyBool")
@MainActor
func sendAnyBool(_ keyPtr: UnsafePointer<CChar>, _ value: Bool) {
    let key = String(cString: keyPtr)
    sendAny(key: key, value: value)
}

@_extern(wasm, module: "env", name: "jsCallback")
func jsCallback()

@_expose(wasm, "registerCallback")
@MainActor
func registerCallback() {
    contextDidChangeCallback = {
        jsCallback()
    }
}
