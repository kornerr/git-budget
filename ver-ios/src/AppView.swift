import SwiftUI

@main
struct AppView: App {
    @UIApplicationDelegateAdaptor(AppDelegate.self) var appDelegate

    var body: some Scene {
        WindowGroup {
            BudgetView(appDelegate.vm)
        }
    }
}

//<!-- Budget -->
struct BudgetView: View {
    @ObservedObject var vm: VM

    init(_ vm: VM) {
        self.vm = vm
    }

    var body: some View {
        HStack {
            TextField(
                vm.inputSpentLabel,
                text: $vm.inputSpent,
                prompt: Text(vm.inputSpentPlaceholder)
            )
                .textFieldStyle(.roundedBorder)
            Button(action: { print("ИГР todo paste")}) {
                Text(vm.inputSpentPasteTitle)
            }
        }
            .padding(24)
        TextField(
            vm.inputMorningBalanceLabel,
            text: $vm.inputMorningBalance,
            prompt: Text(vm.inputMorningBalancePlaceholder)
        )
            .textFieldStyle(.roundedBorder)
            .padding([.leading, .bottom, .trailing], 24)
        HStack {
            HStack {
                Text(vm.result)
                Spacer()
            }
            Button(action: { print("ИГР todo copy")}) {
                Text(vm.resultCopyTitle)
            }
        }
            .padding([.leading, .trailing], 24)
        Spacer()
    }
}
