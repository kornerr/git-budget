import Combine

final class HPVM: ObservableObject {
    @Published var countText = ""

    static private(set) weak var shared: HPVM?

    init() {
        Self.shared = self
    }
}
