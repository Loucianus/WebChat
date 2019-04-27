package tech.loucianus.im.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RestController
import org.apache.commons.logging.LogFactory
import org.apache.shiro.authz.annotation.Logical
import org.apache.shiro.authz.annotation.RequiresRoles
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import tech.loucianus.im.model.JsonResponse
import tech.loucianus.im.model.vo.MessageGetter
import tech.loucianus.im.service.ChatService
import tech.loucianus.im.websocket.WebSocketConfig

@RestController
class ChatController {

    @Autowired private lateinit var chatService: ChatService

    // 私聊
    @MessageMapping("/private")
    fun chat(@RequestBody @Validated message: MessageGetter) {

        chatService.sentToUser(message)

    }

    // 群聊
    @MessageMapping("/group")
    @SendTo("/topic/group")
    fun chatWithGroup(@RequestBody @Validated message: MessageGetter): JsonResponse {

        val result = chatService.sentToGroup(message)

        return JsonResponse.ok().message(result)
    }
}