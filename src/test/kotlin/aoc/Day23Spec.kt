package aoc.day23

import io.kotlintest.*
import io.kotlintest.specs.StringSpec

class Day23Spec : StringSpec({

  "should solve()" {
    "part1" {
      Day23.Part1.solve().first shouldBe 6724
    }

    "part2" {
      Day23.Part2.solve().first shouldBe 903
    }
  }
})
