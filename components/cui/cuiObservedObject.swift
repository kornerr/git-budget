#if os(Linux) || os(Windows)
    import SwiftCrossUI

    public typealias CUIObservedObject<Value: ObservableObject> = State<Value>
#else
    import SwiftUI

    public typealias CUIObservedObject<Value: ObservableObject> = ObservedObject<Value>
#endif
