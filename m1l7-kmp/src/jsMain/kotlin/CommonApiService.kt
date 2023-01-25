import kotlinx.coroutines.delay

actual class CommonApiService {
    // TODO (Kotlin/JS): асинхронный код с корутинами преобразуется в Promise
    actual suspend fun makeCall(): String {
        delay(1000)
        return "JS api call"
    }
}