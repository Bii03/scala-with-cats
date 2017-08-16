package com.btesila.typeclasses

import cats.Show
import com.btesila.typeclasses.model.Cat

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

