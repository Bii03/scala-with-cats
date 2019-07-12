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
package com.btesila.monoidssemigroups.exercise

object PuttingAllTogether extends App {

  /**
    * The cutting edge SuperAdder v3.5a-32 is the world’s first choice for adding
    * together numbers. The main function in the program has signature def
    * add(items: List[Int]): Int. In a tragic accident this code is deleted!
    * Rewrite the method and save the day!
    */

  // def add(items: List[Int]): Int = items.sum

  import cats.instances.int._ // for Monoid
  import cats.syntax.semigroup._ // for |+|
  def add(items: List[Int]): Int =
    items.foldLeft(cats.Monoid[Int].empty)(_ |+| _)

  /**
    * Well done! SuperAdder’s market share con􀦞nues to grow, and now
    * there is demand for additional functionality. People now want to add
    * [[List[Option[Int]]. Change add so this is possible. The SuperAdder code
    * base is of the highest quality, so make sure there is no code duplication!
    */

  import cats.instances.option._ // for Monoid
  def addOption(items: List[Option[Int]]): Option[Int] =
    items.foldLeft(cats.Monoid[Option[Int]].empty)(_ |+| _)

  /**
    * Do not duplicate code
    */

  import cats.syntax.semigroup._
  def add[A](items: List[A])(implicit monoid: cats.Monoid[A]): A =
    items.foldLeft(monoid.empty)(_ |+| _)


  /**
    * SuperAdder is entering the POS (point-of-sale, not the other POS) market.
    * Now we want to add up Orders
    */
  case class Order(totalCost: Double, quantity: Double)

  implicit val orderAdder: Monoid[Order] = new Monoid[Order] {
    override def empty: Order = Order(0, 0)

    override def combine(x: Order, y: Order): Order = Order(x.totalCost + y.totalCost, x.quantity + y.quantity)
  }
}
