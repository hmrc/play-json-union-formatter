import sbt._
import sbt.Keys._
import uk.gov.hmrc.versioning.SbtGitVersioning.autoImport.majorVersion

lazy val appName = "play-json-union-formatter"

lazy val scala212 = "2.12.16"
lazy val scala213 = "2.13.12"
lazy val supportedScalaVersions = List(scala212, scala213)

inThisBuild(
  List(
    scalaVersion := scala213,
    semanticdbEnabled := true,
    semanticdbVersion := scalafixSemanticdb.revision
  )
)

ThisBuild / libraryDependencySchemes += "org.scala-lang.modules" %% "scala-xml" % VersionScheme.Always

lazy val library = Project(appName, file("."))
  .settings(PlayCrossCompilation.playCrossCompilationSettings)
  .settings(
    crossScalaVersions := supportedScalaVersions,
  )
  .settings(
    scalaVersion := scala213,
    name := appName,
    majorVersion := 1,
    isPublicArtefact := true,
    libraryDependencies ++= deps
  )

val testDepsShared: Seq[ModuleID] = Seq(
  "org.scalatest" %% "scalatest" % "3.2.17",
  "com.vladsch.flexmark" % "flexmark-all" % "0.62.2"
).map(_ % Test)

val compileDepsPlay27: Seq[ModuleID] = Seq(
  "com.typesafe.play" %% "play-json" % "2.7.4"
)

val compileDepsPlay28: Seq[ModuleID] = Seq(
  "com.typesafe.play" %% "play-json" % "2.8.2"
)

val deps: Seq[ModuleID] = PlayCrossCompilation.dependencies(
  play27 = compileDepsPlay27,
  play28 = compileDepsPlay28,
  shared = testDepsShared
)

commands ++= Seq(
  Command.command("run-all-tests") { state => "test" :: state },

  Command.command("clean-and-test") { state => "clean" :: "compile" :: "run-all-tests" :: state },

  Command.command("pre-commit") { state => "clean" :: "scalafmtAll" :: "scalafixAll" :: "run-all-tests" :: state }
)
