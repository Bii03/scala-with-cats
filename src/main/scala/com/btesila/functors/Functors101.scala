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
package com.btesila.functors

import scala.language.higherKinds
import cats.Functor
import cats.instances.list._
import cats.instances.option._

import scala.collection.immutable // for Functor

object Functors101 extends App {

  val list1 = List(1, 2, 3)

  val functor1: Functor[List] = Functor[List]

  val list2: immutable.Seq[Int] = functor1.map(list1)(_ * 2)

  val functor2 = Functor[Option]

  val option1: Option[Int] = Option(123)

  val option2: Option[String] = functor2.map(option1)(_.toString)

  val func = (x: Int) => x + 1

  // convert a function: A => B to a Functor: F[A] => F[B] using `lift`
  val liftedFunc: Option[Int] => Option[Int] = Functor[Option].lift(func)

  liftedFunc(Option(1))


  // for map - not all types can be easily translated to Functors
  // e.g. Function1 does not have map, but andThen
  import cats.syntax.functor._

  /**
    * A method that applies an equation to a number no matter what functor context it’s in
    *
    * We need an implicit functor to gain the `map`
    */
  def doMath[F[_]](start: F[Int])(implicit functor: Functor[F]): F[Int] =
    start.map(n => n + 1 * 2)

  println(doMath(Option(20)))
  println(doMath(List(1, 2, 3)))

}
