package tech.loucianus.im.controller

import com.github.pagehelper.PageHelper
import com.github.pagehelper.PageInfo
import org.apache.commons.logging.LogFactory
import org.apache.shiro.authz.annotation.Logical
import org.apache.shiro.authz.annotation.RequiresPermissions
import org.apache.shiro.authz.annotation.RequiresRoles
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import tech.loucianus.im.model.JsonResponse
import tech.loucianus.im.model.po.Message
import tech.loucianus.im.service.MessageService

@RestController
@RequestMapping("/message")
class MessageController {
    companion object {
        private val log = LogFactory.getLog(this::class.java)
    }


    @Autowired lateinit var messageService: MessageService

    // 获取历史记录
    @RequiresRoles(value = ["worker", "manager"],logical =  Logical.OR)
    @RequiresPermissions(value = ["view"])
    @GetMapping("/history")
    fun getHistoryMessages(
            @RequestParam(defaultValue = "1") pageNo: Int,
            @RequestParam(defaultValue = "30") pageSize: Int,
            @RequestParam("id") id: Int,
            @RequestParam("uid") uid: Int,
            @RequestParam(value = "msg", defaultValue = "") msg: String): JsonResponse {
        PageHelper.startPage<Message>(pageNo, pageSize)

        val result =  if (id != 0) {

            PageInfo<Message>(messageService.getHistoryMessage(id, uid, msg))
        } else {

            PageInfo<Message>(messageService.getGroupHistoryMessage(msg))
        }

        return JsonResponse.ok().message( result )
    }

    // 获取最近的聊天信息
    @RequiresRoles(value = ["worker", "manager"],logical =  Logical.OR)
    @GetMapping("/recent")
    fun recentMessages(@RequestParam("id")id: Int,@RequestParam("uid") uid: Int): JsonResponse {

        val list =
                if (id != 0) {
                    messageService.getRecentFile(id, uid)
                } else {
                    messageService.getGroupRecentFile()
                }

        return JsonResponse.ok().message(list)
    }

    @RequiresRoles(value = ["worker", "manager"],logical =  Logical.OR)
    @GetMapping
    fun actMessage(@RequestParam("id")id: Int,@RequestParam("uid") uid: Int): JsonResponse {
        if (log.isInfoEnabled) log.info("uid:$uid--id:$id")
        return JsonResponse.ok().message(messageService.actMessage(uid, id))
    }
}