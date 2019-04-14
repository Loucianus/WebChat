package tech.loucianus.im.util

import java.text.SimpleDateFormat
import java.util.*

object Utils {

    fun getNowTimestamp(): String {
        return System.currentTimeMillis().toString()
    }

    fun getSQLDateNow(): java.sql.Date {
        return java.sql.Date(System.currentTimeMillis())
    }

    fun getDate(pattern : String = "yyyy-MM-dd"): String {
        return SimpleDateFormat(pattern).format(Date()).toString()
    }
}