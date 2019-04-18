package tech.loucianus.im.controller

import org.apache.commons.logging.LogFactory
import org.apache.shiro.authz.annotation.Logical
import org.apache.shiro.authz.annotation.RequiresPermissions
import org.apache.shiro.authz.annotation.RequiresRoles
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Lazy
import org.springframework.web.bind.annotation.*
import tech.loucianus.im.model.JsonResponse
import tech.loucianus.im.model.dao.TaskSetter
import tech.loucianus.im.service.TaskService

@RestController
@RequestMapping("/task")
class TaskController {

    companion object {
        private val log = LogFactory.getLog(this::class.java)
    }

    @Autowired @Lazy lateinit var taskService: TaskService

    /**
     * 查看全部任务
     */
    @RequiresRoles(value = ["worker", "manager"],logical =  Logical.OR)
    @RequiresPermissions(value = ["view"])
    @GetMapping("/all")
    fun getTasks(): JsonResponse {
        if (log.isInfoEnabled) {
            log.info("Get All Task Information")
        }
        return JsonResponse.ok().message(taskService.getTasks())
    }

    /**
     * 获取任务详细信息
     * @param id task id
     */
    @RequiresRoles(value = ["worker", "manager"],logical =  Logical.OR)
    @RequiresPermissions(value = ["view"])
    @GetMapping
    fun getTaskDetails(@RequestParam("id") id: Int): JsonResponse {
        if (log.isInfoEnabled) {
            log.info("Get Task[$id] Details")
        }
        return JsonResponse.ok().message(taskService.getTaskById(id))
    }

    /**
     * 获取员工自己的全部任务信息
     */
    @RequiresRoles(value = ["worker", "manager"],logical =  Logical.OR)
    @RequiresPermissions(value = ["edict", "view"], logical = Logical.AND)
    @GetMapping("/{uid}/details")
    fun getTaskDetailsByWorkerId(@PathVariable("uid") uid: Int): JsonResponse {
        if (log.isInfoEnabled) {
            log.info("Get Worker[$uid]'s Task Details.")
        }
        return JsonResponse.ok().message(taskService.getTaskById(uid))
    }

    /**
     * 员工提交任务
     */
    @RequiresRoles(value = ["worker", "manager"],logical =  Logical.OR)
    @RequiresPermissions(value = ["edict", "view"], logical = Logical.AND)
    @PutMapping("/{tid}")
    fun submitTaskById(@PathVariable("tid") tid: Int,
                       @RequestParam("subTitle") subTitle: String,
                       @RequestParam("progress") progress: String): JsonResponse {
        if (log.isInfoEnabled) {
            log.info("Submit Task -> $tid")
        }
        return JsonResponse.ok().message(taskService.submitTaskById(tid, subTitle, progress))
    }

    /**
     * 添加任务
     */
    @RequiresRoles(value = ["manager"])
    @RequiresPermissions(value = ["edict", "view"], logical = Logical.AND)
    @PostMapping
    fun publishTask(taskSetterList: List<TaskSetter>): JsonResponse {
        if (log.isInfoEnabled) {
            log.info("Add Task(s) -> $taskSetterList")
        }
        return JsonResponse.ok().message(taskService.setTask(taskSetterList))
    }
}