package utils

/**
  *
  * Created with IntelliJ IDEA User on 22.04.2017.
  * User: Смирнов Иван (killips)(ismirnoval)
  * Date: 22.04.2017
  * Time: 16:02
  */
object Helper {

  implicit class SafeNull[T](val obj: T) extends AnyVal {

    def getOr(default: => T) = if(obj == null) default else obj

  }

  implicit class OptionHelper[String](val obj: String) extends AnyVal {

    def trimToOption() =  if(obj == null) None else Option(obj.toString.trim)

  }

}
