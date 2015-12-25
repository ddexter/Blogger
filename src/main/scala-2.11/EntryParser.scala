import java.text.SimpleDateFormat
import java.util.Date
import scala.annotation.tailrec

import org.markdown4j.Markdown4jProcessor

/**
  * A utility for extracting formatted markdown and converting to html
  * @author ddexter
  */

object EntryParser {
  // Keys for blog, denotes the start of a section.  Must be on their own line
  val KEYS: Set[String] = Set("@author", "@date", "@title", "@abstract", "@body")
  private val DATE_VAR = "@date"
  private val MD_PROCESSOR = new Markdown4jProcessor

  def parseBlogLines(lines: List[String]): Map[String, String] = {
    @tailrec
    def accumulate(lines: List[String], acc: Map[String, String]): Map[String, String] = lines match {
      case Nil => acc
      case head :: tail =>
        val (vals, rest) = tail.span(!KEYS(_))
        accumulate(rest, acc + (head -> MD_PROCESSOR.process(vals.mkString("\n"))))
    }

    val results = accumulate(lines, Map())
    if (results.contains(DATE_VAR))
      results
    else {
      val sdf: SimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
      results + (DATE_VAR -> MD_PROCESSOR.process(sdf.format(new Date())))
    }
  }
}

// TODO: Figure out better way to pass this info in without side effect of traversing iterator
class EntryParser(val lineList: List[String]) {
  val blogPieces = EntryParser.parseBlogLines(lineList)

  // Verify all pieces exist
  EntryParser.KEYS.foreach(x => blogPieces.get(x) match {
    case None => throw new IllegalArgumentException("Missing blog variable: " + x)
    case _ => ;
  })
}
