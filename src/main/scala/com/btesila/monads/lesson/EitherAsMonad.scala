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

object EitherAsMonad {

  /**
    * Error recovery is important when processing large jobs. We don’t want to run a job for a day
    * and then find it failed on the last element.
    *
    * Error reporting is equally important. We need to know what went wrong, not just that something
    * went wrong.
    *
    * In a number of cases we want to collect all the errors, not just the first one we encountered.
    * A typical example is validating a web form. It’s a far better experience to report all errors
    * to the user when they submit a form than to report them one at a time.
    */

  /**
    * Cats provides an additional type class called MonadError that abstracts over Either-like data
    * types that are used for error handling. MonadError provides extra operations for raising and
    * handling errors.
    *
    * {{{
    *   package cats
    *
    *   trait MonadError[F[_], E] extends Monad[F] {
    *     // Lift an error into the `F` context:
    *     def raiseError[A](e: E): F[A] // like the pure method of the monad
    *
    *     // Handle an error, potentially recovering from it:
    *     def handleError[A](fa: F[A])(f: E => A): F[A] // similar to recover method of [[Future]]
    *
    *     // Test an instance of `F`, failing if the predicate is not satisfied:
    *     def ensure[A](fa: F[A])(e: E)(f: A => Boolean): F[A] // filter like behaviour
    *     }
    * }}}
    *
    * MonadError is defined in terms of two type parameters:
    * • F is the type of the monad;
    * • E is the type of error contained within F.
    */

  import cats.MonadError
  import cats.instances.either._ // for MonadError

  type ErrorOr[A] = Either[String, A]

  val monadError: MonadError[ErrorOr, String] = MonadError[ErrorOr, String]

  val failure: ErrorOr[Nothing] = monadError.raiseError("Badness")
  // failure: ErrorOr[Nothing] = Left(Badness)

  monadError.handleError(failure) {
    case "Badness" =>
      monadError.pure("It's ok")
    case other =>
      monadError.raiseError("It's not ok")
  }
  // res2: ErrorOr[ErrorOr[String]] = Right(Right(It's ok))

  import cats.syntax.either._ // for asRight

  val success: ErrorOr[Int] = monadError.pure(42)
  // success: ErrorOr[Int] = Right(42)

  monadError.ensure(success)("Number too low!")(_ > 1000)
  // res3: ErrorOr[Int] = Left(Number too low!)

}
