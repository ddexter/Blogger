import java.text.SimpleDateFormat
import java.util.{Date, Calendar}

import org.markdown4j.Markdown4jProcessor

import scala.io.{Codec, Source, BufferedSource}

/**
  * A utility for extracting formatted markdown and converting to html
  * @author ddexter
  */

object EntryParser {
  // Keys for blog, denotes the start of a section.  Must be on their own line
  private val KEYS: Set[String] = Set("@author", "@date", "@title", "@abstract", "@body")
  private val DATE_VAR = "@date"
  private val MD_PROCESSOR = new Markdown4jProcessor
}

// TODO: Figure out better way to pass this info in without side effect of traversing iterator
class EntryParser(val lineList: List[String]) {
  var blogPieces: Map[String, String] = Map()

  // TODO: figure out a better way to extract file into map
  var lines: Iterator[String] = lineList.iterator
  while (lines.hasNext) {
    val head: String = lines.next()
    if (!EntryParser.KEYS.contains(head)) throw new IllegalArgumentException("Blog variable @<var> expected, but " + head + " found.")
    val (entries, rest) = lines.span(!EntryParser.KEYS.contains(_))
    lines = rest
    blogPieces += (head -> EntryParser.MD_PROCESSOR.process(entries.mkString("\n")))
  }

  if (!blogPieces.contains(EntryParser.DATE_VAR)) {
    val sdf: SimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    blogPieces += (EntryParser.DATE_VAR ->
      EntryParser.MD_PROCESSOR.process(sdf.format(new Date())))
  }

  // Verify all pieces exist
  EntryParser.KEYS.foreach(x => blogPieces.get(x) match {
    case None => throw new IllegalArgumentException("Missing blog variable: " + x)
    case _ => ;
  })

  // TODO: Remove
  def preview(): Unit = {
    blogPieces.foreach { x =>
      println(x._1)
      println(x._2)
      println()
    }
  }
}
