package tech.loucianus.im.util

import org.apache.commons.logging.LogFactory
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.nio.charset.Charset
import java.io.IOException



object FileUtil {

    private val log = LogFactory.getLog(this::class.java)
    /**
     * Get File text
     *
     * @param filename
     * @return The string of text[filename]
     */
    @Throws(IOException::class)
    fun getFileContent(filename: String): String{
        return File(filename).readText(Charset.forName("UTF-8"))
    }

    /**
     * Add text into [filename].txt
     *
     * @param filename
     * @param text the content of text that into [filename].txt
     */
    @Throws(IOException::class)
    fun appendFile(filename: String, text: String) {
        val f = File(filename)
        if (!f.exists()) {
            f.createNewFile()
        }
        f.appendText(text, Charset.defaultCharset())
    }

    /**
     * Write text into [filename].txt
     *
     * @param filename
     * @param text the content of text that into [filename].txt
     */
    @Throws(IOException::class)
    fun writeFile(filename: String, text: String) {
        val f = File(filename)
        if (!f.exists()) {
            f.createNewFile()
        }
        f.writeText(text, Charset.defaultCharset())
    }

    @Throws(IOException::class)
    fun upload(file: MultipartFile, path: String, fileName: String): Boolean {


        val realPath = "$path/$fileName"
        log.info(realPath)

        val dest = File(realPath)

        if(!dest.parentFile.exists()){
            dest.parentFile.mkdir()
        }

        return try {
            file.transferTo(dest)
            true
        } catch (e: IOException) {
            throw IOException("文件不存在!!")
        }
    }

    fun getSuffix(fileName: String): String {

        return fileName.substring(fileName.lastIndexOf("."),fileName.length)
    }

    fun getNewFileName(originalFilename: String): String {
        val filename = Utils.getNowTimestampStr()
        val fileSuffix = FileUtil.getSuffix(originalFilename)
        return "$filename.$fileSuffix"
    }
}