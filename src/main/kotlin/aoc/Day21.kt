package aoc.day21

import java.util.*

typealias Grid = Array<CharArray>

/** Problem: [[http://adventofcode.com/2017/day/21]]
  *
  * Solution:
  *
  * General - This was a tricky one. First, you have to get the transformations
  * right. Means you read all of the rules from the input, but then you have to
  * *add* rules for all of the [[https://en.wikipedia.org/wiki/Dihedral_group_of_order_8 transformations ]]
  * (by flipping and rotating the input pattern).
  *
  * We then need to implement a/the divide-enhance-join functions and call them
  * recursively for N iterations.
  *
  * Part1 - Run divide-enhance-join for 5 iterations.
  *
  * Part2 - Run divide-enhance-join for 18 iterations.
  *
  */
object Day21 {

  val input = aoc.Util.readInput("Day21input.txt")

  val start = arrayOf(
    ".#.".toCharArray(),
    "..#".toCharArray(),
    "###".toCharArray()
  )

  data class Rule(val from: String, val to: Grid)

  fun gridToString(thiz: Grid): String {
    return thiz.map { it.joinToString("") }.joinToString("/")
  }

  fun parseInput(input: List<String>): List<Rule> {
    require(input.isNotEmpty()) { "input.nonEmpty failed" }

    fun Array<CharArray>.transposed(): Array<CharArray> {
      require(this.isNotEmpty()) { "this.isNotEmpty() failed" }
      require(this.first().isNotEmpty()) { "this.first().isNotEmpty() failed" }

      val rows = this.size
      val cols = this.first().size

      val transpose = Array(cols) { CharArray(rows) }
      for (r in 0..rows - 1) {
        for (c in 0..cols - 1) {
          transpose[c][r] = this[r][c]
        }
      }
      return transpose
    }

    fun rotateClockWise(thiz: Grid): Grid {
      return thiz.transposed().map { it.reversed().toCharArray() }.toTypedArray()
    }

    fun rotations(fromGrid: Grid): List<Grid> {
      return (1..3).fold(Pair(fromGrid, emptyList<Grid>()), { p: Pair<Grid, List<Grid>>, _: Int ->
        val (g, gs) = p
        val rotated = rotateClockWise(g)
        Pair(rotated, gs + listOf(rotated))
      }).second
    }

    fun flipHorizontal(thiz: Grid): Grid {
      return thiz.reversed().toTypedArray()
    }

    fun flips(fromGrid: Grid): List<Grid> {
      return listOf(fromGrid) +
      listOf(flipHorizontal(fromGrid)) +
      rotations(fromGrid) +
      rotations(flipHorizontal(fromGrid))
    }

    return input.flatMap { l ->
      // ../.. => ###/.##/#..
      val from = l.substring(0, l.indexOf('=') - 1)
      val to = l.substring(l.indexOf('=') + 3)
      val fromGrid = from.split('/').map { it.toCharArray() }.toTypedArray()
      val toGrid = to.split('/').map { it.toCharArray() }.toTypedArray()

      flips(fromGrid).map { g -> Rule(gridToString(g), toGrid) }
    }
  } //ensuring(_.size >= input.size)

  fun copy(row: Int, col: Int, size: Int, thiz: Grid): Grid {
    require(row in 0..thiz.size-1) { "row >= 0 && row < thiz.size failed; with >${row}<" }
    require(col in 0..thiz.size-1) { "col >= 0 && col < thiz.size failed; with >${col}<" }
    require(size in 1..thiz.size) { "size >= 1 && size <= thiz.size; with >${size}<" }

    val copied = Array(size) { CharArray(size) }
    var rr = 0
    for(r in row..row + size - 1) {
      var cc = 0
      for (c in col..col + size - 1) {
        copied[rr][cc] = thiz[r][c]
        cc = cc + 1
      }
      rr = rr + 1
    }

    return copied
  } //ensuring(_.size == size)

  fun divide(thiz: Grid): List<Grid> {
    val stepSize =
      if(thiz.size % 2 == 0) 2
      else if(thiz.size % 3 == 0) 3
      else {
        assert(false)
        0
      }

    var grids = emptyList<Grid>()
    for(row in 0..thiz.size-1 step stepSize) {
      for(col in 0..thiz.size-1 step stepSize) {
        grids = grids + listOf(copy(row, col, stepSize, thiz))
      }
    }

    return grids
  } //ensuring(gs => gs.nonEmpty && gs.head.size <= thiz.size)

  fun enhance(thiz: List<Grid>, rules: List<Rule>): List<Grid> {
    require(thiz.isNotEmpty()) { "thiz.nonEmpty failed" }
    require(rules.isNotEmpty()) { "rules.nonEmpty failed" }

    val result = thiz.map { g ->
      val rule = rules.find { r -> r.from == gridToString(g) }!!
      rule.to
    }

    return result
  } //ensuring(gs => gs.nonEmpty && gs.head.size > thiz.head.size)

  fun join(thiz: List<Grid>): Grid {
    require(thiz.isNotEmpty()) { "thiz.nonEmpty failed" }

    val rows = thiz.first().size
    val windowSize = Math.sqrt(thiz.size.toDouble()).toInt()
    val grids2Dim = thiz.windowed(windowSize, windowSize)

    var grid = emptyArray<CharArray>()
    for(gs in grids2Dim) {
      for(r in 0..rows-1) {
        var line = emptyArray<Char>().toCharArray()
        for (g in gs) {
          line = line + g[r]
        }
        grid = grid.plus(line)
      }
    }

    return grid
  } //ensuring(g => g.size == Math.sqrt(thiz.size).toInt * thiz(0).size)

  fun run(current: Grid, rules: List<Rule>, iterations: Int): Grid {
    require(current.isNotEmpty()) { "current.nonEmpty failed" }
    require(rules.isNotEmpty()) { "rules.nonEmpty failed" }

    return if(iterations <= 0) current
    else run(join(enhance(divide(current), rules)), rules, iterations - 1)
  }

  object Part1 {
    fun solve(input: List<String>): Int {
      return run(start, parseInput(input), 5).flatMap { it.asIterable() }.count { it == '#' }
    }
  }

  object Part2 {
    fun solve(input: List<String>): Int {
      return run(start, parseInput(input), 18).flatMap { it.asIterable() }.count { it == '#' }
    }
  }
}