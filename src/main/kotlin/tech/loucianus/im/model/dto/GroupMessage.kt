package tech.loucianus.im.model.dto

import java.sql.Timestamp

data class GroupMessage(
    val fromId: Int,

    val content: String,

    val type: String = "s",

    val date: Timestamp,

    val name: String
)