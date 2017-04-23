package utils

/**
  *
  * Created with IntelliJ IDEA User on 22.04.2017.
  * User: Смирнов Иван (killips)(ismirnoval)
  * Date: 22.04.2017
  * Time: 1:30
  */
trait HashHelper {

  def md5(data: String): String = hash(data, "MD5")

  private def hash(data: String, alg: String) = byteArrayToHex(hashBytes(data, alg))

  private def hashBytes(data: String, alg: String) = getMD(alg).digest(data.getBytes("UTF-8"))

  private def getMD(alg: String) = java.security.MessageDigest.getInstance(alg)

  def byteArrayToHex(byteData: Array[Byte]): String = new java.math.BigInteger(1, byteData).toString(16)

}

object HashHelper extends HashHelper