package aoc

object Util {
  fun readInput(fileName: String): List<String> {
    val devDir = System.getenv("PWD")
    val resDir = "/src/main/resources"
    val fqn = "${devDir}/${resDir}/${fileName}"
    val lines = java.io.File(fqn).readLines()
    return lines
  }

  fun <R> measureTimeMillis(init: R, block: () -> R): Pair<R, Long> {
    var result = init
    val ms = kotlin.system.measureTimeMillis {
      result = block()
    }
    return Pair(result, ms)
  }
}
