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
package com.btesila.monadtransformers.lesson

import cats.data.{EitherT, OptionT}

import scala.concurrent.Future
import scala.util.Try

object MonadTransformersInCats extends App {

  /**
    * Each monad transformer is a data type, defined in cats.data, that allows
    * us to wrap stacks of monads to produce new monads.
    *
    * The main concepts we have to cover to understand monad transformers are:
    * • the available transformer classes;
    * • how to build stacks of monads using transformers;
    * • how to construct instances of a monad stack; and
    * • how to pull apart a stack to access the wrapped monads.
    */

  /**
    * Many monads and all transformers have at least two type parameters, so we
    * often have to define type aliases for intermediate stages.
    */

  // Alias Either to a type constructor with one parameter:
  type ErrorOr[A] = Either[String, A]

  // Build our final monad stack using OptionT:
  type ErrorOrOption[A] = OptionT[ErrorOr, A]

  import cats.instances.either._
  import cats.syntax.applicative._

  val a = 10.pure[ErrorOrOption]
  // a: ErrorOrOption[Int] = OptionT(Right(Some(10)))

  val b = 32.pure[ErrorOrOption]
  // b: ErrorOrOption[Int] = OptionT(Right(Some(32)))

  val c = a.flatMap(x => b.map(y => x + y))

  // c: cats.data.OptionT[ErrorOr,Int] = OptionT(Right(Some(42)))

  /**
    * Things become even more confusing when we want to stack three or more monads.
    *
    * For example, let’s create a Future of an Either of Option.
    */

  // Impossible to do it the same way - because EitherT has three type parameters: EitherT[F[_], A, B]
  // type FutureErrorOrOption[E, A] = EitherT[Future, E, A] - WRONG!

  // We need this
  //case class EitherT[F[_], E, A](stack: F[Either[E, A]])

  type FutureEither[A] = EitherT[Future, String, A]

  // See how it all turns out to be inside out: Option -> Either -> Future
  type FutureEitherOption[A] = OptionT[FutureEither, A]

  import cats.instances.future._
  import cats.syntax.applicative._

  import scala.concurrent.ExecutionContext.Implicits.global

  val futureEitherOr: FutureEitherOption[Int] =
    for {
      a <- 10.pure[FutureEitherOption]
      b <- 32.pure[FutureEitherOption]
    } yield a + b

  // Each call to value unpacks a single monad transformer:
  val stackValue: Future[Either[String, Option[Int]]] = futureEitherOr.value.value

  /**
    * ----------------------------------------------------------------------------------------------
    * ------------------------------------ Usage Patterns ----------------------------------------
    * ----------------------------------------------------------------------------------------------
    */

  /**
    * One approach involves creating a single “super stack” and sticking to it throughout our code
    * base. For example, in a web application, we could decide that all request handlers are
    * asynchronous and all can fail with the same set of HTTP error codes.
    * We could design a custom ADT representing the errors and use a fusion Future and Either
    * everywhere in our code.
    */

  sealed abstract class HttpError

  final case class NotFound(item: String) extends HttpError

  final case class BadRequest(msg: String) extends HttpError

  type FutureEitherHttp[A] = EitherT[Future, HttpError, A]

  /**
    * The “super stack” approach starts to fail in larger, more heterogeneous code
    * bases where different stacks make sense in different contexts.
    * Another design pattern that makes more sense in these contexts uses monad transformers as local
    * glue code.
    *
    * We expose untransformed stacks at module boundaries, transform them to operate on them locally,
    * and untransform them before passing them on. This allows each module of code to make its own
    * decisions about which transformers to use.
    */

  import cats.data.Writer

  type Logged[A] = Writer[List[String], A] // WriterT[Id, L, V]

  // Methods generally return untransformed stacks:
  def parseNumber(str: String): Logged[Option[Int]] =
    Try(str.toInt).toOption match {
      case Some(num) => Writer(List(s"Read $str"), Some(num))
      case None => Writer(List(s"Failed on $str"), None)
    }

  // Consumers use monad transformers locally to simplify composition:
//  def addAll(a: String, b: String, c: String): Logged[Option[Int]] = {
//    import cats.data.OptionT
//    val result: OptionT[Logged, Int] = for {
//      a <- OptionT(parseNumber(a))
//      b <- OptionT(parseNumber(b))
//      c <- OptionT(parseNumber(c))
//    } yield a + b + c
//    result.value
//  }

  // This approach doesn't force OptionT on other users' code:
//  val result1: Logged[Option[Int]] = addAll("1", "2", "3")
  // result1: Logged[Option[Int]] = WriterT((List(Read 1, Read 2, Read 3), Some(6)))


  /**
    * In this chapter we introduced monad transformers, which eliminate the need
    * for nested for comprehensions and pattern matching when working with
    * “stacks” of nested monads.
    */
}
