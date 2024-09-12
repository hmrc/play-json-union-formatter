import sbt._
import sbt.Keys._
import uk.gov.hmrc.versioning.SbtGitVersioning.autoImport.majorVersion

lazy val appName = "play-json-union-formatter"

Global / bloopAggregateSourceDependencies := true
Global / bloopExportJarClassifiers := Some(Set("sources"))

ThisBuild / scalaVersion := "3.3.3"
ThisBuild / majorVersion := 2
ThisBuild / semanticdbEnabled := true
ThisBuild / semanticdbVersion := scalafixSemanticdb.revision
ThisBuild / libraryDependencySchemes += "org.scala-lang.modules" %% "scala-xml" % VersionScheme.Always

lazy val library = Project(appName, file("."))
  .settings(
    isPublicArtefact := true,
    libraryDependencies ++= LibraryDependencies()
  )

commands ++= Seq(
  Command.command("run-all-tests") { state => "test" :: state },

  Command.command("clean-and-test") { state => "clean" :: "compile" :: "run-all-tests" :: state },

  Command.command("pre-commit") { state => "clean" :: "scalafmtAll" :: "scalafixAll" :: "run-all-tests" :: state }
)
