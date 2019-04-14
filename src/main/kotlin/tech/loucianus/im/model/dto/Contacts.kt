package tech.loucianus.im.model.dto

import java.io.Serializable

data class Contacts (

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
    val date: String = "",

    /**
     * email
     */
    val email: String,

    /**
     * role
     */
    val role: String

) : Serializable