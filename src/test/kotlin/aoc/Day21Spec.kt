package aoc.day21

import io.kotlintest.*
import io.kotlintest.specs.ShouldSpec

class Day21Spec : ShouldSpec({

  "solve()" should {
    "part1" {
      Day21.Part1.solve(Day21.input) shouldBe 205
    }

    "part2" {
      Day21.Part2.solve(Day21.input) shouldBe 3389823
    }
  }
})
