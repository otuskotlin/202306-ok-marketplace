package ru.otus.otuskotlin.marketplace.biz.repo

import kotlinx.coroutines.test.runTest
import repo.repoNotFoundTest
import ru.otus.otuskotlin.marketplace.backend.repo.tests.AdRepositoryMock
import ru.otus.otuskotlin.marketplace.biz.MkplAdProcessor
import ru.otus.otuskotlin.marketplace.biz.addTestPrincipal
import ru.otus.otuskotlin.marketplace.common.MkplContext
import ru.otus.otuskotlin.marketplace.common.MkplCorSettings
import ru.otus.otuskotlin.marketplace.common.models.*
import ru.otus.otuskotlin.marketplace.common.repo.DbAdResponse
import kotlin.test.Test
import kotlin.test.assertEquals

class BizRepoReadTest {

    private val userId = MkplUserId("321")
    private val command = MkplCommand.READ
    private val initAd = MkplAd(
        id = MkplAdId("123"),
        title = "abc",
        description = "abc",
        ownerId = userId,
        adType = MkplDealSide.DEMAND,
        visibility = MkplVisibility.VISIBLE_PUBLIC,
    )
    private val repo by lazy { AdRepositoryMock(
        invokeReadAd = {
            DbAdResponse(
                isSuccess = true,
                data = initAd,
            )
        }
    ) }
    private val settings by lazy {
        MkplCorSettings(
            repoTest = repo
        )
    }
    private val processor by lazy { MkplAdProcessor(settings) }

    @Test
    fun repoReadSuccessTest() = runTest {
        val ctx = MkplContext(
            command = command,
            state = MkplState.NONE,
            workMode = MkplWorkMode.TEST,
            adRequest = MkplAd(
                id = MkplAdId("123"),
            ),
        )
        ctx.addTestPrincipal()
        processor.exec(ctx)
        assertEquals(MkplState.FINISHING, ctx.state)
        assertEquals(initAd.id, ctx.adResponse.id)
        assertEquals(initAd.title, ctx.adResponse.title)
        assertEquals(initAd.description, ctx.adResponse.description)
        assertEquals(initAd.adType, ctx.adResponse.adType)
        assertEquals(initAd.visibility, ctx.adResponse.visibility)
    }

    @Test
    fun repoReadNotFoundTest() = repoNotFoundTest(command)
}
