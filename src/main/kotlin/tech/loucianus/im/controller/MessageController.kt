package tech.loucianus.im.controller

import org.apache.shiro.authz.annotation.Logical
import org.apache.shiro.authz.annotation.RequiresPermissions
import org.apache.shiro.authz.annotation.RequiresRoles
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import tech.loucianus.im.model.JsonResponse
import tech.loucianus.im.service.MessageService

@RestController
class MessageController {

    @Autowired lateinit var messageService: MessageService

    /**
     * Get history message.
     *
     * @param id The chat partner's id at database.
     * @param uid My id at database.
     * @return The messages list
     */
    @RequiresRoles(value = ["worker", "manager"],logical =  Logical.OR)
    @RequiresPermissions(value = ["view"])
    @GetMapping("/message/history")
    fun getAllMessage(@RequestParam("id")id: Int,
                      @RequestParam("uid") uid: Int): JsonResponse {

        val result =  if (id != 0) {

            messageService.getHistoryMessage(id, uid)

        } else {

            messageService.getGroupHistoryMessage()
        }

        return JsonResponse.ok().message( result )
    }

}