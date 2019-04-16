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
import tech.loucianus.im.model.dto.MessageGetter
import tech.loucianus.im.service.ChatService
import tech.loucianus.im.websocket.WebSocketConfig

@RestController
class ChatController {

    companion object {
        private val log = LogFactory.getLog(this::class.java)
    }

    @Autowired private lateinit var chatService: ChatService
    @Autowired private lateinit var webSocketConfig: WebSocketConfig
    /**
     * 通讯
     *
     * @param message
     */
    @MessageMapping("/private")
    fun chat(@RequestBody @Validated message: MessageGetter){

        if (log.isInfoEnabled) log.info("webSocketConfig::${webSocketConfig.users}")

        chatService.sentToUser(message)
    }

    @MessageMapping("/group")
    @SendTo("/topic/greetings")
    fun chatWithGroup(@RequestBody @Validated message: MessageGetter): JsonResponse{

        if (log.isInfoEnabled) log.info("webSocketConfig::${webSocketConfig.users}")

        return JsonResponse.ok().message(chatService.sentToGroup(message))
    }

    @RequiresRoles(value = ["worker", "manager"],logical =  Logical.OR)
    @GetMapping("/chatfile")
    fun chatFile(@RequestParam("id")id: Int,@RequestParam("uid") uid: Int): JsonResponse {
        if (log.isInfoEnabled)
            log.info("chat:$uid-$id")

        val list =
            if (id != 0) {
                chatService.getChatFile(id, uid)
            } else {
                chatService.getGroupChatFile()
            }

        return JsonResponse.ok().message(list)
    }

    @RequiresRoles(value = ["user", "admin"],logical =  Logical.OR)
    @GetMapping("/undefined")
    fun undefined() {

    }

}