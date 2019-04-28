package tech.loucianus.im.controller

import org.apache.commons.logging.LogFactory
import org.apache.shiro.SecurityUtils
import org.apache.shiro.authz.annotation.Logical
import org.apache.shiro.authz.annotation.RequiresPermissions
import org.apache.shiro.authz.annotation.RequiresRoles
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import tech.loucianus.im.model.JsonResponse
import tech.loucianus.im.model.dao.WorkerStorer
import tech.loucianus.im.model.dao.WorkerUpdater
import tech.loucianus.im.service.WorkerService

@RestController
@RequestMapping("/worker")
class WorkerController {

    companion object {
        private val log = LogFactory.getLog(this::class.java)
    }

    @Autowired lateinit var workerService: WorkerService

    // 添加用户
    @RequiresRoles(value = ["manager"])
    @RequiresPermissions(value = ["update", "view"], logical = Logical.AND)
    @PostMapping
    fun addWorker(@RequestBody @Validated workerStorer : WorkerStorer):JsonResponse {

        val result = workerService.setWorker(workerStorer)

        return JsonResponse.ok().message( result )
    }

    // 修改权限
    @RequiresRoles(value = ["manager"])
    @RequiresPermissions(value = ["update"])
    @PostMapping("/permission")
    fun updatePermission(@RequestBody workerUpdater: WorkerUpdater):JsonResponse {

        val result = workerService.updateWorkerPermission(workerUpdater)

        return JsonResponse.ok().message( result )
    }

    // 获取联系人
    @RequiresRoles(value = ["worker", "manager"],logical =  Logical.OR)
    @RequiresPermissions(value = ["view"])
    @GetMapping("/contacts")
    fun getContacts(): JsonResponse {

        val email: String = SecurityUtils.getSubject().principal as String

        val result = workerService.getContacts(email)

        return JsonResponse.ok().message( result )
    }

    // 获取用户信息
    @RequiresRoles(value = ["worker", "manager"], logical =  Logical.OR)
    @RequiresPermissions(value = ["view"])
    @GetMapping("/info/{id}")
    fun getUserInfo(@PathVariable("id") id: Int): JsonResponse {

        val result = workerService.getWorker(id)

        return JsonResponse.ok().message( result )
    }

    // 修改用户信息
    @RequiresRoles(value = ["worker", "manager"], logical =  Logical.OR)
    @RequiresPermissions(value = ["view"])
    @PutMapping("/info")
    fun updateUserInfo(@RequestParam("uid") uid: Int,
                       @RequestParam("name") name: String,
                       @RequestParam("gender") gender: String,
                       @RequestParam(value = "portrait") portrait: MultipartFile?): JsonResponse {
        if (log.isInfoEnabled) {
            log.info("portrait$portrait")
        }

        val result = workerService.updateWorkerInfo(uid, name, gender, portrait)

        return if (result) {
            JsonResponse.ok().message( "修改 $name 的信息成功." )
        } else {
            JsonResponse.internalServerError().message( "修改 $name 的信息失败，请稍后重试." )
        }
    }
}