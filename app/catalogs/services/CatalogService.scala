package catalogs.services

import catalogs.entity.Catalog

/**
  *
  * Created with IntelliJ IDEA User on 22.04.2017.
  * User: Смирнов Иван (killips)(ismirnoval)
  * Date: 22.04.2017
  * Time: 20:50
  */
trait CatalogService extends ServiceCatalog[Catalog] {
  def selectChecksum(id: String): Option[String]
}

class CatalogServiceImpl extends CatalogService {

  def selectAllEntry(): Seq[Catalog] = ???

  def selectOneEntry(id: String): Catalog = ???

  def insertEntry(data: Catalog): Unit = ???

  def deleteEntry(id: String): Unit = ???

  def updateEntry(data: Catalog): Unit = ???

  def selectChecksum(id: String): Option[String] = Option("olol")
}