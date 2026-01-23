import gb

extension CLDController {
    // Unify set() call across platforms
    func set(_ k: String, _ v: Any) {
        set(fieldName: k, value: v)
    }
}

// Bind effects to a Context changes
func registerOneliners<T>(
    _ ctrl: CLDController,
    _ items: [Any]
) -> T? {
    let halfCount = items.count / 2
    for i in 0..<halfCount {
        let field = items[i * 2] as! String
        let callback = items[i * 2 + 1] as! (T) -> Void
        ctrl.registerFieldCallback(fieldName: field) { cc in
            let c = cc as! T
            callback(c)
        }
    }

    // A hack for generics to operate
    return nil
}
