package aoc.day05

import io.kotlintest.*
import io.kotlintest.specs.ShouldSpec

class Day05Spec : ShouldSpec({
  "solve()" should {
    "part1" {
      Day05.Part1.solve(Day05.input).first shouldBe 372139
    }

    "part2" {
      Day05.Part2.solve(Day05.input).first shouldBe 29629538
    }
  }
})
