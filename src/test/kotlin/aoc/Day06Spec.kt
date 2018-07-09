package aoc.day06

import io.kotlintest.*
import io.kotlintest.specs.ShouldSpec

class Day06Spec : ShouldSpec({
  "solve()" should {
    "part1" {
      Day06.Part1.solve(Day06.input) shouldBe 14029
    }

    "part2" {
      Day06.Part2.solve(Day06.input) shouldBe 2765
    }
  }
})
