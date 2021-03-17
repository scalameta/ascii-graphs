def scala213 = "2.13.5"
def scala212 = "2.12.13"
inThisBuild(
  List(
    organization := "org.scalameta",
    scalaVersion := scala213,
    crossScalaVersions := Seq(scala213, scala212),
    scalacOptions ++= Seq("-Xlint:unused"),
    javacOptions ++= Seq("-source", "1.8", "-target", "1.8"),
    scalafixDependencies +=
      "com.github.liancheng" %% "organize-imports" % "0.5.0",
    scalafixCaching := true,
    semanticdbEnabled := true,
    semanticdbVersion := "4.4.10"
  )
)

commands +=
  Command.command("fixAll") { s =>
    "scalafixAll" :: "scalafmtAll" :: "scalafmtSbt" :: "javafmtAll" :: s
  }

commands +=
  Command.command("checkAll") { s =>
    "scalafmtCheckAll" :: "scalafmtSbtCheck" :: "scalafixAll --check" ::
      "javafmtCheckAll" :: "publishLocal" :: s
  }

skip.in(publish) := true
lazy val graphs = project
  .in(file("ascii-graphs"))
  .settings(
    moduleName := "ascii-graphs",
    libraryDependencies ++=
      List(
        "org.scalatest" %% "scalatest" % "3.0.9" % Test,
        "org.scalacheck" %% "scalacheck" % "1.14.3" % Test
      )
  )
