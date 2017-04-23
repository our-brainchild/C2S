package catalogs.services

import catalogs.entity.Country

/**
  * Created by User on 20.04.2017.
  */
trait CountryServices extends ServiceCatalog[Country]

class CountryServicesImpl extends CountryServices{

  def selectAllEntry(): Seq[Country] = ???

  def selectOneEntry(id: Int): Country = ???

  def insertEntry(data: Country): Unit = ???

  def deleteEntry(id: Int): Unit = ???

  def updateEntry(data: Country): Unit = ???

}