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
package com.btesila.functors.lesson

object Functors {

  /**
    * An abstraction that allows us to represent sequences of operations within a context such as a
    * List, an Option, or any one of a thousand other possibilities. Functors on their own aren’t so
    * useful, but special cases of functors such as monads and applicative functors
    * are some of the most commonly used abstractions in Cats.
    */

  /**
    * Basically, a functor is anything with a map method.
    * We typically first encounter map when iterating over Lists. However, to understand
    * functors we need to think of the method in another way. Rather than
    * traversing the list, we should think of it as transforming all of the values inside
    * in one go.
    *
    * The values change but the structure of the list remains the same. Because map leaves the
    * structure of the context unchanged, we can call it
    * repeatedly to sequence multiple computations on the contents of an initial
    * data structure.
    */

  List(1, 2, 3).
    map(n => n + 1).
    map(n => n * 2).
    map(n => n + "!")
  // res1: List[String] = List(4!, 6!, 8!)

  /**
    * The map methods of List, Option, and Either apply functions eagerly. However,
    * the idea of sequencing computations is more general than this. Let’s
    * investigate the behaviour of some other functors that apply the pattern in
    * different ways.
    */

  /**
    * Futures - we don’t know when our functions will be called, but we do know what
    * order they will be called in. In this way, Future provides the same sequencing
    * behaviour seen in List, Option, and Either.
    *
    * Note that Scala’s Futures aren’t a great example of pure functional programming
    * because they aren’t referentially transparent. Future always
    * computes and caches a result and there’s no way for us to tweak this
    * behaviour. This means we can get unpredictable results when we use
    * Future to wrap side-effecting computations.
    */

  /**
    * Functions - single argument functors!
    *
    * A function A => B has two type parameters:
    * the parameter type A and the result type B. To coerce them to the correct
    * shape we can fix the parameter type and let the result type vary.
    */

  import cats.instances.function._
  import cats.syntax.functor._

  val func1: Int => Double = (x: Int) => x.toDouble
  val func2: Double => Double = (y: Double) => y * 2

  (func1 map func2)(1)
  (func1 andThen func2)(1)
  func2(func1(1))

  /**
    * Functor Definition
    *
    * {
    *   package cats
    *   import scala.language.higherKinds
    *
    *   trait Functor[F[_]] {
    *     def map[A, B](fa: F[A])(f: A => B): F[B]
    *   }
    * }
    */

  /**
    * Functor Laws
    *
    * Functors guarantee the same semantics whether we sequence many
    * small operations one by one, or combine them into a larger function
    * before mapping. To ensure this is the case the following laws must hold:
    */

  // Identity
  // fa.map(a => a) == fa

  // Composition
  // fa.map(g(f(_))) == fa.map(f).map(g)
}

