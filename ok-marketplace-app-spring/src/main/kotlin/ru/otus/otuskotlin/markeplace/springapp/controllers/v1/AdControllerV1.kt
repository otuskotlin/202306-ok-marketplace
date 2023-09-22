package ru.otus.otuskotlin.markeplace.springapp.api.v1.controller

import org.springframework.web.bind.annotation.*
import ru.otus.otuskotlin.markeplace.springapp.controllers.v1.processV1
import ru.otus.otuskotlin.markeplace.springapp.models.MkplAppSettings
import ru.otus.otuskotlin.marketplace.api.v1.models.*

@Suppress("unused")
@RestController
@RequestMapping("v1/ad")
class AdControllerV1(
    private val appSettings: MkplAppSettings
) {

    @PostMapping("create")
    suspend fun createAd(@RequestBody request: AdCreateRequest): AdCreateResponse =
        processV1(appSettings, request = request, this::class, "createAd")

    @PostMapping("read")
    suspend fun  readAd(@RequestBody request: AdReadRequest): AdReadResponse =
        processV1(appSettings, request = request, this::class, "readAd")

    @RequestMapping("update", method = [RequestMethod.POST])
    suspend fun  updateAd(@RequestBody request: AdUpdateRequest): AdUpdateResponse =
        processV1(appSettings, request = request, this::class, "updateAd")

    @PostMapping("delete")
    suspend fun  deleteAd(@RequestBody request: AdDeleteRequest): AdDeleteResponse =
        processV1(appSettings, request = request, this::class, "deleteAd")

    @PostMapping("search")
    suspend fun  searchAd(@RequestBody request: AdSearchRequest): AdSearchResponse =
        processV1(appSettings, request = request, this::class, "searchAd")
}
