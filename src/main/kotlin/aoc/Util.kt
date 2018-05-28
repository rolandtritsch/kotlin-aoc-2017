package aoc

object Util {
  fun readInput(fileName: String): List<String> {
    val home = System.getenv("HOME")
    val devDir = "/Development/Home/kotlin-aoc-2017"
    val resDir = "/src/main/resources"
    val fqn = "${home}/${devDir}/${resDir}/${fileName}"
    val lines = java.io.File(fqn).readLines()
    return lines
  }
}
