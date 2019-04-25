package tech.loucianus.im.controller

import com.github.pagehelper.PageHelper
import com.github.pagehelper.PageInfo
import org.apache.commons.logging.LogFactory
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
import tech.loucianus.im.model.po.File
import tech.loucianus.im.service.FileService
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@RestController
@RequestMapping("/file")
class FileController {

    @Autowired @Lazy lateinit var fileService: FileService

    /**
     * Get the file list.
     *
     * @param pageNo The PageHelper needed.
     * @param pageSize The PageHelper needed.
     * @param filename The file name that user wanna to search, default value is empty string.
     * @param uid The uploader's id.
     * @return The file details list. Every page shows the max 10 record.
     */
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

    /**
     * Upload the file.
     *
     * @param file The File that user uploads.
     * @param uid Uploader.
     * @param toId The recipient.
     * @return If succeed to upload file and save to the database, return true; otherwise false.
     * @see FileService.upload If has any exception during uploading, throw the IO Exception.
     */
    @RequiresRoles(value = ["worker", "manager"],logical =  Logical.OR)
    @RequiresPermissions(value = ["upload", "view"], logical = Logical.AND)
    @PostMapping
    fun uploadFile(@RequestParam("file") file: MultipartFile,
                   @RequestParam("uploader_id") uid: Int,
                   @RequestParam("to_id") toId: Int): JsonResponse {

        return JsonResponse.ok().message(fileService.upload(file, uid, toId))
    }

    /**
     * Download files.
     *
     * @param request Network stream needed.
     * @param response Network stream needed.
     * @param fileId The file's id at database.
     */
    @RequiresRoles(value = ["worker", "manager"],logical =  Logical.OR)
    @RequiresPermissions(value = ["download", "view"])
    @GetMapping
    fun download(
        request: HttpServletRequest,
        response: HttpServletResponse,
        @RequestParam("file_id") fileId: Int) {

        fileService.download(request, response, fileId)
    }

    /**
     * Delete the file.
     *
     * Users can delete the file that their uploading.
     * The Root user can delete any file.
     *
     * @param id The file Id.
     * @see SecurityUtils.getSubject Get the subject of user.Use it to confirm the permission if can delete the file.
     */
    @RequiresRoles(value = ["worker", "manager"],logical =  Logical.OR)
    @RequiresPermissions(value = ["view", "delete"], logical = Logical.OR)
    @DeleteMapping
    fun deleteFile(@RequestParam("file_id") id: Int): JsonResponse {

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