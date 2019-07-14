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
package com.btesila.functors.lesson

object ContravariantInvariantFunctors extends App {

  /**
    * As we have seen, we can think of Functor's map method as “appending” a
    * transformation to a chain.
    *
    * The are two other type classes, which aid in:
    * - prepending operations to the chain aka Contravariant Functor
    * - building a bidirectional chain of operations aka Invariant Functor
    */

  /**
    * Contravariant Functor
    *
    * Provides a method called `contramap` and makes sense only for data types that represent
    * transformations.
    *
    * We can’t define contramap for an Option because there is no way of feeding a value in an
    * Option[B] backwards through a function A => B.
    *
    * However, we can do so for our [[Printable]] instance.
    */

  trait Printable[A] {
    self =>
    def format(value: A): String

    def contramap[B](f: B => A): Printable[B] = new Printable[B] {
      override def format(value: B): String = self.format(f(value))
    }
  }

  implicit val stringPrintable: Printable[String] = new Printable[String] {
    override def format(value: String): String = "|" + value + "|"
  }

  implicit val booleanPrintable: Printable[Boolean] = new Printable[Boolean] {
    override def format(value: Boolean): String =
      if (value) "yes" else "no"
  }

  /**
    * Rather than writing out the complete definition from scratch (new Printable[Box] etc…),
    * create your instance from an existing instance using contramap.
    */

  final case class Box[A](value: A)

  implicit def printableBox[A](implicit printable: Printable[A]): Printable[Box[A]] = {
    val f: Box[A] => A = (box: Box[A]) => box.value
    printable.contramap(f) // p.contramap[Box[A]](_.value)
  }

  /**
    * Using contramap is much simpler, and conveys the functional programming
    * approach of building solutions by combining simple building blocks using pure
    * functional combinators.
    */

  /**
    * -------------------------------------------------------------------------------------------- *
    * -------------------------------------------------------------------------------------------- *
    * -------------------------------------------------------------------------------------------- *
    * -------------------------------------------------------------------------------------------- *
    */

  /**
    * Invariant Functor
    *
    * Provides a method called `imap`, which is an equivalent of the combination of `map` and
    * `contramap`.
    *
    * The most intuitive examples are type classes that provide encoding and decoding
    * transformations.
    *
    * We can build our own Codec by enhancing Printable to support encoding and decoding to/from
    * a String:
    */

  trait Codec[A] { self =>
    def encode(value: A): String
    def decode(value: String): A

    def imap[B](dec: A => B, enc: B => A): Codec[B] = new Codec[B] {
      override def encode(value: B): String = self.encode(enc(value))

      override def decode(value: String): B = dec(self.decode(value))
    }
  }

  def encode[A](value: A)(implicit codec: Codec[A]): String = codec.encode(value)
  def decode[A](value: String)(implicit codec: Codec[A]): A = codec.decode(value)

  implicit val stringCodec: Codec[String] = new Codec[String] {
    override def encode(value: String): String = value

    override def decode(value: String): String = value
  }

  /**
    * We can construct many useful Codecs for other types by building off of stringCodec using imap:
    */
  implicit val intCodec: Codec[Int] = stringCodec.imap(_.toInt, _.toString)

  implicit def boxCodec[A](implicit codec: Codec[A]): Codec[Box[A]] = codec.imap(Box(_), _.value)

  /**
    * What’s the relationship between the terms “contravariance”, “invariance”,
    * and “covariance” and these different kinds of functor?
    *
    * Subtyping can be viewed as a conversion. If B is a subtype of A, we can always convert a B to
    * an A.
    *
    * Equivalently we could say that B is a subtype of A if there exists a function A => B.
    *
    * A standard covariant functor captures exactly this. If F is a covariant functor, wherever we
    * have an F[A] and a conversion A => B we can always convert to an F[B].
    *
    * A contravariant functor captures the opposite case. If F is a contravariant functor, whenever
    * we have a F[A] and a conversion B => A we can convert to an F[B].
    *
    * Finally, invariant functors capture the case where we can convert from
    * F[A] to F[B] via a function A => B and vice versa via a function B => A.
    */
}
