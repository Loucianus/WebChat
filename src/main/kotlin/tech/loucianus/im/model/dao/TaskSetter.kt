package tech.loucianus.im.model.dao

import java.io.Serializable

data class TaskSetter(

    val title: String = "",

    val subTitle: String = "",

    val progress: String = "o",

    val targetWorkers: List<Int> = listOf(),

    val PublishWorker: Int = 0

) : Serializable