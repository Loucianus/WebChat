package tech.loucianus.im.controller

import org.apache.commons.logging.LogFactory
import org.apache.shiro.SecurityUtils
import org.apache.shiro.authz.annotation.Logical
import org.apache.shiro.authz.annotation.RequiresPermissions
import org.apache.shiro.authz.annotation.RequiresRoles
import org.apache.shiro.subject.PrincipalCollection
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import tech.loucianus.im.model.JsonResponse
import tech.loucianus.im.service.WorkerService

@RestController
@RequestMapping()
class ContactController {

    companion object {
        private val log = LogFactory.getLog(this::class.java)
    }

    @Autowired
    lateinit var workerService: WorkerService

    /**
     * 获取全部联系人
     *
     * @return 返回所有联系人
     */
    @RequiresRoles(value = ["worker", "manager"],logical =  Logical.OR)
    @RequiresPermissions(value = ["view"])
    @GetMapping("/contacts/{id}")
    fun getAllContacts(@PathVariable("id") id: Int): JsonResponse {
        val contacts = workerService.getCurrentMessageAndContactList(id)
        if (log.isInfoEnabled) log.info("Get all contacts.")
        return JsonResponse.ok().message(contacts)
    }

    /**
     * 获取个人信息
     *
     * @param id 用户id
     */
    @RequiresRoles(value = ["worker", "manager"],logical =  Logical.OR)
    @RequiresPermissions(value = ["view"])
    @GetMapping("/info/{id}")
    fun getOther(@PathVariable("id") id: Int): JsonResponse {
        val userInfo = workerService.getWorker(id)
        return JsonResponse.ok().message(userInfo)
    }
}