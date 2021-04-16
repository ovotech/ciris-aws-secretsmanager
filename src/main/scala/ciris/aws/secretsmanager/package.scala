package ciris.aws

import cats.effect.{IO, Resource}
import ciris.ConfigValue
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider
import software.amazon.awssdk.services.secretsmanager.SecretsManagerAsyncClient
import software.amazon.awssdk.services.secretsmanager.SecretsManagerAsyncClientBuilder

package object secretsmanager {
  def secrets(
    region: Region
  ): ConfigValue[SecretString] =
    secrets(SecretsManagerAsyncClient.builder().region(region.asJava).credentialsProvider(DefaultCredentialsProvider.create()))

  def secrets(
    clientBuilder: SecretsManagerAsyncClientBuilder
  ): ConfigValue[SecretString] =
    ConfigValue.resource {
      Resource {
        IO {
          val client =
            clientBuilder
              .build()

          val shutdown =
            IO(client.close())

          (ConfigValue.default(SecretString(client)), shutdown)
        }
      }
    }
}
