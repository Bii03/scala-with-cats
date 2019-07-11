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
package com.btesila.typeclasses.lesson

trait Json

final case class JsObject(get: Map[String, Json]) extends Json

final case class JsString(get: String) extends Json

final case class JsNumber(get: Double) extends Json

case object JsNull extends Json

/**
  * JsonWriter is our type class in this example, with Json and its subtypes providing
  * supporting code.
  */
trait JsonWriter[A] {
  def write(value: A): Json
}

/**
  * The instances of a type class provide implementations for the types we care
  * about, including types from the Scala standard library and types from our domain
  * model.
  *
  * In Scala we define instances by creating concrete implementations of the type
  * class and tagging them with the implicit keyword:
  */
object JsonWriterInstances {
  implicit val stringWriter: JsonWriter[String] = (value: String) => JsString(value)

  implicit val personWriter: JsonWriter[Person] = (person: Person) => JsObject(Map(
    "name" -> JsString(person.name),
    "email" -> JsString(person.email)
  ))
}

/**
  * A type class interface is any functionality we expose to users. Interfaces are
  * generic methods that accept instances of the type class as implicit parameters.
  * There are two common ways of specifying an interface: Interface Objects and
  * Interface Syntax.
  *
  * There are two common ways of specifying an interface: Interface Objects and
  * Interface Syntax.
  */

/**
  * Interface Objects
  */
object Json {
  def toJson[A](value: A)(implicit writer: JsonWriter[A]): Json = writer.write(value)
}

object InterfaceObjectsUsage extends App {

  import JsonWriterInstances._

  val person = Person("jon doe", "jon.doe@gmail.com")

  println(Json.toJson(person))
}

/**
  * Interface Syntax
  *
  * Aka we can alternatively use extension methods to extend existing types with interface
  * methods.
  */
object JsonSyntax {

  implicit class JsonWriterOps[A](value: A) {
    def toJson(implicit writer: JsonWriter[A]): Json = writer.write(value)
  }

}

object JsonSyntaxUsage extends App {

  import JsonWriterInstances._
  import JsonSyntax._

  val person = Person("jon doe", "jon.doe@gmail.com")

  println(person.toJson)
}

/**
  * Earlier we insinuated that all type class instances are implicit vals. This
  * was a simplification. We can actually define instances in two ways:
  *
  * - by defining concrete instances as implicit vals of the required
  * type
  *
  * - by defining implicit methods to construct instances from other type
  * class instances
  *
  * Why would we construct instances from other instances?
  *
  * As a motivational example, consider defining a JsonWriter for Options. We would need a
  * [[JsonWriter[Option[A]] for every A we care about in our application.
  */
object RecursiveImplicitResolution {

  implicit def optionWriter[A](implicit writer: JsonWriter[A]): JsonWriter[Option[A]] =
    (value: Option[A]) => value match {
      case Some(aValue) => writer.write(aValue)
      case None => JsNull
    }

  /**
    * When the compiler sees an expression like this, it searches for an implicit [[JsonWriter[Option[String]].
    * It finds the implicit method for [[JsonWriter[Option[A]], and recursively searches for a
    * JsonWriter[String] to use as the parameter to optionWriter.
    */
  // Json.toJson(Option("A string"))
}

object Warning {

  /***
    * When you create a type class instance constructor using an implicit
    * def, be sure to mark the parameters to the method as implicit parameters.
    * Without this keyword, the compiler won’t be able to fill in the
    * parameters during implicit resolution.
    *
    * Implicit methods with non-implicit parameters form a different
    * Scala pattern called an implicit conversion. This is an
    * older programming pattern that is frowned upon in modern Scala
    * code. Fortunately, the compiler will warn you when you do
    * this. You have to manually enable implicit conversions by importing
    * scala.language.implicitConversions in your file.
    */

  implicit def optionWriter[A](writer: JsonWriter[A]): JsonWriter[Option[A]] = ???

  // <console>:18: warning: implicit conversion method
  // optionWriter should be enabled
  // by making the implicit value scala.language.
  // implicitConversions visible.
  // This can be achieved by adding the import clause 'import
  // scala.language.implicitConversions'
  // or by setting the compiler option -language:
  // implicitConversions.
  // See the Scaladoc for value scala.language.implicitConversions
  // for a discussion
  // why the feature should be explicitly enabled.
  // implicit def optionWriter[A]
  // ^
  // error: No warnings can be incurred under -Xfatal-warnings.
}
