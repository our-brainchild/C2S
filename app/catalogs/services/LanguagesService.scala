package catalogs.services

import catalogs.entity.{Country, Language}
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
@ImplementedBy(classOf[LanguageServiceImpl])
trait LanguageService extends ServiceCatalog[Language]

class LanguageServiceImpl @Inject()(db: Database) extends LanguageService {

  import catalogs.CatalogSchem._

  SessionFactory.concreteFactory = Some(()=> Session.create(db.getConnection(), new PostgreSqlAdapter))

  def selectAllEntry(): Seq[Language] = {
    transaction {
      from(language)(lang => select(lang)).toList
    }
  }
  def selectOneEntry(id: String): Language = {
    transaction {
      from(language)(lang => where(lang.code === id) select(lang)).single
    }
  }

  def insertEntry(data: Language) = {
    transaction {
      language.insert(data)
    }
  }

  def deleteEntry(id: String) = {
    transaction {
      language.deleteWhere(lang => lang.code === id)
    }
  }

  def updateEntry(data: Language) = {
    transaction {
      update(language)(lang =>
        where(lang.code === data.code)
          set(lang.nameRaw := data.nameRaw, lang.isoCode := data.isoCode, lang.gostCode := data.gostCode, lang.priority := data.priority, lang.localeString := data.localeString)
      )
    }
  }

  def insertUpdateAllEntry(data: NodeSeq) = {
    data \\ "item" map { d =>
      transaction {
        if(from(language)(lang => where(lang.code === (d \ "@code" text)) select(lang.code)).nonEmpty){
          update(language)(lang =>
            where(lang.code === (d \ "@code" text))
              set(lang.nameRaw := (d \ "@name" text), lang.isoCode := (d \ "@isoCode" text), lang.gostCode := (d \ "@gostCode" text), lang.priority := (d \ "@codeAlpha3" text), lang.localeString := (d \ "@localeString" text))
          )
        }else{
          language.insert(Language( code = d \ "@code" text, nameRaw = d \ "@name" text, isoCode = d \ "@isoCode" text, gostCode = d \ "@gostCode" text, priority = d \ "@codeAlpha3" text, localeString =  d \ "@localeString" text))
        }
      }
    }
  }
}