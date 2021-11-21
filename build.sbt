name := "ciris-aws-secretsmanager"
organization := "com.ovoenergy"
licenses += ("MIT", url("http://opensource.org/licenses/MIT"))

scalaVersion := "3.1.0"
crossScalaVersions := Seq(scalaVersion.value, "2.13.5", "2.12.13")
releaseCrossBuild := true

libraryDependencies ++= Seq(
  "is.cir" %% "ciris" % "2.2.1",
  "org.typelevel" %% "cats-core" % "2.6.1",
  "org.typelevel" %% "cats-effect" % "3.2.9",
  "software.amazon.awssdk" % "secretsmanager" % "2.15.58",
  "org.typelevel" %% "munit-cats-effect-3" % "1.0.6" % Test
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