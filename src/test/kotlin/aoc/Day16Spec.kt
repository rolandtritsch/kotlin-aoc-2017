package aoc.day16

import io.kotlintest.*
import io.kotlintest.specs.ShouldSpec

class Day16Spec : ShouldSpec({

  "solve()" should {
    "part1" {
      Day16.Part1.solve(Day16.input).first shouldBe "bijankplfgmeodhc"
    }
    "part2" {
      Day16.Part2.solve(Day16.input).first shouldBe "bpjahknliomefdgc"
    }
  }
})
