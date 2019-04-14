package tech.loucianus.im.model.dto

import java.io.Serializable

data class Contact (

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
    val role: String

) : Serializable