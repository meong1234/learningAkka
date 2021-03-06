name := "akkademy-db-core"
organization := "com.akkademy-db"
version := "0.0.1-SNAPSHOT"

scalaVersion := "2.11.7"


libraryDependencies ++= Seq(
	"com.typesafe.akka" %% "akka-actor" % "2.3.6",
	"com.typesafe.akka" %% "akka-remote" % "2.3.6",
	"com.typesafe.akka" %% "akka-testkit" % "2.3.6" % "test",
	
	"com.akkademy-db" %% "akkademy-db-api" % "0.0.1-SNAPSHOT",
	
	
	"org.scalatest" %% "scalatest" % "2.1.6" % "test"
)


