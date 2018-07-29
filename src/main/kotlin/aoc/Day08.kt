package aoc.day08

/** Problem: [[https://adventofcode.com/2017/day/8]]
  *
  * Solution:
  *
  * General - Very simple idea. We have a set of (immutable) instructions (no jumps)
  * and a set of registers. We just need to go through all instructions and update
  * the set of registers on the way (with every instruction). While we do this, we
  * collect all register states. The final register state is the head of the collected
  * register states.
  *
  * Part1 - Simple. The max of the register values in the final/last register state/set.
  *
  * Part2 - Simple. The max of all register values in all collected register states/sets.
  */

data class Instruction(
  val register: String,
  val operation: String,
  val operand: Int,
  val conditionRegister: String,
  val condition: String,
  val conditionOperand: Int
)

object Day08 {

  val input = aoc.Util.readInput("Day08input.txt")

  fun parseInput(lines: List<String>): List<Instruction> {
    fun parseLine(line: String): Instruction {
      // b inc 5 if a > 1
      val tokens = line.split(" ")
      assert (tokens.size >= 7) { "tokens >= 7 failed; with >${tokens.size}<" }

      val register = tokens.get(0)
      val operation = tokens.get(1)
      val operand = tokens.get(2).toInt()
      val conditionRegister = tokens.get(4)
      val condition = tokens.get(5)
      val conditionOperand = tokens.get(6).toInt()

      return Instruction(
        register,
        operation,
        operand,
        conditionRegister,
        condition,
        conditionOperand
      )
    }

    return lines.map { parseLine(it) }
  }

  fun buildRegisters(instructions: List<Instruction>): MutableMap<String, Int> {
    require(instructions.isNotEmpty()) { "instructions.isNonEmpty() failed" }
    return instructions.map { it.register to 0 }.toMap().toMutableMap()
  } // ensuring(_.nonEmpty, s"_.nonEmpty failed")

  fun evalCondition(i: Instruction, registers: MutableMap<String, Int>): Boolean {
    require(listOf("==", "!=", "<", ">", "<=", ">=").contains(i.condition)) { "List(conditions).contains(i.condition) failed; with >${i.condition}<" }

    return when(i.condition) {
      "==" -> if(registers.get(i.conditionRegister)!! == i.conditionOperand) true else false
      "!=" -> if(registers.get(i.conditionRegister)!! != i.conditionOperand) true else false
      "<"  -> if(registers.get(i.conditionRegister)!! < i.conditionOperand) true else false
      ">"  -> if(registers.get(i.conditionRegister)!! > i.conditionOperand) true else false
      "<=" -> if(registers.get(i.conditionRegister)!! <= i.conditionOperand) true else false
      ">=" -> if(registers.get(i.conditionRegister)!! >= i.conditionOperand) true else false
      else -> false
    }
  }

  fun executeInstruction(i: Instruction, registers: MutableMap<String, Int>): MutableMap<String, Int> {
    require(listOf("inc", "dec").contains(i.operation)) { "List(operations).contains(i.operation) failed; with >${i.operation}<" }

    val value = if(evalCondition(i, registers)) when(i.operation) {
      "inc" -> registers.get(i.register)!! + i.operand
      "dec" -> registers.get(i.register)!! - i.operand
      else -> { assert(false); 0 }
    } else registers.get(i.register)!!

    registers.put(i.register, value)
    return registers
  }

  fun runProgram(instructions: List<Instruction>, registers: MutableMap<String, Int>): List<MutableMap<String, Int>> {
    require(instructions.isNotEmpty()) { "instructions.nonEmpty" }
    require(registers.isNotEmpty()) { "registers.nonEmpty" }

    return instructions.fold(mutableListOf(registers), { currentRegisters, i ->
      currentRegisters.add(0, executeInstruction(i, currentRegisters.first()).toMutableMap())
      currentRegisters
    }).toList()
  }

  object Part1 {
    fun solve(input: List<String>): Pair<Int, Long> = aoc.Util.measureTimeMillis(0) {
      val instructions = parseInput(input)
      val registers = buildRegisters(instructions)
      val allRegisterStates = runProgram(instructions, registers)

      allRegisterStates.first().values.max()!!
    }
  }

  object Part2 {
    fun solve(input: List<String>): Pair<Int, Long> = aoc.Util.measureTimeMillis(0) {
      val instructions = parseInput(input)
      val registers = buildRegisters(instructions)
      val allRegisterStates = runProgram(instructions, registers)

      allRegisterStates.flatMap { it.values }.max()!!
    }
  }
}
