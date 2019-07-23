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

object ReaderWorking extends App {

  /**
    * The classic use of Readers is to build programs that accept a configuration
    * as a parameter. Let’s ground this with a complete example of a simple login
    * system. Our configuration will consist of two databases: a list of valid users
    * and a list of their passwords:
    */
  case class Db(usernames: Map[Int, String], passwords: Map[String, String])

  import cats.data.Reader

  type DbReader[A] = Reader[Db, A]

  def findUsername(userId: Int): DbReader[Option[String]] =
    Reader(db => db.usernames.get(userId))

  def checkPassword(username: String, password: String): DbReader[Boolean] =
    Reader(db => db.passwords(username) == password)

  import cats.syntax.applicative._
  def checkLogin(userId: Int, password: String): DbReader[Boolean] =
    for {
      username <- findUsername(userId)
      check <- username.map(checkPassword(_, password)).getOrElse(false.pure[DbReader])
    } yield check

  val users = Map(
    1 -> "dade",
    2 -> "kate",
    3 -> "margo")

  val passwords = Map(
    "dade" -> "zerocool",
    "kate" -> "acidburn",
    "margo" -> "secret")

  val db = Db(users, passwords)

  checkLogin(1, "zerocool").run(db)
  // res10: cats.Id[Boolean] = true

  checkLogin(4, "davinci").run(db)
  // res11: cats.Id[Boolean] = false

}
