package aoc.day06

typealias MemoryBanks = List<Int>

/** Problem: [[https://adventofcode.com/2017/day/6]]
  *
  * General - This is a [[https://en.wikipedia.org/wiki/Cycle_detection cycle detection problem]].
  *
  * Part1 - Is lambda + mu.
  *
  * Part2 - Is just lambda.
  *
  * @see [[https://en.wikipedia.org/wiki/Cycle_detection#Floyd's_Tortoise_and_Hare]]
  */
object Day06 {

  val input = aoc.Util.readInput("Day06input.txt").first().split('\t').map { it.toInt() }.toList()

  fun <T> List<T>.updated(n: Int, v: T): List<T> {
    val l = this.toMutableList()
    l[n] = v
    return l
  }

  fun cycle(banks: MemoryBanks): MemoryBanks {
    val mostBlocksIndex = banks.indexOfFirst { it == banks.max()!! }

    val cycledBanks = (1..banks.max()!!).fold(banks.updated(mostBlocksIndex, 0), { currentBanks, i ->
      val n = (mostBlocksIndex + i) % banks.size
      currentBanks.updated(n, currentBanks.get(n) + 1)
    })

    return cycledBanks
  }

  fun floyd(banks: MemoryBanks, cycle: (MemoryBanks) -> MemoryBanks): Pair<Int, Int> {
    tailrec fun phase1(tortoise: MemoryBanks, hare: MemoryBanks): MemoryBanks {
      return if(tortoise == hare) hare
        else phase1(cycle(tortoise), cycle(cycle(hare)))
    }

    val hare = phase1(cycle(banks), cycle(cycle(banks)))

    tailrec fun phase2(tortoise: MemoryBanks, hare: MemoryBanks, mu: Int): Pair<MemoryBanks, Int> {
      return if(tortoise == hare) Pair(tortoise, mu)
        else phase2(cycle(tortoise), cycle(hare), mu + 1)
    }

    val (tortoise, mu) = phase2(banks, hare, 0)

    tailrec fun phase3(tortoise: MemoryBanks, hare: MemoryBanks, lambda: Int): Int {
      return if(tortoise == hare) lambda
        else phase3(tortoise, cycle(hare), lambda + 1)
    }

    val lambda = phase3(tortoise, cycle(tortoise), 1)

    return Pair(lambda, mu)
  }

  object Part1 {
    fun solve(banks: List<Int>): Pair<Int, Long> = aoc.Util.measureTimeMillis(0) {
      val (lambda, mu) = floyd(banks, Day06::cycle)
      lambda + mu
    }
  }

  object Part2 {
    fun solve(banks: List<Int>): Pair<Int, Long> = aoc.Util.measureTimeMillis(0) {
      val (lambda, _) = floyd(banks, Day06::cycle)
      lambda
    }
  }
}
