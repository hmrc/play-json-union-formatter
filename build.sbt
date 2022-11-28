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
import uk.gov.hmrc.versioning.SbtGitVersioning.autoImport.majorVersion


lazy val scala212 = "2.12.16"
lazy val scala213 = "2.13.8"
lazy val supportedScalaVersions = List(scala212, scala213)

lazy val library = (project in file("."))
  .settings(PlayCrossCompilation.playCrossCompilationSettings)
  .settings(
    crossScalaVersions := supportedScalaVersions,
  )
  .settings(
    scalaVersion := scala213,
    name := "play-json-union-formatter",
    majorVersion := 1,
    isPublicArtefact := true,
    targetJvm := "jvm-1.8",
    libraryDependencies ++= deps
  )

val testDepsShared: Seq[ModuleID] = Seq(
  "org.scalatest" %% "scalatest" % "3.2.13",
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