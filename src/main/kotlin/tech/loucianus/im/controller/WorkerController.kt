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
@RequestMapping()
class WorkerController {

    @Autowired
    lateinit var workerService: WorkerService

    /**
     * Add a new user to database.
     *
     * @param workerStorer The details of user.
     * @return If succeed to save user into database, return true; otherwise return false.
     * @see WorkerService.setWorker
     */
    @RequiresRoles(value = ["manager"])
    @RequiresPermissions(value = ["update", "view"], logical = Logical.AND)
    @PostMapping("/worker")
    fun addOneWorker(@RequestBody @Validated workerStorer : WorkerStorer):JsonResponse {

        val result = workerService.setWorker(workerStorer)

        return JsonResponse.ok().message( result )
    }

    /**
     * Update the user's permission.
     *
     * @param workerUpdater The details of user.
     * @return If succeed to update the user, return true; otherwise false.
     * @see WorkerService.updateWorkerByAdmin
     */
    @RequiresRoles(value = ["manager"])
    @RequiresPermissions(value = ["update"])
    @PostMapping("/worker/permission")
    fun updatePermission(@RequestBody workerUpdater: WorkerUpdater):JsonResponse {

        val result = workerService.updateWorkerByAdmin(workerUpdater)

        return JsonResponse.ok().message( result )
    }

    /**
     * Get all contacts.
     *
     * @return All contacts.
     */
    @RequiresRoles(value = ["worker", "manager"],logical =  Logical.OR)
    @RequiresPermissions(value = ["view"])
    @GetMapping("/worker/contacts")
    fun getAllContacts(): JsonResponse {

        val email: String = SecurityUtils.getSubject().principal as String

        val result = workerService.getContacts(email)

        return JsonResponse.ok().message( result )
    }

    /**
     * Get the details of user.
     *
     * @param id The user id at database.
     * @return Return the user details.
     */
    @RequiresRoles(value = ["worker", "manager"], logical =  Logical.OR)
    @RequiresPermissions(value = ["view"])
    @GetMapping("/worker/info/{id}")
    fun getOther(@PathVariable("id") id: Int): JsonResponse? {

        val result = workerService.getWorker(id)

        return JsonResponse.ok().message( result )
    }
}