package tech.loucianus.im.model.vo

import java.io.Serializable

data class TaskGetter (

    val title: String,

    val subList: List<String>,

    val process: String,

    val targetWorkers: List<String>,

    val targetWorkersId: List<Int>,

    val publishWorker: Int

) : Serializable