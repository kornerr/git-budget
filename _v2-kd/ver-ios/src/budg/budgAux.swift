import Foundation

// Date of the report (yesterday)
func budgReportedDate() -> String {
    let cal = Calendar.current
    let yesterday = cal.date(byAdding: .day, value: -1, to: Date())!
    let parts = cal.dateComponents([.day, .month], from: yesterday)
    let sday = String(format: "%02d", parts.day!)
    let smon = String(format: "%02d", parts.month!)
    return "\(sday).\(smon)"
}

// Reported week day: 1 == Monday, …, 7 == Sunday
func budgReportedWeekday() -> Int32 {
    let cal = Calendar.current
    let yesterday = cal.date(byAdding: .day, value: -1, to: Date())!
    let weekdayApple = cal.component(.weekday, from: yesterday)
    let weekdayJava = mondayBasedWeekday(weekdayApple)
    return Int32(weekdayJava)
}
