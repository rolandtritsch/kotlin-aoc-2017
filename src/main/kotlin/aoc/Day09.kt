package aoc.day09

/** Problem: [[https://adventofcode.com/2017/day/9]]
  *
  * Solution:
  *
  * General - My first implementation was based on a recursive processing
  * of the input stream. Hard to read. Hard to understand. Hard to extend/
  * maintain. And hard to run (needs a special -Xss setting (more/large
  * stack).
  *
  * This (refactored) implementation is using a state machine. The states
  * are OutOfGroup, InGroup, InGarbage, InCanceled ([[https://www.dropbox.com/s/pwogl9jhc8x7rqe/2018-02-21%2013.01.24.jpg?dl=0 pic]]). While I am running the
  * state machine I am collecting stats that will allow me (at the end) to
  * answer (the given) questions about the input stream.
  *
  * Part1 - Trivial. Collect and show the right stats.
  *
  * Part2 - Trivial. Collect and show the right stats.
  */

data class Statistics(val scores: List<Int> = emptyList<Int>(), val garbageCharCounter: Int = 0) {
  fun collectScore(score: Int): Statistics = Statistics(scores + score, garbageCharCounter)
  fun collectGarbage() = Statistics(scores, garbageCharCounter + 1)
}

sealed class State(open val level: Int, open val stats: Statistics) {
  abstract fun next(c: Char): State
}

data class BadState(override val level: Int = 0, override val stats: Statistics = Statistics()): State(level, stats) {
  override fun next(c: Char): State { assert(false); return BadState() }
}

data class OutOfGroup(override val level: Int, override val stats: Statistics): State(level, stats) {
  override fun next(c: Char): State = when(c) {
    '{' -> InGroup(level + 1, stats)
    else -> { assert(false); BadState() }
  }
}

data class InGroup(override val level: Int, override val stats: Statistics): State(level, stats) {
  override fun next(c: Char): State = when(c) {
    '{' -> InGroup(level + 1, stats)
    '}' -> if(level > 1) InGroup(level - 1, stats.collectScore(level)) else OutOfGroup(level - 1, stats.collectScore(level))
    '<' -> InGarbage(level, stats)
    else -> InGroup(level, stats)
  }
}

data class InGarbage(override val level: Int, override val stats: Statistics): State(level, stats) {
  override fun next(c: Char): State = when(c) {
    '!' -> InCanceled(level, stats)
    '>' -> InGroup(level, stats)
    else -> InGarbage(level, stats.collectGarbage())
  }
}

data class InCanceled(override val level: Int, override val stats: Statistics): State(level, stats) {
  override fun next(c: Char) = InGarbage(level, stats)
}

object Day09 {

  val input = aoc.Util.readInput("Day09input.txt").first().toList()

  data class StateMachine(val stream: List<Char>) {
    fun run(): State = stream.fold(OutOfGroup(0, Statistics()) as State, { currentState, c -> currentState.next(c) })
  }

  object Part1 {
    fun solve(input: List<Char>): Pair<Int, Long> = aoc.Util.measureTimeMillis(0) {
      val finalState = StateMachine(input).run()
      assert(finalState is OutOfGroup) { "finalState.isInstanceOf[OutOfGroup] failed" }
      finalState.stats.scores.sum()
    }
  }

  object Part2 {
    fun solve(input: List<Char>): Pair<Int, Long> = aoc.Util.measureTimeMillis(0) {
      val finalState = StateMachine(input).run()
      assert(finalState is OutOfGroup) { "finalState.isInstanceOf[OutOfGroup] failed" }
      finalState.stats.garbageCharCounter
    }
  }
}
