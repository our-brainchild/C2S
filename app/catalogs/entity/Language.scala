package catalogs.entity

import org.squeryl.annotations.Column
import utils.UUIDHelper

/**
  * Created by User on 20.04.2017.
  */
case class Language(id: String = UUIDHelper.randomUUID, code: String, @Column("name")nameRaw: String, isoCode: String, gostCode: String, priority: String, localeString: String)