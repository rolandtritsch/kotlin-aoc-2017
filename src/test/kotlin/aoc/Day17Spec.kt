package aoc.day17

import io.kotlintest.*
import io.kotlintest.specs.ShouldSpec

class Day17Spec : ShouldSpec({

  "solve()" should {
    "part1" {
      Day17.Part1.solve(Day17.steps, Day17.times) shouldBe 1311
    }

    "part2" {
      Day17.Part2.solve(Day17.steps, Day17.times2) shouldBe 39170601
    }
  }
})
