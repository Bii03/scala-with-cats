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
package com.btesila.functors

import com.btesila.typeclasses.model.Box


/**
  * The idea is pretty simple: if we have
  * - a [[Codec[A]]] fixed in return type: [[String]]
  * - a pair of functions:  f: A => B and f: B => A
  *
  * then we can use `imap` to obtain a [[Codec[B]]]
  *
  */
trait Codec[A] {
  def encode(value: A): String

  def decode(value: String): A

  def imap[B](dec: A => B, enc: B => A): Codec[B] = {
    // construct recursive types
    val self = this

    new Codec[B] {
      override def encode(value: B): String = self.encode(enc(value))

      override def decode(value: String): B = dec(self.decode(value))
    }
  }
}

object Codec {

  implicit val stringCodec: Codec[String] = new Codec[String] {
    override def encode(value: String): String = value

    override def decode(value: String): String = value
  }

  implicit def doubleCodec(implicit c: Codec[String]): Codec[Double] =
    c.imap(_.toDouble, _.toString)

  implicit def boxCodec[A](implicit c: Codec[A]): Codec[Box[A]] =
    c.imap(Box(_), _.value)

  def encode[A](value: A)(implicit c: Codec[A]): String = c.encode(value)

  def decode[A](value: String)(implicit c: Codec[A]): A = c.decode(value)
}
