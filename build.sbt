/*
 * Copyright 2018 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import sbt._
import sbt.Keys._
import uk.gov.hmrc.DefaultBuildSettings.targetJvm
import uk.gov.hmrc.SbtAutoBuildPlugin
import uk.gov.hmrc.SbtArtifactory
import uk.gov.hmrc.versioning.SbtGitVersioning.autoImport.majorVersion
import uk.gov.hmrc.versioning.SbtGitVersioning

lazy val library = (project in file("."))
  .enablePlugins(SbtAutoBuildPlugin, SbtGitVersioning, SbtArtifactory)
  .settings(PlayCrossCompilation.playCrossCompilationSettings)
  .settings(
    scalaVersion := "2.11.12",
    name := "play-json-union-formatter",
    majorVersion := 1,
    makePublicallyAvailableOnBintray := true,
    targetJvm := "jvm-1.8",
    crossScalaVersions := Seq("2.11.12", "2.12.8"),
    libraryDependencies ++= deps,
    resolvers := Seq(
      Resolver.bintrayRepo("hmrc", "releases")
    )
  )

val compileDepsPlay25: Seq[ModuleID] = Seq(
  "com.typesafe.play" %% "play-json" % "2.5.19"
)

val testDepsPlay25: Seq[ModuleID] = Seq(
  "uk.gov.hmrc"       %% "hmrctest"  % "3.9.0-play-25"  % "test",
  "org.scalatest"     %% "scalatest" % "2.2.5"  % "test"
)

val compileDepsPlay26: Seq[ModuleID] = Seq(
  "com.typesafe.play" %% "play-json" % "2.6.14"
)

val testDepsPlay26: Seq[ModuleID] = Seq(
  "org.scalatest"     %% "scalatest" % "3.0.8"  % "test",
  "uk.gov.hmrc"       %% "hmrctest"  % "3.9.0-play-26"  % "test"
)

val deps: Seq[ModuleID] = PlayCrossCompilation.dependencies(
  play25 = compileDepsPlay25 ++ testDepsPlay25,
  play26 = compileDepsPlay26 ++ testDepsPlay26
)
