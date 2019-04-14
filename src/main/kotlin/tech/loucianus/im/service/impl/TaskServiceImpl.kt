package tech.loucianus.im.service.impl

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import tech.loucianus.im.model.entity.Task
import tech.loucianus.im.repository.TaskRepository
import tech.loucianus.im.service.TaskService

@Service
class TaskServiceImpl: TaskService {

    @Autowired lateinit var taskRepository: TaskRepository

    override fun getTasks(): List<Task>
            = taskRepository.getTasks()

    override fun getTasksTopFive(): List<Task>
            = taskRepository.getTasks()

    override fun getTaskById(id : Int): Task
            = taskRepository.getTaskById(id)

    override fun getTaskByTask(task: String): Task
            = taskRepository.getTaskByTask(task)

}