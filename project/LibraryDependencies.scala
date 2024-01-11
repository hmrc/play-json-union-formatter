import sbt._

object LibraryDependencies {
  def apply() = compileDependencies ++ testDependencies

  lazy val compileDependencies = Seq(
    "com.typesafe.play" %% "play-json" % "2.8.2"
  )

  lazy val testDependencies = Seq(
    "org.scalatest" %% "scalatest" % "3.2.17",
    "com.vladsch.flexmark" % "flexmark-all" % "0.62.2"
  ).map(_ % "test")
}