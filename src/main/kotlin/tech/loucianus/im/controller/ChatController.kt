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

    /**
     * Send message to a body.
     *
     * @param message The message details, See data class [MessageGetter].
     * @see ChatService.sentToUser The service of sending the message to a body.
     */
    @MessageMapping("/private")
    fun chat(@RequestBody @Validated message: MessageGetter): JsonResponse{

        chatService.sentToUser(message)

        return JsonResponse.ok().message("Send!")
    }

    /**
     * Send group message
     *
     * @param message the message details, See [MessageGetter]
     * @see ChatService
     */
    @MessageMapping("/group")
    @SendTo("/topic/group")
    fun chatWithGroup(@RequestBody @Validated message: MessageGetter): JsonResponse{

        chatService.sentToGroup(message)

        return JsonResponse.ok().message("Send!")
    }

    /**
     * Get the last 10 messages.
     *
     * @param id The chat partner's id of the chatting.
     * @param uid My id of the chatting.
     * @return The message list of the last 10 messages.
     */
    @RequiresRoles(value = ["worker", "manager"],logical =  Logical.OR)
    @GetMapping("/chatfile")
    fun chatFile(@RequestParam("id")id: Int,@RequestParam("uid") uid: Int): JsonResponse {

        val list =
                if (id != 0) {
                    chatService.getChatFile(id, uid)
                } else {
                    chatService.getGroupChatFile()
                }

        return JsonResponse.ok().message(list)
    }
}