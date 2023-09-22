package ru.otus.otuskotlin.markeplace.springapp.controllers.v2.ws

import kotlinx.coroutines.runBlocking
import kotlinx.serialization.encodeToString
import org.springframework.stereotype.Component
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler
import ru.otus.otuskotlin.markeplace.springapp.models.MkplAppSettings
import ru.otus.otuskotlin.marketplace.api.v2.apiV2Mapper
import ru.otus.otuskotlin.marketplace.api.v2.apiV2ResponseSerialize
import ru.otus.otuskotlin.marketplace.api.v2.models.IRequest
import ru.otus.otuskotlin.marketplace.app.common.controllerHelper
import ru.otus.otuskotlin.marketplace.common.helpers.isUpdatableCommand
import ru.otus.otuskotlin.marketplace.common.models.MkplCommand
import ru.otus.otuskotlin.marketplace.mappers.v2.fromTransport
import ru.otus.otuskotlin.marketplace.mappers.v2.toTransportAd
import ru.otus.otuskotlin.marketplace.mappers.v2.toTransportInit
import java.util.concurrent.ConcurrentHashMap

@Component
class WsAdHandlerV2(private val appSettings: MkplAppSettings) : TextWebSocketHandler() {
    private val sessions = ConcurrentHashMap<String, WebSocketSession>()

    override fun afterConnectionEstablished(session: WebSocketSession) = runBlocking {
        sessions[session.id] = session

        appSettings.controllerHelper(
            { command = MkplCommand.INIT },
            {
                val msg = apiV2ResponseSerialize(toTransportInit())
                session.sendMessage(TextMessage(msg))
            },
            this::class,
            "WsAdHandlerV2-init",
        )
    }

    override fun handleTextMessage(session: WebSocketSession, message: TextMessage) = runBlocking {
        appSettings.controllerHelper(
            {
                val request = apiV2Mapper.decodeFromString<IRequest>(message.payload)
                fromTransport(request)
            },
            {
                val result = apiV2Mapper.encodeToString(toTransportAd())
                if (isUpdatableCommand()) {
                    sessions.values.forEach {
                        it.sendMessage(TextMessage(result))
                    }
                } else {
                    session.sendMessage(TextMessage(result))
                }
            },
            this::class,
            "WsAdHandlerV2-message",
        )
    }

    override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus): Unit = runBlocking {
        appSettings.controllerHelper(
            { command = MkplCommand.FINISH },
            {},
            this::class,
            "WsAdHandlerV2-finish",
        )
        sessions.remove(session.id)
    }
}
