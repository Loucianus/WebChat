package tech.loucianus.im.model.entity

import java.io.Serializable

data class Transaction(

    /**
     * id
     */
    val id: Int,

    /**
     * the content
     */
    val content: String,

    /**
     * the worker
     */
    val targetWorker: Int

) : Serializable