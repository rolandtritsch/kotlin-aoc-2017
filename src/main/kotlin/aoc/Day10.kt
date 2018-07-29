package aoc.day10

/** Problem: [[https://adventofcode.com/2017/day/10]]
  *
  * Solution:
  *
  * General - Nothing special here. Just very careful reading
  * of the problem statement and the requirements that come
  * with it.
  *
  * Part1 - Implement the knot.
  *
  * Part2 - Implement the sparse/dense hash.
  */
object Day10 {

  val input = aoc.Util.readInput("Day10input.txt").first().filterNot { it.isWhitespace() }

  fun shiftLeft(hash: List<Int>, times: Int): List<Int> {
    require(hash.isNotEmpty()) { "hash.nonEmpty failed" }
    require(times >= 0) { "times >= 0 failed; with >${times}<" }

    fun shiftLeftOnce(hash: List<Int>): List<Int> = hash.drop(1) + hash.first()

    return if(times == 0) hash
    else shiftLeft(shiftLeftOnce(hash), times - 1)
  }

  fun shiftRight(hash: List<Int>, times: Int): List<Int> {
    return shiftLeft(hash, hash.size - times)
  }

  fun reverse(hash: List<Int>, length: Int): List<Int> {
    require(hash.isNotEmpty()) { "hash.nonEmpty failed" }
    require(length >= 0 && length <= hash.size) { "length >= 0 && length <= hash.size failed; with >${length}<" }

    val front = hash.slice(0..length-1)
    val end = hash.slice(length..hash.size - 1)
    return front.reversed() + end
  }

  data class Hash(val hash: List<Int>, val position: Int, val skip: Int) {
    fun next(length: Int): Hash {
      val nextHash = shiftRight(reverse(shiftLeft(hash, position), length), position)
      val nextPosition = (position + length + skip) % hash.size
      val nextSkip = skip + 1
      return Hash(nextHash, nextPosition, nextSkip)
    }
  }

  fun input2Lengths(input: String): List<Int> {
    return input.split(",").map { it.toInt() }.toList()
  }

  val seed = Hash((0..255).toList(), 0, 0)
  fun knot(lengths: List<Int>, seed: Hash): Hash {
    return lengths.fold(seed, { currentHash, length -> currentHash.next(length) })
  }

  object Part1 {
    fun solve(input: String): Pair<Int, Long> = aoc.Util.measureTimeMillis(0) {
      val hash = knot(input2Lengths(input), seed).hash
      hash.get(0) * hash.get(1)
    }
  }

  val suffix = listOf(17, 31, 73, 47, 23)
  fun encode(input: String, suffix: List<Int>): List<Int> {
    return input.map { it.toInt() }.toList() + suffix
  }

  val rounds = 64
  fun sparse(lengths: List<Int>, seed: Hash, rounds: Int): Hash {
    require(rounds >= 1) { "rounds >= 1 failed; with >${rounds}<" }

    return if(rounds == 1) knot(lengths, seed)
    else sparse(lengths, knot(lengths, seed), rounds - 1)
  } //ensuring(_.hash.forall(n => n >= 0 && n < 256))

  fun xorHashSlice(slice: List<Int>): Int {
    return slice.fold(0, { acc, i -> acc xor i })
  }

  val sliceSize = 16
  fun dense(hash: List<Int>): List<Int> {
    require(hash.size % sliceSize == 0) { "hash.size % sliceSize == 0 failed; with >${hash.size}<" }
    require(hash.all { n -> n >= 0 && n < hash.size }) { "hash.forall(n => n >= 0 && n < hash.size) failed" }

    val hashSlices = hash.chunked(sliceSize).toList()
    return hashSlices.map { xorHashSlice(it) }
  } //ensuring(result => result.size == hash.size / sliceSize)

  fun dense2hex(hash: List<Int>): String {
    return hash.fold(emptyList<String>(), { acc, i -> acc + listOf("%02x".format(i)) }).joinToString("")
  }

  object Part2 {
    fun solve(input: String): Pair<String, Long> = aoc.Util.measureTimeMillis("") {
      dense2hex(dense(sparse(encode(input, suffix), seed, rounds).hash))
    }
  }
}
