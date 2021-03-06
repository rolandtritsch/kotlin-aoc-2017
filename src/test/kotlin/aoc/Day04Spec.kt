package aoc.day04

import io.kotlintest.*
import io.kotlintest.specs.ShouldSpec

class Day04Spec : ShouldSpec({
  "solve()" should {
    "part1" {
      Day04.Part1.solve(Day04.input).first shouldBe 383
    }

    "part2" {
      Day04.Part2.solve(Day04.input).first shouldBe 265
    }
  }
})
