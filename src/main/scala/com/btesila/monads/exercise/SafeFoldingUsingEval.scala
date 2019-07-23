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

import cats.Eval

object SafeFoldingUsingEval extends App {

  /**
    * One useful property of Eval is that its map and flatMap methods are trampolined.
    * This means we can nest calls to map and flatMap arbitrarily without
    * consuming stack frames. We call this property “stack safety”.
    */

  /**
    * Eval.defer, which takes an existing instance of Eval and defers its evaluation. The defer
    * method is trampolined like map and flatMap, so we can use it as a quick way to make an
    * existing operation stack safe.
    */

  /**
    * The naive implementation of foldRight below is not stack safe. Make it so
    * using Eval:
    */

  def foldRight[A, B](as: List[A], acc: B)(fn: (A, B) => B): B =
    as match {
      case head :: tail =>
        fn(head, foldRight(tail, acc)(fn))
      case Nil =>
        acc
    }

  def foldRightEval[A, B](as: List[A], acc: Eval[B])(fn: (A, Eval[B]) => Eval[B]): Eval[B] =
    as match {
      case head :: tail =>
        Eval.defer(fn(head, foldRightEval(tail, acc)(fn)))
      case Nil =>
        acc
    }

  def safeFoldRight[A, B](as: List[A], acc: B)(fn: (A, B) => B): B =
    foldRightEval(as, Eval.now(acc)) { (a: A, b: Eval[B]) =>
      b.map(fn(a, _))
    }.value
}
