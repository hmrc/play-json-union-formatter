import sbt._
import sbt.Keys._
import uk.gov.hmrc.versioning.SbtGitVersioning.autoImport.majorVersion

lazy val appName = "play-json-union-formatter"

lazy val scala213 = "2.13.12"

inThisBuild(
  List(
    scalaVersion := scala213,
    semanticdbEnabled := true,
    semanticdbVersion := scalafixSemanticdb.revision
  )
)

ThisBuild / libraryDependencySchemes += "org.scala-lang.modules" %% "scala-xml" % VersionScheme.Always

lazy val library = Project(appName, file("."))
  .settings(
    scalaVersion := scala213,
    name := appName,
    majorVersion := 1,
    isPublicArtefact := true,
    libraryDependencies ++= LibraryDependencies()
  )

commands ++= Seq(
  Command.command("run-all-tests") { state => "test" :: state },

  Command.command("clean-and-test") { state => "clean" :: "compile" :: "run-all-tests" :: state },

  Command.command("pre-commit") { state => "clean" :: "scalafmtAll" :: "scalafixAll" :: "run-all-tests" :: state }
)
