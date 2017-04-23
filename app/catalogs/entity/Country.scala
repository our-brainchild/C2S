package catalogs.entity

import utils.UUIDHelper

/**
  * Created by User on 20.04.2017.
  */

case class Country(id: String = UUIDHelper.randomUUID, code: String, name: String, fullname: String, codeAlpha2: Int, codeAlpha3: Int, regionWorld: String)