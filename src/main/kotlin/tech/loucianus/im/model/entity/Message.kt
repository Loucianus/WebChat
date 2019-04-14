package tech.loucianus.im.model.entity

import java.io.Serializable
import java.sql.Timestamp

data class Message (
    /**
     * id
     */
    val id: Int = 0,

    /**
     * from id
     */
    val fromId: Int = 0,

    /**
     * to id
     */
    val toId: Int = 0,

    /**
     * message
     */
    val content: String = "你们还没有发过消息.",

    /**
     * s/i/f: string / image / file
     */
    val type: String = "s",

    /**
     * date
     */
    val date: Timestamp = Timestamp(0L)

) : Serializable