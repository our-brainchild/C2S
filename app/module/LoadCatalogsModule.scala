package module

import catalogs.{CatalogProcessor, CatalogProcessorImpl}
import com.google.inject.{AbstractModule, Inject}
import play.api.inject.ApplicationLifecycle

/**
  *
  * Created with IntelliJ IDEA User on 23.04.2017.
  * User: Смирнов Иван (killips)(ismirnoval)
  * Date: 23.04.2017
  * Time: 0:47
  */
trait LoadCatalogs {

}

class LoadCatalogsImpl @Inject()(lifecycle: ApplicationLifecycle, catalog: CatalogProcessorImpl) extends LoadCatalogs {
  catalog.importCatalogs()
}
//TODO: Надо удалить данный модуль, если всё будет работать без него.
class LoadCatalogsModule extends AbstractModule {
  override def configure(): Unit = {
    bind(classOf[LoadCatalogs]).to(classOf[LoadCatalogsImpl])
  }
}