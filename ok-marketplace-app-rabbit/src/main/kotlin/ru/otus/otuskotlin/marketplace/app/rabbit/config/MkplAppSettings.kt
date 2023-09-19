package ru.otus.otuskotlin.marketplace.app.rabbit.config

import ru.otus.otuskotlin.marketplace.app.common.IMkplAppSettings
import ru.otus.otuskotlin.marketplace.biz.MkplAdProcessor

data class MkplAppSettings(
    val config: RabbitConfig = RabbitConfig(),
    override val processor: MkplAdProcessor = MkplAdProcessor(),
): IMkplAppSettings {
}
