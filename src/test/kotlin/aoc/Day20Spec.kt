package aoc.day20

import io.kotlintest.*
import io.kotlintest.specs.ShouldSpec

class Day20Spec : ShouldSpec({

  "solve()" should {
    "part1" {
      Day20.Part1.solve(Day20.input).first shouldBe 243
    }

    "part2" {
      Day20.Part2.solve(Day20.input).first shouldBe 648
    }
  }
})
