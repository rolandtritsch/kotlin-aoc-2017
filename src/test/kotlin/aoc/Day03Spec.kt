package aoc.day03

import io.kotlintest.*
import io.kotlintest.specs.ShouldSpec

class Day03Spec : ShouldSpec({
  "solve()" should {
    "part1" {
      Day03.Part1.solve(Day03.input).first shouldBe 371
    }

    "part2" {
      Day03.Part2.solve(Day03.input).first shouldBe 369601
    }
  }
})
