package tech.loucianus.im.model.po

import java.io.Serializable
import java.sql.Timestamp

data class Message (
    /**
     * id
     */
    val id: Int,

    /**
     * from id
     */
    val fromId: Int,

    /**
     * to id
     */
    val toId: Int,

    /**
     * message
     */
    val content: String = "Has no message.",

    /**
     * s/i/f: string / image / file
     */
    val type: String = "s",

    /**
     * date
     */
    val date: Timestamp = Timestamp(0L),

    /**
     * the flag of chat partner
     *
     * example: "max{[fromId],[toId]}#min{[fromId],[toId]}"
     */
    val target: String,

    /**
     * isRead
     *
     * f|t false|true
     */
    val isRead: String,

    val filename: String

) : Serializable