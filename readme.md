## Ciris AWS Secrets Manager

[AWS Secrets Manager](https://aws.amazon.com/secrets-manager/) support for [Ciris](https://cir.is).

This module is heavily derived from the existing [Ciris SSM module](https://github.com/ovotech/ciris-aws-ssm).

### Getting Started

To get started with [sbt](https://www.scala-sbt.org), simply add the following lines to your `build.sbt` file.

```scala
libraryDependencies += "com.ovoenergy" %% "ciris-aws-secretsmanager" % "5.0.1"
```

The library is published for Scala 2.12 and 2.13.

### Example

```scala
import cats.effect.{Blocker, ExitCode, IO, IOApp}
import cats.implicits._
import ciris._
import ciris.aws.secretsmanager._

final case class Passwords(
  dbPassword: Secret[String],
  apiKey: Secret[String],
)

object Main extends IOApp {
  def run(args: List[String]): IO[ExitCode] = {
      val password =
        for {
          region <- env("AWS_REGION").as[Region].default(Region.EU_WEST_1)
          secret <- secrets(region)
          passwords <- (
              secret(key = "db-password-prod", version = "1.0"),
              secret(key = "api-key", version = "1.0")
            ).parMapN { (dbPassword, apiKey) =>
              Passwords(
                dbPassword = dbPassword,
                apiKey = apiKey
              )
            }
        } yield passwords

      password.load[IO].as(ExitCode.Success)
    }
}
```

### Release

To release a new version, use the following command.

```
$ sbt release
```
