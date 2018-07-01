package aoc

import io.kotlintest.*
import io.kotlintest.specs.ShouldSpec

class Day07Spec : ShouldSpec({
  "solve()" should {
    "solve part1" {
      Day07.Part1.solve(Day07.input) shouldBe "uownj"
    }
/*
    "solve part2" {
      Day07.Part2.solve(Day07.input) shouldBe 596
    }
    */
  }
})
