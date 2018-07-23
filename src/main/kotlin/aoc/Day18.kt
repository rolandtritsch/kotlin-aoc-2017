package aoc.day18

import java.util.concurrent.LinkedBlockingDeque
import java.util.concurrent.TimeUnit

/** Problem: [[https://adventofcode.com/2017/day/18]]
  *
  * General - First we need to implement all of the operations. We then
  * execute the progrom by executing all instructions (the input). Running
  * the program can be parameterized by `when is the program done?` and
  * `what do we do when we exit?`.
  *
  * Note: To solve the puzzle(s) we need two program instances to talk
  * to each other (send/receive register values (frequencies)). For that
  * to happen I am using a blocking queue between the instances.
  *
  * Note: The instruction on line 35 is >jgz 1 3<. Not sure, if this is
  * a typo. I changed it to >jgz l 3< and added >set l 1< as the first
  * instruction in the input file.
  *
  * Part1 - Simple. Run the program. When we exit we need to return the
  * value of the recovered frequency (the value of the most recently
  * played sound; basically most recent value in the queue). We are done,
  * the first time a receive instruction is executed with a non-zero value.
  *
  * Part2 - Also (kind of) simple (after some refactoring; using Future and
  * replacing LinkedBlockingDeque with Queue). Run until both sides are waiting
  * (i.e. are deadlocked) and (at that point in time) return/exit with the
  * writeCount.
  *
  * Note: This solution will not compile with scala native (because there
  * is no implementation of LinkedBlockingDequeue in scala native).
  */
object Day18 {

  val input = aoc.Util.readInput("Day18input.txt")

  data class Program(
    val id: Int,
    val counter: Int,
    val instructions: List<Operation>,
    val register: MutableMap<Char, Long>,
    val readChannel: LinkedBlockingDeque<Long>,
    val writeChannel: LinkedBlockingDeque<Long>,
    val writeCount: Int,
    val checkRegisterOnReceive: Boolean,
    val deadlocked: Boolean
  )

  sealed class Operation {
    abstract fun execute(program: Program): Program

    data class Send(val r: Char) : Operation() {
      override fun execute(program: Program): Program {
        val success = program.writeChannel.offerFirst(program.register.getValue(r))
        assert(success)
        return program.copy(
          counter = program.counter + 1,
          writeCount = if(program.id == 1) program.writeCount + 1 else program.writeCount
        )
      }
    }

    data class Set(val r: Char, val v: Long) : Operation() {
      override fun execute(program: Program): Program {
        program.register.put(r, v)
        return program.copy(
          counter = program.counter + 1
        )
      }
    }

    data class SetR(val r: Char, val v: Char) : Operation() {
      override fun execute(program: Program): Program {
        program.register.put(r, program.register.getValue(v))
        return program.copy(
          counter = program.counter + 1
        )
      }
    }

    data class Add(val r: Char, val v: Long) : Operation() {
      override fun execute(program: Program): Program {
        program.register.put(r, program.register.getValue(r) + v)
        return program.copy(
          counter = program.counter + 1
        )
      }
    }

    data class AddR(val r: Char, val v: Char) : Operation() {
      override fun execute(program: Program): Program {
        program.register.put(r, program.register.getValue(r) + program.register.getValue(v))
        return program.copy(
          counter = program.counter + 1
        )
      }
    }

    data class Multiply(val r: Char, val v: Long) : Operation() {
      override fun execute(program: Program): Program {
        program.register.put(r, program.register.getValue(r) * v)
        return program.copy(
          counter = program.counter + 1
        )
      }
    }

    data class MultiplyR(val r: Char, val v: Char) : Operation() {
      override fun execute(program: Program): Program {
        program.register.put(r, program.register.getValue(r) * program.register.getValue(v))
        return program.copy(
          counter = program.counter + 1
        )
      }
    }

    data class Modulo(val r: Char, val v: Long) : Operation() {
      override fun execute(program: Program): Program {
        program.register.put(r, program.register.getValue(r).rem(v))
        return program.copy(
          counter = program.counter + 1
        )
      }
    }

    data class ModuloR(val r: Char, val v: Char) : Operation() {
      override fun execute(program: Program): Program {
        program.register.put(r, program.register.getValue(r).rem(program.register.getValue(v)))
        return program.copy(
          counter = program.counter + 1
        )
      }
    }

    data class Receive(val r: Char) : Operation() {
      override fun execute(program: Program): Program {
        return if (program.checkRegisterOnReceive) {
          if (program.register.getValue(r) != 0L) {
            val v = program.readChannel.pollLast(1, TimeUnit.SECONDS)
            if(v == null) {
              program.copy(
                counter = program.counter + 1,
                deadlocked = true
              )
            } else {
              program.register.put(r, v)
              program.copy(
                counter = program.counter + 1
              )
            }
          } else {
            program.copy(
              counter = program.counter + 1
            )
          }
        } else {
          val v = program.readChannel.pollLast(1, TimeUnit.SECONDS)
          if(v == null) {
            program.copy(
              counter = program.counter + 1,
              deadlocked = true
            )
          } else {
            program.register[r] = v
            program.copy(
              counter = program.counter + 1
            )
          }
        }
      }
    }

    data class JumpIfGreaterThanZero(val r: Char, val v: Int) : Operation() {
      override fun execute(program: Program): Program {
        return if (program.register.getValue(r) > 0L) {
          program.copy(
            counter = program.counter + v
          )
        }
        else {
          program.copy(
            counter = program.counter + 1
          )
        }
      }
    }

    data class JumpIfGreaterThanZeroR(val r: Char, val v: Char) : Operation() {
      override fun execute(program: Program): Program {
        return if (program.register.getValue(r) > 0L) {
          program.copy(
            counter = program.counter + program.register.getValue(v).toInt()
          )
        }
        else {
          program.copy(
            counter = program.counter + 1
          )
        }
      }
    }
  }

  fun parseInput(input: List<String>): List<Operation> {
    require(input.isNotEmpty()) { "input.nonEmpty failed" }

    val registerRange = ('a'..'z').toList()

    return input.map { l ->
      val tokens = l.split(" ")
      assert(tokens.size >= 2)
      val operation = tokens[0]
      val register = tokens[1][0]
      assert(registerRange.contains(register))

      when(operation) {
        "snd" -> Operation.Send(register)
        "set" -> {
          assert(tokens.size == 3)
          val operand = tokens[2]
          if (registerRange.contains(operand[0])) Operation.SetR(register, operand[0])
          else Operation.Set(register, operand.toLong())
        }
        "add" -> {
          assert(tokens.size == 3)
          val operand = tokens[2]
          if (registerRange.contains(operand[0])) Operation.AddR(register, operand[0])
          else Operation.Add(register, operand.toLong())
        }
        "mul" -> {
          assert(tokens.size == 3)
          val operand = tokens[2]
          if (registerRange.contains(operand[0])) Operation.MultiplyR(register, operand[0])
          else Operation.Multiply(register, operand.toLong())
        }
        "mod" -> {
          assert(tokens.size == 3)
          val operand = tokens[2]
          if (registerRange.contains(operand[0])) Operation.ModuloR(register, operand[0])
          else Operation.Modulo(register, operand.toLong())
        }
        "rcv" -> Operation.Receive(register)
        "jgz" -> {
          assert(tokens.size == 3)
          val operand = tokens[2]
          if (registerRange.contains(operand[0])) Operation.JumpIfGreaterThanZeroR(register, operand[0])
          else Operation.JumpIfGreaterThanZero(register, operand.toInt())
        }
        else -> { assert(false); Operation.Send('a') }
      }
    } //ensuring(_.nonEmpty, s"_.nonEmpty failed")
  }

  tailrec fun run(program: Program, done: (Program) -> Boolean, exit: (Program) -> Long): Long {
    return if (done(program)) exit(program)
    else run(program.instructions[program.counter].execute(program), done, exit)
  }

  object Part1 {
    fun solve(input: List<String>): Long {
      fun done(p: Program): Boolean {
        return if (p.counter < 0 || p.counter >= p.instructions.size) true
        else when(p.instructions[p.counter]) {
          is Operation.Receive -> {
            val r = (p.instructions[p.counter] as Operation.Receive).r
            if (p.register.getValue(r) > 0L) true else false
          }
          else -> false
        }
      }

      fun exit(p: Program): Long {
        return if (p.counter < 0 || p.counter >= p.instructions.size) -1L
        else p.readChannel.pollFirst(5, TimeUnit.SECONDS)
      }

      val instructions = parseInput(input)
      val registers = emptyMap<Char, Long>().toMutableMap().withDefault { 0L }
      val channel = LinkedBlockingDeque<Long>()
      val program = Program(0, 0, instructions, registers, channel, channel, 0, true, false)

      return run(program, ::done, ::exit)
    }
  }

  @Volatile private var result0 = 0L
  @Volatile private var result1 = 0L
  object Part2 {
    fun solve(input: List<String>): Long {
      fun done(p: Program): Boolean {
        return p.counter < 0 || p.counter >= p.instructions.size || p.deadlocked
      }

      fun exit(p: Program): Long {
        return if(p.deadlocked) p.writeCount.toLong() else -1L
      }

      val instructions = parseInput(input)
      val p0Registers = emptyMap<Char, Long>().toMutableMap().withDefault { 0L }
      val p1Registers = emptyMap<Char, Long>().toMutableMap().withDefault { 0L }
      p0Registers['p'] = 0L
      p1Registers['p'] = 1L
      val p0Channel = LinkedBlockingDeque<Long>()
      val p1Channel = LinkedBlockingDeque<Long>()
      val p0 = Program(0, 0, instructions, p0Registers, p1Channel, p0Channel, 0, false, false)
      val p1 = Program(1, 0, instructions, p1Registers, p0Channel, p1Channel, 0, false, false)

      val thread0 = kotlin.concurrent.thread() { result0 = run(p0, ::done, ::exit) }
      val thread1 = kotlin.concurrent.thread() { result1 = run(p1, ::done, ::exit) }

      val SECS = 1000L
      thread0.join(60*SECS)
      thread1.join(60*SECS)

      return result0 + result1
    }
  }
}
