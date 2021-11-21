package ciris.aws.secretsmanager

import cats.implicits._
import ciris.{ConfigKey, ConfigValue, Secret}
import software.amazon.awssdk.services.secretsmanager.SecretsManagerAsyncClient
import software.amazon.awssdk.services.secretsmanager.model._

sealed abstract class SecretString[F[_]] {
  def apply(key: String): ConfigValue[F, Secret[String]]
  def apply(key: String, version: String): ConfigValue[F, Secret[String]]
}

private[secretsmanager] object SecretString {
  final def apply[F[_]](client: SecretsManagerAsyncClient): SecretString[F] =
    new SecretString[F] {
      override final def apply(key: String): ConfigValue[F, Secret[String]] =
        fetch(key, GetSecretValueRequest.builder().secretId(key).build())

      def apply(key: String, version: String): ConfigValue[F, Secret[String]] =
        fetch(key, GetSecretValueRequest.builder().secretId(key).versionId(version).build())


      private def fetch(key: String, request: GetSecretValueRequest): ConfigValue[F, Secret[String]] =
      ConfigValue.async { cb =>
        val configKey =
              ConfigKey(s"secret string $key from AWS secrets manager")
        client.getSecretValue(request).whenComplete { (resp, error) =>
          cb {
            if (error != null) {
              Left(error)
            } else {
              val str = Option(resp.secretString())
              Right(str.fold(ConfigValue.missing[Secret[String]](configKey))( value => ConfigValue.loaded(configKey, value).secret))
            }
          }
        }
      }
    }
}
