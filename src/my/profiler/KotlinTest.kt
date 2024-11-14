import org.junit.Assert
import org.junit.Test

class KotlinTests {
    @Test
    fun myTest() {
        Assert.assertEquals("bruh", "lol")
    }
class KotlinTests {
    private val expectedValue = "bruh"
    private val actualValue = "lol"
    
    @Test
    fun testStringEquality() {
        Assert.assertEquals(expectedValue, actualValue)
    }
}
}