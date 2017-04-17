package models.services

/**
  *
  * Created with IntelliJ IDEA.
  * User: Смирнов Иван (killips)
  * Date: 18.04.2017
  *
  */

trait Service[T] {
  def selectAllEntry(): Seq[T]

  def selectOneEntry(id: Int): T

  def insertEntry(data: T)

  def deletEntry(id: Int)

  def updateEntry(data: T)
}