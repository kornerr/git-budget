import gb
import UIKit

func budgetCopyResult(_ result: String) {
    UIPasteboard.general.string = result
}

func budgetPasteMorningBalance() {
    let txt = UIPasteboard.general.string ?? ""
    budgetSet(F.pastedMorningBalance, txt)
}

func budgetPasteSpent() {
    let txt = UIPasteboard.general.string ?? ""
    budgetSet(F.pastedSpent, txt)
}
