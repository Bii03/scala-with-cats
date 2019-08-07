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
package com.btesila.monadtransformers.exercise

object ComposingMonads extends App {

  /**
    * A question arises. Given two arbitrary monads, can we combine them in some
    * way to make a single monad? That is, do monads compose? We can try to
    * write the code but we soon hit problems:
    */

  import cats.Monad
  import cats.syntax.applicative._ // for pure
  import cats.syntax.flatMap._ // for flatMap
  import scala.language.higherKinds

  // Hypothetical example. This won't actually compile:
//  def compose[M1[_] : Monad, M2[_] : Monad] = {
//    type Composed[A] = M1[M2[A]]
//    new Monad[Composed] {
//      def pure[A](a: A): Composed[A] =
//        a.pure[M2].pure[M1]
//
//      def flatMap[A, B](fa: Composed[A])
//                       (f: A => Composed[B]): Composed[B] =
//      // Problem! How do we write flatMap?
//      // It is impossible to write a general definition of flatMap without knowing
//      // something about M1 or M2. However, if we do know something about one
//      // or other monad, we can typically complete this code.
//    }
//  }

  // For example, if we fix M2 above to be Option, a definition of flatMap comes to light:

  import cats.instances.option._
  import cats.implicits._
//
//  def compose[M[_] : Monad] = {
//    type Composed[A] = M[Option[A]]
//    new Monad[Composed] {
//      def pure[A](a: A): Composed[A] =
//        a.pure[Option].pure[M]
//
//      def flatMap[A, B](fa: Composed[A])(f: A => Composed[B]): Composed[B] =
//        M.flatMap(_.fold(None.pure[M])(f))
//    }
//  }
}
