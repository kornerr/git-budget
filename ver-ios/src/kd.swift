extension KDController {
    // Make registerFieldCallback() call similar to Android
    func registerFieldCallback(_ fieldName: String, _ cb: @escaping (KDContext) -> Void) {
        registerFieldCallback(fieldName: fieldName) { cc in
            cb(cc)
        }
    }

    // Make set() call similar to Android
    func set(_ k: String, _ v: Any) {
        set(fieldName: k, value: v)
    }
}
// Bind effects to a Context changes
func registerOneliners<T>(
    _ ctrl: KDController,
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

// Special object to reference context fields with a compile time validation
struct F {
    static let didClickCopy = "didClickCopy"
    static let didClickPaste = "didClickPaste"
    static let didClickPasteMorningBalance = "didClickPasteMorningBalance"
    static let didLaunch = "didLaunch"
    static let inputDate = "inputDate"
    static let inputMorningBalance = "inputMorningBalance"
    static let inputSpent = "inputSpent"
    static let morningBalance = "morningBalance"
    static let none = "none"
    static let pastedMorningBalance = "pastedMorningBalance"
    static let pastedSpent = "pastedSpent"
    static let reportedDate = "reportedDate"
    static let reportedWeekday = "reportedWeekday"
    static let result = "result"
    static let spent = "spent"

}
