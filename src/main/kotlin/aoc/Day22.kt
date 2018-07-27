package aoc.day22

/** Problem: [[http://adventofcode.com/2017/day/22]]
  *
  * Solution:
  *
  * General - Basicly I am implementing two grids (with a static/
  * pre-allocated size (not good; needs to be fixed)) and walk
  * these grids follwoing the rules in the problem statement.
  *
  * Part1 - Walk the [[SimpleGrid]] and count the infections.
  *
  * Part2 - Walk the [[AdvancedGrid]] and count the infections.
  *
  */
object Day22 {

  val input = aoc.Util.readInput("Day22input.txt")

  fun parseInput(input: List<String>): Array<CharArray> {
    return input.map { it.toCharArray() }.toTypedArray()
  }

  object Default {
    val ticks1 = 10000
    val ticks2 = 10000000
  }

  abstract class Grid(private val dimension: Int) {
    var numOfTicks = 0
    var numOfInfections = 0

    object State {
      val CLEAN = '.'
      val INFECTED = '#'
      val WEAKEND = 'W'
      val FLAGGED = 'F'
    }

    object Direction {
      val UP = "UP"
      val DOWN = "DOWN"
      val LEFT = "LEFT"
      val RIGHT = "RIGHT"
    }

    fun midPoint(): Int = grid.size / 2

    protected val grid = Array(dimension, {
      Array(dimension, { State.CLEAN })
    })

    protected var currentPosition = Pair(midPoint(), midPoint())
    protected var currentDirection = Direction.UP
    protected fun currentIsInfected() = grid[currentPosition.first][currentPosition.second] == State.INFECTED

    protected fun turnLeft() = when(currentDirection) {
      Direction.UP -> Direction.LEFT
      Direction.DOWN -> Direction.RIGHT
      Direction.LEFT -> Direction.DOWN
      Direction.RIGHT -> Direction.UP
      else -> { assert(false) { "Unknown direction" }; Direction.UP }
    }

    protected fun turnRight() = when(currentDirection) {
      Direction.UP -> Direction.RIGHT
      Direction.DOWN -> Direction.LEFT
      Direction.LEFT -> Direction.UP
      Direction.RIGHT -> Direction.DOWN
      else -> { assert(false) { "Unknown direction" }; Direction.UP }
    }

    fun mapInput(input: Array<CharArray>): Grid {
      val offset = (grid.size - input.size) / 2

      for(r in 0..input.size-1) {
        for (c in 0..input.size - 1) {
          grid[r + offset][c + offset] = input[r][c]
        }
      }

      return this
    }

    abstract fun tick(): Grid
  }

  data class SimpleGrid(private val d: Int) : Grid(d) {
    override fun tick(): SimpleGrid {
      numOfTicks = numOfTicks + 1
      val (row, col) = currentPosition

      currentDirection = when(grid[row][col]) {
        State.CLEAN -> turnLeft()
        State.INFECTED -> turnRight()
        else -> { assert(false) { "Unknown state" }; Direction.UP }
      }

      grid[row][col] = when(grid[row][col]) {
        State.CLEAN -> { numOfInfections = numOfInfections + 1; State.INFECTED }
        State.INFECTED -> State.CLEAN
        else -> { assert(false) { "Unknown state" }; State.CLEAN }
      }

      currentPosition = when(currentDirection) {
        Direction.UP -> Pair(row - 1, col)
        Direction.DOWN -> Pair(row + 1, col)
        Direction.LEFT -> Pair(row, col - 1)
        Direction.RIGHT -> Pair(row, col + 1)
        else -> { assert(false) { "Unknown direction" }; Pair(0, 0) }
      }

      return this
    }
  }

  data class AdvancedGrid(private val d: Int) : Grid(d) {
    fun doNotTurn() = currentDirection
    fun turnAround() = when(currentDirection) {
      Direction.UP -> Direction.DOWN
      Direction.DOWN -> Direction.UP
      Direction.LEFT -> Direction.RIGHT
      Direction.RIGHT -> Direction.LEFT
      else -> { assert(false) { "Unknown direction" }; Direction.UP }
    }

    override fun tick(): AdvancedGrid {
      numOfTicks = numOfTicks + 1
      val (row, col) = currentPosition

      currentDirection = when(grid[row][col]) {
        State.CLEAN -> turnLeft()
        State.WEAKEND -> doNotTurn()
        State.INFECTED -> turnRight()
        State.FLAGGED -> turnAround()
        else -> { assert(false) { "Unknown state" }; Direction.UP }
      }

      grid[row][col] = when(grid[row][col]) {
        State.CLEAN -> State.WEAKEND
        State.WEAKEND -> { numOfInfections = numOfInfections + 1; State.INFECTED }
        State.INFECTED -> State.FLAGGED
        State.FLAGGED -> State.CLEAN
        else -> { assert(false) { "Unknown state" }; State.CLEAN }
      }

      currentPosition = when(currentDirection) {
        Direction.UP -> Pair(row - 1, col)
        Direction.DOWN -> Pair(row + 1, col)
        Direction.LEFT -> Pair(row, col - 1)
        Direction.RIGHT -> Pair(row, col + 1)
        else -> { assert(false) { "Unknown direction" }; Pair(0, 0) }
      }

      return this
    }
  }

  tailrec fun run(grid: Grid, ticks: Int): Grid {
    return if(ticks <= 0) grid
    else run(grid.tick(), ticks - 1)
  }

  object Part1 {
    fun solve(input: List<String>): Int {
      return run(SimpleGrid(1001).mapInput(parseInput(input)), Default.ticks1).numOfInfections
    }
  }

  object Part2 {
    fun solve(input: List<String>): Int {
      return run(AdvancedGrid(1001).mapInput(parseInput(input)), Default.ticks2).numOfInfections
    }
  }
}
