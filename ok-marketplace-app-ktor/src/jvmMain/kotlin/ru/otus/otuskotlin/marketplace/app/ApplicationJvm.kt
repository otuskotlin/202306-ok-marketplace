package ru.otus.otuskotlin.marketplace.app

import io.ktor.http.*
import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.locations.*
import io.ktor.server.plugins.autohead.*
import io.ktor.server.plugins.cachingheaders.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.plugins.defaultheaders.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import org.slf4j.event.Level
import ru.otus.otuskotlin.marketplace.api.v1.apiV1Mapper
import ru.otus.otuskotlin.marketplace.app.v1.v1Ad
import ru.otus.otuskotlin.marketplace.app.v1.v1Offer
import ru.otus.otuskotlin.marketplace.app.v1.wsHandlerV1
import ru.otus.otuskotlin.marketplace.biz.MkplAdProcessor

@Suppress("unused") // Referenced in application.conf
fun Application.moduleJvm() {
    val processor = MkplAdProcessor()

    install(CachingHeaders)
    install(DefaultHeaders)
    install(AutoHeadResponse)

    install(CORS) {
        allowMethod(HttpMethod.Options)
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Delete)
        allowMethod(HttpMethod.Patch)
        allowHeader(HttpHeaders.Authorization)
        allowHeader("MyCustomHeader")
        allowCredentials = true
        anyHost() // TODO remove
    }

    install(CallLogging) {
        level = Level.INFO
    }

    @Suppress("OPT_IN_USAGE")
    install(Locations)

    routing {
        route("v1") {
            install(ContentNegotiation) {
                jackson {
                    setConfig(apiV1Mapper.serializationConfig)
                    setConfig(apiV1Mapper.deserializationConfig)
                }
            }

            v1Ad(processor)
            v1Offer(processor)
        }

        webSocket("/ws/v1") {
            wsHandlerV1()
        }

        static("static") {
            resources("static")
        }
    }
}
