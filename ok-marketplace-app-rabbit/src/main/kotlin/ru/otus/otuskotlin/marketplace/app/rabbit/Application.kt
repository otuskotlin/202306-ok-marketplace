package ru.otus.otuskotlin.marketplace.app.rabbit

import ru.otus.otuskotlin.marketplace.app.rabbit.config.MkplAppSettings
import ru.otus.otuskotlin.marketplace.app.rabbit.config.RabbitApp

// TODO-rmq-2: смотрим настройки приложения
fun main() {
    val appSettings = MkplAppSettings()
    val app = RabbitApp(appSettings = appSettings)
    app.controller.start()
}
