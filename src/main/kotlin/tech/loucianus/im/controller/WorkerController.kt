package tech.loucianus.im.controller

import org.apache.commons.logging.LogFactory
import org.apache.shiro.SecurityUtils
import org.apache.shiro.authz.annotation.Logical
import org.apache.shiro.authz.annotation.RequiresPermissions
import org.apache.shiro.authz.annotation.RequiresRoles
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import tech.loucianus.im.model.JsonResponse
import tech.loucianus.im.model.dao.WorkerStorer
import tech.loucianus.im.model.dao.WorkerUpdater
import tech.loucianus.im.service.WorkerService

@RestController
class WorkerController {

    companion object {
        private val log = LogFactory.getLog(this::class.java)
    }

    @Autowired
    lateinit var workerService: WorkerService

    /**
     * 添加用户
     */
    @RequiresRoles(value = ["manager"])
    @RequiresPermissions(value = ["update", "view"], logical = Logical.AND)
    @PostMapping("/worker")
    fun addOneWorker(@RequestBody @Validated workerStorer : WorkerStorer):JsonResponse {
        if (log.isInfoEnabled)
            log.info("Add a worker : $workerStorer")
        return JsonResponse.ok().message(workerService.setWorker(workerStorer))
    }

    @RequiresRoles(value = ["manager"])
    @RequiresPermissions(value = ["update", "view"], logical = Logical.AND)
    @PostMapping("/workers")
    fun addOWorkers(@RequestBody @Validated workerStorerList : List<WorkerStorer>):JsonResponse {
        if (log.isInfoEnabled)
            log.info("Add a worker : $workerStorerList")
        return JsonResponse.ok().message(workerService.setWorkers(workerStorerList))
    }

    @RequiresRoles(value = ["manager","worker"], logical = Logical.OR)
    @RequiresPermissions(value = ["update", "edict"], logical = Logical.OR)
    @PutMapping("/worker")
    fun updateWorker(@RequestBody @Validated workerUpdater: WorkerUpdater):JsonResponse {
        if (log.isInfoEnabled)
            log.info("Add a worker : $workerUpdater")
        val subject = SecurityUtils.getSubject()
        return if (subject.hasRole("manager") && subject.isPermitted("update")) {
            JsonResponse.ok().message(workerService.updateWorkerByAdmin(workerUpdater))
        } else if (subject.hasRole("worker") && subject.isPermitted("edict")) {
            val email: String = SecurityUtils.getSubject().principal as String
            if (email == workerUpdater.email) {
                JsonResponse.ok().message(workerService.updateWorkerByWorker(workerUpdater))
            } else {
                JsonResponse.unauthorized().message("No permission to update other's information.")
            }
        } else {
            JsonResponse.unauthorized().message("Has No Permission to update the information of worker.")
        }
    }

    /**
     * 获取全部联系人
     *
     * @return 返回所有联系人
     */
    @RequiresRoles(value = ["worker", "manager"],logical =  Logical.OR)
    @RequiresPermissions(value = ["view"])
    @GetMapping("/worker/contacts")
    fun getAllContacts(): JsonResponse {
        val email: String = SecurityUtils.getSubject().principal as String
        val contacts = workerService.getContacts(email)
        if (log.isInfoEnabled) log.info("Get all contacts.")
        return JsonResponse.ok().message(contacts)
    }

    /**
     * 获取员工信息
     *
     * @param id 用户id
     */
    @RequiresRoles(value = ["worker", "manager"], logical =  Logical.OR)
    @RequiresPermissions(value = ["view"])
    @GetMapping("/info/{id}")
    fun getOther(@PathVariable("id") id: Int): JsonResponse? {
        val userInfo = workerService.getWorker(id)
        return JsonResponse.ok().message(userInfo)
    }
}