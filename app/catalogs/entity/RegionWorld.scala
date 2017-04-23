package catalogs.entity

import utils.UUIDHelper

/**
  * Created by User on 20.04.2017.
  */
case class RegionWorld(id: String = UUIDHelper.randomUUID, code: String, name: String)
