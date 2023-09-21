package ru.otus.otuskotlin.marketplace.app

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import ru.otus.otuskotlin.marketplace.api.v2.apiV2Mapper
import ru.otus.otuskotlin.marketplace.app.plugins.initAppSettings
import ru.otus.otuskotlin.marketplace.app.plugins.initPlugins
import ru.otus.otuskotlin.marketplace.app.v2.wsHandlerV2
import ru.otus.otuskotlin.marketplace.app.v2.v2Ad
import ru.otus.otuskotlin.marketplace.app.v2.v2Offer

fun main(args: Array<String>): Unit = io.ktor.server.cio.EngineMain.main(args)

fun Application.module(appSettings: MkplAppSettings = initAppSettings()) {
    initPlugins(appSettings)
    routing {
        get("/") {
            call.respondText("Hello, world!")
        }
        route("v2") {
            install(ContentNegotiation) {
                json(apiV2Mapper)
            }

            v2Ad(appSettings)
            v2Offer(appSettings)

        }
        webSocket("/ws/v2") {
            wsHandlerV2(appSettings)
        }
    }
}
