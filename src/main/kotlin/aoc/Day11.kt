package aoc.day11

/** Problem: [[https://adventofcode.com/2017/day/11]]
  *
  * Solution:
  *
  * General - Calculating the [[https://en.wikipedia.org/wiki/Taxicab_geometry manhatten distance]] on a [[https://stackoverflow.com/a/47749887/2374327 hexagon grid]]
  * is a well understood problem. The implementation of the algorithm will go
  * through the grid and will collect/calculate the finalDistance and the maxDistance
  * that was found/encountered on the way to the final tile.
  *
  * Part1 - Return the (manhatten) distance of the final tile
  * from the center (tile).
  *
  * Part2 - Return the max distance that was encountered while
  * getting/going to the final/target tile.
  */
object Day11 {

  val input = aoc.Util.readInput("Day11input.txt").first().split(",").toList()

  data class Tile(val x: Int, val y: Int, val z: Int) {
    fun distance(that: Tile): Int {
      return (Math.abs(this.x - that.x) + Math.abs(this.y - that.y) + Math.abs(this.z - that.z)) / 2
    }
  }

  /** @see [[https://stackoverflow.com/a/47749887/2374327]]
    */
  fun calcSteps(path: List<String>): Pair<Int, Int> {
    val centerTile = Tile(0, 0, 0)
    val (finalTile, finalMax) = path.fold(Pair(centerTile, 0), { current, move ->
      val (currentTile, currentMax) = current
      val nextTile = when(move) {
        "n" -> Tile(currentTile.x, currentTile.y + 1, currentTile.z - 1)
        "ne" -> Tile(currentTile.x + 1, currentTile.y, currentTile.z - 1)
        "nw" -> Tile(currentTile.x - 1 , currentTile.y + 1, currentTile.z)
        "s" -> Tile(currentTile.x, currentTile.y - 1, currentTile.z + 1)
        "se" -> Tile(currentTile.x + 1, currentTile.y - 1, currentTile.z)
        "sw" -> Tile(currentTile.x - 1, currentTile.y, currentTile.z + 1)
        else -> {assert(false); Tile(0, 0, 0)}
      }
      Pair(nextTile, Math.max(currentMax, centerTile.distance(nextTile)))
    })
    return Pair(centerTile.distance(finalTile), finalMax)
  } //ensuring(result => result._1 >= 0 && result._2 >= result._1)

  object Part1 {
    fun solve(input: List<String>): Int = calcSteps(input).first
  }

  object Part2 {
    fun solve(input: List<String>): Int = calcSteps(input).second
  }
}