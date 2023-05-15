import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class ApiServiceTest {

    // TODO-general-7: use runTest instead of runBlocking for coroutine
    //  (skips delay calls on all platforms)
    @Test
    fun test1() = runTest {
        assertEquals("Api call response", ApiService().call())
    }
}
