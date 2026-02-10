import sbt._

object LibraryDependencies {
  def apply() = compileDependencies ++ testDependencies

  lazy val compileDependencies = Seq(
    "org.playframework" %% "play-json" % "3.0.6"
  )

  lazy val testDependencies = Seq(
    "org.scalatest" %% "scalatest" % "3.2.19",
    "com.vladsch.flexmark" % "flexmark-all" % "0.62.2"
  ).map(_ % "test")
}
