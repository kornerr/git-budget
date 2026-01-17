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
            Button(action: { print("ИГР todo btn")}) {
                Text(vm.inputSpentPasteTitle)
            }
        }
            .padding(24)
        Spacer()
    }
}
