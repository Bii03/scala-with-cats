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

object ReaderMonad extends App {

  /**
    * cats.data.Reader is a monad that allows us to sequence operations that depend
    * on some input. Instances of Reader wrap up functions of one argument,
    * providing us with useful methods for composing them.
    *
    * One common use for Readers is dependency injection. If we have a number
    * of operations that all depend on some external configuration, we can chain
    * them together using a Reader to produce one large operation that accepts
    * the configuration as a parameter and runs our program in the order specified.
    */

  /**
    * We can create a Reader[A, B] from a function A => B using the Reader.apply constructor:
    */

  import cats.data.Reader

  case class Cat(name: String, favoriteFood: String)

  val catName: Reader[Cat, String] = Reader(cat => cat.name)
  // catName: cats.data.Reader[Cat,String] = Kleisli(<function1>)

  /**
    * We can extract the function again using the Reader's run method and call it
    * using apply as usual:
    */
  catName.run(Cat("Garfield", "lasagne"))
  // res0: cats.Id[String] = Garfield

  /**
    * So far so simple, but what advantage do Readers give us over the raw functions?
    *
    * The power of Readers comes from their map and flatMap methods, which
    * represent different kinds of function composition. We typically create a set of
    * Readers that accept the same type of configuration, combine them with map
    * and flatMap, and then call run to inject the config at the end.
    * The map method simply extends the computation in the Reader by passing its
    * result through a function:
    */

  val greetKitty: Reader[Cat, String] = catName.map(name => s"Hello $name")

  println(greetKitty.run(Cat("Heathcliff", "junk food")))

  /**
    * The flatMap method is more interesting. It allows us to combine readers that
    * depend on the same input type. To illustrate this, let’s extend our greeting
    * example to also feed the cat:
    */

  val feedKitty: Reader[Cat, String] = Reader(cat => s"Have a nice bowl of ${cat.favoriteFood}")

  val greetAndFeed: Reader[Cat, String] =
    for {
      greet <- greetKitty
      feed <- feedKitty
    } yield s"$greet. $feed."

  greetAndFeed(Cat("Garfield", "lasagne"))
  // res3: cats.Id[String] = Hello Garfield. Have a nice bowl of lasagne.

  greetAndFeed(Cat("Heathcliff", "junk food"))
  // res4: cats.Id[String] = Hello Heathcliff. Have a nice bowl of junk food.

  /**
    * When to use Readers?
    *
    * Readers are most useful in situations where:
    * • we are constructing a batch program that can easily be represented by
    * a function;
    * • we need to defer injection of a known parameter or set of parameters;
    * • we want to be able to test parts of the program in isolation.
    *
    * By representing the steps of our program as Readers we can test them as
    * easily as pure functions, plus we gain access to the map and flatMap combinators.
    */



}
