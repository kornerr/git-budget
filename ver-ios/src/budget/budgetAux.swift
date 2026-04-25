import Foundation

// Date of the report (yesterday)
func budgetReportedDate() -> String {
    let cal = Calendar.current
    let yesterday = cal.date(byAdding: .day, value: -1, to: Date())!
    let parts = cal.dateComponents([.day, .month], from: yesterday)
    let sday = String(format: "%02d", parts.day!)
    let smon = String(format: "%02d", parts.month!)
    return "\(sday).\(smon)"
}

// Reported week day: 1 == Monday, …, 7 == Sunday
func budgetReportedWeekday() -> Int32 {
    let cal = Calendar.current
    let yesterday = cal.date(byAdding: .day, value: -1, to: Date())!
    let weekdayApple = cal.component(.weekday, from: yesterday)
    let weekdayJava = mondayBasedWeekday(weekdayApple)
    return Int32(weekdayJava)
}

// Convert Apple weekday (1 = Sunday, 2 = Monday, …) to Monday-based (1 = Monday, …, 7 = Sunday)
func mondayBasedWeekday(_ dayIn: Int) -> Int {
    var dayOut = dayIn - 1
    if dayOut == 0 {
        dayOut = 7
    }
    return dayOut
}
