import com.typesafe.sbt.SbtStartScript
import de.johoop.jacoco4sbt._
import JacocoPlugin._

name := "my-app"

organization := "scalapenos"

version := "0.1-SNAPSHOT"

scalaVersion := "2.10.3"

scalacOptions := Seq("-encoding", "utf8",
                     "-target:jvm-1.6",
                     "-feature",
                     "-language:implicitConversions",
                     "-language:postfixOps",
                     "-unchecked",
                     "-deprecation",
                     "-Xlog-reflective-calls"
                    )

mainClass := Some("com.scalapenos.myapp.api.Boot")

resolvers ++= Seq("Sonatype Releases"   at "http://oss.sonatype.org/content/repositories/releases",
                  "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/",
                  "Spray Repository"    at "http://repo.spray.io/",
                  "Spray Nightlies"     at "http://nightlies.spray.io/",
                  "Base64 Repo"         at "http://dl.bintray.com/content/softprops/maven")

libraryDependencies ++= {
  val akkaVersion  = "2.2.3"
  val sprayVersion = "1.2-RC2"
  Seq(
    "com.typesafe.akka"       %%  "akka-actor"             % akkaVersion,
    "com.typesafe.akka"       %%  "akka-slf4j"             % akkaVersion,
    "io.spray"                %   "spray-caching"          % sprayVersion,
    "io.spray"                %   "spray-can"              % sprayVersion,
    "io.spray"                %   "spray-client"           % sprayVersion,
    "io.spray"                %   "spray-routing"          % sprayVersion,
    "io.spray"                %%  "spray-json"             % "1.2.5",
    "me.lessis"               %%  "base64"                 % "0.1.0",
    "com.github.nscala-time"  %%  "nscala-time"            % "0.6.0",
    "ch.qos.logback"          %   "logback-classic"        % "1.0.13",
    "javax.mail"              %   "mail"                   % "1.5.0-b01",
    "com.typesafe.akka"       %%  "akka-testkit"           % akkaVersion    % "test",
    "io.spray"                %   "spray-testkit"          % sprayVersion   % "test",
    "org.specs2"              %%  "specs2"                 % "2.2.3"        % "test"
  )
}


seq(SbtStartScript.startScriptForClassesSettings: _*)

seq(Revolver.settings: _*)

seq(jacoco.settings : _*)