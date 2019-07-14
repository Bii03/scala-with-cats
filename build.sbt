
name := "advanced-scala-with-cats"

version := "1.0"

scalaVersion := "2.12.3"

resolvers ++= Seq(
  "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"
)

libraryDependencies ++= Seq(
  "org.typelevel" %% "cats-core" % "2.0.0-M1"
)

scalacOptions += "-language:higherKinds"
scalacOptions += "-Ypartial-unification"

