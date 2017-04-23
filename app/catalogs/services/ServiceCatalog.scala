package catalogs.services

import scala.xml.NodeSeq

/**
  *
  * Created with IntelliJ IDEA.
  * User: Смирнов Иван (killips)
  * Date: 19.04.2017
  *
  */

trait ServiceCatalog[T] {

  def selectAllEntry(): Seq[T]

  def selectOneEntry(id: String): T

  def insertEntry(data: T)

  def deleteEntry(id: String)

  def updateEntry(data: T)

  def insertUpdateAllEntry(data: NodeSeq)

}