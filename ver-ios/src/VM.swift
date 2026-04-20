import Combine

class VM: ObservableObject {
    @Published var morningBalance = ""
    @Published var inputMorningBalanceLabel = "Morning balance"
    @Published var inputMorningBalancePasteTitle = "Paste"
    @Published var inputMorningBalancePlaceholder = "TODO-Morning-balance"

    @Published var spent = ""
    @Published var inputSpentLabel = "Spent"
    @Published var inputSpentPasteTitle = "Paste"
    @Published var inputSpentPlaceholder = "Spent"

    @Published var result = "TODO-Result"
    @Published var resultCopyTitle = "Copy"

    static private(set) weak var singleton: VM?

    init() {
        Self.singleton = self
    }
}
