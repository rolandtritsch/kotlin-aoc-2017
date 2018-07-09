package aoc.day10

import io.kotlintest.*
import io.kotlintest.specs.ShouldSpec

class Day10Spec : ShouldSpec({
  "solve()" should {
    "part1" {
      Day10.Part1.solve(Day10.input) shouldBe 4114
    }
/*
    "part2" {
      Day10.Part2.solve(Day10.input) shouldBe "2f8c3d2100fdd57cec130d928b0fd2dd"
    }
    */
  }
})
