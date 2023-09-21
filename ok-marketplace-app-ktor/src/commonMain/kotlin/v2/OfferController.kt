package ru.otus.otuskotlin.marketplace.app.v2

import io.ktor.server.application.*
import ru.otus.otuskotlin.marketplace.api.v2.models.AdOffersRequest
import ru.otus.otuskotlin.marketplace.api.v2.models.AdOffersResponse
import ru.otus.otuskotlin.marketplace.app.MkplAppSettings

suspend fun ApplicationCall.offersAd(appSettings: MkplAppSettings) =
    processV2<AdOffersRequest, AdOffersResponse>(appSettings)
