package aoc.day22

import io.kotlintest.*
import io.kotlintest.specs.ShouldSpec

class Day22Spec : ShouldSpec({

  "solve()" should {
    "part1" {
      Day22.Part1.solve(Day22.input).first shouldBe 5305
    }

    "part2" {
      Day22.Part2.solve(Day22.input).first shouldBe 2511424
    }
  }
})
