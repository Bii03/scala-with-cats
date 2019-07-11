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
package com.btesila.typeclasses.lesson

sealed trait Shape

case class Circle(radius: Double) extends Shape

/**
  * Covariance means that the type F[B] is a subtype of the type F[A] if B is a
  * subtype of A. This is useful for modelling many types, including collections like
  * List and Option.
  */

object Covariance {
  /**
    * The covariance of Scala collec􀦞ons allows us to subs􀦞tute collec􀦞ons of one
    * type for another in our code. For example, we can use a List[Circle] anywhere
    * we expect a List[Shape] because Circle is a subtype of Shape:
    */
  object Shape {
    val circles: List[Circle] = ???
    val shapes: List[Shape] = circles
  }

}

/**
  * Confusingly, contravariance means that the type F[B] is a subtype of F[A] if
  * A is a subtype of B. This is useful for modelling types that represent processes,
  * like our JsonWriter type class above:
  */
object Contravariance {

  trait JsonWriter[-A] {
    def write(value: A): Json
  }

  val shape: Shape = ???
  val circle: Circle = ???

  val shapeWriter: JsonWriter[Shape] = ???
  val circleWriter: JsonWriter[Circle] = ???

  def format[A](value: A, writer: JsonWriter[A]): Json = writer.write(value)

  /**
    * Now ask yourself the question: “Which of combinations of value and writer
    * can I pass to format?” We can combine circle with either writer because
    * all Circles are Shapes. Conversely, we can’t combine shape with circleWriter
    * because not all Shapes are Circles.
    */

  /**
    * JsonWriter[Shape] is a subtype of JsonWriter[Circle] because Circle is a
    * subtype of Shape. This means we can use shapeWriter anywhere we expect
    * to see a JsonWriter[Circle]
    */
}

/**
  * This means the types F[A] and F[B] are never subtypes of one another, no
  * matter what the relationship between A and B. This is the default semantics
  * for Scala type constructors.
  *
  * When the compiler searches for an implicit it looks for one matching the type
  * or subtype. Thus we can use variance annotations to control type class instance
  * selection to some extent.
  */
object Invariance {

  sealed trait A
  final case object B extends A
  final case object C extends A

  /**
    * Will an instance defined on a supertype be selected if one is available?
    * For example, can we define an instance for A and have it work for values
    * of type B and C?
    */

  /**
    * Will an instance for a subtype be selected in preference to that of a
    * supertype. For instance, if we define an instance for A and B, and we
    * have a value of type B, will the instance for B be selected in preference
    * to A?
    */

  /**
    * It turns out we can’t have both at once. The three choices give us behaviour
    * as follows:
    * Type Class Variance            | Invariant | Covariant | Contravariant
    * Supertype instance used?       | No        | No        | Yes
    * More specific type preferred?  | No        | Yes       | No
    */

  /**
    * Cats generally prefers to use invariant type classes. This allows us to specify more specific
    * instances for subtypes if we want. It does mean that if we have, for example, a value of type
    * Some[Int], our type class instance for Option will not be used. We can solve this problem
    * with a type annotation like Some(1) : Option[Int] or by using “smart constructors”
    * like the Option.apply, Option.empty, some, and none methods
    */

}

