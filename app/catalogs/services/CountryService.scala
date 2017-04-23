package catalogs.services

import catalogs.entity.{Country, RegionWorld}
import com.google.inject.{ImplementedBy, Inject}
import org.squeryl.PrimitiveTypeMode.{from, select, transaction, where}
import org.squeryl.{Session, SessionFactory}
import org.squeryl.adapters.PostgreSqlAdapter
import play.api.db.Database

import scala.xml.NodeSeq
import org.squeryl.PrimitiveTypeMode._

/**
  *
  * Created with IntelliJ IDEA.
  * User: Смирнов Иван (killips)
  * Date: 20.04.2017
  *
  */
@ImplementedBy(classOf[CountryServiceImpl])
trait CountryService extends ServiceCatalog[Country]

class CountryServiceImpl @Inject()(db: Database) extends CountryService{

  import catalogs.CatalogSchem._

  SessionFactory.concreteFactory = Some(()=> Session.create(db.getConnection(), new PostgreSqlAdapter))

  def selectAllEntry(): Seq[Country] =  {
    transaction {
      from(country)(ctr => select(ctr)).toList
    }
  }

  def selectOneEntry(id: String): Country = {
    transaction {
      from(country)(ctr => where(ctr.code === id) select(ctr)).single
    }
  }
  def insertEntry(data: Country): Unit =  {
    transaction {
      country.insert(data)
    }
  }

  def deleteEntry(id: String): Unit = {
    transaction {
      country.deleteWhere(ctr => ctr.code === id)
    }
  }

  def updateEntry(data: Country): Unit = {
    transaction {
      update(country)(ctr =>
        where(ctr.code === data.code)
          set(ctr.name := data.name, ctr.fullname := data.fullname, ctr.codeAlpha2 := data.codeAlpha2, ctr.codeAlpha3 := data.codeAlpha3, ctr.regionWorld := data.regionWorld)
      )
    }
  }


  def insertUpdateAllEntry(data: NodeSeq): Unit = {
    data \\ "item" map { d =>
      transaction {
        if(from(country)(ctr => where(ctr.code === (d \ "@code" text)) select(ctr.code)).nonEmpty){
          update(country)(ctr =>
            where(ctr.code === (d \ "@code" text))
              set(ctr.name := (d \ "@name" text), ctr.fullname := (d \ "@fullname" text), ctr.codeAlpha2 := (d \ "@codeAlpha2" text).toInt, ctr.codeAlpha3 := (d \ "@codeAlpha3" text).toInt, ctr.regionWorld := (d \ "@regionWorld" text))
          )
        }else{
          country.insert(Country( code = d \ "@code" text, name = d \ "@name" text, fullname = d \ "@fullname" text, codeAlpha2 = (d \ "@codeAlpha2" text).toInt, codeAlpha3 = (d \ "@codeAlpha3" text).toInt, regionWorld =  d \ "@regionWorld" text))
        }
      }
    }
  }

}