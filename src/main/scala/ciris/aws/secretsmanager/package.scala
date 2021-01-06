package ciris.aws

import cats.effect.{IO, Resource}
import ciris.ConfigValue
import software.amazon.awssdk.auth.credentials.AwsCredentialsProviderChain
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider
import software.amazon.awssdk.services.secretsmanager.SecretsManagerAsyncClient

package object secretsmanager {
  def secrets(
    region: Region
  ): ConfigValue[SecretString] =
    secrets(region, AwsCredentialsProviderChain.builder().addCredentialsProvider(DefaultCredentialsProvider.create()).build())

  def secrets(
    region: Region,
    credentials: AwsCredentialsProvider
  ): ConfigValue[SecretString] =
    ConfigValue.resource {
      Resource {
        IO {
          val client =
            SecretsManagerAsyncClient.builder()
              .region(region.asJava)
              .credentialsProvider(credentials)
              .build()

          val shutdown =
            IO(client.close())

          (ConfigValue.default(SecretString(client)), shutdown)
        }
      }
    }
}
