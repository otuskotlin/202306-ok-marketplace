package ru.otus.m1l5

import kotlinx.coroutines.*
import mu.KotlinLogging
import org.junit.Test
import java.util.concurrent.CompletableFuture
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class Ex7Async {
    private val log = KotlinLogging.logger {}

    @Test
    fun async1(): Unit = runBlocking {// ***
        val res1 = async {
            delay(1000)
            log.info("async1")
            42
        }
        val res2 = async {
            delay(500)
            log.info("async2")
            42
        }

        log.info("completed: ${res1.await()}, ${res2.await()}")
    }

    @Test
    fun asyncLazy(): Unit = runBlocking {// ***
        val res1 = async(start = CoroutineStart.LAZY) {
            log.info("async1 stated")
            delay(1000)
            log.info("async1")
            42
        }
        val res2 = async(start = CoroutineStart.LAZY) {
            log.info("async2 stated")
            delay(500)
            log.info("async2")
            66
        }

        delay(100)
        log.info("we are here")
        //res1.start()
        //res2.start()

        log.info("completed: ${res1.await()}, ${res2.await()}")
    }
}