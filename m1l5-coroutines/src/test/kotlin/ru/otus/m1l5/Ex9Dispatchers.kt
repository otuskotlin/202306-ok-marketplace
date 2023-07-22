package ru.otus.m1l5

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.launch
import mu.KotlinLogging
import org.junit.Test
import java.util.concurrent.Executors
import kotlin.concurrent.thread
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class Ex10Dispatchers {
    private val log = KotlinLogging.logger {}

    fun CoroutineScope.createCoro() {
        repeat(30) {
            launch {
                log.info("coroutine $it, start")
                Thread.sleep(500)
                log.info("coroutine $it, end")
            }
        }
    }

    @Test
    fun default() {
        CoroutineScope(Job()).createCoro()
        Thread.sleep(2000)
    }

    @Test
    fun io() {
        CoroutineScope(Job() + Dispatchers.IO).createCoro()
        Thread.sleep(2000)
    }

    @Test
    fun custom() {
        val dispatcher = Executors.newSingleThreadExecutor().asCoroutineDispatcher()
        CoroutineScope(Job() + dispatcher).createCoro()
        Thread.sleep(6000)
    }

    @Test
    fun unconfined() {
        val scope = CoroutineScope(Dispatchers.Default)
        //val scope = CoroutineScope(Dispatchers.Unconfined)

        scope.launch() {
            log.info("start coroutine")
            suspendCoroutine {
                log.info("suspend function, start")
                thread {
                    log.info("suspend function, background work")
                    Thread.sleep(1000)
                    it.resume("Data!")
                }
            }
            log.info("end coroutine")
        }

        Thread.sleep(2000)
    }

}