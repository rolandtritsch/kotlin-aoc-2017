package aoc

import io.kotlintest.*
import io.kotlintest.specs.ShouldSpec

class Day09Spec : ShouldSpec({
  "solve()" should {
    "solve part1" {
      Day09.Part1.solve(Day09.input) shouldBe 10800
    }

    "solve part2" {
      Day09.Part2.solve(Day09.input) shouldBe 4522
    }
  }
})