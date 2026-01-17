import Combine

class VM: ObservableObject {
    @Published var inputSpent = ""
    @Published var inputSpentLabel = "Spent"
    @Published var inputSpentPasteTitle = "Paste"
    @Published var inputSpentPlaceholder = "Spent"
}
