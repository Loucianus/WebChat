package tech.loucianus.im.service

import tech.loucianus.im.model.dao.TaskSetter
import tech.loucianus.im.model.vo.TaskGetter

interface TaskService {

    /**
     * Get all the Tasks.
     *
     * @return The Task list.
     */
    fun getTasks(): List<TaskGetter>

    /**
     * Get Task where task id = [id]
     *
     * @return The Task Details.
     */
    fun getTaskById(id : Int): TaskGetter

    /**
     * Get Task witch worker id in [TaskSetter.targetWorkers]
     *
     * @return The list of task details.
     */
    fun getTaskByWorkerId(uid: Int): List<TaskGetter>

    /**
     * Get the subTitle List of The Title.
     *
     * @param title The father Title.
     * @return The subTitle List.
     */
    fun getSubTitleList(title: String): List<String>

    /**
     * Submit the task.
     *
     * @param uid The worker's id <pk>.
     * @param subTitle The subTitle of Task
     * @param progress The progress status.
     * @return If succeed to submit, return true; otherwise false.
     */
    fun submitTaskById(uid: Int, subTitle: String, progress: String): Boolean

    /**
     * Update the task.
     *
     * @param taskSetterList The task details list
     * @return If succeed to save, return true; otherwise false.
     */
    fun setTask(taskSetterList: List<TaskSetter>): Boolean

}