package tech.loucianus.im.model.dto

import java.io.Serializable
import java.sql.Date

data class FileList(

    val fileId: Int,

    val fullPath: String,

    val filename: String,

    val uploadDate: Date,

    val downloadTimes: Int,

    val name: String,

    val email: String

) : Serializable