package aoc.day12

import io.kotlintest.*
import io.kotlintest.specs.ShouldSpec

class Day12Spec : ShouldSpec({

  "solve()" should {
    "part1" {
      Day12.Part1.solve(Day12.input) shouldBe 152
    }
    "part2" {
      Day12.Part2.solve(Day12.input) shouldBe 186
    }
  }
})