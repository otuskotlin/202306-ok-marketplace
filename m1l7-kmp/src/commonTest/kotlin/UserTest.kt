import kotlin.test.Test
import kotlin.test.assertEquals

// Run cross-platform tests (./gradlew clean jsTest)
//  show where artifacts converted to a specific platform are located
//  (build/classes/kotlin, build/klib/cache/main)
class UserTest {

    @Test
    fun test1() {
        val user = User("1", "Ivan", 24)
        assertEquals("1", user.id)
        assertEquals("Ivan", user.name)
        assertEquals(24, user.age)
    }
}
