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

object EveryFunctorIsAMonad {

  /**
    * Every monad is also a functor. We can define map in the same way for every
    * monad using the existing methods, flatMap and pure:
    */

  trait Monad[F[_]] {
    def pure[A](a: A): F[A]

    def flatMap[A, B](a: F[A])(f: A => F[B]): F[B]

    def map[A, B](a: F[A])(f: A => B): F[B] =
      flatMap(a)(value => pure(f(value)))
  }
}
