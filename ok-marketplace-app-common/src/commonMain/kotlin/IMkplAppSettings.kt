package ru.otus.otuskotlin.marketplace.app.common

import ru.otus.otuskotlin.marketplace.biz.MkplAdProcessor

interface IMkplAppSettings {
    val processor: MkplAdProcessor
}
