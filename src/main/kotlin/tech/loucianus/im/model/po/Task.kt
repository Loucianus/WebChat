package tech.loucianus.im.model.po

import java.io.Serializable

data class Task(

    /**
     * id
     */
    val id: Int,

    /**
     * task name
     */
    val title: String,

    /**
     * sub task name
     */
    val subTitle: String,

    /**
     * progress: u/o/p : unknow / on the march / pause
     */
    val process: String,

    /**
     * the string of worker id
     */
    val targetWorkers: String,

    /**
     * the worker of publishing
     */
    val publishWorker: Int
) : Serializable