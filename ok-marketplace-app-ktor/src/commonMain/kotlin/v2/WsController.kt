package ru.otus.otuskotlin.marketplace.app.v2

import io.ktor.websocket.*
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.isActive
import ru.otus.otuskotlin.marketplace.api.v2.apiV2RequestDeserialize
import ru.otus.otuskotlin.marketplace.api.v2.apiV2ResponseSerialize
import ru.otus.otuskotlin.marketplace.api.v2.models.IRequest
import ru.otus.otuskotlin.marketplace.app.MkplAppSettings
import ru.otus.otuskotlin.marketplace.app.common.controllerHelper
import ru.otus.otuskotlin.marketplace.common.helpers.isUpdatableCommand
import ru.otus.otuskotlin.marketplace.common.models.MkplCommand
import ru.otus.otuskotlin.marketplace.mappers.v2.fromTransport
import ru.otus.otuskotlin.marketplace.mappers.v2.toTransportAd
import ru.otus.otuskotlin.marketplace.mappers.v2.toTransportInit

val sessions = mutableSetOf<WebSocketSession>()

suspend fun WebSocketSession.wsHandlerV2(appSettings: MkplAppSettings) {
    sessions.add(this)

    // Handle init request
    appSettings.controllerHelper(
        { command = MkplCommand.INIT },
        { outgoing.send(Frame.Text(apiV2ResponseSerialize(toTransportInit()))) }
    )

    // Handle flow
    incoming.receiveAsFlow().mapNotNull { it ->
        val frame = it as? Frame.Text ?: return@mapNotNull
        // Handle without flow destruction
        try {
            appSettings.controllerHelper(
                { fromTransport(apiV2RequestDeserialize<IRequest>(frame.readText())) },
                {
                    val result = apiV2ResponseSerialize(toTransportAd())
                    // If change request, response is sent to everyone
                    if (isUpdatableCommand()) {
                        sessions.forEach {
                            if (it.isActive) it.send(Frame.Text(result))
                        }
                    } else {
                        outgoing.send(Frame.Text(result))
                    }
                }
            )

        } catch (_: ClosedReceiveChannelException) {
            sessions.clear()
        } catch (e: Throwable) {
            println("FFF")
        }

        // Handle finish request
        appSettings.controllerHelper(
            { command = MkplCommand.FINISH },
            { }
        )
    }.collect()
}
