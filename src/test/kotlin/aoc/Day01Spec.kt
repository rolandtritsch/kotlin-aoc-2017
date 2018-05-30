package aoc

class Day01Spec : ShouldSpec({
  "solve() - Part1" should {
    "solve the puzzle" {
      Day01.Part1.solve(Day01.input) shouldBe 1223
    }
  }

  "solve() - Part2" should {
    "solve the puzzle" {
      Day01.Part2.solve(Day01.input) shouldBe 1284
    }
  }
})