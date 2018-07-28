package aoc.day24

/** Problem: [[http://adventofcode.com/2017/day/24]]
  *
  * Solution:
  *
  * General - Kind of simple. First you need to [[findZero]].
  * Then you need to [[findPath]] for one component. And then
  * you need to [[findPaths]] for all components. You basically
  * build a tree of all possible paths/solutions. And on that
  * you can then look for the strongest and/or for the longest
  * path and/or for ...
  *
  * Part1 - Just look for the strongest path (but that might
  * not be the longest path).
  *
  * Part2 - Now look for the longest path (and if you find
  * more than one longest one, pick the strongest of the
  * long ones).
  *
  */
 object Day24 {

  val input = aoc.Util.readInput("Day24input.txt")

  data class Component(val left: Int, val right: Int) {
    fun matches(port: Int) = left == port || right == port

    fun appendTo(l: List<Component>): List<Component> {
      return if(l.last().right == left) l + Component(left, right)
      else if(l.last().right == right) l + Component(right, left)
      else {
        assert(false); emptyList()
      }
    }
  }

  fun parseInput(input: List<String>): List<Component> {
    require(input.isNotEmpty()) { "input.nonEmpty failed" }

    return input.map { l ->
      val tokens = l.split("/")
      assert(tokens.size == 2)
      val port1 = tokens[0].toInt()
      val port2 = tokens[1].toInt()
      if(port1 <= port2) Component(port1, port2)
      else Component(port2, port1)
    }.sortedWith(compareBy { it.left })
  } //ensuring(_.nonEmpty, "_.nonEmpty failed")

  fun findZero(components: List<Component>): List<Component> {
    require(components.isNotEmpty()) { "components.nonEmpty failed" }

    return components.filter { c -> c.left == 0 }
  } //ensuring(_.size >= 1, "_.size >= 1 failed")

  fun findPath(head: List<Component>, rest: List<Component>, all: List<List<Component>>): List<List<Component>> {
    return if(rest.isEmpty()) all + listOf(head)
    else {
      val matches = rest.filter { it.matches(head.last().right) }
      if(matches.isEmpty()) all + listOf(head)
      else matches.fold(all, { acc, next ->
        findPath(next.appendTo(head), rest.minus(listOf(next)), acc)
      })
    }
  }

  fun findPaths(components: List<Component>): List<List<Component>> {
    return findZero(components).flatMap { z -> findPath(listOf(z), components.minus(listOf(z)), emptyList()) }
  }

  fun findStrongestPath(paths: List<List<Component>>): Pair<Int, List<Component>> {
    val strength = paths.map { p ->
      Pair(p.fold(0, { acc, c -> acc + c.left + c.right }), p)
    }
    return strength.maxBy { it.first }!!
  } //ensuring(_._1 >= 0, "_._1 >= 0 failed")

  fun findLongestPath(paths: List<List<Component>>): Pair<Int, Int> {
    val length = paths.map { p ->
      Pair(p.size, p.fold(0, { acc, c -> acc + c.left + c.right }))
    }
    val maxLength = length.maxBy { it.first }!!.first
    return length.filter { l -> l.first == maxLength }.maxBy { it.second }!!
  } //ensuring(_._2 >= 0, "_._2 >= 0 failed")

  object Part1 {
    fun solve(input: List<String>): Int {
      val (maxStrength, _) = findStrongestPath(findPaths(parseInput(input)))
      return maxStrength
    }
  }

  object Part2 {
    fun solve(input: List<String>): Int {
      val (_, maxLength) = findLongestPath(findPaths(parseInput(input)))
      return maxLength
    }
  }
}
