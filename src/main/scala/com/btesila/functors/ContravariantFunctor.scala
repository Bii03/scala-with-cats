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

import com.btesila.typeclasses.model.Box


/**
  * We can think of Functor's map method as “appending” a
  * transformation to a chain.
  * Contravariant Functor - typeclass that prepends an operation to the chain
  * Invariant Functor - typeclass that build bidirectional chain of operations
  */
object ContravariantFunctor extends App {

  // this is represented by the contramap function, which only makes sense for data types that respresent
  // transformations (e.g. Encoder). For instance, we cannot define contramap on Option because there is
  // no way of feeding a value in Option[B] backwards through a function A => B
  // we can define for Printable, though

  // see Printable contramap implementation

  import com.btesila.typeclasses.Printable._
  import com.btesila.typeclasses.PrintableInstances._

  println(format(Box("hello world")))
}

/**
  * Combination between map and contramap
  * - map generates new type class by appending a function to the chain
  * - contramap generates a new type class by prepending a function to the chain
  * - imap generates them via a pair of bidirectional transformations
  */
object InvariantFunctor extends App {

  import Codec._

  encode(123.4)
  decode[Double]("123.4")


  encode(Box(123.4))
  decode[Box[Double]]("123.4")
}
