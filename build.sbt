
name := "advanced-scala-with-cats"

version := "1.0"

scalaVersion := "2.12.3"

resolvers ++= Seq(
  "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"
)

libraryDependencies ++= Seq(
  "org.typelevel" %% "cats-core" % "1.0.0-MF"
)

scalacOptions += "-language:higherKinds"

