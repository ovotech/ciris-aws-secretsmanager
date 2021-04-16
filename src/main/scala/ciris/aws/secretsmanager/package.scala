package ciris.aws

import cats.effect.kernel.{Resource, Sync}
import ciris.ConfigValue
import software.amazon.awssdk.auth.credentials.{AwsCredentialsProvider, DefaultCredentialsProvider}
import software.amazon.awssdk.services.secretsmanager.SecretsManagerAsyncClient
import software.amazon.awssdk.services.secretsmanager.SecretsManagerAsyncClientBuilder

package object secretsmanager {
  def secrets[F[_]: Sync](
    region: Region
  ): ConfigValue[F, SecretString[F]] =
    secrets(SecretsManagerAsyncClient.builder().region(region.asJava).credentialsProvider(DefaultCredentialsProvider.create()))

  def secrets[F[_]: Sync](
    clientBuilder: SecretsManagerAsyncClientBuilder
  ): ConfigValue[F, SecretString[F]] =
    ConfigValue.resource {
      Resource {
        Sync[F].delay {
          val client =
            clientBuilder
              .build()

          val shutdown =
            Sync[F].delay(client.close())

          (ConfigValue.default(SecretString[F](client)), shutdown)
        }
      }
    }
}
