package tech.loucianus.im.model.dao

import java.io.Serializable

data class WorkerUpdater (

    /**
     * email
     */
    val email: String = "",

    /**
     * password
     */
    val password: String = "nj3XZyhDdihRW2HddvpMykfRrowzFyoo",

    /**
     * path of img
     */
    val portrait: String = "static/img/portrait/portrait-4.jpeg",

    /**
     * q/i/v: Quit/incumbency/vacation
     */
    val status: String = "i",

    /**
     * name of worker
     */
    val name: String = "",

    /**
     * f/m/u: female/male/unknow
     */
    val gender: String = "u",

    /**
     * role: worker | manager
     */
    val role: String = "worker",

    /**
     * permission: view | edict | download | upload | update | delete
     */
    val permission: String = "view#edict#download#upload"

) : Serializable