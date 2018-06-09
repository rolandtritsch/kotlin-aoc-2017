package aoc

/** Problem: [[https://adventofcode.com/2017/day/5]]
  *
  * Solution:
  *
  * General - Walk through the stack and update the stack with the offset()
  * function until the stack counter is/runs out of bounds (which indicates
  * that the "program" has exited.
  *
  * Note: The given program will exit at the higher end of the stack, but
  * for completeness we are also checking for the lower bound.
  *
  * Note: Picking the right [[https://docs.scala-lang.org/overviews/collections/performance-characteristics.html datastructure]] is important (and
  * in our case right means Vector, because it updates in constant time,
  * not linear time (like e.g. List)).
  *
  * Part1 - Trivial. Just increase the stack counter by 1.
  *
  * Part2 - Increase the stack counter as described in the problem statement.
  */
object Day05 {

  val input = Util.readInput("Day05input.txt").map { it.toInt() }.toMutableList()

  fun outOfBounds(stack: MutableList<Int>, stackCounter: Int): Boolean {
    return stackCounter + stack[stackCounter] < 0 || stackCounter + stack[stackCounter] >= stack.size
  }

  fun countSteps(stack: MutableList<Int>, stackCounter: Int, steps: Int, offset: (Int) -> Int): Int {
    require(stack.size > 0) { "stack.nonEmpty failed" }
    require(steps >= 1) { "steps >= 1 failed; with >${steps}<" }

    val count = if(outOfBounds(stack, stackCounter)) steps
      else {
        println("${stack.size}/${stackCounter}/${steps}/${offset(stack[stackCounter])}")
        stack[stackCounter] = offset(stack[stackCounter])
        countSteps(stack, stackCounter + stack[stackCounter], steps + 1, offset)
      }
    return count
  }

  object Part1 {
    fun offset(sc: Int) = sc + 1
    fun solve(stack: MutableList<Int>): Int = countSteps(stack, 0, 1, Part1::offset)
  }

  object Part2 {
    fun offset(sc: Int) = if(sc >= 3) sc - 1 else sc + 1
    fun solve(stack: MutableList<Int>): Int = countSteps(stack, 0, 1, Part2::offset)
  }
}
