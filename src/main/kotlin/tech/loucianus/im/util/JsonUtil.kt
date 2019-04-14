package tech.loucianus.im.util

import com.google.gson.Gson

object JsonUtil {

    fun convertToJson(string: String, clazz: Any) {
           Gson().fromJson(string,clazz.javaClass)
    }
}