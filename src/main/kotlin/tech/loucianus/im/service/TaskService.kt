package tech.loucianus.im.service

import tech.loucianus.im.model.entity.Task

interface TaskService {

    fun getTasks(): List<Task>

    fun getTasksTopFive(): List<Task>

    fun getTaskById(id : Int): Task

    fun getTaskByTask(task: String): Task

}