package tech.loucianus.im.service

import com.github.pagehelper.Page
import org.springframework.web.multipart.MultipartFile
import tech.loucianus.im.model.vo.FileList
import tech.loucianus.im.model.po.File
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

interface FileService {

    fun deleteFileById(id: Int): Boolean

    fun searchFile(filename: String, uid: Int): Page<FileList>

    fun upload(file: MultipartFile, uid: Int, toId: Int): File

    fun download(request: HttpServletRequest, response: HttpServletResponse, fileId: Int )

}