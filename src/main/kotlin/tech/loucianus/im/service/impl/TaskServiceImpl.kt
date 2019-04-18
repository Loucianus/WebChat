package tech.loucianus.im.service.impl

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import tech.loucianus.im.model.dao.TaskSetter
import tech.loucianus.im.model.po.Task
import tech.loucianus.im.model.vo.TaskGetter
import tech.loucianus.im.repository.TaskRepository
import tech.loucianus.im.service.TaskService
import tech.loucianus.im.service.WorkerService

@Service
class TaskServiceImpl: TaskService {

    @Autowired lateinit var taskRepository: TaskRepository
    @Autowired lateinit var workerRepository: WorkerService

    override fun getTasks(): List<TaskGetter> {
        val tasks = taskRepository.findTasks()
        val taskGetterList = mutableListOf<TaskGetter>()
            tasks.forEach{
                val workerNameList = mutableListOf<String>()
                val workerIdList = mutableListOf<Int>()
                workerRepository.getWorkersById(it.targetWorkers.split("#")).forEach {worker ->
                    workerNameList.add(worker.name)
                    workerIdList.add(worker.id)
                }
                taskRepository
                taskGetterList.add(
                    TaskGetter(
                        title = it.title,
                        subList = getSubTitleList(it.subTitle),
                        process = it.process,
                        targetWorkers = workerNameList,
                        targetWorkersId = workerIdList,
                        publishWorker = it.publishWorker
                    )
                )
        }
        return taskGetterList
    }

    override fun getSubTitleList(title: String): List<String> {
        return taskRepository.findTaskSubTitleByTitle(title)
    }

    override fun getTaskById(id: Int): TaskGetter {
        val task = taskRepository.findTaskById(id)

        val workerNameList = mutableListOf<String>()
        val workerIdList = mutableListOf<Int>()
        workerRepository.getWorkersById(task.targetWorkers.split("#")).forEach {worker ->
            workerNameList.add(worker.name)
            workerIdList.add(worker.id)
        }

        return TaskGetter(
            title = task.title,
            subList = getSubTitleList(task.subTitle),
            process = task.process,
            targetWorkers = workerNameList,
            targetWorkersId = workerIdList,
            publishWorker = task.publishWorker

        )
    }

    override fun getTaskByWorkerId(uid: Int): List<TaskGetter> {
        val tasks = taskRepository.findTaskByByWorkerId(uid)
        val taskGetterList = mutableListOf<TaskGetter>()
        tasks.forEach{
            val workerNameList = mutableListOf<String>()
            val workerIdList = mutableListOf<Int>()
            workerRepository.getWorkersById(it.targetWorkers.split("#")).forEach {worker ->
                workerNameList.add(worker.name)
                workerIdList.add(worker.id)
            }
            taskRepository
            taskGetterList.add(
                TaskGetter(
                    title = it.title,
                    subList = getSubTitleList(it.subTitle),
                    process = it.process,
                    targetWorkers = workerNameList,
                    targetWorkersId = workerIdList,
                    publishWorker = it.publishWorker
                )
            )
        }
        return taskGetterList
    }

    override fun setTask(taskSetterList: List<TaskSetter>): Boolean {
        val taskList = mutableListOf<Task>()
        taskSetterList.forEach {
            taskList.add(
                Task(
                    id = 0,
                    title = it.title,
                    subTitle = it.subTitle,
                    process = it.progress,
                    targetWorkers = targetWorkersFormat(it.targetWorkers),
                    publishWorker = it.PublishWorker
                )
            )
        }
        return taskRepository.saveTask(taskList) == taskList.size
    }

    override fun submitTaskById(uid: Int, subTitle: String, progress: String): Boolean {
        return taskRepository.updateTaskById(uid,subTitle, progress) == 1
    }

    private fun targetWorkersFormat(workers: List<Int>): String {
        var targetWorkers = ""
        workers.forEach {
            targetWorkers += "#$it"
        }
        return targetWorkers
    }
}