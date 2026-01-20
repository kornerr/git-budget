import Foundation

// Convert Apple's day of week (1 = Sunday, 2 = Monday, ...)
// to what Java uses (1 = Monday, ..., 7 = Sunday)
func mondayBasedWeekday(_ dayIn: Int) -> Int {
    var dayOut = dayIn - 1
    // Convert Sunday
    if (dayOut == 0) {
        dayOut = 7
    }

    return dayOut
}
