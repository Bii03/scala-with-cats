package com.btesila.typeclasses

import com.btesila.typeclasses.model.{Box, Cat}

/**
  * There are three important components to the type class pattern: the
  * type class itself, instances for particular types, and the interface methods
  * that we expose to users.
  *
  * This trait exposes the type class itself - an interface or API that represents some functionality we
  * want to implement.
  */
trait Printable[A] { self => // using this for defining recursive types inside contramap
  def format(value: A): String

  def contramap[B](func: B => A): Printable[B] = new Printable[B] {
    override def format(value: B): String = self.format(func(value))
  }
}

/**
  * This holds the available instances of `Printable` type.
  */
object PrintableInstances {
  implicit val printableInt: Printable[Int] = (value: Int) => s"$value: Int"

  implicit val printableString: Printable[String] = (value: String) => s"$value: String"

  implicit val printableCat: Printable[Cat] = (value: Cat) => s"$value: Cat"

  implicit def printableBox[A](implicit p: Printable[A]): Printable[Box[A]] =
    p.contramap[Box[A]](_.value)
}

/**
  * An interface is any functionality we expose to users. Interfaces to type
  * classes are generic methods that accept instances of the type class as
  * implicit parameters.
  *
  * There are two common ways of specifying an interface: Interface Objects
  * and Interface Syntax.
  *
  * This is an Interface Object.
  */
object Printable {

  def format[A: Printable](value: A): String = implicitly[Printable[A]].format(value)

  def print[A: Printable](value: A): Unit = println(format(value))
}

/**
  * This is an Interface Syntax.
  */
object PrintableSyntax {
  implicit class PrintOps[A](value: A) {
    def format()(implicit p: Printable[A]): String = p.format(value)
    def print()(implicit p: Printable[A]): Unit = println(format())
  }
}

object Test extends App {
  import Cat._

  // Provide available implementations for the Printable typeclass
  import PrintableInstances._

  // Using Interface Object
  import Printable._
  print(cat)

  // Using Interface Syntax
  import PrintableSyntax._
  cat.print
}

