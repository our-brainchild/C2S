package catalogs

import catalogs.entity._
import org.squeryl.Schema
import org.squeryl.PrimitiveTypeMode._


/**
  *
  * Created with IntelliJ IDEA.
  * User: Смирнов Иван (killips)
  * Date: 19.04.2017
  *
  */

object CatalogSchem extends Schema {
  val catalog = table[Catalog]
  val city = table[City]
  val country = table[Country]
  val language = table[Language]
  val regionWorld = table[RegionWorld]
  val typeEducationalInstitution = table[TypeEducationalInstitution]
}
