package aoc.day09

import io.kotlintest.*
import io.kotlintest.specs.ShouldSpec

class Day09Spec : ShouldSpec({
  "solve()" should {
    "part1" {
      Day09.Part1.solve(Day09.input).first shouldBe 10800
    }

    "part2" {
      Day09.Part2.solve(Day09.input).first shouldBe 4522
    }
  }
})
