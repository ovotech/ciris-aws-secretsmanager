package ciris.aws.secretsmanager

import cats.implicits._
import ciris.ConfigDecoder
import software.amazon.awssdk.regions.{Region => SdkRegion}
import collection.JavaConverters._

sealed abstract class Region(val asJava: SdkRegion) {
  final def name: String =
    asJava.metadata().id()

  final def description: String =
    asJava.metadata().description()
}

object Region {
  case object AF_SOUTH_1 extends Region(SdkRegion.AF_SOUTH_1)
  case object AP_EAST_1 extends Region(SdkRegion.AP_EAST_1)
  case object AP_NORTHEAST_1 extends Region(SdkRegion.AP_NORTHEAST_1)
  case object AP_NORTHEAST_2 extends Region(SdkRegion.AP_NORTHEAST_2)
  case object AP_SOUTH_1 extends Region(SdkRegion.AP_SOUTH_1)
  case object AP_SOUTHEAST_1 extends Region(SdkRegion.AP_SOUTHEAST_1)
  case object AP_SOUTHEAST_2 extends Region(SdkRegion.AP_SOUTHEAST_2)
  case object CA_CENTRAL_1 extends Region(SdkRegion.CA_CENTRAL_1)
  case object CN_NORTH_1 extends Region(SdkRegion.CN_NORTH_1)
  case object CN_NORTHWEST_1 extends Region(SdkRegion.CN_NORTHWEST_1)
  case object EU_CENTRAL_1 extends Region(SdkRegion.EU_CENTRAL_1)
  case object EU_NORTH_1 extends Region(SdkRegion.EU_NORTH_1)
  case object EU_SOUTH_1 extends Region(SdkRegion.EU_SOUTH_1)
  case object EU_WEST_1 extends Region(SdkRegion.EU_WEST_1)
  case object EU_WEST_2 extends Region(SdkRegion.EU_WEST_2)
  case object EU_WEST_3 extends Region(SdkRegion.EU_WEST_3)
  case object AWS_US_GOV_GLOBAL extends Region(SdkRegion.AWS_US_GOV_GLOBAL)
  case object ME_SOUTH_1 extends Region(SdkRegion.ME_SOUTH_1)
  case object SA_EAST_1 extends Region(SdkRegion.SA_EAST_1)
  case object US_EAST_1 extends Region(SdkRegion.US_EAST_1)
  case object US_EAST_2 extends Region(SdkRegion.US_EAST_2)
  case object US_GOV_EAST_1 extends Region(SdkRegion.US_GOV_EAST_1)
  case object US_GOV_WEST_1 extends Region(SdkRegion.US_GOV_WEST_1)
  case object US_WEST_1 extends Region(SdkRegion.US_WEST_1)
  case object US_WEST_2 extends Region(SdkRegion.US_WEST_2)

  def apply(region: SdkRegion): Region =
    region match {
      case SdkRegion.AP_EAST_1         => Region.AP_EAST_1
      case SdkRegion.AP_NORTHEAST_1    => Region.AP_NORTHEAST_1
      case SdkRegion.AP_NORTHEAST_2    => Region.AP_NORTHEAST_2
      case SdkRegion.AP_SOUTH_1        => Region.AP_SOUTH_1
      case SdkRegion.AP_SOUTHEAST_1    => Region.AP_SOUTHEAST_1
      case SdkRegion.AP_SOUTHEAST_2    => Region.AP_SOUTHEAST_2
      case SdkRegion.CA_CENTRAL_1      => Region.CA_CENTRAL_1
      case SdkRegion.CN_NORTH_1        => Region.CN_NORTH_1
      case SdkRegion.CN_NORTHWEST_1    => Region.CN_NORTHWEST_1
      case SdkRegion.EU_CENTRAL_1      => Region.EU_CENTRAL_1
      case SdkRegion.EU_NORTH_1        => Region.EU_NORTH_1
      case SdkRegion.EU_WEST_1         => Region.EU_WEST_1
      case SdkRegion.EU_WEST_2         => Region.EU_WEST_2
      case SdkRegion.EU_WEST_3         => Region.EU_WEST_3
      case SdkRegion.AWS_US_GOV_GLOBAL => Region.AWS_US_GOV_GLOBAL
      case SdkRegion.ME_SOUTH_1        => Region.ME_SOUTH_1
      case SdkRegion.SA_EAST_1         => Region.SA_EAST_1
      case SdkRegion.US_EAST_1         => Region.US_EAST_1
      case SdkRegion.US_EAST_2         => Region.US_EAST_2
      case SdkRegion.US_GOV_EAST_1     => Region.US_GOV_EAST_1
      case SdkRegion.US_WEST_1         => Region.US_WEST_1
      case SdkRegion.US_WEST_2         => Region.US_WEST_2
      case SdkRegion.AF_SOUTH_1        => Region.AF_SOUTH_1
      case SdkRegion.EU_SOUTH_1        => Region.EU_SOUTH_1
    }

  def unapply(region: Region): Some[SdkRegion] =
    Some(region.asJava)

  def fromName(name: String): Option[Region] =
    SdkRegion.regions().asScala.find(_.id() == name).map(apply)

  implicit val regionConfigDecoder: ConfigDecoder[String, Region] =
    ConfigDecoder[String].mapOption("Region")(fromName)
}
