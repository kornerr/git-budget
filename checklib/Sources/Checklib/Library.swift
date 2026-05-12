// Your journey into Swift for Android starts here!
//
// This file contains examples of how to interact with Java/Kotlin code
// using JNI Kit and Android NDK Swift bindings.
//
// It also demonstrates how to wrap Java objects into Swift classes
// for easier and more idiomatic usage in Swift.
//
// Detailed instructions here https://docs.swifdroid.com/lib

// JNI Kit with a lot of conveniences
import JNIKit
#if os(Android)
// Official Swift bindings for Android NDK
import Android
// Specific logging handler for Android platform
import AndroidLogging
#endif
// Lightweight and fast logging system
import Logging
// It is necessary for `Task`
// otherwise you need `FoundationEssentials` package for `Date`
import Foundation

// MARK: - JNI Initialization

#if os(Android)
/// This method should be called first from Java/Kotlin side to initialize everything
///
/// Parameters:
///   - envPointer: it's a pointer to the JNI environment, which is used to interact with the JVM.
///   - clazzRef: it's a reference to the Java class that contains the native method being called.
///   - callerRef: it's a reference to the Java object that called this native method
/// Returns: Void
///
/// Note:
/// 
/// The naming convention for the function is important.
/// It follows the JNI naming pattern: `Java_<package>_<class>_<method>`
/// where `<package>` is the fully qualified package name with underscores instead of dots,
/// `<class>` is the class name, and `<method>` is the method name.
/// 
/// For a Kotlin `object` (a singleton), methods are compiled as `static`.
/// Therefore, `clazzRef` is a jclass reference to the 'SwiftInterface' class.
/// If this were an instance method (e.g., inside a regular `class`), this second
/// parameter would be a jobject (typically named `thizRef`) representing the instance.
@_cdecl("Java_org_opengamestudio_checklib_SwiftInterface_initialize")
public func initialize(envPointer: UnsafeMutablePointer<JNIEnv?>, clazzRef: jobject, callerRef: jobject) {
    // Activate logger
    LoggingSystem.bootstrap(AndroidLogHandler.taggedBySource)
    let logger = Logger(label: "🐦‍🔥 SWIFT")
    logger.info("🚀 Hello World!")
    // Initialize JVM
    let jvm = envPointer.jvm()
    JNIKit.shared.initialize(with: jvm)
    // Access current environment
    let localEnv = JEnv(envPointer)
    // Convert caller's local ref into global ref
    let callerBox = callerRef.box(localEnv)
    // Defer block to clean up local references
    defer {
        // Release local ref to caller object
        localEnv.deleteLocalRefPure(callerRef)
    }
    // Initialize `JObject` from boxed global reference to the caller object
    guard let callerObject = callerBox?.object() else { return }
    
    // Cache the class loader from the caller object
    // it is important to load non-system classes later
    // e.g. your own Java/Kotlin classes
    if let classLoader = callerObject.getClassLoader(localEnv) {
        JNICache.shared.setClassLoader(classLoader)
        logger.info("🚀 class loader cached successfully")
    }
    
    // Example of calling standard Java `toString` method
    let callerDescription = callerObject.toString()
    logger.info("🚀 caller description: \(callerDescription)")
    
    // Example of switching to another thread
    Task {
        // Access current environment in this thread
        guard let env = JEnv.current() else { return }
        logger.info("🚀 new env: \(env)")
        // Print JNI version into LogCat
        logger.info("🚀 jni version: \(env.getVersionString())")
    }
}
#endif

// MARK: - JNI Method Examples

// Call the following methods after the `initialize` method above has been called

#if os(Android)
/// Example of receiving an integer from Java/Kotlin
@_cdecl("Java_org_opengamestudio_checklib_SwiftInterface_sendInt")
public func sendInt(envPointer: UnsafeMutablePointer<JNIEnv?>, clazzRef: jobject, number: jint) {
    let logger = Logger(label: "🐦‍🔥 SWIFT")
    logger.info("#️⃣ sendInt: \(number)")
    print("ИГР this was Swift number: '\(number)'")
}

/// Example of receiving any type from Java/Kotlin (String, Int, Boolean, etc.)
@_cdecl("Java_org_opengamestudio_checklib_SwiftInterface_sendAny")
public func sendAny(envPointer: UnsafeMutablePointer<JNIEnv?>, clazzRef: jobject, key: jobject, value: jobject) {
    let logger = Logger(label: "ИГР sendA")
    let localEnv = JEnv(envPointer)
    defer {
        localEnv.deleteLocalRefPure(key)
        localEnv.deleteLocalRefPure(value)
    }

    // Key
    guard let keyString = key.wrap().string() else {
        logger.info("unable to unwrap key")
        return
    }
    logger.info("ИГР key: \(keyString)")

    // Unwrap value
    guard let box = value.box(localEnv) else {
        logger.info("unable to box value")
        return
    }
    guard let obj = box.object() else {
        logger.info("unable to unwrap value")
        return
    }

    // Int
    let intVal = obj.callIntMethod(name: "intValue")
    localEnv.clearException()
    if let intVal = intVal {
        logger.info("ИГР value int: \(intVal)")
        if keyString == "selectedId" {
            currentDataContext.recentField = keyString
            currentDataContext.selectedId = intVal
            logger.info("ИГР currentDC: \(currentDataContext)")
            currentDataContextDidChangeCallback?.callVoidMethod(name: "onChanged")
        }
        return
    }

    // Long
    let longVal = obj.callLongMethod(name: "longValue")
    localEnv.clearException()
    if let longVal = longVal {
        logger.info("value long: \(longVal)")
        return
    }

    // Bool
    let boolVal = obj.callBoolMethod(name: "booleanValue")
    localEnv.clearException()
    if let boolVal = boolVal {
        logger.info("ИГР value bool: \(boolVal)")
        if keyString == "didLaunch" {
            currentDataContext.recentField = keyString
            currentDataContext.didLaunch = boolVal
            logger.info("ИГР currentDC: \(currentDataContext)")
            currentDataContextDidChangeCallback?.callVoidMethod(name: "onChanged")
        }
        return
    }

    // String
    let valStr = obj.toString()
    logger.info("ИГР value str: \(valStr)")
    if keyString == "url" {
        currentDataContext.recentField = keyString
        currentDataContext.url = valStr
        logger.info("ИГР currentDC: \(currentDataContext)")
        currentDataContextDidChangeCallback?.callVoidMethod(name: "onChanged")
    }
}

/// Example of async operation with callback (this is the best practice)
@_cdecl("Java_org_opengamestudio_checklib_SwiftInterface_fetchAsyncDataWithCallback")
public func fetchAsyncDataWithCallback(env: UnsafeMutablePointer<JNIEnv?>, obj: jobject, callback: jobject) {
    let env = JEnv(env)
    defer { env.deleteLocalRefPure(callback) }
    guard let object = callback.box(env)?.object() else { return }
    Task {
        // Simulate async operation
        try? await Task.sleep(nanoseconds: 5_000_000_000) // 5 seconds
        // Call callback.onResult method
        object.callVoidMethod(name: "onResult", args: "Async data fetched successfully!")
    }
}

@_cdecl("Java_org_opengamestudio_checklib_SwiftInterface_registerCallback")
public func registerCallback(envPointer: UnsafeMutablePointer<JNIEnv?>, clazzRef: jobject, callback: jobject) {
    let env = JEnv(envPointer)
    defer { env.deleteLocalRefPure(callback) }
    guard let object = callback.box(env)?.object() else { return }
    currentDataContextDidChangeCallback = object
}
#endif

struct DataContext {
    var didLaunch = false
    var selectedId: Int32 = 0
    var url = ""

    var recentField = ""
}

// Singleton context.
nonisolated(unsafe) var currentDataContext = DataContext()

// Optional callback invoked after each context change.
nonisolated(unsafe) var currentDataContextDidChangeCallback: JObject? = nil

func getCurrentDataContext() -> DataContext {
    return currentDataContext
}

#if os(Android)
@_cdecl("Java_org_opengamestudio_checklib_SwiftInterface_getCurrentDataContext")
public func getCurrentDataContextJNI(envPointer: UnsafeMutablePointer<JNIEnv?>, clazzRef: jobject) -> jobject? {
    let context = getCurrentDataContext()
    let localEnv = JEnv(envPointer)
    guard
        let clazz = JClass.load("org/opengamestudio/checklib/DataContextDto"),
        let obj = clazz.newObject(args:
            context.didLaunch,
            Int32(context.selectedId),
            context.url,
            context.recentField,
        ) else {
            return nil
        }
    return localEnv.newLocalRefPure(obj.ref.ref)
}
#endif
