package ru.otus.otuskotlin.markeplace.springapp.models

import ru.otus.otuskotlin.marketplace.app.common.AuthConfig
import ru.otus.otuskotlin.marketplace.app.common.IMkplAppSettings
import ru.otus.otuskotlin.marketplace.biz.MkplAdProcessor
import ru.otus.otuskotlin.marketplace.common.MkplCorSettings
import ru.otus.otuskotlin.marketplace.logging.common.MpLoggerProvider

data class MkplAppSettings(
    override val corSettings: MkplCorSettings,
    override val processor: MkplAdProcessor,
    override val logger: MpLoggerProvider,
    override val auth: AuthConfig,
): IMkplAppSettings
