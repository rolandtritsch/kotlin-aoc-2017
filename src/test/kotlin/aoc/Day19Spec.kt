package aoc.day19

import io.kotlintest.*
import io.kotlintest.specs.ShouldSpec

class Day19Spec : ShouldSpec({

  "solve()" should {
    "part1" {
      Day19.Part1.solve(Day19.input) shouldBe "PVBSCMEQHY"
    }

    "part2" {
      Day19.Part2.solve(Day19.input) shouldBe 17736
    }
  }
})