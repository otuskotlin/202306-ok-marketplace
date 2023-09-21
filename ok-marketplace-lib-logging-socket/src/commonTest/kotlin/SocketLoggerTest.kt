package ru.otus.otuskotlin.marketplace.logging.socket

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext
import kotlin.test.Test
import kotlin.time.Duration.Companion.seconds

class SocketLoggerTest {
    @OptIn(ExperimentalStdlibApi::class)
    @Test
    fun socketTest() = runTest(timeout = 3.seconds) {
        withContext(Dispatchers.Default) {
            val logger = mpLoggerSocket("test")

            while ((logger as? MpLoggerWrapperSocket)?.isReady?.value != true) {
                println("SS")
                delay(1)
            }
            launch {
                repeat(100) {
                    logger.info(
                        msg = "Test message $it",
                        marker = "TST",
                        data = object {
                            @Suppress("unused")
                            val str: String = "one"
                            @Suppress("unused")
                            val ival: Int = 2
                        }
                    )
                }
            }
        }
    }
}
