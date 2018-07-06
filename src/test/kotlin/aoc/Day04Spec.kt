package aoc.day04

import io.kotlintest.*
import io.kotlintest.specs.ShouldSpec

class Day04Spec : ShouldSpec({
  "solve()" should {
    "solve part1" {
      Day04.Part1.solve(Day04.input) shouldBe 383
    }

    "solve part2" {
      Day04.Part2.solve(Day04.input) shouldBe 265
    }
  }
})