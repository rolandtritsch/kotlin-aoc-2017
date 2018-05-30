package aoc

import io.kotlintest.*
import io.kotlintest.specs.ShouldSpec

class Day02Spec : ShouldSpec({
  "solve()" should {
    "solve Part1" {
      Day02.Part1.solve(Day02.input) shouldBe 0
      //Day02.Part1.solve(Day02.input) shouldBe 34925
    }

    "solve Part2" {
      Day02.Part2.solve(Day02.input) shouldBe 0
      //Day02.Part2.solve(Day02.input) shouldBe 221
    }
  }
})
