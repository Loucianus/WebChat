package tech.loucianus.im.controller

import org.apache.shiro.authz.annotation.Logical
import org.apache.shiro.authz.annotation.RequiresPermissions
import org.apache.shiro.authz.annotation.RequiresRoles
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Lazy
import org.springframework.web.bind.annotation.*
import tech.loucianus.im.model.JsonResponse
import tech.loucianus.im.service.TaskService
@RestController
@RequestMapping("/task")
class TaskController {

    @Autowired @Lazy lateinit var taskService: TaskService

    @RequiresRoles(value = ["worker", "manager"],logical =  Logical.OR)
    @RequiresPermissions(value = ["view"])
    @GetMapping("/all")
    fun getTasks(): JsonResponse {
        val tasks = taskService.getTasks()
        return JsonResponse.ok().message(tasks)
    }

    /**
     * 获取任务详细信息
     * @param id task id
     */
    @RequiresRoles(value = ["worker", "manager"],logical =  Logical.OR)
    @RequiresPermissions(value = ["view"])
    @GetMapping
    fun getTaskDetails(@RequestParam("task_id") id: Int): JsonResponse {
        val task = taskService.getTaskById(id)
        return JsonResponse.ok().message(task)
    }

}