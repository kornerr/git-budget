import Combine

class VM: ObservableObject {
    @Published var morningBalance = ""
    @Published var morningBalancePasteTitle = "Paste"
    @Published var morningBalancePlaceholder = "Morning balance"

    @Published var spent = ""
    @Published var spentPasteTitle = "Paste"
    @Published var spentPlaceholder = "Spent"

    @Published var result = "TODO-Result"
    @Published var resultCopyTitle = "Copy"

    static private(set) weak var singleton: VM?

    init() {
        Self.singleton = self
    }
}
