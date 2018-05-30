package aoc

import io.kotlintest.*
import io.kotlintest.specs.ShouldSpec

class Day02Spec : ShouldSpec({
  "solve()" should {
    "solve part1" {
      Day02.Part1.solve(Day02.input) shouldBe 0
    }

    "solve part2" {
      Day02.Part2.solve(Day02.input) shouldBe 0
    }
  }
})
