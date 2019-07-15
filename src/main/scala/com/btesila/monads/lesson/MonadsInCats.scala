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

object MonadsInCats extends App {

  /**
    * Expressed through the medium of two type classes:
    *
    * - [[cats.FlatMap]] - exposes a `flatMap` method
    * - [[cats.Applicative]] - exposes a `pure` method; [[cats.Applicative]] also extends
    * [[cats.Functor]], which means that any [[cats.Monad]] has a `map` method as well
    *
    * Cats provides instances for all the monads in the standard library (Option,
    * List, Vector and so on) via cats.instances.
    */

  import cats.Monad
  import cats.instances.list._
  import cats.instances.option._

  val opt1: Option[Int] = Monad[Option].pure(3)
  // opt1: Option[Int] = Some(3)

  val opt2: Option[Int] = Monad[Option].flatMap(opt1)(a => Some(a + 2))
  // opt2: Option[Int] = Some(5)

  val opt3: Option[Int] = Monad[Option].map(opt2)(a => 100 * a)
  // opt3: Option[Int] = Some(500)

  val list1: List[Int] = Monad[List].pure(3)
  // list1: List[Int] = List(3)

  val list2: List[Int] = Monad[List].flatMap(List(1, 2, 3))(a => List(a, a * 10))
  // list2: List[Int] = List(1, 10, 2, 20, 3, 30)

  val list3: List[Int] = Monad[List].map(list2)(a => a + 123)
  // list3: List[Int] = List(124, 133, 125, 143, 126, 153)

  /**
    * Cats also provides a Monad for Future. Unlike the methods on the Future
    * class itself, the pure and flatMap methods on the monad can’t accept implicit
    * ExecutionContext parameters (because the parameters aren’t part of the definitions in the
    * Monad trait).
    *
    * To work around this, Cats requires us to have an ExecutionContext in scope when we summon a
    * Monad for Future:
    *
    * {{{
    *     implicit def catsStdInstancesForFuture(implicit ec: ExecutionContext)
    * }}}
    */

  import cats.instances.future._ // for Monad
  import scala.concurrent._
  import scala.concurrent.duration._


  // val fm = Monad[Future]
  // <console>:37: error: could not find implicit value for parameter
  // instance: cats.Monad[scala.concurrent.Future]
  // val fm = Monad[Future]
  //

  import scala.concurrent.ExecutionContext.Implicits.global

  val fm: Monad[Future] = Monad[Future]

  import cats.instances.option._ // for Monad
  import cats.instances.list._ // for Monad
  import cats.syntax.applicative._ // for pure

  println(1.pure[Option])
  println(1.pure[List])

  import cats.Monad
  import cats.syntax.functor._ // for map
  import cats.syntax.flatMap._ // for flatMap
  import scala.language.higherKinds

  def sumSquare[F[_] : Monad](a: F[Int], b: F[Int]): F[Int] =
    a.flatMap(x => b.map(y => x * x + y * y))

  import cats.instances.option._ // for Monad
  import cats.instances.list._ // for Monad

  sumSquare(Option(3), Option(4))
  // res8: Option[Int] = Some(25)

  sumSquare(List(1, 2, 3), List(4, 5))
  // res9: List[Int] = List(17, 26, 20, 29, 25, 34)
}
