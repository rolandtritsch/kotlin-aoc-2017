package aoc.day23

/** Problem: [[http://adventofcode.com/2017/day/23]]
  *
  * Solution:
  *
  * General - This was a difficult one (at least for me). Part1 is
  * straightforward enough, but I really struggled to get Part2 done.
  * For Part1 I implemented a *normal* [[Operation]]-interpreter to
  * run the [[Program]]. But this prooved to be way too slow to solve
  * Part2. From the discussion group I then realized that I have to
  * reverse engineer the code to find out what it is doing and write
  * a compilable version of that code. First I annotated the code
  * in Day23Input.txt.annotated. From this you can deduct the following
  * code ...
  * {{{
  * b = c = 84
  * if (a != 0)
  *     b = b * 100 + 100000
  *     c = b + 17000
  * do
  *     f = 1
  *     d = 2
  *     do
  *         e = 2
  *         do
  *             g = d * e - b
  *             if (g == 0)
  *                 f = 0
  *             e = e + 1
  *             g = e - b
  *         while g != 0
  *         d = d + 1
  *         g = d - b
  *     while g != 0
  *     if (f != 0)
  *         h = h + 1
  *     g = b - c
  *     if (g != 0)
  *         break
  *     b = b + 17
  * while (true)
  * }}}
  * Note that, for Part1 a == 0 and for Part2 a == 1, means for Part1
  * `b` and `c` get initialized to 84, but for Part2 `b` and `c` get
  * initialized to ...
  * {{{
  * b = 84 * 100 + 100000
  * c = b + 17000
  * }}}
  * ... which explains, why Part2 is running very slow, because the algorithm
  * will look for all non-prime numbers between `b` and `c` (with a stepsize
  * of 17) ...
  * {{{
  * from = 84 * 100 + 100000
  * to = from + 17000
  * stepsize = 17
  * for (n in range(from, to , stepsize) {
  *     found = false
  *     for first in range(2, n, 1) {
  *         for (second in range(2, n, 1)
  *             if (first * second == n) found = true
  *         }
  *     }
  *     if (found) nonPrime= nonPrime + 1
  * }
  * }}}
  *
  * Part1 - Build a list of [[Operation]]s to run the [[Program]].
  *
  * Part2 - Implement the algorithm above in [[Part2.solve]].
  *
  */
object Day23 {

  val input = aoc.Util.readInput("Day23input.txt")

  sealed class Operation {
    data class Set(val register: Char, val value: Long) : Operation()
    data class SetR(val register: Char, val value: Char) : Operation()
    data class Sub(val register: Char, val value: Long) : Operation()
    data class SubR(val register: Char, val value: Char) : Operation()
    data class Mul(val register: Char, val value: Long) : Operation()
    data class MulR(val register: Char, val value: Char) : Operation()
    data class JumpIfNotZero(val register: Char, val value: Int) : Operation()
  }

  val registerRange = ('a'..'h').toList()

  fun parseInput(input: List<String>): List<Operation> {
    require(input.isNotEmpty()) { "input.nonEmpty failed" }

    return input.map { l ->
      val tokens = l.split(" ")
      assert(tokens.size >= 2)
      val operation = tokens[0]
      val register = tokens[1][0]
      when(operation) {
        "set" -> {
          assert(tokens.size == 3)
          val operand = tokens[2]
          if(registerRange.contains(operand[0])) Operation.SetR(register, operand[0])
          else Operation.Set(register, operand.toLong())
        }
        "sub" -> {
          assert(tokens.size == 3)
          val operand = tokens[2]
          if(registerRange.contains(operand[0])) Operation.SubR(register, operand[0])
          else Operation.Sub(register, operand.toLong())
        }
        "mul" -> {
          assert(tokens.size == 3)
          val operand = tokens[2]
          if(registerRange.contains(operand[0])) Operation.MulR(register, operand[0])
          else Operation.Mul(register, operand.toLong())
        }
        "jnz" -> {
          assert(tokens.size == 3)
          val operand = tokens[2]
          Operation.JumpIfNotZero(register, operand.toInt())
        }
        else -> {
          assert(false)
          Operation.Set(' ', 0)
        }
      }
    }
  } //ensuring(_.nonEmpty, "_.nonEmpty failed")

  data class Program(val counter: Int, val instructions: List<Operation>, val register: MutableMap<Char, Long>, val instructionCounter: MutableMap<String, Long>)

  tailrec fun run(program: Program, done: (Program) -> Boolean, exit: (Program) -> Long): Long {
    return if(done(program)) exit(program)
    else {
      val current = program.instructions[program.counter]
      val next = when(current) {
        is Operation.Set -> {
          program.register[current.register] = current.value
          program.instructionCounter["set"] = program.instructionCounter.getValue("set") + 1
          Program(
            program.counter + 1,
            program.instructions,
            program.register,
            program.instructionCounter
          )
        }
        is Operation.SetR -> {
          program.register[current.register] = program.register.getValue(current.value)
          program.instructionCounter["set"] = program.instructionCounter.getValue("set") + 1
          Program(
            program.counter + 1,
            program.instructions,
            program.register,
            program.instructionCounter
          )
        }
        is Operation.Sub -> {
          program.register[current.register] = program.register.getValue(current.register) - current.value
          program.instructionCounter["sub"] = program.instructionCounter.getValue("sub") + 1
          Program(
            program.counter + 1,
            program.instructions,
            program.register,
            program.instructionCounter
          )
        }
        is Operation.SubR -> {
          program.register[current.register] = program.register.getValue(current.register) - program.register.getValue(current.value)
          program.instructionCounter["sub"] = program.instructionCounter.getValue("sub") + 1
          Program(
            program.counter + 1,
            program.instructions,
            program.register,
            program.instructionCounter
          )
        }
        is Operation.Mul -> {
          program.register[current.register] = program.register.getValue(current.register) * current.value
          program.instructionCounter["mul"] = program.instructionCounter.getValue("mul") + 1
          Program(
            program.counter + 1,
            program.instructions,
            program.register,
            program.instructionCounter
          )
        }
        is Operation.MulR -> {
          program.register[current.register] = program.register.getValue(current.register) * program.register.getValue(current.value)
          program.instructionCounter["mul"] = program.instructionCounter.getValue("mul") + 1
          Program(
            program.counter + 1,
            program.instructions,
            program.register,
            program.instructionCounter
          )
        }
        is Operation.JumpIfNotZero -> {
          program.instructionCounter["jnz"] = program.instructionCounter.getValue("jnz") + 1
          if(program.register.getValue(current.register) != 0L) {
            Program(
              program.counter + current.value,
              program.instructions,
              program.register,
              program.instructionCounter
            )
          }
          else {
            Program(
              program.counter + 1,
              program.instructions,
              program.register,
              program.instructionCounter
            )
          }
        }
      }
      run(next, done, exit)
    }
  }

  object Part1 {
    fun done(p: Program) = p.counter < 0 || p.counter >= p.instructions.size
    fun exit(p: Program) = p.instructionCounter["mul"]!!

    val program = Program(0, parseInput(input), emptyMap<Char, Long>().toMutableMap().withDefault { 0 }, emptyMap<String, Long>().toMutableMap().withDefault { 0 })

    fun solve(): Long = run(program, ::done, ::exit)
  }

  object Part2 {
    fun findPrime(n: Int): Boolean = !(2..n-1).any { x -> n % x == 0 }

    val seed = 84
    val start = seed * 100 + 100000
    val end = start + 17000
    val stepsize = 17

    fun solve(): Long = (start..end step stepsize).count { !findPrime(it) }.toLong()
  }
}
