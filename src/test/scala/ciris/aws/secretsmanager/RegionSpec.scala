package ciris.aws.secretsmanager

import cats.effect.IO
import ciris.ConfigValue
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import scala.concurrent.ExecutionContext


class RegionSpec extends AnyFlatSpec with Matchers {

  implicit val cs = IO.contextShift(ExecutionContext.global)

  it should "Convert a region string to a Region" in {

    val region = ConfigValue.default("eu-west-1")
      .as[Region].default(Region.EU_WEST_1)
      .load[IO]
      .unsafeRunSync()
    region shouldBe Region.EU_WEST_1
  }
}
