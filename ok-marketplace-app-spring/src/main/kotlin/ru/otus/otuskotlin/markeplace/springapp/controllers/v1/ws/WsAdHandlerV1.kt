package ru.otus.otuskotlin.markeplace.springapp.api.v1.ws

import kotlinx.coroutines.runBlocking
import org.springframework.stereotype.Component
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler
import ru.otus.otuskotlin.markeplace.springapp.models.MkplAppSettings
import ru.otus.otuskotlin.marketplace.api.v1.apiV1Mapper
import ru.otus.otuskotlin.marketplace.api.v1.models.IRequest
import ru.otus.otuskotlin.marketplace.app.common.controllerHelper
import ru.otus.otuskotlin.marketplace.common.helpers.isUpdatableCommand
import ru.otus.otuskotlin.marketplace.common.models.MkplCommand
import ru.otus.otuskotlin.marketplace.mappers.v1.fromTransport
import ru.otus.otuskotlin.marketplace.mappers.v1.toTransportAd
import ru.otus.otuskotlin.marketplace.mappers.v1.toTransportInit
import java.util.concurrent.ConcurrentHashMap

@Component
class WsAdHandlerV1(private val appSettings: MkplAppSettings) : TextWebSocketHandler() {
    private val sessions = ConcurrentHashMap<String, WebSocketSession>()

    override fun afterConnectionEstablished(session: WebSocketSession) = runBlocking {
        sessions[session.id] = session

        appSettings.controllerHelper(
            { command = MkplCommand.INIT },
            {
                val msg = apiV1Mapper.writeValueAsString(toTransportInit())
                session.sendMessage(TextMessage(msg))
            }
        )
    }

    override fun handleTextMessage(session: WebSocketSession, message: TextMessage) = runBlocking {
        appSettings.controllerHelper(
            {
                val request = apiV1Mapper.readValue(message.payload, IRequest::class.java)
                fromTransport(request)
            },
            {
                val result = apiV1Mapper.writeValueAsString(toTransportAd())
                if (isUpdatableCommand()) {
                    sessions.values.forEach {
                        it.sendMessage(TextMessage(result))
                    }
                } else {
                    session.sendMessage(TextMessage(result))
                }
            }
        )
    }

    override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus): Unit = runBlocking {
        appSettings.controllerHelper(
            { command = MkplCommand.FINISH },
            {}
        )
        sessions.remove(session.id)
    }
}
