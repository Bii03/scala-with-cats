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

object WriterWorking extends App {

  def slowly[A](body: => A): A =
    try body finally Thread.sleep(100)

  def factorial(n: Int): Int = {
    val ans = slowly(if (n == 0) 1 else n * factorial(n - 1))
    println(s"factorial 1: fact $n $ans")
    ans
  }

  import scala.concurrent._
  import scala.concurrent.ExecutionContext.Implicits.global
  import scala.concurrent.duration._

  /**
    * If we start several factorials in parallel, the log messages can become interleaved
    * on standard out. This makes it difficult to see which messages come
    * from which computation:
    */
  Await.result(Future.sequence(Vector(
    Future(factorial(3)),
    Future(factorial(3)),
    Future(factorial(3))
  )), 5.seconds)

  /**
    * If we start several factorials in parallel, the log messages can become interleaved
    * on standard out. This makes it difficult to see which messages come
    * from which computation.
    */

  /**
    * Rewrite factorial so it captures the log messages in a Writer. Demonstrate
    * that this allows us to reliably separate the logs for concurrent computations.
    */


  import cats.data.Writer
  import cats.syntax.applicative._ // for pure

  type Logged[A] = Writer[Vector[String], A]

  //  42.pure[Logged]
  // res13: Logged[Int] = WriterT((Vector(),42))


  import cats.syntax.writer._ // for tell
  Vector("Message").tell // a sort of defer
  // res14: cats.data.Writer[scala.collection.immutable.Vector[String],
  // Unit] = WriterT((Vector(Message),()))

  /**
    * Finally, we’ll import the Semigroup instance for Vector. We need this to map
    * and flatMap over Logged:
    */

  import cats.instances.vector._ // for Monoid
  41.pure[Logged].map(_ + 1)
  // res15: cats.data.WriterT[cats.Id,Vector[String],Int] = WriterT((
  // Vector(),42))


  println()
  println()
  println()

  def factorialWriter(n: Int): Logged[Int] =
    for {
      ans <- if (n == 0) 1.pure[Logged] else slowly(factorialWriter(n - 1).map(_ * n))
      _ <- Vector(s"factorial 2: fact $n $ans").tell
    } yield ans

  val (log, res) = factorialWriter(5).run

  // val Vector((logA, ansA), (logB, ansB), (logC, ansC)) =
   val r =  Await.result(Future.sequence(Vector(
      Future(factorialWriter(3).run),
      Future(factorialWriter(3).run),
      Future(factorialWriter(3).run)
    )), 5.seconds)

  println(r)
}
