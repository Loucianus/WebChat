package tech.loucianus.im.model.dto

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
    val data: String = "",

    /**
     * Date
     */
    val date: String = "",

    /**
     * type
     */
    val type: String = ""

) :Serializable {
    override fun toString(): String {
        return "{" +
                "'from':'$from_id'," +
                "'to':'$to_id'," +
                "'username':'$name'," +
                "'data':'$data'," +
                "'date':'$date'" +
                "}"
    }
}