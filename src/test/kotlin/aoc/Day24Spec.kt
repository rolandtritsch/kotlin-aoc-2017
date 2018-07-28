package aoc.day24

import io.kotlintest.*
import io.kotlintest.specs.StringSpec

object JustThisOne : Tag()

class Day24Spec : StringSpec({

  "should solve()".config(tags = setOf(JustThisOne)) {
    "part1" {
      Day24.Part1.solve(Day24.input) shouldBe 1695
    }

    "part2" {
      Day24.Part2.solve(Day24.input) shouldBe 1673
    }
  }
})