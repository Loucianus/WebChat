package tech.loucianus.im.service

import com.github.pagehelper.Page
import org.springframework.web.multipart.MultipartFile
import tech.loucianus.im.model.vo.FileList
import tech.loucianus.im.model.po.File
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

interface FileService {

    fun getFiles(): Page<File>

    fun getFileList(): Page<FileList>

    fun deleteFileById(id: Int): Boolean

    fun searchFile(filename: String): Page<File>

    fun getFIleByPath(path: String): String

    fun setFile(file: File)

    fun upload(file: MultipartFile, uid: Int): String

    fun download(request: HttpServletRequest, response: HttpServletResponse, absoluteName: String )

}