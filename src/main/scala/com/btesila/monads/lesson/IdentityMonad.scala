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

object IdentityMonad {

  import scala.language.higherKinds
  import cats.Monad
  import cats.syntax.functor._ // for map
  import cats.syntax.flatMap._ // for flatMap

  def sumSquare[F[_] : Monad](a: F[Int], b: F[Int]): F[Int] =
    for {
      x <- a
      y <- b
    } yield x * x + y * y

  /**
    * This method works well on Options and Lists but we can’t call it passing in
    * plain values.
    *
    * It would be incredibly useful if we could use sumSquare with parameters that
    * were either in a monad or not in a monad at all. This would allow us to abstract
    * over monadic and non-monadic code.
    *
    * Fortunately, Cats provides the Id type to bridge the gap:
    */

  import cats.Id

  println(sumSquare(3: Id[Int], 4: Id[Int]))
  // res2: cats.Id[Int] = 25

  /**
    * Id is actually a type alias that turns an atomic type into a single-parameter
    * type constructor. We can cast any value of any type to a corresponding Id.
    *
    * {{{
    *   package cats
    *   type Id[A] = A
    * }}}
    *
    * The ability to abstract over monadic and non-monadic code is extremely powerful.
    * For example, we can run code asynchronously in production using Future
    * and synchronously in test using Id.
    */
}
