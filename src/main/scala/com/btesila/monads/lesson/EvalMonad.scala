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
package com.btesila.monads.lesson

object EvalMonad {

  /**
    * Eval is a monad that allows us to abstract over different models of evaluation. We typically
    * hear of two such models: eager and lazy. Eval throws in a further distinction of whether or
    * not a result is memoized.
    *
    * Eager computations happen immediately whereas lazy computations happen
    * on access. Memoized computations are run once on first access, after which
    * the results are cached.
    *
    * For example, Scala vals are eager and memoized.
    *
    * By contrast, defs are lazy and not memoized.
    */


  /**
    * Eval has three subtypes: Now, Later, and Always. We construct these with
    * three constructor methods, which create instances of the three classes and
    * return them typed as Eval:
    */

  import cats.Eval

  val now: Eval[Double] = Eval.now(math.random + 1000) // similar to val
  // now: cats.Eval[Double] = Now(1000.6884369117727)

  val later: Eval[Double] = Eval.later(math.random + 2000) // similar to lazy val
  // later: cats.Eval[Double] = cats.Later@71175ee9

  val always: Eval[Double] = Eval.always(math.random + 3000) // similar to def
  // always: cats.Eval[Double] = cats.Always@462e2fea

  val x: Eval[Double] = Eval.now {
    println("Computing X")
    math.random
  }
  // Computing X
  // x: cats.Eval[Double] = Now(0.8724950064732552)

  x.value // first access
  // res9: Double = 0.8724950064732552

  x.value // second access
  // res10: Double = 0.8724950064732552

  val y: Eval[Double] = Eval.always {
    println("Computing Y")
    math.random
  }
  // y: cats.Eval[Double] = cats.Always@5212e1f5

  y.value // first access
  // Computing Y
  // res11: Double = 0.8795680260041828

  y.value // second access
  // Computing Y
  // res12: Double = 0.5640213059400854

  val z: Eval[Double] = Eval.later {
    println("Computing Z")
    math.random
  }
  // z: cats.Eval[Double] = cats.Later@33eda11
  z.value // first access
  // Computing Z
  // res13: Double = 0.5813583535421343
  z.value // second access
  // res14: Double = 0.5813583535421343
}
