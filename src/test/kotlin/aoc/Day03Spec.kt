package aoc

import io.kotlintest.*
import io.kotlintest.specs.ShouldSpec

class Day03Spec : ShouldSpec({
  "solve()" should {
    "solve part1" {
      Day03.Part1.solve(Day03.input) shouldBe 371
    }

    "solve part2" {
      Day03.Part2.solve(Day03.input) shouldBe 369601
    }
  }
})
