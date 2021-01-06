package ciris.aws.secretsmanager

import ciris.{ConfigKey, ConfigValue, Secret}
import software.amazon.awssdk.services.secretsmanager.SecretsManagerAsyncClient
import cats.implicits._
import software.amazon.awssdk.services.secretsmanager.model._

sealed abstract class SecretString {
  def apply(key: String): ConfigValue[Secret[String]]
  def apply(key: String, version: String): ConfigValue[Secret[String]]
}

private[secretsmanager] final object SecretString {
  final def apply(client: SecretsManagerAsyncClient): SecretString =
    new SecretString {
      override final def apply(key: String): ConfigValue[Secret[String]] =
        fetch(key, GetSecretValueRequest.builder().secretId(key).build())

      def apply(key: String, version: String): ConfigValue[Secret[String]] =
        fetch(key, GetSecretValueRequest.builder().secretId(key).versionId(version).build())


      private def fetch(key: String, request: GetSecretValueRequest): ConfigValue[Secret[String]] =
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
