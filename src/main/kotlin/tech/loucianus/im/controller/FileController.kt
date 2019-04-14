package tech.loucianus.im.controller

import com.github.pagehelper.PageHelper
import com.github.pagehelper.PageInfo
import org.apache.commons.logging.LogFactory
import org.apache.shiro.authz.annotation.Logical
import org.apache.shiro.authz.annotation.RequiresRoles
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Lazy
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import tech.loucianus.im.model.JsonResponse
import tech.loucianus.im.model.entity.File
import tech.loucianus.im.service.FileService
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse



@RestController
@RequestMapping("/file")
class FileController {

    companion object {
        private val log = LogFactory.getLog(this::class.java)
    }

    @Autowired @Lazy lateinit var fileService: FileService

    /**
     * 分页查询 全部
     */
    @RequiresRoles(value = ["worker", "manager"],logical =  Logical.OR)
    @GetMapping("/all")
    fun getFiles(@RequestParam(defaultValue = "1") pageNo: Int,
                 @RequestParam(defaultValue = "10") pageSize: Int): JsonResponse {
        PageHelper.startPage<File>(pageNo, pageSize)
        val pageInfo = PageInfo<File>(fileService.getFiles())
        return JsonResponse.ok().message(pageInfo)
    }

    /**
     * 分页查询 搜索
     */
    @RequiresRoles(value = ["worker", "manager"],logical =  Logical.OR)
    @GetMapping("/all/{filename}")
    fun searchFile(@RequestParam(defaultValue = "1") pageNo: Int,
                   @RequestParam(defaultValue = "10") pageSize: Int,
                   @PathVariable("filename") filename: String): JsonResponse {
        PageHelper.startPage<File>(pageNo, pageSize)
        val pageInfo = PageInfo<File>(fileService.searchFile(filename))
        return JsonResponse.ok().message(pageInfo)
    }

    /**
     * 上传文件
     */
    @RequiresRoles(value = ["worker", "manager"],logical =  Logical.OR)
    @PostMapping("/{uid}")
    fun uploadFile(@RequestParam("file") file: MultipartFile,@PathVariable("uid") uid: Int): JsonResponse {
        if (log.isInfoEnabled) log.info("upload file")

        return JsonResponse.ok().message(fileService.upload(file, uid))
    }

    /**
     * 下载文件
     */
    @RequiresRoles(value = ["worker", "manager"],logical =  Logical.OR)
    @GetMapping
    fun download(
        request: HttpServletRequest,
        response: HttpServletResponse,
        @RequestParam("fullPath") fullPath: String) {
        fileService.download(request, response, fullPath)

    }
}