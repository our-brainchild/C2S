package utils

/**
  *
  * Created with IntelliJ IDEA User on 22.04.2017.
  * User: Смирнов Иван (killips)(ismirnoval)
  * Date: 22.04.2017
  * Time: 15:37
  */
object  IOUtils extends IOUtils

trait IOUtils
{
  def ensureClose[T <: {def close()}, R](resource: T)(block: T => R) =
  {
    try { block(resource) } finally { if (resource != null) resource.close() }
  }
}