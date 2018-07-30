package aoc.day21

import io.kotlintest.*
import io.kotlintest.specs.StringSpec

class Day21Spec : StringSpec({

  "should solve()".config(timeout = 60.minutes) {
    "part1" {
      Day21.Part1.solve(Day21.input).first shouldBe 205
    }

    "part2" {
      Day21.Part2.solve(Day21.input).first shouldBe 3389823
    }
  }
})