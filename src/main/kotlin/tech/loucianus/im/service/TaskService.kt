package tech.loucianus.im.service

import tech.loucianus.im.model.dao.TaskSetter
import tech.loucianus.im.model.vo.TaskGetter

interface TaskService {

    fun getTasks(): List<TaskGetter>

    fun getTaskById(id : Int): TaskGetter

    fun getTaskByWorkerId(uid: Int): List<TaskGetter>

    fun getSubTitleList(title: String): List<String>

    fun submitTaskById(uid: Int, subTitle: String, progress: String): Boolean

    fun setTask(taskSetterList: List<TaskSetter>): Boolean

}