package aoc.day24

import io.kotlintest.*
import io.kotlintest.specs.StringSpec

class Day24Spec : StringSpec({

  "should solve()" {
    "part1" {
      Day24.Part1.solve(Day24.input).first shouldBe 1695
    }

    "part2" {
      Day24.Part2.solve(Day24.input).first shouldBe 1673
    }
  }
})
