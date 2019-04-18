package tech.loucianus.im.util

import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*

object Utils {

    fun getNowTimestampStr(): String {
        return System.currentTimeMillis().toString()
    }

    fun getNowTimestamp() : Timestamp
            = Timestamp(System.currentTimeMillis())

    fun getSQLDateNow(): java.sql.Date {
        return java.sql.Date(System.currentTimeMillis())
    }

    fun getDate(pattern : String = "yyyy-MM-dd"): String {
        return SimpleDateFormat(pattern).format(Date()).toString()
    }
}