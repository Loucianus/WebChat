package tech.loucianus.im.model.dao

import java.io.Serializable

data class Permission(
    /**
     * role
     */
    val role: String = "",

    /**
     * permission
     */
    val permission: String = ""

) : Serializable