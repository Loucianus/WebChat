package tech.loucianus.im.controller

import org.apache.commons.logging.LogFactory
import org.apache.shiro.authz.annotation.Logical
import org.apache.shiro.authz.annotation.RequiresPermissions
import org.apache.shiro.authz.annotation.RequiresRoles
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Lazy
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import tech.loucianus.im.model.JsonResponse
import tech.loucianus.im.model.dto.BulletinDetails
import tech.loucianus.im.service.BulletinService

@RestController
@RequestMapping("/bulletin")
class BulletinController {

    companion object {
        private val log = LogFactory.getLog(this::class.java)
    }

    @Autowired @Lazy lateinit var bulletinService: BulletinService

    /**
     * Get Bulletin
     *
     * @return Get the last bulletin.
     */
    @RequiresRoles(value = ["worker", "manager"],logical =  Logical.OR)
    @RequiresPermissions(value = ["view"])
    @GetMapping
    fun getBulletin(): JsonResponse {
        val bulletin = bulletinService.getBulletin()

        if (log.isInfoEnabled) log.info("bulletin::$bulletin")

        return JsonResponse.ok().message(bulletin)
    }

    /**
     * Save the last Bulletin
     *
     * @param bulletinDetails the bulletin
     * @return the result
     */
    @RequiresRoles(value = ["manager"],logical =  Logical.AND)
    @RequiresPermissions(value = ["edict"])
    @PostMapping
    fun setBulletin( @RequestBody @Validated bulletinDetails: BulletinDetails): JsonResponse {
        if (log.isInfoEnabled) log.info("bulletinDetails::$bulletinDetails")
        bulletinService.setBulletin(bulletinDetails)
        return JsonResponse.ok().message("Succeed to Save The Bulletin.")
    }
}