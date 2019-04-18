package tech.loucianus.im.model.vo

import java.io.Serializable

data class Contacts (

    /**
     * id
     */
    val id: Int = 0,

    /**
     * name
     */
    val name: String = "",

    /**
     * path of img
     */
    val portrait: String = "",

    /**
     * Email
     */
    val email: String = "",

    /**
     * role
     */
    val role: String = ""

) : Serializable