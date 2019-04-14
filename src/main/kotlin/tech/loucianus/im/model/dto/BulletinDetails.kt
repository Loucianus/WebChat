package tech.loucianus.im.model.dto

import java.io.Serializable
import java.sql.Date

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
    val date: Date,

    /**
     * the details
     */
    val details: String,

    /**
     * worker of publish
     */
    val publishWorker: String,

    val publishWorkerId: Int

) : Serializable