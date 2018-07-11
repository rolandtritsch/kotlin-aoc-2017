package aoc.day11

import io.kotlintest.*
import io.kotlintest.specs.ShouldSpec

class Day11Spec : ShouldSpec({

  "solve()" should {
    "part1" {
      Day11.Part1.solve(Day11.input) shouldBe 810
    }
    "part2" {
      Day11.Part2.solve(Day11.input) shouldBe 1567
    }
  }
})
