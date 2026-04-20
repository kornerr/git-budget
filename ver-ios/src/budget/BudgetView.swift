import SwiftUI

struct BudgetView: View {
    @ObservedObject var vm: VM

    init(_ vm: VM) {
        self.vm = vm
    }

    var body: some View {
        HStack {
            TextField(
                vm.inputSpentLabel,
                text: Binding(
                    get: { vm.spent },
                    set: { budgetSet(F.inputSpent, $0) }
                ),
                prompt: Text(vm.inputSpentPlaceholder)
            )
                .textFieldStyle(.roundedBorder)
            Button(action: { budgetSet(F.didClickPasteSpent, true) }) {
                Text(vm.inputSpentPasteTitle)
            }
        }
            .padding(24)
        HStack {
            TextField(
                vm.inputMorningBalanceLabel,
                text: Binding(
                    get: { vm.morningBalance },
                    set: { budgetSet(F.inputMorningBalance, $0) }
                ),
                prompt: Text(vm.inputMorningBalancePlaceholder)
            )
                .textFieldStyle(.roundedBorder)
            Button(action: { budgetSet(F.didClickPasteMorningBalance, true) }) {
                Text(vm.inputMorningBalancePasteTitle)
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
