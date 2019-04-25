package tech.loucianus.im.model.vo

import java.io.Serializable
import java.sql.Date

data class FileList(

    /**
     * file id
     */
    val fileId: Int,

    /**
     * full path
     */
    val fullPath: String,

    /**
     * file name
     */
    val filename: String,

    /**
     * date uploaded
     */
    val uploadDate: Date,

    /**
     * download times
     */
    val downloadTimes: Int,


    val toId: Int,

    val uploadWorker: Int,
    /**
     * uploader name
     */
    val name: String,

    /**
     * uploader email
     */
    val email: String


) : Serializable