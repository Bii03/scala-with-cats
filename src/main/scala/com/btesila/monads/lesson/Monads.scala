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

object Monads {

  /**
    * Informally, a monad is anything with a constructor and a flatMap method. All
    * of the functors we saw in the last chapter are also monads, including Option,
    * List, and Future.
    *
    * We even have special syntax to support monads: for comprehensions.
    *
    * However, despite the ubiquity of the concept, the Scala standard
    * library lacks a concrete type to encompass “things that can be flatMapped”.
    * This type class is one of the benefits bought to us by Cats.
    *
    * A monad is a mechanism for sequencing computations. However, last chapter we said the exact
    * same thing for functors. Nevertheless, functors are limited in that they only allow
    * this complication to occur once at the beginning of the sequence. They don’t
    * account further complications at each step in the sequence.
    *
    * This is where monads come in. A monad’s flatMap method allows us to specify
    * what happens next, taking into account an intermediate complication. The
    * flatMap method of Option takes intermediate Options into account. The
    * flatMap method of List handles intermediate Lists. And so on. In each
    * case, the function passed to flatMap specifies the application-specific part
    * of the computation, and flatMap itself takes care of the complication allowing
    * us to flatMap again. Let’s ground things by looking at some examples.
    */

  def parseInt(str: String): Option[Int] =
    scala.util.Try(str.toInt).toOption

  def divide(a: Int, b: Int): Option[Int] =
    if (b == 0) None else Some(a / b)

  /**
    * Each of these methods may “fail” by returning None. The flatMap method
    * allows us to ignore this when we sequence operations:
    */

  def stringDivideBy(aStr: String, bStr: String): Option[Int] =
    parseInt(aStr).flatMap { aNum =>
      parseInt(bStr).flatMap { bNum =>
        divide(aNum, bNum)
      }
    }

  /**
    * Every monad is also a functor (see below for proof), so we can rely on both
    * flatMap and map to sequence computations that do and don’t introduce a
    * new monad. Plus, if we have both flatMap and map we can use for comprehensions
    * to clarify the sequencing behaviour.
    */

  /**
    * Monadic behaviour is captured by two operations:
    * - pure: A => F[A]  - abstracts over constructors, providing a way to create a new monadic
    * context from a plain value
    * - flatMap: (F[A], A => F[B]) => F[B] - provides the sequencing step we have
    * already discussed, extracting the value from a context and generating the next
    * context in the sequence
    *
    * {{{
    *   // Monad in Cats
    *
    *   trait Monad[F[_]] {
    *     def pure[A](value: A): F[A]
    *     def flatMap[A, B](value: F[A])(func: A => F[B]): F[B]
    *   }
    *
    * }}}
    */

  /**
    * Monad Laws:
    * - left identity: calling pure and transforming the result with func is the
    * same as calling func
    * {{{
    *   pure(a).flatMap(func) == func(a)
    * }}}
    *
    * - right identity: passing pure to flatMap is the same as doing nothing
    * {{{
    *   m.flatMap(pure) == m
    * }}}
    *
    * - associativity: flatMapping over two functions f and g is the same as
    * flatMapping over f and then flatMapping over g:
    * {{{
    *   m.flatMap(f).flatMap(g) == m.flatMap(x => f(x).flatMap(g))
    * }}}
    *
    */

}
