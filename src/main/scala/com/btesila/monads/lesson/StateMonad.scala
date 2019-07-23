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
package com.btesila.monads.lesson

import cats.Eval
import cats.data.IndexedStateT

object StateMonad extends App {

  /**
    * cats.data.State allows us to pass additional state around as part of a computation. We define
    * State instances representing atomic state operations and thread them together using map and
    * flatMap. In this way we can model mutable state in a purely functional way, without using
    * mutation.
    *
    * Boiled down to their simplest form, instances of State[S, A] represent functions of type
    * S => (S, A). S is the type of the state and A is the type of the result.
    */

  import cats.data.State

  val a: State[Int, String] = State[Int, String] { state => (state, s"The state is $state") }
  // a: cats.data.State[Int,String] = cats.data.IndexedStateT@70142af6

  /**
    * In other words, an instance of State is a function that does two things:
    * • transforms an input state to an output state;
    * • computes a result.
    */

  /**
    * We can “run” our monad by supplying an initial state. State provides three
    * methods — run, runS, and runA — that return different combinations of state
    * and result.
    *
    * Each method returns an instance of Eval, which State uses to maintain stack safety. We call
    * the value method as usual to extract the actual result:
    */

  // Get the state and the result:
  val running: Eval[(Int, String)] = a.run(10)
  val (state, result) = running.value

  // Get the state, ignore the result:
  val onlyState: Int = a.runS(10).value

  // Get the result, ignore the state:
  val onlyResult: String = a.runA(10).value

  /**
    * As we’ve seen with Reader and Writer, the power of the State monad
    * comes from combining instances. The map and flatMap methods thread the
    * state from one instance to another. Each individual instance represents an
    * atomic state transformation, and their combination represents a complete sequence
    * of changes:
    */

  val step1: State[Int, String] = State[Int, String] { num =>
    val ans = num + 1
    (ans, s"Result of step1: $ans")
  }

  val step2: State[Int, String] = State[Int, String] { num =>
    val ans = num * 2
    (ans, s"Result of step2: $ans")
  }

  val both: IndexedStateT[Eval, Int, Int, (String, String)] = for {
    a <- step1
    b <- step2
  } yield (a, b)

  val (s, r) = both.run(20).value
  println(s)
  println(r)

  /**
    * As you can see, in this example the final state is the result of applying both
    * transformations in sequence. State is threaded from step to step even though
    * we don’t interact with it in the for comprehension.
    */

  /**
    * The general model for using the State monad is to represent each step of a
    * computation as an instance and compose the steps using the standard monad
    * operators.
    *
    * Cats provides several convenience constructors for creating primitive steps:
    * • get extracts the state as the result;
    * • set updates the state and returns unit as the result;
    * • pure ignores the state and returns a supplied result;
    * • inspect extracts the state via a transformation function;
    * • modify updates the state using an update function.
    */


  // Return the input state without modifying it.
  val getDemo = State.get[Int] // extracts the state as the result;
  // getDemo: cats.data.State[Int,Int] = cats.data.IndexedStateT@280446c5
  println(getDemo.run(10).value)

  val setDemo = State.set[Int](30) // updates the state and returns unit as the result
  println(setDemo.run(10).value)

  val pureDemo = State.pure[Int, String]("Result") // ignores the state and returns a supplied result
  println(pureDemo.run(10).value)

  val inspectDemo: State[Int, String] = State.inspect[Int, String](_ + "!") // extracts the state via a transformation function
  println(inspectDemo.run(10).value)

  val modifyDemo = State.modify[Int](_ + 1) // updates the state using an update function
  println(modifyDemo.run(10).value)

  /**
    * We can assemble these building blocks using a for comprehension. We typically
    * ignore the result of intermediate stages that only represent transformations on the state:
    */

  import State._

  println("PROGRAM")
  val program: State[Int, (Int, Int, Int)] = for {
    a <- get[Int]
    _ = println(a)
    aa <- set[Int](a + 1)
    _ = println(aa)
    b <- get[Int]
    _ = println(b)
    bb <- modify[Int](_ + 1)
    _ = println(bb)
    c <- inspect[Int, Int](_ * 1000)
  } yield (a, b, c)

  val (sp, rp) = program.run(1).value
  println(s"Program state: $sp")
  println(s"Program result: $rp")
}
