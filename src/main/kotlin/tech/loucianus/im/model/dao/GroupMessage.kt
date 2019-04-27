package tech.loucianus.im.model.dao

import tech.loucianus.im.util.Utils
import java.sql.Timestamp

data class GroupMessage(
    val fromId: Int = -1,

    val content: String = "",

    val type: String = "s",

    val filename: String = "",

    val isRead: String = "",

    val date: Timestamp = Utils.getNowTimestamp(),

    val name: String =""
)