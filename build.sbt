name := "ciris-aws-secretsmanager"
organization := "com.ovoenergy"
licenses += ("MIT", url("http://opensource.org/licenses/MIT"))

scalaVersion := "2.13.4"
crossScalaVersions := Seq(scalaVersion.value, "2.12.10")
releaseCrossBuild := true

libraryDependencies ++= Seq(
  "is.cir" %% "ciris" % "1.1.0",
  "software.amazon.awssdk" % "secretsmanager" % "2.15.58",
  "org.scalameta" %% "munit" % "0.7.11" % Test
)

publishTo := sonatypePublishToBundle.value

testFrameworks += new TestFramework("munit.Framework")

sonatypeProfileName := "com.ovoenergy"
publishMavenStyle := true

homepage := Some(url("https://github.com/ovotech/ciris-aws-secretsmanager"))
scmInfo := Some(
  ScmInfo(
    url("https://github.com/ovotech/ciris-aws-secretsmanager"),
    "scm:git@github.com:ovotech/ciris-aws-secretsmanager.git"
  )
)
developers := List(
  Developer(id="keirlawson", name="Keir Lawson", email="keirlawson@gmail.com", url=url("https://github.com/keirlawson/"))
)

import ReleaseTransformations._

releaseProcess := Seq[ReleaseStep](
  checkSnapshotDependencies,
  inquireVersions,
  runClean,
  runTest,
  setReleaseVersion,
  commitReleaseVersion,
  tagRelease,
  releaseStepCommandAndRemaining("+publishSigned"),
  releaseStepCommand("sonatypeBundleRelease"),
  setNextVersion,
  commitNextVersion,
  pushChanges
)