import org.scalatest.testng.TestNGSuite
import org.testng.annotations._

/**
  * @author ddexter
  */
class EntryParserTest extends TestNGSuite {
  @DataProvider(name = "missingComponentProvider")
  def missingComponentProvider(): Array[Array[Object]] = {
    Array(
      Array(List("@title", "test2", "@abstract", "test3", "@body", "test4")),
      Array(List("@author", "test1", "@abstract", "test3", "@body", "test4")),
      Array(List("@author", "test1", "@title", "test2", "@body", "test4")),
      Array(List("@author", "test1", "@title", "test2", "@abstract", "test3"))
    )
  }

  @Test(dataProvider = "missingComponentProvider", expectedExceptions = Array(classOf[IllegalArgumentException]))
  def testMissingComponetsThrowErrors(lst: List[String]): Unit = {
    new EntryParser(lst)
  }
}
