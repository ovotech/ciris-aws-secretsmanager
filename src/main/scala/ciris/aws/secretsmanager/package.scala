package ciris.aws

import cats.effect.{Blocker, IO, Resource}
import ciris.ConfigValue
import com.amazonaws.auth._
import com.amazonaws.services.secretsmanager._

package object secretsmanager {
  def secrets(
    blocker: Blocker,
    region: Region
  ): ConfigValue[SecretString] =
    secrets(blocker, region, new DefaultAWSCredentialsProviderChain())

  def secrets(
    blocker: Blocker,
    region: Region,
    credentials: AWSCredentialsProvider
  ): ConfigValue[SecretString] =
    ConfigValue.resource {
      Resource {
        IO {
          val client =
            AWSSecretsManagerClientBuilder
              .standard()
              .withRegion(region.asJava)
              .withCredentials(credentials)
              .build()

          val shutdown =
            IO(client.shutdown())

          (ConfigValue.default(SecretString(client, blocker)), shutdown)
        }
      }
    }
}
