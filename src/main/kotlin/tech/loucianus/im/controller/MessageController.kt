package tech.loucianus.im.controller

import com.github.pagehelper.PageHelper
import com.github.pagehelper.PageInfo
import org.apache.commons.logging.LogFactory
import org.apache.shiro.authz.annotation.Logical
import org.apache.shiro.authz.annotation.RequiresRoles
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import tech.loucianus.im.model.JsonResponse
import tech.loucianus.im.model.entity.File
import tech.loucianus.im.model.entity.Message
import tech.loucianus.im.service.MessageService

@RestController
class MessageController {

    companion object {
        private val log = LogFactory.getLog(this::class.java)
    }

    @Autowired lateinit var messageService: MessageService

    @RequiresRoles(value = ["worker", "manager"],logical =  Logical.OR)
    @GetMapping("/message/history")
    fun getAllMessage(@RequestParam(defaultValue = "1") pageNo: Int,
                      @RequestParam(defaultValue = "30") pageSize: Int,
                      @RequestParam("id")id: Int,
                      @RequestParam("uid") uid: Int): JsonResponse {

        PageHelper.startPage<Message>(pageNo, pageSize)
        val pageInfo = PageInfo<Message>(messageService.getHistoryMessage(id, uid))

        if (log.isInfoEnabled) log.info("history message:<$id-$uid>::$pageInfo")

        return JsonResponse.ok().message(pageInfo)
    }


    @RequiresRoles(value = ["worker", "manager"],logical =  Logical.OR)
    @GetMapping("/message")
    fun searchMessage(@RequestParam(defaultValue = "1") pageNo: Int,
                      @RequestParam(defaultValue = "30") pageSize: Int,
                      @RequestParam("id")id: Int,
                      @RequestParam("uid") uid: Int,
                      @RequestParam("msg")msg: String): JsonResponse {

        PageHelper.startPage<Message>(pageNo, pageSize)
        val pageInfo = PageInfo<Message>(messageService.searchMessage(id, uid, msg))
        if (log.isInfoEnabled) log.info("search message:<$id-$uid>::$pageInfo")

        return JsonResponse.ok().message(pageInfo)
    }
}