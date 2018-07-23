package aoc.day20

/** Problem: [[http://adventofcode.com/2017/day/20]]
  *
  * Solution:
  *
  * General - A [[Particle]] got a [[Position]] and a [[Velocity]]
  * and an [[Acceleration]]. With every *tick* the Particle will get
  * a new Position. Run a/the simulation for a while (defaultDepth).
  *
  * Part1 - Check on/in the resulting list of particles, where the
  * minimum is and find the closest particle to the center.
  *
  * Part2 - Run a/the simulation again, but filter out all collisions.
  * And then *just* take teh size of the resulting list.
  *
  */
object Day20 {

  val input = aoc.Util.readInput("Day20input.txt")

  data class Position(val x: Int, val y: Int, val z: Int) {
    fun add(v: Velocity): Position {
      return Position(x + v.x, y + v.y, z + v.z)
    }
    fun distance(): Int = Math.abs(Math.abs(x) + Math.abs(y) + Math.abs(z))
  }

  data class Velocity(val x: Int, val y: Int, val z: Int) {
    fun add(a: Acceleration): Velocity {
      return Velocity(x + a.x, y + a.y, z + a.z)
    }
  }

  data class Acceleration(val x: Int, val y: Int, val z: Int)

  data class Particle(val p: Position, val v: Velocity, val a: Acceleration) {
    fun tick(): Particle {
      return Particle(p.add(v.add(a)), v.add(a), a)
    }
  }

  fun parseInput(input: List<String>): List<Particle> {
    require(input.isNotEmpty()) { "input.nonEmpty failed" }

    return input.map { l ->
      // p=<1199,-2918,1457>, v=<-13,115,-8>, a=<-7,8,-10>
      val tokens = l.split("<", ">", ",").map { it.trim() }
      val p = Position(tokens[1].toInt(), tokens[2].toInt(), tokens[3].toInt())
      val v = Velocity(tokens[6].toInt(), tokens[7].toInt(), tokens[8].toInt())
      val a = Acceleration(tokens[11].toInt(), tokens[12].toInt(), tokens[13].toInt())
      Particle(p, v, a)
    }
  } //ensuring(_.nonEmpty, "_.nonEmpty failed")

  private val defaultDepth = 1000
  fun run(ps: List<Particle>, depth: Int = defaultDepth): List<Particle> {
    return if(depth <= 0) ps
    else run(ps.map { it.tick() }, depth - 1)
  }

  fun findClosest(ps: List<Particle>): Int {
    require(ps.isNotEmpty()) { "ps.nonEmpty failed" }

    return ps.indexOf(ps.minBy { it.p.distance() })
  } //ensuring(_ >= 0, "_ >= 0 failed")

  fun removeCollisions(ps: List<Particle>): List<Particle> {
    require(ps.isNotEmpty()) { "ps.nonEmpty failed" }

    return ps.groupBy { it.p }.toList().map { kvp -> Pair(kvp.second, kvp.second.size) }.filter { kvp -> kvp.second == 1 }.map { kvp -> kvp.first.first() }
  } //ensuring(_.nonEmpty, "_.nonEmpty failed")

  fun runWithCollisionDetection(ps: List<Particle>, depth: Int = defaultDepth): List<Particle> {
    return if(depth <= 0) ps
    else runWithCollisionDetection(removeCollisions(ps.map { it.tick() }), depth - 1)
  }

  object Part1 {
    fun solve(input: List<String>): Int {
      return findClosest(run(parseInput(input)))
    }
  }

  object Part2 {
    fun solve(input: List<String>): Int {
      return runWithCollisionDetection(parseInput(input)).size
    }
  }
}