package aoc

import io.kotlintest.*
import io.kotlintest.specs.ShouldSpec

class Day08Spec : ShouldSpec({
  "solve()" should {
    "solve part1" {
      Day08.Part1.solve(Day08.input) shouldBe 4163
    }

    "solve part2" {
      Day08.Part2.solve(Day08.input) shouldBe 5347
    }
  }
})