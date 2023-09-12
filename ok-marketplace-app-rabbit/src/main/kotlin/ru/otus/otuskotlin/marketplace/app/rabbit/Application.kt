package ru.otus.otuskotlin.marketplace.app.rabbit

import ru.otus.otuskotlin.marketplace.app.rabbit.config.AppSettings

// TODO-rmq-2: смотрим настройки приложения
fun main() {
    val appSettings = AppSettings()
    appSettings.controller.start()
}
