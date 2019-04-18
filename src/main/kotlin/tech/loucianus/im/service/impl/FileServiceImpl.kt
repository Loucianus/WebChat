package tech.loucianus.im.service.impl

import com.github.pagehelper.Page
import org.apache.commons.logging.LogFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import tech.loucianus.im.exception.CustomNotFoundException
import tech.loucianus.im.model.vo.FileList
import tech.loucianus.im.model.po.File
import tech.loucianus.im.repository.FileRepository
import tech.loucianus.im.service.FileService
import tech.loucianus.im.util.FileUtil
import java.io.IOException
import java.io.UnsupportedEncodingException
import java.nio.charset.Charset
import java.nio.file.Files
import java.nio.file.Paths
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Service
class FileServiceImpl: FileService {

    companion object {
        private val log = LogFactory.getLog(this::class.java)
        private const val localPath = "/home/citrine/files/"
    }

    @Autowired @Lazy lateinit var fileRepository: FileRepository


    override fun getFileList(): Page<FileList> {
        return fileRepository.findFileList()
    }

    override fun deleteFileById(id: Int): Boolean {
        val filepath = fileRepository.findFilePathById(id)
        val file = java.io.File(filepath)

        try {
            file.delete()
        } catch (ex: IOException) {
            throw IOException("Fail to delete the file.")
        }
        return fileRepository.deleteFileById(id) == 1
    }

    override fun searchFile(filename: String): Page<File> {
        return fileRepository.findFileLikeFilename(filename)
    }

    override fun getFIleByPath(path: String): String {
        return fileRepository.findFileByPath(path)
    }

    override fun setFile(file: File) {
        fileRepository.saveFile(file)
    }

    override fun upload(file: MultipartFile, uid: Int): Boolean {

        if (log.isInfoEnabled) log.info("filename::${file.originalFilename}")

        if (file.originalFilename == null) {
            throw CustomNotFoundException("File not exits!!")
        }

        val filename = FileUtil.getNewFileName(file.originalFilename!!)

        return if (FileUtil.upload(file, localPath, filename)) {
            setFile(
                File(
                    id = 0,
                    filename = file.originalFilename!!,
                    filepath = "$localPath$filename",
                    uploadWorker = uid
                )
            )
            true
        } else {
            false
        }
    }

    override fun download(request: HttpServletRequest, response: HttpServletResponse,absoluteName: String ) {
        //通过response输出流将文件传递到浏览器
        //1、获取文件路径
        //2.构建一个文件通过Paths工具类获取一个Path对象
        val path = Paths.get(absoluteName)
        //判断文件是否存在
        if (Files.exists(path)) {
            //存在则下载
            //通过response设定他的响应类型
            //4.获取文件的后缀名
            val fileSuffix = FileUtil.getSuffix(absoluteName)
            //            5.设置contentType ,只有指定contentType才能下载
            response.contentType = "application/$fileSuffix"
            //            6.添加http头信息
            //            因为fileName的编码格式是UTF-8 但是http头信息只识别 ISO8859-1 的编码格式
            //            因此要对fileName重新编码
            try {
                val downFileName = getFIleByPath(absoluteName)
                response.addHeader(
                    "Content-Disposition",
                    "attachment;filename=" + String(downFileName.toByteArray(charset("UTF-8")), Charset.forName("ISO8859-1"))
                )
            } catch (e: UnsupportedEncodingException) {
                throw UnsupportedEncodingException("下载失败")
            }
            //            7.使用  Path 和response输出流将文件输出到浏览器
            try {
                Files.copy(path, response.outputStream)
            } catch (e: IOException) {
                throw IOException("下载失败")
            }

            log.info("下载完成")
        }
    }
}