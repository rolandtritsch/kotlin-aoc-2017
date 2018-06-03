package aoc

typealias PassPhrase = List<String>

/** Problem: [[https://adventofcode.com/2017/day/4]]
  *
  * Solution:
  *
  * General - Build a histogram of all words in the passphrase
  * and make sure that there are no duplicates and last but not
  * least count the valid passphrases.
  *
  * Part1 - Simple histogram.
  *
  * Part2 - Now we need to detect anagrams. We do that simply
  * by sorting the chars in the words (two words with the same
  * chars in it are an anagram, right).
  *
  * Note: Strictly speaking for Part2 you need to check Policy1
  * and Policy2 (this is how the problem/requirement is expressed),
  * but practically we only need to check Policy2, because Policy2
  * is a (stronger) superset of Policy1.
  */
object Day04 {

  val input = Util.readInput("Day04input.txt").map { it.split(' ').toList() }

  fun isValid(words: PassPhrase): Boolean {
    require(words.size > 0) { "words.nonEmpty failed" }

    val groupByWords = words.groupBy { it }
    val countByWords = groupByWords.map { it.key to it.value.size }
    val count = countByWords.all { it.second == 1 }
    return count
  }

  fun countValid(passPhrases: List<PassPhrase>): Int {
    require(passPhrases.size > 0) { "passPhrases.nonEmpty" }

    val valid = passPhrases.count { isValid(it) }
    // ensuring(_ >= 0, s"_ >= 0 failed")
    return valid
  }

  object Part1 {
    fun solve(passPhrases: List<PassPhrase>): Int = countValid(passPhrases)
  }

  object Part2 {
    fun solve(passPhrases: List<PassPhrase>): Int = countValid(passPhrases.map { it.map { it.toCharArray().sorted().joinToString("") }})
  }
}
