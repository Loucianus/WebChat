package tech.loucianus.im.controller

import org.apache.shiro.authz.annotation.Logical
import org.apache.shiro.authz.annotation.RequiresRoles
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Lazy
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import tech.loucianus.im.model.JsonResponse
import tech.loucianus.im.service.TaskService
@RestController
@RequestMapping("/task")
class TaskController {

    @Autowired @Lazy lateinit var taskService: TaskService

    @RequiresRoles(value = ["worker", "manager"],logical =  Logical.OR)
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
    @GetMapping("/{id}")
    fun getTaskDetails(@PathVariable id: Int): JsonResponse {
        val task = taskService.getTaskById(id)
        return JsonResponse.ok().message(task)
    }

}