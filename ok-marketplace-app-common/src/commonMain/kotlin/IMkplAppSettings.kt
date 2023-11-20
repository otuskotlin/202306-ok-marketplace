package ru.otus.otuskotlin.marketplace.app.common

import ru.otus.otuskotlin.marketplace.biz.MkplAdProcessor
import ru.otus.otuskotlin.marketplace.common.MkplCorSettings
import ru.otus.otuskotlin.marketplace.logging.common.MpLoggerProvider

interface IMkplAppSettings {
    val processor: MkplAdProcessor
    val corSettings: MkplCorSettings
    val logger: MpLoggerProvider
    val auth: AuthConfig
}
