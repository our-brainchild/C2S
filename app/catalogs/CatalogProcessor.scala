package catalogs

import java.io.File
import javax.inject.Inject

import catalogs.entity.Catalog
import catalogs.services._
import config.AppConfig
import play.api.Logger
import utils.HashHelper._
import utils.Helper.SafeNull
import utils.IOUtils

import scala.io.Source
import scala.xml.NodeSeq
import scala.xml.parsing.XhtmlParser
/**
  *
  * Created with IntelliJ IDEA User on 22.04.2017.
  * User: Смирнов Иван (killips)(ismirnoval)
  * Date: 22.04.2017
  * Time: 0:25
  */

trait CatalogProcessor {

  def readCatalogs(): Seq[(String, Seq[CatalogData])]

  protected def listFiles(dirName: String, extension: String) =
    new File(dirName).listFiles.getOr(Array.empty).filter(_.getName.matches(".*\\." + extension))

  def importCatalogs()
}


class CatalogProcessorImpl @Inject()(catalogServiceImpl: CatalogServiceImpl, cityServiceImpl: CityServiceImpl, countryServiceImpl: CountryServiceImpl, languageServiceImpl: LanguageServiceImpl, regionWorldServiceImpl: RegionWorldServiceImpl, typeEducationalInstitutionServiceImpl: TypeEducationalInstitutionServiceImpl)  extends CatalogProcessor {

  def readCatalogs() = {
    listFiles(AppConfig.catalogFilePath, "xml").map(file => (file.getName, readCatalogFile(file)))
  }

  def readCatalogFile(file: File) = {
    IOUtils.ensureClose(Source.fromFile(file, "UTF-8")){ source =>
      XhtmlParser(source) \\ "root" \\ "catalog" map { ch =>
        CatalogData(
          ch \ "@code" text,
          ch \ "@title" text,
          md5(ch.toString()),
          ch \\ "item"
        )
      }
    }
  }
 //TODO: Явно в будущем надо всё это изменять адекватно
  def importCatalogs() {
   Logger.debug("Import catalogs")
    readCatalogs.map{rc => rc._2.map{
        ct => ct.code match {
          case "catalogs.entity.RegionWorld" => {
            catalogServiceImpl.selectChecksum(ct.code) match {
              case "false" => {
                catalogServiceImpl.insertOrUpdate(Catalog(ct.code, ct.title, ct.checksum))
                regionWorldServiceImpl.insertUpdateAllEntry(ct.item)
              }
              case ct.checksum => Logger.info(s"""Catalog normal ${ct.title}""")
              case _ =>  {
                catalogServiceImpl.insertOrUpdate(Catalog(ct.code, ct.title, ct.checksum))
                regionWorldServiceImpl.insertUpdateAllEntry(ct.item)
              }
            }
          }
          case "catalogs.entity.Country" => {
            catalogServiceImpl.selectChecksum(ct.code) match {
              case "false" => {
                catalogServiceImpl.insertOrUpdate(Catalog(ct.code, ct.title, ct.checksum))
                countryServiceImpl.insertUpdateAllEntry(ct.item)
              }
              case ct.checksum => Logger.info(s"""Catalog normal ${ct.title}""")
              case _ => {
                catalogServiceImpl.insertOrUpdate(Catalog(ct.code, ct.title, ct.checksum))
                countryServiceImpl.insertUpdateAllEntry(ct.item)
              }
            }
          }
          case "catalogs.entity.City" => {
            catalogServiceImpl.selectChecksum(ct.code) match {
              case "false" => {
                catalogServiceImpl.insertOrUpdate(Catalog(ct.code, ct.title, ct.checksum))
                cityServiceImpl.insertUpdateAllEntry(ct.item)
              }
              case ct.checksum => Logger.info(s"""Catalog normal ${ct.title}""")
              case _ => {
                catalogServiceImpl.insertOrUpdate(Catalog(ct.code, ct.title, ct.checksum))
                cityServiceImpl.insertUpdateAllEntry(ct.item)
              }
            }
          }
          case "catalogs.entity.TypeEducationalInstitution" => {
            catalogServiceImpl.selectChecksum(ct.code) match {
              case "false" => {
                catalogServiceImpl.insertOrUpdate(Catalog(ct.code, ct.title, ct.checksum))
                typeEducationalInstitutionServiceImpl.insertUpdateAllEntry(ct.item)
              }
              case ct.checksum => Logger.info(s"""Catalog normal ${ct.title}""")
              case _ => {
                catalogServiceImpl.insertOrUpdate(Catalog(ct.code, ct.title, ct.checksum))
                typeEducationalInstitutionServiceImpl.insertUpdateAllEntry(ct.item)
              }
            }
          }
          case "catalogs.entity.Language"  => {
            catalogServiceImpl.selectChecksum(ct.code) match {
              case "false" => {
                catalogServiceImpl.insertOrUpdate(Catalog(ct.code, ct.title, ct.checksum))
                languageServiceImpl.insertUpdateAllEntry(ct.item)
              }
              case ct.checksum => Logger.info(s"""Catalog normal ${ct.title}""")
              case _ => {
                catalogServiceImpl.insertOrUpdate(Catalog(ct.code, ct.title, ct.checksum))
                languageServiceImpl.insertUpdateAllEntry(ct.item)
              }
            }
          }
          case _ => println(s"""Данного каталога нет в системе:  ${ct}""")
        }
      }
    }
  }
}

case class CatalogData(code: String, title: String, checksum: String, item: NodeSeq)
