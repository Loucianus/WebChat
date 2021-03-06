package tech.loucianus.im.model.vo

import java.io.Serializable

data class MessageSender (

    /**
     * Message Sender.
     */
    val from_id: Int,

    /**
     * Message receiver.
     */
    val to_id: Int,

    /**
     * User's that received username.
     */
    val name: String,

    /**
     * Message content
     */
    val content: String = "",

    /**
     * Date
     */
    val date: String = "",

    /**
     * type
     */
    val type: String = "",

    val filename: String = ""

) :Serializable {
    override fun toString(): String {
        return "{" +
                "'from':'$from_id'," +
                "'to':'$to_id'," +
                "'username':'$name'," +
                "'data':'$content'," +
                "'date':'$date'" +
                "}"
    }
}