package aoc

object Util {
  fun readInput(fileName: String): List<String> {
    val fqn = "/Users/rtritsch/Development/Home/kotlin-aoc-2017/src/main/resources" + "/" + fileName
    val lines = java.io.File(fqn).readLines()
    return lines
  }
}
