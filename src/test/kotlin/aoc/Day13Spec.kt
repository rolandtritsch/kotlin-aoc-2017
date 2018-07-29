package aoc.day13

import io.kotlintest.*
import io.kotlintest.specs.ShouldSpec

class Day13Spec : ShouldSpec({

  "solve()" should {
    "part1" {
      Day13.Part1.solve(Day13.input).first shouldBe 1632
    }
    "part2" {
      Day13.Part2.solve(Day13.input).first shouldBe 3834136
    }
  }
})
