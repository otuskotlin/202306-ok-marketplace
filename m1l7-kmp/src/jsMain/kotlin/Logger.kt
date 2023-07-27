actual class Logger {
    // Use the JS console API from Kotlin via the external (native) interface that it represents
    // objects whose implementation will be available in the target platform
    actual fun log(message: String) {
        console.log("JS log: $message")
    }
}
