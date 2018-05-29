package aoc

/** Problem: [[https://adventofcode.com/2017/day/2]]
  *
  * Solution:
  *
  * Part 1 - Straight forward. For every row, build a/the tuple
  * of the largest and the smallest value in that row. Calc the
  * diffs and sum them up.
  *
  * Part 2 - A little bit more interesting. Build the pairs of all
  * values in a row (but only the ones where x > y), filter for the
  * one pair where x is divisible by y with no remainder and sum up
  * the result of the division.
  */
object Day02 {

  val input = Util.readInput("Day02input.txt").map { it.split('\t') }.map { line -> line.map { it.toInt() }}

  fun checksum(spreadSheet: List<List<Int>>, processRow: (List<List<Int>>) -> List<Int>): Int {
    //require(spreadSheet.nonEmpty, "spreadSheet.nonEmpty failed")
    //require(spreadSheet.forall(_.nonEmpty), "spreadSheet.forall(_.nonEmpty) failed")

    return processRow(spreadSheet).sum()
  }
  //} ensuring(_ >= 0, s"_ >= 0 failed")

  object Part1 {
    fun processRow(s: List<List<Int>>): List<Int> {
      return s.map { (it.max() ?: 0) - (it.min() ?: 0) }
    }

    fun solve(spreadSheet: List<List<Int>>): Int {
      return Day02.checksum(spreadSheet, Part1::processRow)
    }
  }

  object Part2 {
    fun processRow(s: List<List<Int>>): List<Int> {
      val pairs = s.flatMap { row -> row.flatMap { (0 until row.size).flatMap {x -> (0 until row.size).map { y -> (x to y) }}}}.filter { it.first > it.second }
      val dividablePair = pairs.filter { (it.first() % it.second()) == 0 }.first()
      return dividablePair.first() / dividablePair.second()
    }

    fun solve(spreadSheet: List<List<Int>>): Int {
      return Day02.checksum(spreadSheet, Part2::processRow)
    }
  }
}
