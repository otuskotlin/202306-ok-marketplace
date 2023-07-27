import kotlinx.coroutines.delay

actual class CommonApiService {
    // Asynchronous code with coroutines is converted to Promise
    actual suspend fun makeCall(): String {
        delay(1000)
        return "JS api call"
    }
}
