/** ***********************************************************************
  * ADOBE CONFIDENTIAL
  * ___________________
  *
  * Copyright 2018 Adobe Systems Incorporated
  * All Rights Reserved.
  *
  * NOTICE:  All information contained herein is, and remains
  * the property of Adobe Systems Incorporated and its suppliers,
  * if any.  The intellectual and technical concepts contained
  * herein are proprietary to Adobe Systems Incorporated and its
  * suppliers and are protected by all applicable intellectual property
  * laws, including trade secret and copyright laws.
  * Dissemination of this information or reproduction of this material
  * is strictly forbidden unless prior written permission is obtained
  * from Adobe Systems Incorporated.
  * *************************************************************************/
package com.btesila.monads.exercise

object StateWorking extends App {

  /**
    * The State monad allows us to implement simple interpreters for complex expressions,
    * passing the values of mutable registers along with the result. We
    * can see a simple example of this by implementing a calculator for post-order
    * integer arithmetic expressions.
    *
    * instead of writing 1 + 2, we write 1 2 +
    *
    * Although post-order expressions are difficult for humans to read, they are easy
    * to evaluate in code. All we need to do is traverse the symbols from left to right,
    * carrying a stack of operands with us as we go:
    * - when we see a number, we push it onto the stack
    * - when we see an operator, we pop two operands off the stack, operate
    * on them, and push the result in their place
    *
    * This allows us to evaluate complex expressions without using parentheses. For
    * example, we can evaluate (1 + 2) * 3) using 1 2 + 3 *
    */

  /**
    * Let’s write an interpreter for these expressions. We can parse each symbol
    * into a State instance representing a transformation on the stack and an intermediate
    * result. The State instances can be threaded together using flatMap
    * to produce an interpreter for any sequence of symbols.
    */

  /**
    * Start by writing a function evalOne that parses a single symbol into an instance
    * of State. Use the code below as a template. Don’t worry about error
    * handling for now—if the stack is in the wrong configuration, it’s OK to throw
    * an exception.
    */

  /**
    * If this seems difficult, think about the basic form of the State instances you’re
    * returning. Each instance represents a functional transformation from a stack
    * to a pair of a stack and a result. You can ignore any wider context and focus
    * on just that one step:
    *
    * {{{
    *   State[List[Int], Int] { oldStack =>
    *     val newStack = someTransformation(oldStack)
    *     val result = someCalculation
    *     (newStack, result)
    *   }
    * }}}
    */

  import cats.data.State

  type CalcState[A] = State[List[Int], A]

  def evalOne(sym: String): CalcState[Int] = sym match {
    case "+" => operator(_ + _)
    case "-" => operator(_ - _)
    case "*" => operator(_ * _)
    case "/" => operator(_ / _)
    case num => operand(num.toInt)
  }

  def operand(i: Int): CalcState[Int] =
    State[List[Int], Int] { oldStack =>
      (i :: oldStack, i)
    }

  def operator(func: (Int, Int) => Int): CalcState[Int] =
    State[List[Int], Int] {
      case x :: y :: tail =>
        val computation = func(x, y)
        (computation :: tail, computation)
      case _ => throw new Error("")
    }

  /**
    * evalOne allows us to evaluate single-symbol expressions as follows. We call
    * runA supplying Nil as an initial stack, and call value to unpack the resulting
    * Eval instance:
    */

  println(evalOne("42").runA(Nil).value)
  // 42

  /**
    * We can represent more complex programs using evalOne, map, and flatMap.
    * Note that most of the work is happening on the stack, so we ignore the results
    * of the intermediate steps for evalOne("1") and evalOne("2")
    */
  val program = for {
    _ <- evalOne("1")
    _ <- evalOne("2")
    ans <- evalOne("+")
  } yield ans

  println(program.runA(Nil).value)

  /**
    * Generalise this example by writing an evalAll method that computes the
    * result of a List[String]. Use evalOne to process each symbol, and thread
    * the resulting State monads together using flatMap.
    */

  import cats.syntax.applicative._ // for pure
  def evalAll(input: List[String]): CalcState[Int] =
    input.foldLeft(0.pure[CalcState]) { (acc, e) =>
      acc.flatMap(_ => evalOne(e))
    }

  /**
    * We can use evalAll to conveniently evaluate multi-stage expressions:
    */
  val program2 = evalAll(List("1", "2", "+", "3", "*"))
  println(program2.runA(Nil).value)

  /**
    * Because evalOne and evalAll both return instances of State, we can thread
    * these results together using flatMap. evalOne produces a simple stack transforma
    * tion and evalAll produces a complex one, but they’re both pure func-
    * tions and we can use them in any order as many times as we like:
    */
  val program3 = for {
    _ <- evalAll(List("1", "2", "+"))
    _ <- evalAll(List("3", "4", "+"))
    ans <- evalOne("*")
  } yield ans

  println(program3)
  println(program3.runA(Nil).value)


  /**
    * Complete the exercise by implementing an evalInput function that splits an
    * input String into symbols, calls evalAll, and runs the result with an initial
    * stack.
    */

  def evalInput(s: String): Int =
    evalAll(s.split(" ").toList).runA(Nil).value

  println(evalInput("1 2 + 3 4 + *"))
}
