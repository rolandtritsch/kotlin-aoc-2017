package aoc

object Util {
  fun readInput(fileName: String): List<String> {
    val lines = this.javaClass.getResource(fileName).readLines()
    return lines
  }
}
