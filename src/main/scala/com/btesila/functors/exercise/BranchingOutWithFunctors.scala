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
package com.btesila.functors.exercise

object BranchingOutWithFunctors extends App {

  /**
    * Write a Functor for the following binary tree data type. Verify that the code
    * works as expected on instances of Branch and Leaf.
    */

  sealed trait Tree[+A]

  final case class Branch[A](left: Tree[A], right: Tree[A]) extends Tree[A]

  final case class Leaf[A](value: A) extends Tree[A]

  import cats.Functor

  implicit val treeFunctor: Functor[Tree] = new Functor[Tree] {
    override def map[A, B](fa: Tree[A])(f: A => B): Tree[B] = fa match {
      case Branch(left, right) => Branch(map(left)(f), map(right)(f))
      case Leaf(value) => Leaf(f(value))
    }
  }

  //  Branch(Leaf(10), Leaf(20)).map(_ * 2)
  // <console>:42: error: value map is not a member of wrapper.Branch[Int]
  // Branch(Leaf(10), Leaf(20)).map(_ * 2)
  //

  /**
    * Oops! This is falls foul of the same invariance problem we discussed before: Cats generally
    * prefers to use invariant type classes. Functor is invariant.
    * The compiler can find a Functor instance for Tree but not for Branch
    * or Leaf. Let’s add some smart constructors to compensate:
    */

  object Tree {
    def branch[A](left: Tree[A], right: Tree[A]): Tree[A] = Branch(left, right)

    def leaf[A](value: A): Tree[A] = Leaf(value)
  }

  import cats.syntax.functor._

  println(Tree.leaf(100).map(_ * 2))
  // res10: wrapper.Tree[Int] = Leaf(200)

  println(Tree.branch(Tree.leaf(10), Tree.leaf(20)).map(_ * 2))
  // res11: wrapper.Tree[Int] = Branch(Leaf(20),Leaf(40))
}
