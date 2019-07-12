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

import cats.Functor

object HigherKindsAndTypeConstructors {

  /**
    * Kinds are like types for types. They describe the number of “holes” in a type.
    * We distinguish between regular types that have no holes and “type constructors”
    * that have holes we can fill to produce types.
    *
    * For example, List is a type constructor with one hole. We fill that hole by
    * specifying a parameter to produce a regular type like List[Int] or List[A].
    * The trick is not to confuse type constructors with generic types. List is a type
    * constructor, List[A] is a type:
    *
    * List    - type constructor, takes one parameter
    * List[A] - type, produced using a type parameter
    */

  /**
    * There’s a close analogy here with functions and values:
    * - Functions are “value constructors” — they produce values when we supply parameters
    *
    * math.abs    - function, takes one parameter
    * math.abs(x) - value, produced using a value parameter
    */

  /**
    * Declaring type constructors:
    *
    * - declaring F using underscores
    * - referencing F without underscores
    */
  def myMethod[F[_]](implicit functor: Functor[F]): Unit = {
    val functor = cats.Functor.apply[F]
  }

  /**
    * Analogue to:
    */
  // Declare f specifying parameters:
  val f: Int => Int = (x: Int) => x * 2

  // Reference f without parameters:
  val f2: Int => Int = f andThen f


  /**
    * Higher kinded types are considered an advanced language feature in
    * Scala. Whenever we declare a type constructor with A[_] syntax, we
    * need to “enable” the higher kinded type language feature to suppress
    * warnings from the compiler. We can either do this with a “language
    * import” as above:
    */
  import scala.language.higherKinds

  /**
    * Or by adding the following to scalacOptions in build.sbt:
    * scalacOptions += "-language:higherKinds
    */
}
