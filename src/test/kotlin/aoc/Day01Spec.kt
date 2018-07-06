package aoc.day01

import io.kotlintest.*
import io.kotlintest.specs.ShouldSpec

class Day01Spec : ShouldSpec({
  "solve()" should {
    "solve part1" {
      Day01.Part1.solve(Day01.input) shouldBe 1223
    }

    "solve part2" {
      Day01.Part2.solve(Day01.input) shouldBe 1284
    }
  }
})
