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

import cats.Monad

import scala.annotation.tailrec

object WritingCustomMonads extends App {

  /**
    * Write a Monad for the Tree type
    */

  sealed trait Tree[+A]

  final case class Branch[A](left: Tree[A], right: Tree[A]) extends Tree[A]

  final case class Leaf[A](value: A) extends Tree[A]

  def branch[A](left: Tree[A], right: Tree[A]): Tree[A] = Branch(left, right)

  def leaf[A](value: A): Tree[A] = Leaf(value)

  val treeMonad: Monad[Tree] = new Monad[Tree] {
    override def flatMap[A, B](fa: Tree[A])(f: A => Tree[B]): Tree[B] =
      fa match {
        case Branch(left, right) => Branch(flatMap(left)(f), flatMap(right)(f))
        case Leaf(value) => f(value)
      }

    /**
      * Its only downside is that Cats cannot make guarantees about stack safety:
      * it's not tail recursive.
      */
    override def tailRecM[A, B](a: A)(f: A => Tree[Either[A, B]]): Tree[B] =
      f(a) match {
        case Leaf(Left(t)) => tailRecM(t)(f)
        case Leaf(Right(r)) => leaf(r)
        case Branch(left, right) =>
          branch(
            flatMap(left) {
              case Right(v) => pure(v)
              case Left(t) => tailRecM(t)(f)
            },
            flatMap(right) {
              case Right(v) => pure(v)
              case Left(t) => tailRecM(t)(f)
            }
          )
      }

    override def pure[A](x: A): Tree[A] = Leaf(x)
  }


  /**
    * The tail-recursive solution is much harder to write. We adapted this solution from this Stack
    * Overflow post by Nazarii Bardiuk. It involves an explicit depth first traversal of the tree,
    * maintaining an open list of nodes to visit and a closed list of nodes to use to reconstruct
    * the tree.
    */
  implicit val tailRecTreeMonad: Monad[Tree] = new Monad[Tree] {
    override def flatMap[A, B](fa: Tree[A])(f: A => Tree[B]): Tree[B] =
      fa match {
        case Branch(left, right) => Branch(flatMap(left)(f), flatMap(right)(f))
        case Leaf(value) => f(value)
      }

    override def tailRecM[A, B](a: A)(f: A => Tree[Either[A, B]]): Tree[B] = {
      @scala.annotation.tailrec
      def loop(open: List[Tree[Either[A, B]]], closed: List[Tree[B]]): List[Tree[B]] =
        open match {
          case Branch(left, right) :: tail => loop(left :: right :: tail, closed)
          case Leaf(Left(t)) :: tail => loop(tail, tailRecM(t)(f) :: closed)
          case Leaf(Right(v)) :: tail => loop(tail, pure(v) :: closed)
          case Nil => closed
        }

      loop(List(f(a)), Nil).head
    }

    override def pure[A](x: A): Tree[A] = Leaf(x)
  }

  import cats.syntax.functor._ // for map
  import cats.syntax.flatMap._ // for flatMap

  println(branch(leaf(100), leaf(200)).flatMap(x => branch(leaf(x - 1), leaf(x + 1))))

  val bla: Tree[Int] = for {
    a <- branch(leaf(100), leaf(200))
    b <- branch(leaf(a - 10), leaf(a + 10))
    c <- branch(leaf(b - 1), leaf(b + 1))
  } yield c

  println(bla)
}
