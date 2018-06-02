package aoc

data class Move(val x: Int, val y: Int)
typealias Moves = List<List<Move>>

/** Problem: [[https://adventofcode.com/2017/day/3]]
  *
  * Solution:
  *
  * General - This is a/the refactored solution. I was only able to come
  * up with it after I saw Part2. My initial approach made the implementation
  * of Part2 hard (and/or at least not elegant).
  *
  * Now the idea is ...
  *
  *   - build a stream of moves
  *   - from these moves build a stream of cells
  *   - a cell has an index (starting from 0), coordinates (x, y - showing/
  *     describing the position of the cell relative to the center) and a value
  *     (the value of the cell)
  *   - Note: For Part1 constructing the cell value is trivial (based on the
  *     index). For Part2 it is more elaborate and requires to search the already
  *     evaluated stream for the 8 coordinates/values surrounding the cell that
  *     needs to be calculated.
  *
  * Part 1 - find the cell with the given value. Take the coordinates of
  * the cell and use them to calculate the Manhatten-Distance to the center
  * from it (realizing that the coordinates are effectively the Manhatten-
  * Distance :)).
  *
  * Part 2 - find the cell after the one with the given value and return
  * the value of that cell.
  *
  * @see [[https://oeis.org/A033951]]
  * @see [[https://oeis.org/A141481]]
  *
  */

object Day03 {

  val input = Util.readInput("Day03input.txt").first().toInt()

  object Advance {
    val up = Move(-1, 0)
    val down = Move(1, 0)
    val left = Move(0, -1)
    val right = Move(0, 1)
  }

  val firstLevelMoves = listOf(
    listOf(Advance.right),
    listOf(Advance.up),
    listOf(Advance.left, Advance.left),
    listOf(Advance.down, Advance.down),
    listOf(Advance.right, Advance.right)
  )

  fun nextLevelMoves(currentLevelMoves: Moves): Moves  {
    require(currentLevelMoves.size > 0) { "currentLevelMoves.nonEmpty failed" }
    require(currentLevelMoves.all { it.size > 0 }) { "currentLevelMoves.all(it.nonEmpty) failed" }

    val moves = listOf(
      currentLevelMoves.get(0),
      currentLevelMoves.get(1) + listOf(Advance.up, Advance.up),
      currentLevelMoves.get(2) + listOf(Advance.left, Advance.left),
      currentLevelMoves.get(3) + listOf(Advance.down, Advance.down),
      currentLevelMoves.get(4) + listOf(Advance.right, Advance.right)
    )

    assert(moves.flatten().size == currentLevelMoves.flatten().size + 8) {
      "moves.flatten().size == currentLevelMoves.flatten().size + 8 failed"
    }
    return moves
  }

  fun moves(seed: Moves): Sequence<Moves> {
    return generateSequence(seed, { nextLevelMoves(it) })
  }

  data class Cell(val index: Int, val value: Int, val coordinates: Pair<Int, Int>)

  object Part1 {
    fun cells(moves: Iterator<Move>): Sequence<Cell> {
      fun nextCell(previousCell: Cell, moves: Iterator<Move>): Cell {
        val move = moves.next()

        val thisCellIndex = previousCell.index + 1
        val thisCellCoordinates = Pair(previousCell.coordinates.first + move.x, previousCell.coordinates.second + move.y)
        val thisCellValue = previousCell.value + 1
        val thisCell = Cell(thisCellIndex, thisCellValue, thisCellCoordinates)
        return thisCell
      }

      val centerCell = Cell(0, 1, Pair(0, 0))
      val cells = generateSequence(centerCell, { nextCell(it, moves) })
      return cells
    }

    fun solve(cellValueToFind: Int): Int {
      val spiral = cells(moves(firstLevelMoves).flatten().flatten().iterator())
      val coordinates = spiral.find { it.value == cellValueToFind }!!.coordinates
      val distance = calcManhattenDistance(coordinates)
      return distance
    }

    fun calcManhattenDistance(coordinates: Pair<Int, Int>): Int {
      return Math.abs(coordinates.first) + Math.abs(coordinates.second)
    }
  }

/*
  object Part2 {
    /** Using stream of moves to produce stream of cells.
      *
      * But ... this time around creating the cell value (the sum of all cell values
      * around the cell we are creating) is not straight forward.
      *
      * To make this work we need to maintain a map of the cell values that we have
      * already created (and need to be able to look them up by coordinates).
      *
      * We also need to cater for the case that (while we calc the cell value from
      * the 8 cells around a given cell) some of the 8 cells (on the "outerside")
      * have not been created yet. We do this, by giving the map a default value
      * of 0 (that means I can just *blindly* look up all 8 cell cooordinates
      * around the given cell and for the once that do not exist yet, I am just
      * getting a value of 0).
      */
    fun cells(moves: Iterator<Move>): Stream<Cell> {
      fun nextCell(previousCell: Cell, valuesSoFar: Map<(Int, Int), Int>): Pair(Cell, Map<(Int, Int), Int>) {
        val move = moves.next()

        val thisCellIndex = previousCell.index + 1
        val thisCellCoordinates = (previousCell.coordinates.first + move.x, previousCell.coordinates.second + move.y)
        val thisCellValue = calcValue(thisCellCoordinates, valuesSoFar)
        val thisCell = Cell(thisCellIndex, thisCellValue, thisCellCoordinates)
        return (thisCell to (valuesSoFar + (thisCellCoordinates to thisCellValue))
      }

      val centerCell = Cell(0, 1, (0, 0))
      val valuesSoFar = Map.empty[(Int, Int), Int].withDefaultValue(0) + (centerCell.coordinates -> centerCell.value)
      val cells = Stream.iterate((centerCell, valuesSoFar), (c, v) -> nextCell(c, v))
      return cells
    }

    def solve(cellValueToFind: Int): Int = {
      val spiral = cells(moves(firstLevelMoves).iterator())
      val nextBiggerValue = spiral.find(c -> c.value > cellValueToFind).get.value
      return nextBiggerValue
    }

    fun calcValue(currentCoordinates: (Int, Int), valuesSoFar: Map<(Int, Int), Int>): Int {
      require(valuesSoFar.size > 0) { "valuesSoFar.nonEmpty failed" }

      val x = currentCoordinates.first
      val y = currentCoordinates.second

      val nextValue = valuesSoFar(x - 1, y) + valuesSoFar(x - 1, y - 1) + valuesSoFar(x - 1, y + 1) +
        valuesSoFar(x + 1, y) + valuesSoFar(x + 1, y - 1) + valuesSoFar(x + 1, y + 1) +
        valuesSoFar(x, y - 1) + valuesSoFar(x, y + 1)
      assert(nextValue -> nextValue == 1 || nextValue > valuesSoFar.values().max())
      return nextValue
    }
  }
*/
}
