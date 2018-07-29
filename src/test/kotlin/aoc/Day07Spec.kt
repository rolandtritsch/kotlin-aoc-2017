package aoc.day07

import io.kotlintest.*
import io.kotlintest.specs.ShouldSpec

class Day07Spec : ShouldSpec({
  "solve()" should {
    "part1" {
      Day07.Part1.solve(Day07.input).first shouldBe "uownj"
    }

    "part2" {
      Day07.Part2.solve(Day07.input).first shouldBe 596
    }
  }
})
