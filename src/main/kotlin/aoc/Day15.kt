package aoc.day15

import kotlin.coroutines.experimental.buildIterator

/** Problem: [[http://adventofcode.com/2017/day/15]]
  *
  * Solution:
  *
  * General - This was heavily refactored. Twice :). I started with a stream
  * based/recursive approach. Then I tried to *just* use the stream (with take/count),
  * but stream is an IteraterableAgain and that means it keeps the front of the stream
  * around and that means we (sooner or later) run out of mem (initially using Strings
  * did not help either; with the mem and the performance). The right approach is
  * obviously to use an Iterator. And then do a recursion.
  *
  * Part1 - solve the puzzle with a modolo of (1, 1) and a depth of 40 * 10e6.
  *
  * Part2 - solve the puzzle with a modolo of (4, 8) and a depth of 5 * 10e6.
  */

object Day15 {
  // val in = ... - the only one with no *in*, means I am not reading Day15input.txt.
  // I just copied the values from it :).

  object Default {
    val startA = 703L
    val startB = 516L

    val factorA = 16807L
    val factorB = 48271L

    val devider = 2147483647L //Int.MaxValue

    fun next(c: Long, f: Long, d: Long, m: Long): Long {
      val n = (c * f) % d

      return if(n % m == 0L) n
      else next(n, f, d, m)
    }
  }

  data class GeneratorConfig(
    val start: Long,
    val factor: Long, val devider: Long,
    val modolo: Long,
    val next: (Long, Long, Long, Long) -> Long
  )

  fun generator(a: GeneratorConfig, b: GeneratorConfig): Iterator<Pair<Long, Long>> = buildIterator {
    var current = Pair(a.start, b.start)
    while(true) {
      yield(current)
      current = Pair(
        a.next(current.first, a.factor, a.devider, a.modolo),
        b.next(current.second, b.factor, b.devider, b.modolo)
      )
    }
  }

  fun matching(pair: Pair<Long, Long>): Boolean {
    return pair.first.and(0xffff) == pair.second.and(0xffff)
  }

  fun countMatchingPairs(gen: Iterator<Pair<Long, Long>>, depth: Int): Int {
    tailrec fun go(g: Iterator<Pair<Long, Long>>, d: Int, c: Int): Int {
      return if(d <= 0) c
      else if(matching(g.next())) go(g, d - 1, c + 1)
      else go(g, d - 1, c)
    }

    return go(gen, depth, 0)
  }

  fun run(modolo: Pair<Long, Long>, depth: Int): Int {
    val genA = GeneratorConfig(Default.startA, Default.factorA, Default.devider, modolo.first, Default::next)
    val genB = GeneratorConfig(Default.startB, Default.factorB, Default.devider, modolo.second, Default::next)
    val gen = generator(genA, genB)

    return countMatchingPairs(gen, depth)
  }


  object Part1 {
    fun solve(): Int = run(Pair(1, 1), 40000000)
  }

  object Part2 {
    fun solve(): Int = run(Pair(4, 8), 5000000)
  }
}
