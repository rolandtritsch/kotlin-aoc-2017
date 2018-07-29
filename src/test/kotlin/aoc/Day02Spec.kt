package aoc.day02

import io.kotlintest.*
import io.kotlintest.specs.ShouldSpec

class Day02Spec : ShouldSpec({
  "solve()" should {
    "part1" {
      Day02.Part1.solve(Day02.input).first shouldBe 34925
    }

    "part2" {
      Day02.Part2.solve(Day02.input).first shouldBe 221
    }
  }
})
