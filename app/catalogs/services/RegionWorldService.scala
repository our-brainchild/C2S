package catalogs.services

import catalogs.entity.RegionWorld
import com.google.inject.{ImplementedBy, Inject}
import org.squeryl.{Session, SessionFactory}
import org.squeryl.adapters.PostgreSqlAdapter
import play.api.db.Database
import org.squeryl.PrimitiveTypeMode._
import scala.xml.NodeSeq
/**
  *
  * Created with IntelliJ IDEA.
  * User: Смирнов Иван (killips)
  * Date: 20.04.2017
  *
  */
@ImplementedBy(classOf[RegionWorldServiceImpl])
trait RegionWorldService extends ServiceCatalog[RegionWorld]

class RegionWorldServiceImpl @Inject()(db: Database) extends RegionWorldService {

  import catalogs.CatalogSchem._

  SessionFactory.concreteFactory = Some(()=> Session.create(db.getConnection(), new PostgreSqlAdapter))

  def selectAllEntry(): Seq[RegionWorld] = {
    transaction {
      from(regionWorld)(rw => select(rw)).toList
    }
  }

  def selectOneEntry(id: String): RegionWorld =  {
    transaction {
      from(regionWorld)(rw => where(rw.code === id) select(rw)).single
    }
  }

  def insertEntry(data: RegionWorld) = {
    transaction {
      regionWorld.insert(data)
    }
  }

  def insertUpdateAllEntry(data: NodeSeq) = {
    data \\ "item" map { d =>
      transaction {
        if(from(regionWorld)(c => where(c.code === (d \ "@code" text)) select(c.code)).nonEmpty){
          update(regionWorld)(rw =>
            where(rw.code === (d \ "@code" text))
              set(rw.name := (d \ "@name" text))
          )
        }else{
          regionWorld.insert(RegionWorld( code = d \ "@code" text, name = d \ "@name" text))
        }
      }
    }
  }

  def deleteEntry(id: String) = {
    transaction {
      regionWorld.deleteWhere(rw => rw.code === id)
    }
  }

  def updateEntry(data: RegionWorld) = {
    transaction {
      update(regionWorld)(rw =>
        where(rw.code === data.code)
          set(rw.name := data.name)
      )
    }
  }
}
