package ru.otus.otuskotlin.marketplace.app

import ru.otus.otuskotlin.marketplace.app.common.AuthConfig
import ru.otus.otuskotlin.marketplace.app.common.IMkplAppSettings
import ru.otus.otuskotlin.marketplace.biz.MkplAdProcessor
import ru.otus.otuskotlin.marketplace.common.MkplCorSettings
import ru.otus.otuskotlin.marketplace.logging.common.MpLoggerProvider

data class MkplAppSettings(
    val appUrls: List<String> = emptyList(),
    override val corSettings: MkplCorSettings,
    override val processor: MkplAdProcessor = MkplAdProcessor(corSettings),
    override val logger: MpLoggerProvider = MpLoggerProvider(),
    override val auth: AuthConfig = AuthConfig.TEST,
): IMkplAppSettings
