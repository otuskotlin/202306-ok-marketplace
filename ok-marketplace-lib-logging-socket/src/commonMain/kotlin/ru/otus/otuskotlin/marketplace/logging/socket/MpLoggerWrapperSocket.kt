package ru.otus.otuskotlin.marketplace.logging.socket

import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import io.ktor.util.cio.*
import io.ktor.utils.io.*
import io.ktor.utils.io.core.*
import kotlinx.atomicfu.AtomicBoolean
import kotlinx.atomicfu.AtomicInt
import kotlinx.atomicfu.atomic
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.onSubscription
import kotlinx.serialization.json.Json
import ru.otus.otuskotlin.marketplace.logging.common.IMpLogWrapper
import ru.otus.otuskotlin.marketplace.logging.common.LogLevel

@ExperimentalStdlibApi
class MpLoggerWrapperSocket(
//    val logger: Logger,
    override val loggerId: String
) : IMpLogWrapper, AutoCloseable {
    private val selectorManager = SelectorManager(Dispatchers.IO)
    private val scope = CoroutineScope(Dispatchers.Default + CoroutineName("Logging"))
    val sf = MutableSharedFlow<LogData>(
        extraBufferCapacity = 16,
        onBufferOverflow = BufferOverflow.SUSPEND
    )
    val isReady: AtomicBoolean = atomic(false)
    val isEmpty: Boolean get() = count.value == 0
    val count: AtomicInt = atomic(0)
    private val jsonSerializer = Json {
        encodeDefaults = true
    }

    private val job = scope.launch { handleLogs() }

    private suspend fun handleLogs() {
        aSocket(selectorManager).tcp().connect("127.0.0.1", 9002).use { socket ->
            socket.openWriteChannel().use {
                sf
                    .onSubscription { isReady.value = true }
                    .collect {
                        val json = jsonSerializer.encodeToString(LogData.serializer(), it)
                        println("LOG: $json")
                        writeStringUtf8(json)
                        flush()
                        count.decrementAndGet()
                    }
            }
        }
    }

    override fun log(
        msg: String,
        level: LogLevel,
        marker: String,
        e: Throwable?,
        data: Any?,
        objs: Map<String, Any>?
    ) {
        count.incrementAndGet()
//        while (
            !sf.tryEmit(
                LogData(
                    level = level,
                    message = msg,
                )
            )
//        ) {
//        }
    }

    override fun close() {
        job.cancel(message = "Finishing")
    }
}
