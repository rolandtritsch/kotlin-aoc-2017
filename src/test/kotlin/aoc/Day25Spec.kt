package aoc.day25

import io.kotlintest.*
import io.kotlintest.specs.StringSpec

class Day25Spec : StringSpec({

  "should solve()" {
    "part1" {
      Day25.Part1.solve(Day25.input).first shouldBe 4769
    }
  }
})
