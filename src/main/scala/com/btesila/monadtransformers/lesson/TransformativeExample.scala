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

object TransformativeExample extends App {

  /**
    * Cats provides transformers for many monads, each named with a T suffix: EitherT
    * composes Either with other monads, OptionT composes Option, and so on.
    */

  // Here’s an example that uses OptionT to compose List and Option.

  import cats.data.OptionT

  /**
    * For example, our ListOption type below is an alias for OptionT[List, A]
    * but the result is effectively a List[Option[A]. In other words, we build
    * monad stacks from the inside out
    */
  type ListOption[A] = OptionT[List, A]

  import cats.Monad
  import cats.instances.list._ // for Monad
  import cats.syntax.applicative._ // for pure

  val result1: ListOption[Int] = OptionT(List(Option(10)))
  // result1: ListOption[Int] = OptionT(List(Some(10)))

  val result2: ListOption[Int] = 32.pure[ListOption]
  // result2: ListOption[Int] = OptionT(List(Some(32)))

  // The map and flatMap methods combine the corresponding methods of List and Option into single
  // operations:

  result1 flatMap { x: Int =>
    result2 map { y: Int =>
      x + y
    }
  }
  // res1: cats.data.OptionT[List,Int] = OptionT(List(Some(42)))

  /**
    * This is the basis of all monad transformers. The combined map and flatMap
    * methods allow us to use both component monads without having to recursively
    * unpack and repack values at each stage in the computation.
    */
}
