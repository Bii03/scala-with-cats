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
package com.btesila.monadtransformers.exercise

import cats.data.EitherT

import scala.concurrent.{Await, Future}
import scala.util.Success

object TransformRollOut extends App {

  /**
    * The Autobots, well-known robots in disguise, frequently send messages during
    * battle requesting the power levels of their team mates. This helps them
    * coordinate strategies and launch devastating attacks. The message sending
    * method looks like this:
    */
  //def getPowerLevel(autobot: String): Response[Int] = ???

  /**
    * Transmissions take time in Earth’s viscous atmosphere, and messages are occasionally
    * lost due to satellite malfunction or sabotage by pesky Decepticons.
    * Responses are therefore represented as a stack of monads:
    */
  type Response[A] = Future[Either[String, A]]

  /**
    * Optimus Prime is getting tired of the nested for comprehensions in his neural
    * matrix. Help him by rewriting Response using a monad transformer.
    */
  type ResponseT[A] = EitherT[Future, String, A]

  /**
    * Now test the code by implementing getPowerLevel to retrieve data from a
    * set of imaginary allies. Here’s the data we’ll use:
    */
  val powerLevels: Map[String, Int] = Map(
    "Jazz" -> 6,
    "Bumblebee" -> 8,
    "Hot Rod" -> 10
  )

  import cats.instances.future._
  import scala.concurrent.ExecutionContext.Implicits.global

  /**
    * If an Autobot isn’t in the powerLevels map, return an error message reporting
    * that they were unreachable. Include the name in the message for good effect.
    */
  def getPowerLevel(autobot: String): ResponseT[Int] =
    powerLevels.get(autobot) match {
      case Some(level) => EitherT.right(Future(level))
      case _ => EitherT.left(Future(s"$autobot unreachable"))
    }

  /**
    * Two autobots can perform a special move if their combined power level is
    * greater than 15. Write a second method, canSpecialMove, that accepts the
    * names of two allies and checks whether a special move is possible. If either
    * ally is unavailable, fail with an appropriate error message:
    */
  def canSpecialMove(ally1: String, ally2: String): ResponseT[Boolean] =
    for {
      one <- getPowerLevel(ally1)
      two <- getPowerLevel(ally2)
    } yield (one + two) > 15

  /**
    * Finally, write a method tacticalReport that takes two ally names and prints
    * a message saying whether they can perform a special move:
    */
  def tacticalReport(ally1: String, ally2: String): String = {
    import scala.concurrent.duration._
    val report: Future[Either[String, Boolean]] = canSpecialMove(ally1, ally2).value

    Await.result(report, 1 second) match {
      case Right(true) => s"$ally1 and $ally2 are ready to roll out"
      case Right(false) => s"$ally1 and $ally2 need a recharge"
      case Left(msg) => s"Comms error: $msg"
    }

  }

  println(tacticalReport("Jazz", "Bumblebee"))
  // res28: String = Jazz and Bumblebee need a recharge.

  println(tacticalReport("Bumblebee", "Hot Rod"))
  // res29: String = Bumblebee and Hot Rod are ready to roll out!

  println(tacticalReport("Jazz", "Ironhide"))
  // res30: String = Comms error: Ironhide unreachable

}
