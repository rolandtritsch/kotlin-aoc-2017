package aoc.day13

/** Problem: [[http://adventofcode.com/2017/day/13]]
  *
  * Solution:
  *
  * General - First implementation was a simulation. That turned out to be too slow
  * for Part2. Second implementation is calculating the solution/collisions. The main
  * idea is that the layers correspond to the number of picosecs, after 0 picosecs we
  * are on layer 0, after 1 on layer 1, and so on. With that it is easy to calculate,
  * if the packet gets dedected at/on that layer in the firewall ((number of picosecs)
  * modolo (the range of the scanner)).
  *
  * Part1 - Fold through the layers of the firewall and sum up the security score of
  * every layer, where the threat gets detected.
  *
  * Part2 - Add an offset/delay to the depth of the layers of the firewall. Increase
  * the delay until we can pass through the firewall undetected.
  */
object Day13 {

  val input = aoc.Util.readInput("Day13input.txt")

  fun parseInput(lines: List<String>): Map<Int, Int> {
    require(lines.isNotEmpty()) { "lines.nonEmpty failed" }

    return lines.map { l ->
      val tokens = l.split(" ", ":")
      val depth = tokens.get(0).toInt()
      val range = tokens.get(2).toInt()
      depth to range
    }.toMap()
  }

  fun buildFw(input: Map<Int, Int>): List<Pair<Int, Int>> {
    require(input.isNotEmpty()) { "input.nonEmpty failed" }

    val keys = 0..input.keys.max()!!
    val pairs = keys.map { k ->
      val v = input.get(k) ?: 0
      Pair (k, v)
    }

    return pairs.toList().sortedWith(compareBy({ it.first }, { it.second }))
  } //ensuring(result => result.size == input.keys.max + 1)

  fun threatDetected(depth: Int, range: Int): Boolean {
    require(depth >= 0) { "depth >= 0 failed; with >${depth}<" }
    require(range >= 0) { "range >= 0 failed; with >${range}<" }

    // Here we go: If the layer on the current depth has no range
    // the packet can never be caught (the layer is not able to
    // catch the packet, right). And if the range of the layer is
    // 1 the packet will always be caught. Otherwise we just do the
    // modolo operation, but ... we need to take into consideration
    // that the scanner is moving down and then up again (this is why
    // it is "*2".
    return if(range == 0) false
    else if(range == 1) true
    else depth % ((range - 1) * 2) == 0
  }

  fun calcSecScore(fw: List<Pair<Int, Int>>): Int {
    require(fw.isNotEmpty()) { "fw.nonEmpty failed" }

    return fw.fold(0, { secScore, layer ->
      val (depth, range) = layer
      if (threatDetected(depth, range)) secScore + depth * range
      else secScore
    })
  } //ensuring(result => result >= 0 && result <= fw.map {case (d, r) => d * r}.sum)

  object Part1 {
    fun solve(input: List<String>): Int {
      return calcSecScore(buildFw(parseInput(input)))
    }
  }

  fun passThrough(fw: List<Pair<Int, Int>>, delay: Int): Boolean {
    require(fw.isNotEmpty()) { "fw.nonEmpty failed" }

    return fw.all { p ->
      val (depth, range) = p
      !threatDetected(depth + delay, range)
    }
  }

  object Part2 {
    fun solve(input: List<String>): Int {
      tailrec fun go(fw: List<Pair<Int, Int>>, delay: Int): Int {
        return if(passThrough(fw, delay)) delay
        else go(fw, delay + 1)
      }

      return go(buildFw(parseInput(input)), 0)
    }
  }
}
