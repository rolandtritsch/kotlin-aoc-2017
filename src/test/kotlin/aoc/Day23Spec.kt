package aoc.day23

import io.kotlintest.*
import io.kotlintest.specs.StringSpec

object JustThisOne : Tag()

class Day23Spec : StringSpec({

  "should solve()".config(tags = setOf(JustThisOne)) {
    "part1" {
      Day23.Part1.solve(Day23.input) shouldBe 6724
    }

    "part2" {
      Day23.Part2.solve(Day23.input) shouldBe 903
    }
  }
})
