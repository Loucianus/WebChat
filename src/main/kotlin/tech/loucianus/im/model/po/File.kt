package tech.loucianus.im.model.po

import tech.loucianus.im.util.Utils
import java.io.Serializable
import java.sql.Date

data class File (
    /**
     * id
     */
    val id: Int,

    /**
     * file name
     */
    val filename: String,

    /**
     * file path
     */
    val filepath: String,

    /**
     * upload date
     */
    val uploadDate: Date = Utils.getSQLDateNow() ,

    /**
     * download times
     */
    val downloadTimes: Int = 0,

    /**
     * worker of uploading
     */
    val uploadWorker: Int

) : Serializable