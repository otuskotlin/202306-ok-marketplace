package ru.otus.otuskotlin.marketplace.app.v1

import com.fasterxml.jackson.module.kotlin.readValue
import io.ktor.websocket.*
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.isActive
import ru.otus.otuskotlin.marketplace.api.v1.apiV1Mapper
import ru.otus.otuskotlin.marketplace.api.v1.models.IRequest
import ru.otus.otuskotlin.marketplace.app.MkplAppSettings
import ru.otus.otuskotlin.marketplace.app.common.controllerHelper
import ru.otus.otuskotlin.marketplace.common.helpers.isUpdatableCommand
import ru.otus.otuskotlin.marketplace.common.models.MkplCommand
import ru.otus.otuskotlin.marketplace.mappers.v1.fromTransport
import ru.otus.otuskotlin.marketplace.mappers.v1.toTransportAd
import ru.otus.otuskotlin.marketplace.mappers.v1.toTransportInit
import kotlin.reflect.KClass

val sessions = mutableSetOf<WebSocketSession>()

private val clazz: KClass<*> = WebSocketSession::wsHandlerV1::class
suspend fun WebSocketSession.wsHandlerV1(appSettings: MkplAppSettings) {
    sessions.add(this)

    // Handle init request
    appSettings.controllerHelper(
        { command = MkplCommand.INIT },
        { outgoing.send(Frame.Text(apiV1Mapper.writeValueAsString(toTransportInit()))) },
        clazz,
        "wsHandlerV1-init",
    )

    // Handle flow
    incoming.receiveAsFlow().mapNotNull { it ->
        val frame = it as? Frame.Text ?: return@mapNotNull
        // Handle without flow destruction
        try {
            appSettings.controllerHelper(
                { fromTransport(apiV1Mapper.readValue<IRequest>(frame.readText())) },
                {
                    val result = apiV1Mapper.writeValueAsString(toTransportAd())
                    // If change request, response is sent to everyone
                    if (isUpdatableCommand()) {
                        sessions.forEach {
                            if (it.isActive) it.send(Frame.Text(result))
                        }
                    } else {
                        outgoing.send(Frame.Text(result))
                    }
                },
                clazz,
                "wsHandlerV1-message",
                )

        } catch (_: ClosedReceiveChannelException) {
            sessions.clear()
        }
        // Handle finish request
        appSettings.controllerHelper(
            { command = MkplCommand.FINISH },
            { },
            clazz,
            "wsHandlerV1-finish",
            )
    }.collect()
}
