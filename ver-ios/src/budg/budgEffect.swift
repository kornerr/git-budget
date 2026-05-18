import gb
import UIKit

func budgCopyResult(_ result: String) {
    UIPasteboard.general.string = result
}

func budgPasteMorningBalance() {
    let txt = UIPasteboard.general.string ?? ""
    budgSet(F.pastedMorningBalance, txt)
}

func budgPasteSpent() {
    let txt = UIPasteboard.general.string ?? ""
    budgSet(F.pastedSpent, txt)
}
