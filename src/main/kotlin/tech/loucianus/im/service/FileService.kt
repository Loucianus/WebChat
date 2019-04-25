package tech.loucianus.im.service

import com.github.pagehelper.Page
import org.springframework.web.multipart.MultipartFile
import tech.loucianus.im.model.vo.FileList
import tech.loucianus.im.model.po.File
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

interface FileService {

    /**
     * Delete the file where file id = [id]
     *
     * @param id The file db pk.
     * @return If succeed to delete, return true; otherwise false.
     */
    fun deleteFileById(id: Int): Boolean

    /**
     * Search file where file name like [filename]
     *
     * @param filename The filename at database.
     * @return Get the file list.
     */
    fun searchFile(filename: String, uid: Int): Page<FileList>

    /**
     * Upload the file.
     *
     * To upload file to server and to save message to database.
     * @return If save file succeed, return true; otherwise false or exp.
     */
    fun upload(file: MultipartFile, uid: Int, toId: Int): File

    /**
     * Download file.
     */
    fun download(request: HttpServletRequest, response: HttpServletResponse, fileId: Int )

}