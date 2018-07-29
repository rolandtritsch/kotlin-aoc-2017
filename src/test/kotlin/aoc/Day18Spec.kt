package aoc.day18

import io.kotlintest.*
import io.kotlintest.specs.ShouldSpec

class Day18Spec : ShouldSpec({

  "solve()" should {
    "part1" {
      Day18.Part1.solve(Day18.input).first shouldBe 3188
    }

    "part2" {
      Day18.Part2.solve(Day18.input).first shouldBe 7112
    }
  }
})
