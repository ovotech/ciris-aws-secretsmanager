package ciris.aws.secretsmanager

import cats.effect.IO
import ciris.ConfigValue
import munit.FunSuite

import scala.concurrent.ExecutionContext


class RegionSpec extends FunSuite {

  implicit val cs = IO.contextShift(ExecutionContext.global)

  test("should convert a region string to a Region") {

    val region = ConfigValue.default("eu-west-1")
      .as[Region]
      .load[IO]
      .unsafeRunSync()

    assertEquals(region, Region.EU_WEST_1)
  }
}
