package aoc

object Util {
  fun readInput(fileName: String): List<String> {
    val devDir = System.getenv("PWD")
    val resDir = "/src/main/resources"
    val fqn = "${devDir}/${resDir}/${fileName}"
    val lines = java.io.File(fqn).readLines()
    return lines
  }
}
