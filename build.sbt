name := "ciris-aws-secretsmanager"
organization := "com.ovoenergy"
bintrayOrganization := Some("ovotech")
licenses += ("MIT", url("http://opensource.org/licenses/MIT"))

scalaVersion := "2.13.1"
crossScalaVersions := Seq(scalaVersion.value, "2.12.10")
releaseCrossBuild := true

libraryDependencies ++= Seq(
  "is.cir" %% "ciris" % "1.1.0",
  "com.amazonaws" % "aws-java-sdk-secretsmanager" % "1.11.667",
  "org.scalameta" %% "munit" % "0.7.11" % Test
)

testFrameworks += new TestFramework("munit.Framework")
