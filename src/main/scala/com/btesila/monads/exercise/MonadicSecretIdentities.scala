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
package com.btesila.monads.exercise

object MonadicSecretIdentities {

  import cats.Id

  def pure[A](value: A): Id[A] = value

  def map[A, B](initial: Id[A])(func: A => B): Id[B] = func(initial)

  def flatMap[A, B](initial: Id[A])(func: A => Id[B]): Id[B] = func(initial)

  /**
    * The final punch line is that, once we strip away the Id type constructors,
    * flatMap and map are actually identical.
    *
    * This ties in with our understanding of functors and monads as sequencing type
    * classes. Each type class allows us to sequence operations ignoring some kind
    * of complication. In the case of Id there is no complication, making map and
    * flatMap the same thing.
    */
}
