package ciris.aws.secretsmanager

import cats.effect.IO
import ciris.ConfigValue
import munit.CatsEffectSuite


class RegionSpec extends CatsEffectSuite {

  test("should convert a region string to a Region") {

    val region = ConfigValue.default("eu-west-1")
      .as[Region]
      .load[IO]

    region.assertEquals(Region.EU_WEST_1)
  }
}
