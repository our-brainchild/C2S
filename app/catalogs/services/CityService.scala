package catalogs.services

import catalogs.entity.{City, Country, Language}
import com.google.inject.{ImplementedBy, Inject}
import org.squeryl.PrimitiveTypeMode.{from, select, transaction, where}
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
@ImplementedBy(classOf[CityServiceImpl])
trait CityService extends ServiceCatalog[City]

class CityServiceImpl @Inject()(db: Database) extends CityService {

  import catalogs.CatalogSchem._

  SessionFactory.concreteFactory = Some(()=> Session.create(db.getConnection(), new PostgreSqlAdapter))

  def selectAllEntry(): Seq[City] = {
    transaction {
      from(city)(cit => select(cit)).toList
    }
  }

  def selectOneEntry(id: String): City = {
    transaction {
      from(city)(cit => where(cit.code === id) select(cit)).single
    }
  }
  def insertEntry(data: City) = {
    transaction {
      city.insert(data)
    }
  }

  def deleteEntry(id: String) = {
    transaction {
      city.deleteWhere(cit => cit.code === id)
    }
  }

  def updateEntry(data: City) = {
    transaction {
      update(city)(cit =>
        where(cit.code === data.code)
          set(cit.name := data.name, cit.CountryCode := data.CountryCode)
      )
    }
  }

  def insertUpdateAllEntry(data: NodeSeq): Unit = {
    data \\ "item" map { d =>
      transaction {
        if(from(city)(cit => where(cit.code === (d \ "@code" text)) select(cit.code)).nonEmpty){
          update(city)(cit =>
            where(cit.code === (d \ "@code" text))
              set(cit.name := (d \ "@name" text), cit.CountryCode := (d \ "@countryId" text))
          )
        }else{
          city.insert(City(code = d \ "@code" text, name = d \ "@name" text, CountryCode = d \ "@countryId" text))
        }
      }
    }
  }
}
