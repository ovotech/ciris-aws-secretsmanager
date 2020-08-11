package ciris.aws.secretsmanager

import cats.effect.Blocker
import ciris.{ConfigKey, ConfigValue, Secret}
import com.amazonaws.services.secretsmanager._
import com.amazonaws.services.secretsmanager.model._
import cats.implicits._

sealed abstract class SecretString {
  def apply(key: String): ConfigValue[Secret[String]]
  def apply(key: String, version: String): ConfigValue[Secret[String]]
}

private[secretsmanager] final object SecretString {
  final def apply(client: AWSSecretsManager, blocker: Blocker): SecretString =
    new SecretString {
      override final def apply(key: String): ConfigValue[Secret[String]] =
        fetch(key, new GetSecretValueRequest().withSecretId(key))

      def apply(key: String, version: String): ConfigValue[Secret[String]] =
        fetch(key, new GetSecretValueRequest().withSecretId(key).withVersionId(version))


      private def fetch(key: String, request: GetSecretValueRequest): ConfigValue[Secret[String]] =
        ConfigValue.blockOn(blocker) {
          ConfigValue.suspend {
            val configKey =
              ConfigKey(s"secret string $key from AWS secrets manager")

            try {
              val resp = client.getSecretValue(request)

              val str = Option(resp.getSecretString)

              str.fold(ConfigValue.missing[Secret[String]](configKey))( value => ConfigValue.loaded(configKey, value).secret)
            } catch {
              case _: ResourceNotFoundException =>
                ConfigValue.missing(configKey)
            }
          }
        }
    }
}
