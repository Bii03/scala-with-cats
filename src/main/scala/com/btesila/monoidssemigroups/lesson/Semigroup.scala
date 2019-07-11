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

/**
  * A semigroup is just the combine part of a monoid. While many semigroups
  * are also monoids, there are some data types for which we cannot define an
  * empty element.
  *
  * For example, we have just seen that sequence concatenation and integer addi􀦞on are monoids.
  * However, if we restrict ourselves to non-empty sequences and positive integers, we are no longer
  * able to define a sensible empty element. Cats has a NonEmptyList data type that has an
  * implementation of Semigroup but no implementation of Monoid.
  */
trait Semigroup[A] {
  def combine(x: A, y: A): A
}

/**
  * We’ll see this kind of inheritance o􀁛en when discussing type classes. It provides
  * modularity and allows us to re-use behaviour. If we define a Monoid
  * for a type A, we get a Semigroup for free. Similarly, if a method requires a
  * parameter of type Semigroup[B], we can pass a Monoid[B] instead.
  */
trait Monoid[A] extends Semigroup[A] {
  def empty: A
}

object Monoid {
  def apply[A](implicit monoid: Monoid[A]): Monoid[A] = monoid
}
