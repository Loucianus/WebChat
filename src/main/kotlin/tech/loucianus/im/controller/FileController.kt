package tech.loucianus.im.controller

import com.github.pagehelper.PageHelper
import com.github.pagehelper.PageInfo
import org.apache.shiro.SecurityUtils
import org.apache.shiro.authz.annotation.Logical
import org.apache.shiro.authz.annotation.RequiresPermissions
import org.apache.shiro.authz.annotation.RequiresRoles
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Lazy
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import tech.loucianus.im.model.JsonResponse
import tech.loucianus.im.model.vo.FileList
import tech.loucianus.im.service.FileService
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@RestController
@RequestMapping("/file")
class FileController {

    @Autowired @Lazy lateinit var fileService: FileService

    // 搜索文件
    @RequiresRoles(value = ["worker", "manager"],logical =  Logical.OR)
    @RequiresPermissions(value = ["view"])
    @GetMapping("/all/{uid}")
    fun getFiles(@RequestParam(defaultValue = "1") pageNo: Int,
                 @RequestParam(defaultValue = "10") pageSize: Int,
                 @RequestParam(value = "filename", defaultValue = "") filename: String,
                 @PathVariable("uid") uid: Int): JsonResponse {
        PageHelper.startPage<FileList>(pageNo, pageSize)

        val pageInfo = PageInfo<FileList>(fileService.searchFile(filename, uid))

        return JsonResponse.ok().message(pageInfo)
    }

    // 上传文件
    @RequiresRoles(value = ["worker", "manager"],logical =  Logical.OR)
    @RequiresPermissions(value = ["upload", "view"], logical = Logical.AND)
    @PostMapping
    fun upload(@RequestParam("file") file: MultipartFile?,
                   @RequestParam("uploader_id") uid: Int,
                   @RequestParam("to_id") toId: Int): JsonResponse {

        if (file == null) {
            return JsonResponse.methodNotAllowed().message("上传的文件不存在!!!")
        }

        return JsonResponse.ok().message(fileService.upload(file, uid, toId))
    }

    // 下载文件
    @RequiresRoles(value = ["worker", "manager"],logical =  Logical.OR)
    @RequiresPermissions(value = ["download", "view"])
    @GetMapping
    fun download(
        request: HttpServletRequest,
        response: HttpServletResponse,
        @RequestParam("file_id") fileId: Int) {

        fileService.download(request, response, fileId)
    }

    // 删除文件
    @RequiresRoles(value = ["worker", "manager"],logical =  Logical.OR)
    @RequiresPermissions(value = ["view", "delete"], logical = Logical.OR)
    @DeleteMapping
    fun delete(@RequestParam("file_id") id: Int): JsonResponse {

        val subject = SecurityUtils.getSubject()

        return if (subject.hasRole("manager") && subject.isPermitted("delete") ) {
            if (fileService.deleteFileById(id)) {
                JsonResponse.noContent().message("Success to delete.")
            } else {
                JsonResponse.notFound().message("File does not exist.")
            }
        } else if (subject.hasRole("worker") && subject.isPermitted("view")) {
            if (fileService.deleteFileById(id)) {
                JsonResponse.noContent().message("Success to delete.")
            } else {
                JsonResponse.notFound().message("File does not exist.")
            }
        } else {
            JsonResponse.notFound().message("Has no permission.")
        }

    }
}