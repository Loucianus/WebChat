package tech.loucianus.im.service

import com.github.pagehelper.Page
import org.springframework.web.multipart.MultipartFile
import tech.loucianus.im.model.vo.FileList
import tech.loucianus.im.model.po.File
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

interface FileService {

    /**
     * Get file list, each page has the 10 file details to show.
     *
     * @return Return the file list. Every time to get the list lower than 10.
     */
    fun getFileList(): Page<FileList>

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
    fun searchFile(filename: String): Page<File>

    /**
     * Get the [File] where file path = [path]
     *
     * @param path The file path.
     * @return The string of file path.
     */
    fun getFIleByPath(path: String): String

    /**
     * Save the file details to database.
     */
    fun setFile(file: File)

    /**
     * Upload the file.
     *
     * To upload file to server and use method [setFile] to save message to database.
     * @return If save file succeed, return true; otherwise false or exp.
     */
    fun upload(file: MultipartFile, uid: Int): Boolean

    /**
     * Download file.
     */
    fun download(request: HttpServletRequest, response: HttpServletResponse, absoluteName: String )

}