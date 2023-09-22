package ru.otus.otuskotlin.marketplace.app.v1

import io.ktor.server.application.*
import ru.otus.otuskotlin.marketplace.api.v1.models.AdOffersRequest
import ru.otus.otuskotlin.marketplace.api.v1.models.AdOffersResponse
import ru.otus.otuskotlin.marketplace.app.MkplAppSettings
import kotlin.reflect.KClass

private val clOffers: KClass<*> = ApplicationCall::offersAd::class
suspend fun ApplicationCall.offersAd(appSettings: MkplAppSettings) =
    processV1<AdOffersRequest, AdOffersResponse>(appSettings, clOffers, "offersAd")
