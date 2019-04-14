package tech.loucianus.im.model.entity

import java.io.Serializable
import java.sql.Date
data class Bulletin (

    /**
     * id
     */
    val id: Int = 0,

    /**
     * title of bulletin
     */
    val title: String,

    /**
     * publish date
     */
    val date: Date,

    /**
     * the file path of the details
     */
    val path: String,

    /**
     * worker of publish
     */
    val publishWorker: Int

) : Serializable