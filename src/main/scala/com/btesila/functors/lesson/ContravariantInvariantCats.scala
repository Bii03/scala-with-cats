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
package com.btesila.functors.lesson

object ContravariantInvariantCats extends App {
  import cats.Contravariant
  import cats.Show
  import cats.instances.string._

  val showString = Show[String] // provides a better to string

  val showSymbol = Contravariant[Show].contramap(showString)((sym: Symbol) => s"'${sym.name}")

  import cats.syntax.contravariant._ // for contramap
  showString.contramap((sym: Symbol) => s"'${sym.name}").show('dave)


  /**
    * Cats provides an instance of Invariant for Monoid:
    *
    * Imagine we want to produce a Monoid for Scala’s Symbol type. Cats doesn’t
    * provide a Monoid for Symbol but it does provide a Monoid for a similar type: String. We can
    * write our new semigroup with an empty method that relies on the empty String:
    */

  import cats.Monoid
  import cats.syntax.invariant._ // for imap
  import cats.instances.string._ // for Monoid instance

  implicit val symbolMonoid: Monoid[Symbol] = Monoid[String].imap(Symbol.apply)(_.name)
}
