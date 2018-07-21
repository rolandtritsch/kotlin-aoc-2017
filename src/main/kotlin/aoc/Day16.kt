package aoc.day16

/** Problem: [[https://adventofcode.com/2017/day/16]]
  *
  * Solution:
  *
  * General - Kind of simple. Take a program and a move and execute
  * the move on the program. Take all moves and execute all of them
  * one after the other (executeMoves). Then (for Part2) execute all
  * moves on a/the program 1000000000 times.
  *
  * Part1 - execute the moves (once).
  *
  * Part2 - execute the moves 1000000000 times (execute the dance).
  * The big trick here is to realize that there is a loop in the
  * dance and that the dance is repeating itself after N iterations,
  * means you can ignore the first M times the dance is executed and
  * need todo the remainder to find the final position.
  */
object Day16 {

  val input = aoc.Util.readInput("Day16input.txt").first().split(",").toList()

  val initial = ('a'..'p').toList()
  val times = 1000000000

  sealed class Move {
    data class Spin(val size: Int) : Move()
    data class Exchange(val thiz: Int, val thaz: Int) : Move()
    data class Partner(val thiz: Char, val thaz: Char) : Move()
  }

  fun parseInput(input: List<String>): List<Move> {
    require(input.isNotEmpty()) { "input.nonEmpty failed" }

    return input.map { l -> when(l.get(0)) {
      's' -> {
        val s = l.substring(1).toInt()
        assert(s in 1..initial.size) { "s >= 1 && s <= initial.size failed; with >${s}}<" }
        Move.Spin(s)
      }

      'x' -> {
        val ps = l.substring(1).split("/")
        assert(ps.size == 2)
        val thiz = ps.get(0).toInt()
        assert(thiz in 0..initial.size - 1) { "thiz >= 0 && thiz <= initial.size - 1 failed; with >${thiz}}<" }
        val thaz = ps.get(1).toInt()
        assert(thaz in 0..initial.size - 1) { "thaz >= 0 && thaz <= initial.size - 1 failed; with >${thaz}}<" }
        assert(thiz != thaz) { "thiz != thaz failed; with >${thiz}</>${thaz}<" }
        Move.Exchange(thiz, thaz)
      }

      'p' -> {
        val ps = l.substring(1).split("/")
        assert(ps.size == 2)
        val thiz = ps.get(0).get(0)
        assert(initial.contains(thiz)) { "initial.contains(thiz) failed; with >${thiz}<" }
        val thaz = ps.get(1).get(0)
        assert(initial.contains(thaz)) { "initial.contains(thaz) failed; with >${thaz}<" }
        assert(thiz != thaz) { "thiz != thaz failed; with >${thiz}</>${thaz}<" }
        Move.Partner(thiz, thaz)
      }
      else -> {
        assert(false) { "Unknown type of move" }
        Move.Spin(0)
      }
    }}
  }

  fun <T> List<T>.splitAt(i: Int) = Pair(
    this.slice(0..i-1),
    this.slice(i..this.size-1)
  )

  fun <T> List<T>.updated(i: Int, e: T): List<T> {
    val head = this.slice(0..i-1)
    val tail = this.slice(i+1..this.size-1)
    return head + listOf(e) + tail
  }

  tailrec fun executeMoves(programs: List<Char>, moves: List<Move>): List<Char> {
    require(programs.isNotEmpty()) { "programs.nonEmpty failed" }

    fun executeMove(programs: List<Char>, move: Move): List<Char> = when(move) {
      is Move.Spin -> {
        // Note: The Spin rotates counter-clockwise
        val (head, tail) = programs.splitAt(programs.size - move.size)
        tail + head
      }

      is Move.Exchange -> {
        val thizProgram = programs.get(move.thiz)
        val thazProgram = programs.get(move.thaz)
        programs.updated(move.thiz, thazProgram).updated(move.thaz, thizProgram)
      }

      is Move.Partner -> {
        val thizPos = programs.indexOf(move.thiz)
        val thazPos = programs.indexOf(move.thaz)
        programs.updated(thizPos, move.thaz).updated(thazPos, move.thiz)
      }
    }

    return if(moves.isEmpty()) programs
    else executeMoves(executeMove(programs, moves.first()), moves.drop(1))
  }

  tailrec fun executeDance(programs: List<Char>, moves: List<Move>, times: Long): List<Char> {
    require(programs.isNotEmpty()) { "programs.nonEmpty failed" }
    require(moves.isNotEmpty()) { "moves.nonEmpty failed" }

    return if(times <= 0) programs
    else executeDance(executeMoves(programs, moves), moves, times - 1)
  }

  tailrec fun findLoop(programs: List<Char>, moves: List<Move>, times: Long): Long {
    require(programs.isNotEmpty()) { "programs.nonEmpty failed" }
    require(moves.isNotEmpty()) { "moves.nonEmpty failed" }

    return if(programs.equals(initial)) times
    else findLoop(executeMoves(programs, moves), moves, times + 1)
  }


  object Part1 {
    fun solve(input: List<String>): String {
      return executeMoves(initial, parseInput(input)).joinToString("")
    }
  }

  object Part2 {
    fun solve(input: List<String>): String {
      val moves = parseInput(input)
      val loopTimes = findLoop(executeMoves(initial, moves), moves,1)
      return executeDance(initial, moves, times % loopTimes).joinToString("")
    }
  }
}
