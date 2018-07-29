package aoc.day14

import io.kotlintest.*
import io.kotlintest.specs.ShouldSpec

class Day14Spec : ShouldSpec({

  "solve()" should {
    "part1" {
      Day14.Part1.solve(Day14.input).first shouldBe 8292
    }
    "part2" {
      Day14.Part2.solve(Day14.input).first shouldBe 1069
    }
  }
})
