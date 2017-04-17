package module

import javax.inject.Inject
import java.sql.Connection

import akka.actor.ActorSystem
import com.google.inject.AbstractModule
import com.typesafe.config.ConfigFactory
import liquibase.Liquibase
import liquibase.database.jvm.JdbcConnection
import liquibase.resource.{ClassLoaderResourceAccessor, CompositeResourceAccessor, FileSystemResourceAccessor}
import play.api.db.Database
import play.api.inject.ApplicationLifecycle

import scala.concurrent.Future

/**
  * Created by User on 17.04.2017.
  */

trait LiquibaseComponent

class LiquibaseComponentImpl @Inject()(lifecycle: ApplicationLifecycle, db: Database) extends LiquibaseComponent {

  lazy val config = ConfigFactory.load()

  def performMigrations: Unit = {

    val changeLog = config.getString("liquibase.c2s.changelog")

    db.withConnection { connection =>
      val liqui = try {getLiquibase(changeLog, connection)} catch {case e: Exception => throw new Exception("Liquibase can't be instantiated")}
      liqui.update("dev")
    }

  }

  def getLiquibase(changeLogFilePath: String, connection: Connection): Liquibase = {
    val fileAccessor = new FileSystemResourceAccessor()
    val classLoaderResourceAccessor = new ClassLoaderResourceAccessor(classLoader)
    val fileOpener = new CompositeResourceAccessor(fileAccessor, classLoaderResourceAccessor)
    new Liquibase(changeLogFilePath, fileOpener, new JdbcConnection(connection))
  }

  def classLoader: ClassLoader = classOf[ActorSystem].getClassLoader

  lifecycle.addStopHook{ () =>
    Future.successful(())
  }

  performMigrations

}

class LiquibaseModule extends AbstractModule {
  override def configure(): Unit = {
    bind(classOf[LiquibaseComponent]).to(classOf[LiquibaseComponentImpl]).asEagerSingleton()
  }
}
