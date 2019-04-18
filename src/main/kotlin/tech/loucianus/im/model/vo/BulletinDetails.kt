package tech.loucianus.im.model.vo

import java.io.Serializable
import java.sql.Timestamp

data class BulletinDetails (
    /**
     * id
     */
    val id: Int,

    /**
     * title of bulletin
     */
    val title: String,

    /**
     * publish date
     */
    val date: Timestamp,

    /**
     * the details
     */
    val details: String,

    /**
     * worker of publish name
     */
    val publishWorker: String,

    /**
     * worker of publish id
     */
    val publishWorkerId: Int

) : Serializable