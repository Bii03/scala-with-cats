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

object FunctorsInCats extends App {

  import cats.Functor
  import cats.instances.list._
  import cats.instances.option._
  import scala.language.higherKinds

  val list1 = List(1, 2, 3)

  val list2: List[Int] = Functor[List].map(list1)(_ * 2)

  val option1 = Option(123)

  val option2: Option[String] = Functor[Option].map(option1)(_.toString)

  /**
    * Functor also provides the lift method, which converts a function of type A
    * => B to one that operates over a functor and has type F[A] => F[B]:
    */

  val func: Int => Int = (x: Int) => x + 1

  val liftedFunc: Option[Int] => Option[Int] = Functor[Option].lift(func)

  /**
    * The main method provided by the syntax for Functor is map.
    * It’s difficult to demonstrate this with Options and Lists as they have their own built-in map
    * methods and the Scala compiler will always prefer a built-in method over an
    * extension method.
    * Scala’s Function1 type doesn’t have a map method (it’s called andThen instead) so there are no
    * naming conflicts.
    */

  import cats.instances.function._ // for Functor
  import cats.syntax.functor._

  val func1: Int => Int = (a: Int) => a + 1
  val func2: Int => Int = (a: Int) => a * 2
  val func3: Int => String = (a: Int) => a + "!"

  val func4: Int => String = func1.map(func2).map(func3)

  /**
    * We can write a method that
    * applies an equation to a number no matter what functor context it’s in:
    */
  def doMath[F[_]](start: F[Int])(implicit functor: Functor[F]): F[Int] =
    start.map(n => n + 1 * 2)

  println(doMath(Option(20)))
  println(doMath(List(1, 2, 3)))

  /**
    * How does this work?
    *
    * {{{
    *   implicit class FunctorOps[F[_], A](src: F[A]) {
    *     def map[B](func: A => B)(implicit functor: Functor[F]): F[B] =
    *       functor.map(src)(func)
    *   }
    * }}}
    *
    */
}
