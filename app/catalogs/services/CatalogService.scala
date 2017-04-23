package catalogs.services

import catalogs.entity.Catalog
import com.google.inject.{ImplementedBy, Inject}
import org.squeryl.adapters.PostgreSqlAdapter
import org.squeryl.{Session, SessionFactory}
import play.api.db.Database
import org.squeryl.PrimitiveTypeMode._
/**
  *
  * Created with IntelliJ IDEA User on 22.04.2017.
  * User: Смирнов Иван (killips)(ismirnoval)
  * Date: 22.04.2017
  * Time: 20:50
  */

@ImplementedBy(classOf[CatalogServiceImpl])
trait CatalogService[T]  {

  def selectChecksum(id: String): String

  def insertOrUpdate(data: T): Unit

  def selectAllEntry(): Seq[T]

  def selectOneEntry(id: String): T

  def insertEntry(data: T): Unit

  def deleteEntry(id: String)

  def updateEntry(data: T)

}

class CatalogServiceImpl @Inject()(db: Database) extends CatalogService[Catalog] {

  import catalogs.CatalogSchem._

  SessionFactory.concreteFactory = Some(()=> Session.create(db.getConnection(), new PostgreSqlAdapter))

  def selectAllEntry(): Seq[Catalog] = {
    transaction {
      from(catalog)(cat => select(cat)).toList
    }
  }

  def selectOneEntry(id: String): Catalog = {
    transaction {
      from(catalog)(c => where(c.code === id) select(c)).single
    }
  }

  def insertEntry(data: Catalog) = {
    transaction {
        catalog.insert(data)
    }
  }

  def deleteEntry(id: String) = {
    transaction {
      catalog.deleteWhere(cat => cat.code === id)
    }
  }

  def updateEntry(data: Catalog) = {
    transaction {
      update(catalog)(cha =>
        where(cha.code === data.code)
          set(cha.title := data.title, cha.checksum := data.checksum)
      )
    }
  }

  def selectChecksum(id: String): String = {
    transaction {
        if(from(catalog)(c => where(c.code === id) select(c.checksum)).isEmpty){
          "false"
        }else{
          from(catalog)(c => where(c.code === id) select(c.checksum)).single.toString
        }
    }
  }


  def insertOrUpdate(data: Catalog) = {
    transaction {
      if(from(catalog)(c => where(c.code === data.code) select(c.code)).nonEmpty){
        update(catalog)(cha =>
          where(cha.code === data.code)
            set(cha.title := data.title, cha.checksum := data.checksum)
        )
      }else{
        catalog.insert(data)
      }
    }
  }
}