package aoc.day17

/** Problem: [[http://adventofcode.com/2017/day/17]]
  *
  * Solution:
  *
  * General - Straight-forward. Take a mutable ListBuffer (for performance reasons) and
  * define the required operations on it.
  *
  * Part1 - Run the algorithm 2017 times with a step size of 371 (input).
  *
  * Part2 - A little bit more tricky. Build a/the list with 50000000 elements in it is
  * not possible (takes too long and takes too much main mem). But we can take a short-cut
  * by (just) moving through the list (virtually) and calculate the insert postions and
  * every time we insert a value after postion 0, we remember it. The last time we insert
  * something after position 0 gives us the solution.
  */
object Day17 {

  val input = aoc.Util.readInput("Day17input.txt").first().toInt()

  val steps = input
  val times = 2017
  val times2 = 50000000

  fun moveForward(position: Int, size: Int, steps: Int): Int {
    require(position in 0..size) { "position >= 0 && position <= size failed; with >${position}<" }
    require(size >= 0) { "size >= 0 failed; with >${size}<" }
    require(steps >= 0) { "steps >= 0 failed; with >${steps}<" }

    return if (steps <= 0) position
    else if (position + 1 >= size) moveForward(0, size, steps - 1)
    else moveForward(position + 1, size, steps - 1)
  }

  fun insertAfter(position: Int, buffer: MutableList<Int>, n: Int): MutableList<Int> {
    require(position in 0..buffer.size) { "position >= 0 && position <= buffer.size failed; with >${position}<" }
    require(buffer.isNotEmpty()) { "buffer.nonEmpty failed" }

    buffer.add(position + 1, n)
    return buffer
  }

  fun nextBuffer(position: Int, buffer: MutableList<Int>, steps: Int, n: Int): Pair<Int, MutableList<Int>> {
    require(position in 0..buffer.size) { "position >= 0 && position <= buffer.size failed; with >${position}<" }
    require(buffer.isNotEmpty()) { "buffer.nonEmpty failed" }

    val insertPosition = moveForward(position, buffer.size, steps)
    val currentPosition = insertPosition + 1
    return Pair(currentPosition, insertAfter(insertPosition, buffer, n))
  }

  fun buildBuffer(buffer: MutableList<Int>, steps: Int, times: Int): Pair<Int, MutableList<Int>> {
    require(steps >= 1) { "step >= 1 failed; with >${steps}<" }
    require(times >= 1) { "times >= 1 failed; with >${times}<" }

    return (1..times).fold(Pair(0, buffer), { current, n ->
      val (currentPosition, currentBuffer) = current
      nextBuffer(currentPosition, currentBuffer, steps, n)
    })
  }

  object Part1 {
    fun solve(steps: Int, times: Int): Int {
      val (position, buffer) = buildBuffer(mutableListOf(0), steps, times)
      return buffer.get(position + 1)
    }
  }

  object Part2 {
    fun solve(steps: Int, times: Int): Int {
      val (_, finalValue) = (1..times).fold(Pair(0, 0), { current, size ->
        val (currentPosition, currentValue) = current
        val nextPosition = moveForward(currentPosition, size, steps)
        if (nextPosition == 0) Pair(nextPosition + 1, size)
        else Pair(nextPosition + 1, currentValue)
      })
      return finalValue
    }
  }
}