package tech.loucianus.im.controller

import org.apache.shiro.authz.annotation.Logical
import org.apache.shiro.authz.annotation.RequiresPermissions
import org.apache.shiro.authz.annotation.RequiresRoles
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Lazy
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import tech.loucianus.im.model.JsonResponse
import tech.loucianus.im.model.vo.BulletinDetails
import tech.loucianus.im.service.BulletinService

@RestController
@RequestMapping("/bulletin")
class BulletinController {

    @Autowired @Lazy lateinit var bulletinService: BulletinService

    // 获取公告
    @RequiresRoles(value = ["worker", "manager"],logical =  Logical.OR)
    @RequiresPermissions(value = ["view"])
    @GetMapping
    fun getBulletin(): JsonResponse  {
        val result = bulletinService.getBulletin()
        return JsonResponse.ok().message( result )
    }

    // 更新公告
    @RequiresRoles(value = ["manager"],logical =  Logical.AND)
    @RequiresPermissions(value = ["view","edict"], logical = Logical.AND)
    @PostMapping
    fun setBulletin(@RequestBody @Validated bulletinDetails: BulletinDetails): JsonResponse {

        val result = bulletinService.setBulletin( bulletinDetails )

        return JsonResponse.ok().message( result )

    }
}