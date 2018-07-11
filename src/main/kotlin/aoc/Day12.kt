package aoc.day12

typealias Pipes = Map<Int, List<Int>>

/** Problem: [[https://adventofcode.com/2017/day/12]]
  *
  * Solution:
  *
  * General - A pipe is just an entry in a map that expresses
  * which program can communicate with which other program.
  *
  * Part1 - To find the number of programs that are connected
  * to program 0, we just walk the tree. We start with the root (0)
  * and visit all children until we have seen all nodes. When we hit
  * a node that we have already seen we can stop looking (because we
  * know, that we have hit a loop). At the end we just need to return
  * the number of nodes we collected while we traversed the tree.
  *
  * Part2 - To find the number of groups, we first take all nodes, then
  * we find all nodes that belong to group 0 (the group with the root 0).
  * Then we get rid of all nodes that are part of group 0, pick the first
  * node of the rest and build the next group. Then we diff the remaining
  * nodes with the ones that we just found group N. Until no nodes are
  * left over.
  */
object Day12 {

  val input = aoc.Util.readInput("Day12input.txt")

  fun parseInput(input: List<String>): Pipes = input.map { line ->
    val tokens = line.split(" ", ",")
    val source = tokens.get(0).toInt()
    val sink = (2..tokens.size step 2).map { i -> tokens.get(i).toInt() }.toList()
    source to sink
  }.toMap()

  fun findPrograms(start: Int, graph: Pipes): List<Int> {
    fun go(node: Int, graph: Pipes, seenAlready: List<Int>): List<Int> =
      if(seenAlready.contains(node)) seenAlready
      else {
        val nodes = graph.get(node)!!
        nodes.fold(seenAlready + node, { seen, n -> go(n, graph, seen) })
      }
    return go(start, graph, emptyList<Int>())
  }

  fun findGroups(graph: Pipes): List<List<Int>> {
    fun go(nodes: List<Int>, graph: Pipes, groups: List<List<Int>>): List<List<Int>> =
      if(nodes.isEmpty()) groups
      else {
        val nextGroup = findPrograms(nodes.first(), graph)
        go(nodes.minus(nextGroup), graph, groups.plus(listOf(nextGroup)))
      }

    val nodes = graph.keys.toList()
    val groupZero = findPrograms(0, graph)
    return go(nodes.minus(groupZero), graph, listOf(groupZero))
  }

  object Part1 {
    fun solve(input: List<String>): Int = findPrograms(0, parseInput(input)).size
  }

  object Part2 {
    fun solve(input: List<String>): Int = findGroups(parseInput(input)).size
  }
}
