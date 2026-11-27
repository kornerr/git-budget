#if os(Linux) || os(Windows)
    import SwiftCrossUI
#else
    import SwiftUI
#endif

struct HPView: View {
    @CUIObservedObject var vm: HPVM

    init(_ vm: HPVM) {
        self.vm = vm
    }

    var body: some View {
        VStack(spacing: 16) {
            Text("Hello, platform!")
                .font(.largeTitle)
            Text(vm.countText)
                .font(.title3.monospaced())
            Button("Increment") {
                hpSet(F.didClickIncrement, true)
            }
        }
            .padding(40)
    }
}
