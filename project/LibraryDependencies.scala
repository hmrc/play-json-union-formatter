import sbt._

object LibraryDependencies {
  def apply() = compileDependencies ++ testDependencies

  lazy val compileDependencies = Seq(
    "org.playframework"    %% "play-json"    % "3.0.4"
  )

  lazy val testDependencies = Seq(
    "org.scalatest"        %% "scalatest"    % "3.2.17",
    "com.vladsch.flexmark"  % "flexmark-all" % "0.62.2"
  ).map(_ % Test)
}
