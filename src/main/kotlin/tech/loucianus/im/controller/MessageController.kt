package tech.loucianus.im.controller

import org.apache.commons.logging.LogFactory
import org.apache.shiro.authz.annotation.Logical
import org.apache.shiro.authz.annotation.RequiresPermissions
import org.apache.shiro.authz.annotation.RequiresRoles
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import tech.loucianus.im.model.JsonResponse
import tech.loucianus.im.service.MessageService

@RestController
@RequestMapping("/message")
class MessageController {
    companion object {
        private val log = LogFactory.getLog(this::class.java)
    }


    @Autowired lateinit var messageService: MessageService

    @RequiresRoles(value = ["worker", "manager"],logical =  Logical.OR)
    @RequiresPermissions(value = ["view"])
    @GetMapping("/history")
    fun getHistoryMessages(@RequestParam("id")id: Int,
                      @RequestParam("uid") uid: Int): JsonResponse {

        val result =  if (id != 0) {

            messageService.getHistoryMessage(id, uid)

        } else {

            messageService.getGroupHistoryMessage()
        }

        return JsonResponse.ok().message( result )
    }

    // 获取最近的聊天信息
    @RequiresRoles(value = ["worker", "manager"],logical =  Logical.OR)
    @GetMapping("/recent")
    fun recentMessages(@RequestParam("id")id: Int,@RequestParam("uid") uid: Int): JsonResponse {

        val list =
                if (id != 0) {
                    messageService.getChatFile(id, uid)
                } else {
                    messageService.getGroupChatFile()
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