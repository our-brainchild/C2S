package catalogs.config

import com.typesafe.config.ConfigFactory

/**
  *
  * Created with IntelliJ IDEA User on 22.04.2017.
  * User: Смирнов Иван (killips)(ismirnoval)
  * Date: 22.04.2017
  * Time: 15:51
  */
object AppConfig {

  lazy val conf = ConfigFactory.load()

  lazy val catalogFilePath = conf.getString("")

}
