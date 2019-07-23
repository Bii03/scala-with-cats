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

import cats.Id
import cats.data.WriterT

object WriterMonad {

  /**
    * cats.data.Writer is a monad that lets us carry a log along with a computation. We can use it
    * to record messages, errors, or additional data about a computation, and extract the log
    * alongside the final result.
    */

  /**
    * One common use for Writers is recording sequences of steps in multi-
    * threaded computations where standard imperative logging techniques can result
    * in interleaved messages from different contexts. With Writer the log for
    * the computation is tied to the result, so we can run concurrent computations
    * without mixing logs.
    */

  import cats.data.Writer
  import cats.instances.vector._ // for Monoid

  Writer(Vector(
    "It was the best of times",
    "it was the worst of times"
  ), 1859)
  // res0: cats.data.WriterT[cats.Id,scala.collection.immutable.Vector[
  // String],Int] = WriterT((Vector(It was the best of times, it was
  //the worst of times),1859))

  /**
    * Writer is a type alias for WriterT, so we can read types like WriterT[Id, W, A]
    * as Writer[L, V]:
    * {{{
    *   type Writer[L, V] = WriterT[Id, L, V]
    * }}}
    *
    * where L -> log, V -> value
    *
    */

  /**
    * For convenience, Cats provides a way of creating Writers specifying only the
    * log or the result. If we only have a result we can use the standard pure syntax.
    * To do this we must have a Monoid[W] in scope so Cats knows how to produce
    * an empty log:
    */

  import cats.instances.vector._ // for Monoid
  import cats.syntax.applicative._ // for pure

  type Logged[A] = Writer[Vector[String], A]

  123.pure[Logged]
  // res2: Logged[Int] = WriterT((Vector(),123))

  /**
    * If we have a log and no result we can create a Writer[Unit] using the tell
    * syntax from cats.syntax.writer:
    */

  import cats.syntax.writer._ // for tell
  Vector("msg1", "msg2", "msg3").tell

  /**
    * If we have both a result and a log, we can either use Writer.apply or we can
    * use the writer syntax from cats.syntax.writer:
    */
  val a: WriterT[Id, Vector[String], Int] = Writer(Vector("msg1", "msg2", "msg3"), 123)
  // a: cats.data.WriterT[cats.Id,scala.collection.immutable.Vector[
  // String], Int] = WriterT((Vector(msg1, msg2, msg3), 123))

  val b: Writer[Vector[String], Int] = 123.writer(Vector("msg1", "msg2", "msg3"))
  // b: cats.data.Writer[scala.collection.immutable.Vector[String],Int]
  //  = WriterT((Vector(msg1, msg2, msg3), 123))

  /**
    * We can extract the result and log from a Writer using the value and written
    * methods respectively:
    */
  val aResult: Int = a.value
  // aResult: Int = 123

  val aLog: Vector[String] = a.written
  // aLog: Vector[String] = Vector(msg1, msg2, msg3)

  /**
    * We can extract both values at the same time using the run method:
    */
  val (log, result) = b.run
  // log: scala.collection.immutable.Vector[String] = Vector(msg1, msg2,msg3)
  // result: Int = 123

  /**
    * The log in a Writer is preserved when we map or flatMap over it. flatMap
    * appends the logs from the source Writer and the result of the user’s sequencing
    * function. For this reason it’s good practice to use a log type that has an
    * efficient append and concatenate operations, such as a Vector:
    */


  val writer1: WriterT[Id, Vector[String], Int] = for {
    a <- 10.pure[Logged]
    _ <- Vector("a", "b", "c").tell
    b <- 32.writer(Vector("x", "y", "z"))
  } yield a + b

  // writer1: cats.data.WriterT[cats.Id,Vector[String],Int] = WriterT((
  //Vector(a, b, c, x, y, z)
  //, 42
  //) )
  writer1.run
  // res4: cats.Id[(Vector[String], Int)] = (Vector(a, b, c, x, y, z)
  // , 42
  //)

}
