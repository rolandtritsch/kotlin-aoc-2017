package aoc.day15

import io.kotlintest.*
import io.kotlintest.specs.ShouldSpec

class Day15Spec : ShouldSpec({

  "solve()" should {
    "part1" {
      Day15.Part1.solve() shouldBe 594
    }
    "part2" {
      Day15.Part2.solve() shouldBe 328
    }
  }
})
