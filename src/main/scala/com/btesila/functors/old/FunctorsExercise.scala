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
package com.btesila.functors.old

object FunctorsExercise extends App {

  import cats.Functor


   implicit val treeFunctor: Functor[Tree] = new Functor[Tree] {
     override def map[A, B](tree: Tree[A])(func: A => B): Tree[B] = tree match {
       case Branch(left, right) => Branch(map(left)(func), map(right)(func))
       case Leaf(value) => Leaf(func(value))
     }
   }

  import cats.syntax.functor._

  // this does not compile - no functor for branch/leaf
  // Branch(Leaf(10), Leaf(20)).map(_ * 2)

  Tree.leaf(100).map(_ * 2)
  Tree.branch(Leaf(10), Leaf(20)).map(_ * 2)

}
