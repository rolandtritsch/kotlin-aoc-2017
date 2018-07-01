package aoc

/** Problem: [[https://adventofcode.com/2017/day/7]]
  *
  * Solution:
  *
  * General - This is a tree searching problem.
  *
  * Part1 - The root is the only node with no parent, means
  * it is never referenced as a child, means we can find it
  * by doing the diff between all nodes and all children.
  *
  * Part2 - Find the root. Go through the tree (top-down) and
  * find the (bad) node, where the tree unbalanced (for the
  * first time). Find the wrong weight (the one that is
  * different to the other ones) and calc you to correct it.
  */

sealed class ParseElement(open val name: String, open val weight: Int)
data class ParseNode(override val name: String, override val weight: Int, val children: List<String>): ParseElement(name, weight)
data class ParseLeaf(override val name: String, override val weight: Int): ParseElement(name, weight)

sealed class Element(open val name: String, open val weight: Int) {
  abstract fun toString(level: Int): String
  override fun toString(): String = "${name}/${weight}"
}

data class Node(override val name: String, override val weight: Int, val children: List<Element>): Element(name, weight) {
  override fun toString(level: Int): String = "${" ".repeat(level)}${name}/${weight}\n${children.map { it.toString(level + 1) }.joinToString("\n")}"
}

data class Leaf(override val name: String, override val weight: Int): Element(name, weight) {
  override fun toString(level: Int): String = "${" ".repeat(level)}${name}/${weight}"
}

object Day07 {

  val input = aoc.Util.readInput("Day07input.txt")

  fun parseInput(lines: List<String>): List<ParseElement> {
    fun parseLine(line: String): ParseElement {
      // fwft (72) -> ktlj, cntj, xhth
      val tokens = line.split(" ", "(", ")", ",")
      val name = tokens.get(0)
      val weight = tokens.get(2).toInt()
      val node = if(tokens.size > 3) {
        val children = tokens.drop(5).filterNot { it.isEmpty() }.toList()
        ParseNode(name, weight, children)
      } else {
        ParseLeaf(name, weight)
      }
      return node
    }
    return lines.map { parseLine(it) }
  }

  object Tree {

    fun collectAllChildrenNames(pnodes: List<ParseElement>): List<String> = when(pnodes.firstOrNull()) {
      is ParseLeaf -> collectAllChildrenNames(pnodes.drop(1))
      is ParseNode -> (pnodes.first() as ParseNode).children + collectAllChildrenNames(pnodes.drop(1))
      else -> emptyList<String>()
    }

    fun findRoot(pnodes: List<ParseElement>): String {
      assert(pnodes.isNotEmpty()) { "pnodes.nonEmpty failed" }

      val allNames = pnodes.map { it.name }
      val allChildrenNames = collectAllChildrenNames(pnodes)
      assert(allNames.size >= allChildrenNames.size) { "failed; with >${allNames.size}< >${allChildrenNames.size}<" }

      val rootNames = allNames.minus(allChildrenNames)
      assert(rootNames.size == 1) { "rootNames.size == 1 failed; with >${rootNames.size}<" }

      return rootNames.first()
    } //ensuring(!pnodes.collect {case n: ParseNode => n.children}.flatten.contains(_), s"!pnodes.collect {case n: ParseNode => n.children}.flatten.contains(_) failed")

    fun build(name: String, pnodes: List<ParseElement>): Element {
      assert(name.isNotEmpty()) { "name.nonEmpty failed" }
      assert(pnodes.isNotEmpty()) { "pnodes.nonEmpty failed" }
      assert(pnodes.map { it.name }.contains(name)) { "pnodes.map(_.name).contains(name) failed" }

      val pn = pnodes.find { it.name == name }!!
      val e = when(pn) {
        is ParseLeaf -> Leaf(pn.name, pn.weight)
        is ParseNode -> Node(pn.name, pn.weight, pn.children.map { build(it, pnodes) })
      }
      return e
    }

    fun calcWeight(node: Element): Int = when(node) {
      is Leaf -> node.weight
      is Node -> node.weight + node.children.map { calcWeight(it) }.sum()
    }

    fun isBalanced(node: Element): Boolean = when(node) {
      is Leaf -> true
      is Node -> {
        val checkSum = calcWeight(node.children.first())
        node.children.all { calcWeight(it) == checkSum }
      }
    }

    fun findBadNode(node: Element): Node = when(node) {
      is Leaf -> { assert(false); Node("", 0, emptyList<Element>()) }
      is Node -> {
        if(!isBalanced(node) && node.children.all { isBalanced(it) }) node
        else {
          val next = node.children.find { !isBalanced(it) }!!
          findBadNode(next)
        }
      }
    }
  }

  object Part1 {
    fun solve(input: List<String>) = Tree.findRoot(parseInput(input))
  }

  object Part2 {
    fun solve(input: List<String>): Int {
      val pnodes = parseInput(input)
      val root = Tree.build(Tree.findRoot(pnodes), pnodes)
      val badNode = Tree.findBadNode(root)

      val nodesByWeight = badNode.children.groupBy { Tree.calcWeight(it) }.toList()
      assert(nodesByWeight.size == 2) { "nodesByWeight.size == 2 failed; with >${nodesByWeight.size}<" }

      val nodesByOccurences = nodesByWeight.map { Pair(it.second.size, it.second.first()) }
      val bad = nodesByOccurences.find { it.first == 1 }!!.second
      val good = nodesByOccurences.find { it.first > 1 }!!.second
      val correctWeight = bad.weight - (Tree.calcWeight(bad) - Tree.calcWeight(good))
      return correctWeight
    }
  }
}
