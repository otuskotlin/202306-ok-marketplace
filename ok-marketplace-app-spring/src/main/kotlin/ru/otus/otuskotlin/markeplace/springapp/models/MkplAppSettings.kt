package ru.otus.otuskotlin.markeplace.springapp.models

import ru.otus.otuskotlin.marketplace.app.common.IMkplAppSettings
import ru.otus.otuskotlin.marketplace.biz.MkplAdProcessor

data class MkplAppSettings(
    override val processor: MkplAdProcessor
): IMkplAppSettings
