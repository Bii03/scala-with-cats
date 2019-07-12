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
package com.btesila.monoidssemigroups.lesson

object MonoidsInCats extends App {

  import cats.instances.string._ // for Monoid instance


  cats.Monoid[String].combine("Hi ", "there")
  // res0: String = Hi there

  cats.Monoid[String].empty
  // res1: String = ""


  // equivalent to:
  /**
    * Access an implicit `Monoid[A]`.
    */
  //  @inline final def apply[A](implicit ev: Monoid[A]): Monoid[A] = ev

  cats.Monoid.apply[String].combine("Hi ", "there")
  // res2: String = Hi there

  cats.Monoid.apply[String].empty
  // res3: String = ""

  /**
    * As we know, Monoid extends Semigroup. If we don’t need empty we can
    * equivalently write:
    * import cats.Semigroup
    */

  cats.Semigroup[String].combine("Hi", "there")
  // res4: String = Hi there


  /**
    * Monoid Syntax
    */

  import cats.syntax.semigroup._ // for |+|
  val stringResult: String = "Hi" |+| "there"
  // stringResult: String = Hi there

  import cats.instances.int._

  val intResult: Int = 1 |+| 2 |+| cats.Monoid[Int].empty
  println(intResult)
  // intResult: Int = 3
}
