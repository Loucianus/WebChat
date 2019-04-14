package tech.loucianus.im.model.entity

import java.io.Serializable

data class Worker (

    /**
     * id
     */
    val id: Int,

    /**
     * email
     */
    val email: String,

    /**
     * password
     */
    val password: String,

    /**
     * path of img
     */
    val portrait: String,

    /**
     * q/i/v: Quit/incumbency/vacation
     */
    val status: String,

    /**
     * name of worker
     */
    val name: String,

    /**
     * f/m/u: female/male/unknow
     */
    val gender: String,

    /**
     * role: worker | manager
     */
    val role: String,

    /**
     * permission: view | edict | download | upload
     */
    val permission: String
) : Serializable