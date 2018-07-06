package aoc.day02

import io.kotlintest.*
import io.kotlintest.specs.ShouldSpec

class Day02Spec : ShouldSpec({
  "solve()" should {
    "solve part1" {
      Day02.Part1.solve(Day02.input) shouldBe 34925
    }

    "solve part2" {
      Day02.Part2.solve(Day02.input) shouldBe 221
    }
  }
})
