package tech.loucianus.im.model.dto

import java.io.Serializable

data class Permission(
    /**
     * id
     */
    val id: Int,

    /**
     * role
     */
    val role: String,

    /**
     * permission
     */
    val permission: String
) : Serializable