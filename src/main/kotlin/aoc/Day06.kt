package aoc

typealias MemoryBanks = MutableList<Int>

/** Problem: [[https://adventofcode.com/2017/day/6]]
  *
  * General - This is a [[https://en.wikipedia.org/wiki/Cycle_detection cycle detection problem]].
  *
  * Part1 - Is lambda + mu.
  *
  * Part2 - Is just lambda.
  */
object Day06 {

  val input = Util.readInput("Day06input.txt").first().split('\t').map { it.toInt() }.toList()

  fun cycle(banks: MemoryBanks): MemoryBanks {
    val mostBlocksIndex = banks.indexOfFirst { it == banks.max()!! }

    banks[mostBlocksIndex] = 0
    val cycledBanks = (1..banks.max()!!).fold(banks, { currentBanks, i ->
      val n = (mostBlocksIndex + i) % banks.size
      currentBanks[n] = currentBanks[n] + 1
      currentBanks
    })

    return cycledBanks
  }

  /** @see [[https://en.wikipedia.org/wiki/Cycle_detection#Floyd's_Tortoise_and_Hare]]
    */
  fun floyd(banks: MemoryBanks, cycle: (MemoryBanks) -> MemoryBanks): Pair<Int, Int> {
    fun phase1(tortoise: MemoryBanks, hare: MemoryBanks): MemoryBanks {
      return if(tortoise == hare) hare
        else phase1(cycle(tortoise), cycle(cycle(hare)))
    }

    val hare = phase1(cycle(banks), cycle(cycle(banks)))

    fun phase2(tortoise: MemoryBanks, hare: MemoryBanks, mu: Int): Pair<MemoryBanks, Int> {
      return if(tortoise == hare) Pair(tortoise, mu)
        else phase2(cycle(tortoise), cycle(hare), mu + 1)
    }

    val p2 = phase2(banks, hare, 0)
    val tortoise = p2.first
    val mu = p2.second

    fun phase3(tortoise: MemoryBanks, hare: MemoryBanks, lambda: Int): Int {
      return if(tortoise == hare) lambda
        else phase3(tortoise, cycle(hare), lambda + 1)
    }

    val lambda = phase3(tortoise, cycle(tortoise), 1)

    return Pair(lambda, mu)
  }

  object Part1 {
    fun solve(banks: List<Int>): Int {
      val f = floyd(banks.toMutableList(), Day06::cycle)
      val lambda = f.first
      val mu = f.second
      return lambda + mu
    }
  }

  object Part2 {
    fun solve(banks: List<Int>): Int {
      val f = floyd(banks.toMutableList(), Day06::cycle)
      val lambda = f.first
      return lambda
    }
  }
}
