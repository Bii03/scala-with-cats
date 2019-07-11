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
package com.btesila.typeclasses.exercise

import cats.Show
import com.btesila.typeclasses.exercise.model.Cat

object MeetingCats extends App {
  import cats.instances.int._
  import cats.instances.string._

  val showInt: Show[Int] = Show[Int]
  val showString: Show[String] = Show[String]

  // Using Interface Object
  println(showInt.show(1123))
  println(showString.show("lala"))

  // Using Interface Syntax
  import cats.syntax.show._
  println(1123.show)
  println("lala".show)

  // Defining & using custom instances
  implicit val booleanShow: Show[Boolean] = Show.show(bool => s"$bool: Boolean")
  implicit val doubleShow: Show[Double] = Show.fromToString[Double]
  implicit val catShow: Show[Cat] = Show.show(cat => s"$cat: Cat")

  println(true.show)
  println(2.0.show)

  import Cat._
  println(cat.show)
}
