@file:JvmName("Main")

package aoc

import aoc.day01.Day01
import aoc.day02.Day02
import aoc.day03.Day03
import aoc.day04.Day04
import aoc.day05.Day05
import aoc.day06.Day06
import aoc.day07.Day07
import aoc.day08.Day08
import aoc.day09.Day09
import aoc.day10.Day10

fun main(args: Array<String>): Unit {
  require(args.isEmpty()) { "args.isEmpty() failed; with >${args.size}<" }

  println("Day01: Part1: captcha -> ${Day01.Part1.solve(Day01.input)}")
  println("Day01: Part2: captcha -> ${Day01.Part2.solve(Day01.input)}")
  println("Day02: Part1: checksum -> ${Day02.Part1.solve(Day02.input)}")
  println("Day02: Part2: checksum -> ${Day02.Part2.solve(Day02.input)}")
  println("Day03: Part1: distance -> ${Day03.Part1.solve(Day03.input)}")
  println("Day03: Part2: number -> ${Day03.Part2.solve(Day03.input)}")
  println("Day04: Part1: countvalid -> ${Day04.Part1.solve(Day04.input)}")
  println("Day04: Part2: countvalid -> ${Day04.Part2.solve(Day04.input)}")
  println("Day05: Part1: countsteps -> ${Day05.Part1.solve(Day05.input)}")
  println("Day05: Part2: countsteps -> ${Day05.Part2.solve(Day05.input)}")
  println("Day06: Part1: cycles -> ${Day06.Part1.solve(Day06.input)}")
  println("Day06: Part2: cycles -> ${Day06.Part2.solve(Day06.input)}")
  println("Day07: Part1: findroot -> ${Day07.Part1.solve(Day07.input)}")
  println("Day07: Part2: correctweight -> ${Day07.Part2.solve(Day07.input)}")
  println("Day08: Part1: maxregister -> ${Day08.Part1.solve(Day08.input)}")
  println("Day08: Part2: maxregisters -> ${Day08.Part2.solve(Day08.input)}")
  println("Day09: Part1: score -> ${Day09.Part1.solve(Day09.input)}")
  println("Day09: Part2: chars -> ${Day09.Part2.solve(Day09.input)}")
  println("Day10: Part1: knot -> ${Day10.Part1.solve(Day10.input)}")
  println("Day10: Part2: hash -> ${Day10.Part2.solve(Day10.input)}")
/*
  println(s"Day11: Part1: steps -> ${Day11.Part1.solve(Day11.input)}")
  println(s"Day11: Part2: max -> ${Day11.Part2.solve(Day11.input)}")
  println(s"Day12: Part1: programs -> ${Day12.Part1.solve(Day12.input)}")
  println(s"Day12: Part2: groups -> ${Day12.Part2.solve(Day12.input)}")
  println(s"Day13: Part1: score -> ${Day13.Part1.solve(Day13.input)}")
  println(s"Day13: Part2: pass -> ${Day13.Part2.solve(Day13.input)}")
  println(s"Day14: Part1: used -> ${Day14.Part1.solve(Day14.input)}")
  println(s"Day14: Part2: regions -> ${Day14.Part2.solve(Day14.input)}")
  println(s"Day15: Part1: count -> ${Day15.Part1.solve}")
  println(s"Day15: Part2: count -> ${Day15.Part2.solve}")
  println(s"Day16: Part1: moves -> ${Day16.Part1.solve(Day16.input)}")
  println(s"Day16: Part2: dance -> ${Day16.Part2.solve(Day16.input)}")
  println(s"Day17: Part1: next -> ${Day17.Part1.solve(Day17.steps, Day17.times)}")
  println(s"Day17: Part2: zero -> ${Day17.Part2.solve(Day17.steps, Day17.times2)}")
  println(s"Day18: Part1: frequency -> ${Day18.Part1.solve(Day18.input)}")
  println(s"Day18: Part2: deadlock -> ${Day18.Part2.solve(Day18.input)}")
  println(s"Day19: Part1: path -> ${Day19.Part1.solve(Day19.input)}")
  println(s"Day19: Part2: steps -> ${Day19.Part2.solve(Day19.input)}")
  println(s"Day20: Part1: findclosest -> ${Day20.Part1.solve(Day20.input)}")
  println(s"Day20: Part2: nocollisions -> ${Day20.Part2.solve(Day20.input)}")
  println(s"Day21: Part1: after5 -> ${Day21.Part1.solve(Day21.input)}")
  println(s"Day21: Part2: after18 -> ${Day21.Part2.solve(Day21.input)}")
  println(s"Day22: Part1: simple -> ${Day22.Part1.solve(Day22.input)}")
  println(s"Day22: Part2: advanced -> ${Day22.Part2.solve(Day22.input)}")
  println(s"Day23: Part1: run -> ${Day23.Part1.solve(Day23.input)}")
  println(s"Day23: Part2: run -> ${Day23.Part2.solve(Day23.input)}")
  println(s"Day24: Part1: strongest -> ${Day24.Part1.solve(Day24.input)}")
  println(s"Day24: Part2: longest -> ${Day24.Part2.solve(Day24.input)}")
  println(s"Day25: Part1: run -> ${Day25.Part1.solve(Day25.input)}")
*/
}
