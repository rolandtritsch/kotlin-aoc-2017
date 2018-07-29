package aoc.day25

/** Problem: [[http://adventofcode.com/2017/day/25]]
  *
  * Solution:
  *
  * General - And another [[State]] machine. Run the state machine for the
  * given number of steps/iterations (on the [[Tape]]).
  *
  * Part1 - Calc and return the [[Tape.checkSum]].
  *
  * Part2 - Merry Christmas :)
  *
  */
object Day25 {

  //val in = Util.readInput("Day25input.txt").head.toInt
  val input = 12667664

  data class Tape(private val init: Array<Int>) {
    fun update(position: Int, value: Int): Tape {
      init[position] = value
      return this
    }

    fun get(position: Int) = init[position]

    val size = init.size

    fun checkSum(): Int = init.count { it == 1 }
  }

  abstract class State(open val programCounter: Int, open val tape: Tape) {
    abstract fun tick(): State
    fun checkSum() = tape.checkSum()
  }

  data class StateA(override val programCounter: Int, override val tape: Tape) : State(programCounter, tape) {
    override fun tick(): State {
      return if(tape.get(programCounter) == 0) StateB(programCounter + 1, tape.update(programCounter, 1))
      else if(tape.get(programCounter) == 1) StateC(programCounter - 1, tape.update(programCounter, 0))
      else { assert(false); StateA(0, Tape(emptyArray())) }
    }
  }

  data class StateB(override val programCounter: Int, override val tape: Tape) : State(programCounter, tape) {
    override fun tick(): State {
      return if(tape.get(programCounter) == 0) StateA(programCounter - 1, tape.update(programCounter, 1))
      else if(tape.get(programCounter) == 1) StateD(programCounter + 1, tape.update(programCounter, 1))
      else { assert(false); StateA(0, Tape(emptyArray())) }
    }
  }

  data class StateC(override val programCounter: Int, override val tape: Tape) : State(programCounter, tape) {
    override fun tick(): State {
      return if(tape.get(programCounter) == 0) StateB(programCounter - 1, tape.update(programCounter, 0))
      else if(tape.get(programCounter) == 1) StateE(programCounter - 1, tape.update(programCounter, 0))
      else { assert(false); StateA(0, Tape(emptyArray())) }
    }
  }

  data class StateD(override val programCounter: Int, override val tape: Tape) : State(programCounter, tape) {
    override fun tick(): State {
      return if(tape.get(programCounter) == 0) StateA(programCounter + 1, tape.update(programCounter, 1))
      else if(tape.get(programCounter) == 1) StateB(programCounter + 1, tape.update(programCounter, 0))
      else { assert(false); StateA(0, Tape(emptyArray())) }
    }
  }

  data class StateE(override val programCounter: Int, override val tape: Tape) : State(programCounter, tape) {
    override fun tick(): State {
      return if(tape.get(programCounter) == 0) StateF(programCounter - 1, tape.update(programCounter, 1))
      else if(tape.get(programCounter) == 1) StateC(programCounter - 1, tape.update(programCounter, 1))
      else { assert(false); StateA(0, Tape(emptyArray())) }
    }
  }

  data class StateF(override val programCounter: Int, override val tape: Tape) : State(programCounter, tape) {
    override fun tick(): State {
      return if(tape.get(programCounter) == 0) StateD(programCounter + 1, tape.update(programCounter, 1))
      else if(tape.get(programCounter) == 1) StateA(programCounter + 1, tape.update(programCounter, 1))
      else { assert(false); StateA(0, Tape(emptyArray())) }
    }
  }

  tailrec fun run(state: State, steps: Int): State {
    return if(steps <= 0) state
    else run(state.tick(), steps - 1)
  }

  object Part1 {
    fun solve(input: Int): Pair<Int, Long> = aoc.Util.measureTimeMillis(0) {
      val tape = Tape(Array(100001) { 0 })
      run(StateA(tape.size / 2, tape), input).checkSum()
    }
  }
}
