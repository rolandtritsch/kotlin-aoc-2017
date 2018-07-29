package aoc.day19

typealias DirectionType = Char

/** Problem: [[http://adventofcode.com/2017/day/19]]
  *
  * Solution:
  *
  * General - Using a very simple state machine, that takes the current field
  * and the current direction and (based on these two inputs) decide what the
  * next state is. We can then (recursively) process states until we get out
  * of the maze.
  *
  * Part1 - Collect all letters, while walking the maze.
  *
  * Part2 - Count the steps, while walking the maze.
  *
  */
object Day19 {

  val input = aoc.Util.readInput("Day19input.txt").map { it.toCharArray() }.toTypedArray()

  object Direction {
    val UP = 'U'
    val DOWN = 'D'
    val LEFT = 'L'
    val RIGHT = 'R'
  }

  data class State(
    val row: Int,
    val col: Int,
    val direction: DirectionType,
    val maze: Array<CharArray>,
    val steps: Int,
    val path: String, val done: Boolean
  ) {
    fun next(): State = when(maze[row][col]) {
      '|' -> if(direction == Direction.UP) State(row - 1, col, Direction.UP, maze, steps + 1, path, false)
      else if(direction == Direction.DOWN) State(row + 1, col, Direction.DOWN, maze, steps + 1, path, false)
      else if(direction == Direction.RIGHT) State(row, col + 1, Direction.RIGHT, maze, steps + 1, path, false)
      else if(direction == Direction.LEFT) State(row, col - 1, Direction.LEFT, maze, steps + 1, path, false)
      else { assert(false) { "Unkown direction" }; State(0, 0, ' ', emptyArray(), 0, "", true)  }

      '-' -> if(direction == Direction.UP) State(row - 1, col, Direction.UP, maze, steps + 1, path, false)
      else if(direction == Direction.DOWN) State(row + 1, col, Direction.DOWN, maze, steps + 1, path, false)
      else if(direction == Direction.RIGHT) State(row, col + 1, Direction.RIGHT, maze, steps + 1, path, false)
      else if(direction == Direction.LEFT) State(row, col - 1, Direction.LEFT, maze, steps + 1, path, false)
      else { assert(false) { "Unkown direction" }; State(0, 0, ' ', emptyArray(), 0, "", true)  }

      '+' -> {
        if(direction == Direction.LEFT || direction == Direction.RIGHT) {
          if(row > 0 && maze[row - 1][col] != ' ') State(row - 1, col, Direction.UP, maze, steps + 1, path, false)
          else if(row < maze.size - 1 && maze[row + 1][col] != ' ') State(row + 1, col, Direction.DOWN, maze, steps + 1, path, false)
          else {
            assert(false) { "Unknown state" }
            State(0, 0, ' ', emptyArray(), 0, "", true)
          }
        } else if(direction == Direction.UP || direction == Direction.DOWN) {
          if(col > 0 && maze[row][col - 1] != ' ') State(row, col - 1, Direction.LEFT, maze, steps + 1, path, false)
          else if(col < maze[row].size - 1 && maze[row][col + 1] != ' ') State(row, col + 1, Direction.RIGHT, maze, steps + 1, path, false)
          else {
            assert(false) { "Unknown state" }
            State(0, 0, ' ', emptyArray(), 0, "", true)
          }
        } else {
          assert(false) { "Unknown state" }
          State(0, 0, ' ', emptyArray(), 0, "", true)
        }
      }

      else -> {
        assert(maze[row][col] in 'A'..'Z')

        if(direction == Direction.DOWN) {
          if(row == maze.size - 1 || maze[row + 1][col] == ' ') State(row, col, direction, maze, steps + 1, path + maze[row][col], true)
          else State(row + 1, col, direction, maze, steps + 1, path + maze[row][col], false)
        } else if(direction == Direction.UP) {
          if(row == 0 || maze[row - 1][col] == ' ') State(row, col, direction, maze, steps + 1, path + maze[row][col], true)
          else State(row - 1, col, direction, maze, steps + 1, path + maze[row][col], false)
        } else if(direction == Direction.LEFT) {
          if(col == 0 || maze[row][col - 1] == ' ') State(row, col, direction, maze, steps + 1, path + maze[row][col], true)
          else State(row, col - 1, direction, maze, steps + 1, path + maze[row][col], false)
        } else if(direction == Direction.RIGHT) {
          if(col == maze[row].size - 1 || maze[row][col + 1] == ' ') State(row, col, direction, maze, steps + 1, path + maze[row][col], true)
          else State(row, col + 1, direction, maze, steps + 1, path + maze[row][col], false)
        } else {
          assert(false)  { "Unknown state" }
          State(0, 0, ' ', emptyArray(), 0, "", false)
        }
      }
    }
  }

  fun walkTheMaze(maze: Array<CharArray>): Pair<String, Int> {
    tailrec fun go(s: State): State {
      return if(s.done) s
      else go(s.next())
    }

    val finalState = go(State(0, maze[0].indexOf('|'), Direction.DOWN, maze, 0, "", false))
    return Pair(finalState.path, finalState.steps)
  }

  object Part1 {
    fun solve(input: Array<CharArray>): Pair<String, Long> = aoc.Util.measureTimeMillis("") {
      walkTheMaze(input).first
    }
  }

  object Part2 {
    fun solve(input: Array<CharArray>): Pair<Int, Long> = aoc.Util.measureTimeMillis(0) {
      walkTheMaze(input).second
    }
  }
}
