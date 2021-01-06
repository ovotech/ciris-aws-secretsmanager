package ciris.aws

import cats.effect.{Blocker, IO, Resource}
import ciris.ConfigValue
import software.amazon.awssdk.auth.credentials.AwsCredentialsProviderChain
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider

package object secretsmanager {
  def secrets(
    blocker: Blocker,
    region: Region
  ): ConfigValue[SecretString] =
    secrets(blocker, region, AwsCredentialsProviderChain.builder().addCredentialsProvider(DefaultCredentialsProvider.create()).build())

  def secrets(
    blocker: Blocker,
    region: Region,
    credentials: AwsCredentialsProvider
  ): ConfigValue[SecretString] =
    ConfigValue.resource {
      Resource {
        IO {
          val client =
            SecretsManagerClient.builder()
              .region(region.asJava)
              .credentialsProvider(credentials)
              .build()

          val shutdown =
            IO(client.close())

          (ConfigValue.default(SecretString(client, blocker)), shutdown)
        }
      }
    }
}
