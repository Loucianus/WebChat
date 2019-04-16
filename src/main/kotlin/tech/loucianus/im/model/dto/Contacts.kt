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
     * email
     */
    val email: String,

    /**
     * role
     */
    val role: String

) : Serializable