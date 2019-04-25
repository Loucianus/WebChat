package tech.loucianus.im.model.dao

import java.io.Serializable

data class WorkerUpdater (

    val id: Int,

    /**
     * q/i/v: Quit/incumbency/vacation
     */
    val status: String = "i",

    /**
     * role: worker | manager
     */
    val role: String = "worker",

    /**
     * permission: view | edict | download | upload | update | delete
     */
    val permission: String = "view#edict#download#upload"

) : Serializable