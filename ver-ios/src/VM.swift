import Combine

class VM: ObservableObject {
    @Published var inputMorningBalance = ""
    @Published var inputMorningBalanceLabel = "Morning balance"
    @Published var inputMorningBalancePlaceholder = "TODO-Morning-balance"

    @Published var inputSpent = ""
    @Published var inputSpentLabel = "Spent"
    @Published var inputSpentPasteTitle = "Paste"
    @Published var inputSpentPlaceholder = "Spent"

    @Published var result = "TODO-Result"
    @Published var resultCopyTitle = "Copy"

    private let budgetCmp = BudgetComponent()
    private var subscriptions = [AnyCancellable]()

    init() {
        $inputSpent
            .sink { v in budgetCtrl().set("inputSpent", v) }
            .store(in: &subscriptions)
    }
}
