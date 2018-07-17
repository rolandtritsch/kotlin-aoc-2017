package aoc.day14

import aoc.day10.Day10

/** Problem: [[https://adventofcode.com/2017/day/14]]
  *
  * Solution:
  *
  * General - Using the solution from Day10, we are calculating
  * the 128x128 bit hash grid. Note: Instead of a grid of chars
  * [#.] I am using a grid of booleans.
  *
  * Part1 - We are counting all the trues in the hashes.
  *
  * Part2 - Is a little bit more tricky. I decided to look at it
  * as a tree search problem. I visit every square and if it is
  * used I recursively visit all adjacent squares until there are
  * no more squares to visit (means I have now visited and collected
  * all squares of the region). I do this for the entire grid and
  * will end up with a/the list of root nodes (coordinates) for
  * all regions.
  */
object Day14 {

  val input = aoc.Util.readInput("Day14input.txt").first()

  fun buildGrid(input: String): List<List<Boolean>> {
    require(input.isNotEmpty()) { "input.nonEmpty failed" }

    val grid = (0..127).map { row ->
      Day10.dense2hex(
        Day10.dense(
          Day10.sparse(
            Day10.encode(
              "${input}-${row}", Day10.suffix), Day10.seed, Day10.rounds).hash))
    }.toList()

    return grid.map { hex2bin(it) }.map { row -> row.map { it == '1' }}.toList()
  } //ensuring(result => result.size == 128 && result.forall(_.size == 128))

  fun hex2bin(hex: String): String {
    require(hex.isNotEmpty()) { "hex.nonEmpty failed" }

    return hex.fold("", { acc, c ->
      acc + java.math.BigInteger(c.toString(), 16).toString(2).padStart(4, '0')
    })
  } // ensuring(result => result.size == hex.size * 4)

  object Part1 {
    fun solve(input: String): Int {
      return buildGrid(input).fold(0, { sum, hash -> sum + hash.count { it }})
    }
  }

  fun findRegions(grid: List<List<Boolean>>): List<Pair<Int, Int>> {
    require(grid.isNotEmpty()) { "grid.nonEmpty failed" }

    fun i2RowCol(i: Int, size: Int): Pair<Int, Int> {
      val row = i / size
      val col = i % size
      return Pair(row, col)
    }

    val range = (0..(grid.size * grid.get(0).size) - 1).toList()
    val (regionRoots, _) = range.fold(
      Pair(emptyList<Pair<Int, Int>>(), emptyList<Pair<Int, Int>>()), { acc, i ->
      val (roots, alreadyVisited) = acc
      val (row, col) = i2RowCol(i, grid.size)
      if(grid.get(row).get(col) && !alreadyVisited.contains(Pair(row, col))) {
        val nodes = findRegion(row, col, grid)
        Pair(roots.plus(Pair(row, col)), alreadyVisited.union(nodes).toList())
      } else acc
    })

    return regionRoots
  } //ensuring(_.nonEmpty)

  // Get the next coordinates. Ignore the coordinates that are outside the grid.
  fun nextCoordinates(row: Int, col: Int, size: Int): List<Pair<Int, Int>> {
    require(row >= 0 && row <= size) { "row >= 0 && row <= size failed; with >${row}<" }
    require(col >= 0 && col <= size) { "col >= 0 && col <= size failed; with >${col}<" }

    val nextOffsets = listOf(Pair(0, 1), Pair(0, -1), Pair(1, 0), Pair(-1, 0))
    return nextOffsets.map { offset ->
      val (rOffset, cOffset) = offset
     Pair(row + rOffset, col + cOffset)
     }.filterNot { coordinates ->
       val (r, c) = coordinates
      r < 0 || c < 0 || r >= size || c >= size
     }
  }

  fun findRegion(row: Int, col: Int, grid: List<List<Boolean>>): List<Pair<Int, Int>> {
    require(row >= 0 && row <= grid.size) { "row >= 0 && row <= grid.size failed; with >${row}<" }
    require(col >= 0 && col <= grid.get(0).size) { "col >= 0 && col <= grid(0).size failed; with >${col}<" }
    require(grid.isNotEmpty()) { "grid.nonEmpty failed" }

    fun go(grid: List<List<Boolean>>, coordinates: List<Pair<Int, Int>>, alreadyVisited: List<Pair<Int, Int>>): List<Pair<Int, Int>> {
      val toVisit = coordinates.minus(alreadyVisited)
      return toVisit.fold(alreadyVisited, { acc, coordinate ->
        val (r, c) = coordinate
        if(grid.get(r).get(c)) go(grid, nextCoordinates(r, c, grid.size), acc.plus(coordinate))
        else acc
      })
    }

    return go(grid, nextCoordinates(row, col, grid.size), listOf(Pair(row, col)))
  } //ensuring(result => result.nonEmpty && result.contains(row, col))

  object Part2 {
    fun solve(input: String): Int {
      return findRegions(buildGrid(input)).size
    }
  }
}
