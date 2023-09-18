package ru.otus.otuskotlin.marketplace.app

import ru.otus.otuskotlin.marketplace.app.common.IMkplAppSettings
import ru.otus.otuskotlin.marketplace.biz.MkplAdProcessor

data class MkplAppSettings(
    val appUrls: List<String> = emptyList(),
    override val processor: MkplAdProcessor = MkplAdProcessor(),
): IMkplAppSettings
