package ru.otus.otuskotlin.marketplace.app.v2

import io.ktor.server.application.*
import ru.otus.otuskotlin.marketplace.api.v2.models.*
import ru.otus.otuskotlin.marketplace.app.MkplAppSettings
import kotlin.reflect.KClass

private val clCreate: KClass<*> = ApplicationCall::createAd::class
suspend fun ApplicationCall.createAd(appSettings: MkplAppSettings) =
    processV2<AdCreateRequest, AdCreateResponse>(appSettings, clCreate, "create")

private val clRead: KClass<*> = ApplicationCall::readAd::class
suspend fun ApplicationCall.readAd(appSettings: MkplAppSettings) =
    processV2<AdReadRequest, AdReadResponse>(appSettings, clRead, "readAd")

private val clUpdate: KClass<*> = ApplicationCall::updateAd::class
suspend fun ApplicationCall.updateAd(appSettings: MkplAppSettings) =
    processV2<AdUpdateRequest, AdUpdateResponse>(appSettings, clUpdate, "updateAd")

private val clDelete: KClass<*> = ApplicationCall::deleteAd::class
suspend fun ApplicationCall.deleteAd(appSettings: MkplAppSettings) =
    processV2<AdDeleteRequest, AdDeleteResponse>(appSettings, clDelete, "deleteAd")

private val clSearch: KClass<*> = ApplicationCall::searchAd::class
suspend fun ApplicationCall.searchAd(appSettings: MkplAppSettings) =
    processV2<AdSearchRequest, AdSearchResponse>(appSettings, clSearch, "searchAd")
