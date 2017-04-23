package catalogs.services

import catalogs.entity.{City, TypeEducationalInstitution}
import com.google.inject.{ImplementedBy, Inject}
import org.squeryl.PrimitiveTypeMode.{from, transaction, update, where}
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
@ImplementedBy(classOf[TypeEducationalInstitutionServiceImpl])
trait TypeEducationalInstitutionService extends ServiceCatalog[TypeEducationalInstitution]

class TypeEducationalInstitutionServiceImpl @Inject()(db: Database) extends TypeEducationalInstitutionService {

  import catalogs.CatalogSchem._

  SessionFactory.concreteFactory = Some(()=> Session.create(db.getConnection(), new PostgreSqlAdapter))

  def selectAllEntry(): Seq[TypeEducationalInstitution] = ???

  def selectOneEntry(id: String): TypeEducationalInstitution = ???

  def insertEntry(data: TypeEducationalInstitution): Unit = ???

  def deleteEntry(id: String): Unit = ???

  def updateEntry(data: TypeEducationalInstitution): Unit = ???

  def insertUpdateAllEntry(data: NodeSeq): Unit = {
    data \\ "item" map { d =>
      transaction {
        if(from(typeEducationalInstitution)(tei => where(tei.code === (d \ "@code" text)) select(tei.code)).nonEmpty){
          update(typeEducationalInstitution)(tei =>
            where(tei.code === (d \ "@code" text))
              set(tei.name := (d \ "@name" text), tei.fullname := (d \ "@fullname" text))
          )
        }else{
          typeEducationalInstitution.insert(TypeEducationalInstitution( code = d \ "@code" text, name = d \ "@name" text, fullname = d \ "@fullname" text))
        }
      }
    }
  }
}
