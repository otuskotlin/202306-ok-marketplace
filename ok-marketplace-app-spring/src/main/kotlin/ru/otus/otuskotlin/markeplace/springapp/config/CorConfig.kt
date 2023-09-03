package ru.otus.otuskotlin.markeplace.springapp.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.otus.otuskotlin.marketplace.biz.MkplAdProcessor

@Configuration
class CorConfig {
    @Bean
    fun processor() = MkplAdProcessor()
}
