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

import cats.Monad

import scala.annotation.tailrec

object CustomMonads {

  /**
    * We can define a Monad for a custom type by providing implementations of
    * three methods: flatMap, pure and tailRecM.
    */

  /**
    * An implementaton of Monad for Option
    */

  val optionMonad: Monad[Option] = new Monad[Option] {

    override def flatMap[A, B](fa: Option[A])(f: A => Option[B]): Option[B] =
      fa flatMap f

    @tailrec
    override def tailRecM[A, B](a: A)(f: A => Option[Either[A, B]]): Option[B] =
      f(a) match {
        case None => None
        case Some(Right(r)) => Some(r)
        case Some(Left(t)) => tailRecM(t)(f)
      }

    override def pure[A](x: A): Option[A] = Option(x)
  }

  /**
    * The tailRecM method is an optimisation used in Cats to limit the amount
    * of stack space consumed by nested calls to flatMap. The technique comes
    * from a 2015 paper by PureScript creator Phil Freeman. The method should
    * recursively call itself until the result of fn returns a Right.
    */

  /**
    * If we can make tailRecM tail-recursive, Cats is able to guarantee stack safety
    * in recursive situations such as folding over large lists (see Sec􀦞on 7.1). If we
    * can’t make tailRecM tail-recursive, Cats cannot make these guarantees and
    * extreme use cases may result in StackOverflowErrors. All of the built-in
    * monads in Cats have tail-recursive implementations of tailRecM, although
    * writing one for custom monads can be a challenge… as we shall see.
    */
}
