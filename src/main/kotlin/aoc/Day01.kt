package aoc.day01

import kotlin.system.measureTimeMillis

/** Problem: [[https://adventofcode.com/2017/day/1]]
  *
  * Solution:
  *
  * General - To avoid to have to deal with the "wrap-around" we
  * we are *just* doubling the size of the input. With that we can
  * build pairs/tuples of values (i, i + offset), find the pairs where
  * both numbers are the same and sum up the values of the pairs.
  *
  * Part 1 - Run the algorithm with an offset of 1.
  *
  * Part 2 - Run the algorithm with an offset of half the length
  * of the input string.
  */
object Day01 {

  val input = aoc.Util.readInput("Day01input.txt").first()

  fun captcha(digits: String, offset: Int): Int {
    require(offset <= digits.length) { "offset <= digits.length failed; with >${offset}< >${digits.length}<" }

    val doubleDigits = (digits + digits).map { it.toString().toInt() }
    val pairs = (0 until digits.length).map { i -> (doubleDigits.get(i) to doubleDigits.get(i + offset)) }
    val sum = pairs.filter { it.first == it.second }.map { it.first }.sum()
    assert(sum >= 0) { "sum >= 0 failed" }
    return sum
  }

  object Part1 {
    fun solve(input: String): Pair<Int, Long> = aoc.Util.measureTimeMillis(0) {
      require(input.isNotEmpty()) { "input.nonEmpty failed" }
      require(input.all { it.isDigit() }) { "input.all(it.isDigit) failed" }

      Day01.captcha(input, 1)
    }
  }

  object Part2 {
    fun solve(input: String): Pair<Int, Long> = aoc.Util.measureTimeMillis(0) {
      require(input.isNotEmpty()) { "input.nonEmpty failed" }
      require(input.all { it.isDigit() }) { "input.all(it.isDigit) failed" }
      require(input.length % 2 == 0) { "input.length % 2 == 0 failed" }

      Day01.captcha(input, input.length / 2)
    }
  }
}
