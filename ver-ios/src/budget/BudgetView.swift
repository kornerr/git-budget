import SwiftUI

struct BudgetView: View {
    @ObservedObject var vm: VM

    init(_ vm: VM) {
        self.vm = vm
    }

    var body: some View {
        VStack(spacing: 8) {
            HStack(spacing: 8, alignment: .center) {
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
                    .buttonStyle(.borderedProminent)
            }
            HStack(spacing: 8, alignment: .center) {
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
                    .buttonStyle(.borderedProminent)
            }
            VStack(spacing: 8) {
                HStack {
                    Text(vm.result)
                    Spacer()
                }
                HStack {
                    Spacer()
                    Button(action: { budgetSet(F.didClickCopy, true) }) {
                        Text(vm.resultCopyTitle)
                    }
                        .buttonStyle(.borderedProminent)
                }
            }
                .padding()
                .background(
                    RoundedRectangle(cornerRadius: 8)
                        .stroke(Color.gray.opacity(0.3), lineWidth: 1)
                )
            Spacer()
        }
            .padding([.leading, .trailing])
    }
}
