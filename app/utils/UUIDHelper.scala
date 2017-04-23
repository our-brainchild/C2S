package utils

/**
  *
  * Created with IntelliJ IDEA User on 23.04.2017.
  * User: Смирнов Иван (killips)(ismirnoval)
  * Date: 23.04.2017
  * Time: 11:52
  */

trait UUIDHelper {

  def  random(size: Int) = {
    assert(size <=32)
    randomUUID.take(size)
  }


  def randomUUID = java.util.UUID.randomUUID().toString.replaceAll("-", "")
}

object UUIDHelper extends UUIDHelper