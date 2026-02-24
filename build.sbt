import sbt._
import sbt.Keys._
import uk.gov.hmrc.versioning.SbtGitVersioning.autoImport.majorVersion

val appName = "play-json-union-formatter"

val scala2_13 = "2.13.16"
val scala3 = "3.3.7"


inThisBuild(
  List(
    majorVersion := 1,
    scalaVersion := scala3,
    isPublicArtefact := true,
    semanticdbEnabled := true,
    semanticdbVersion := scalafixSemanticdb.revision,
    libraryDependencySchemes += "org.scala-lang.modules" %% "scala-xml" % VersionScheme.Always
  )
)

lazy val sharedScalacOptions =
  Seq("-encoding", "UTF-8", "-Wunused:imports,privates,locals")

lazy val scala2Options = sharedScalacOptions ++
  Seq("-explaintypes")

lazy val scala3Options = sharedScalacOptions ++
  Seq("-explain")

lazy val commonSettings = Seq(
  excludeFilter.in(headerSources) := HiddenFileFilter || "**/bcrypt.scala",

  scalafixConfig := {
    val base = (ThisBuild / baseDirectory).value
    val file =
      CrossVersion.partialVersion(scalaVersion.value) match {
        case Some((3, _)) => base / ".scalafix-scala3.conf"
        case _            => base / ".scalafix-scala2.conf"
      }
    Some(file)
  },

  scalafmtConfig := {
    val base = (ThisBuild / baseDirectory).value
    val file =
      CrossVersion.partialVersion(scalaVersion.value) match {
        case Some((3, _)) => base / ".scalafmt-scala3.conf"
        case _            => base / ".scalafmt-scala2.conf"
      }
    file
  },

  scalacOptions ++= 
    (CrossVersion.partialVersion(scalaVersion.value) match {
      case Some((3, _)) => scala3Options
      case _            => scala2Options
    }),

    crossScalaVersions := Seq(scala3, scala2_13),
)


lazy val library = Project(appName, file("."))
  .settings(
    commonSettings,
    name := appName,
    majorVersion := 1,
    isPublicArtefact := true,
    libraryDependencies ++= LibraryDependencies()
  )

// Whilst some of these may seem superflous we have a standardised set of commands across repos where these may be more involved (and therefore more useful)
//
  commands ++= Seq(
  Command.command("run-all-tests") { state => "test" :: state },
  Command.command("clean-and-test") { state => "clean" :: "run-all-tests" :: state },
  Command.command("all") { state => "clean" :: "scalafmtAll" :: "scalafixAll" :: "run-all-tests" :: state },

  Command.command("pre-commit") { state => "all" :: state }
  )