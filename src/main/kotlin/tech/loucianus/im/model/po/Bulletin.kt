package tech.loucianus.im.model.po

import java.io.Serializable
import java.sql.Timestamp

data class Bulletin (

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
     * the file path of the details
     */
    val path: String,

    /**
     * worker of publish
     */
    val publishWorker: Int

) : Serializable