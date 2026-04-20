import SwiftUI

struct BudgetView: View {
    @ObservedObject var vm: VM

    init(_ vm: VM) {
        self.vm = vm
    }

    var body: some View {
        HStack {
            TextField(
                "",
                text: Binding(
                    get: { vm.spent },
                    set: { budgetSet(F.inputSpent, $0) }
                ),
                prompt: Text(vm.spentPlaceholder)
            )
                .textFieldStyle(.roundedBorder)
            Button(action: { budgetSet(F.didClickPasteSpent, true) }) {
                Text(vm.spentPasteTitle)
            }
        }
            .padding(24)
        HStack {
            TextField(
                "",
                text: Binding(
                    get: { vm.morningBalance },
                    set: { budgetSet(F.inputMorningBalance, $0) }
                ),
                prompt: Text(vm.morningBalancePlaceholder)
            )
                .textFieldStyle(.roundedBorder)
            Button(action: { budgetSet(F.didClickPasteMorningBalance, true) }) {
                Text(vm.morningBalancePasteTitle)
            }
        }
            .padding([.leading, .bottom, .trailing], 24)
        HStack {
            HStack {
                Text(vm.result)
                Spacer()
            }
            Button(action: { budgetSet(F.didClickCopy, true) }) {
                Text(vm.resultCopyTitle)
            }
        }
            .padding([.leading, .trailing], 24)
        Spacer()
    }
}
