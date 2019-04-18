package tech.loucianus.im.model.vo

import tech.loucianus.im.util.Utils
import java.io.Serializable
import java.sql.Timestamp


data class ContactsWithCurrentMessage (

    /**
     * id
     */
    val id: Int,

    /**
     * name
     */
    val name: String,

    /**
     * path of img
     */
    val portrait: String,

    /**
     * Email
     */
    val email: String,

    /**
     * role
     */
    val role: String,

    /**
     *
     */
    val lastDate: Timestamp = Utils.getNowTimestamp(),

    /**
     *
     */
    val message: String = "has no message.",

    /**
     *
     */
    val type: String = "s"

) : Serializable