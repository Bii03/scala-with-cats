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

object PartialUnification {

  /**
    * Remember we had the following code that raised a compiler error:
    *
    * {{{
    *   val func1 = (x: Int) => x.toDouble
    *   val func2 = (y: Double) => y * 2
    *   val func3 = func1.map(func2)
    *     // func3: Int => Double = scala.runtime.AbstractFunction1$$Lambda$2313
    *     /1769218156@6b5ae8b2
    * }}}
    *
    * This got resolved by setting the following flag to the scala compiler:
    * {{{
    *   scalacOptions += "-Ypartial-unification"
    * }}}
    *
    * The error is due to the fact that a functor is defined using a type constructor
    *{{{
    *   trait Functor[F[_]]
    *}}}
    *
    * While a Function1 takes 2 parameters
    * {{{
    *   trait Function1[-T, +R]
    * }}}
    *
    * The compiler needs to be able to fix one type to get a Functor, and that's what is happening
    * when partial unification is turned on.
    */

  /**
    * Partial Unification works by fixing type parameters from left to right. In the above example,
    * the compiler fixes the Int in Int => Double and looks for a Functor for functions of type
    * Int => A:
    */

  type F[A] = Int => A
  // val functor = Functor[F]

}
