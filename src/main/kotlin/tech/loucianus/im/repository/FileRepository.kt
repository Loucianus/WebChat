package tech.loucianus.im.repository

import com.github.pagehelper.Page
import org.apache.ibatis.annotations.Delete
import org.apache.ibatis.annotations.Insert
import org.apache.ibatis.annotations.Param
import org.apache.ibatis.annotations.Select
import org.springframework.stereotype.Repository
import tech.loucianus.im.model.vo.FileList
import tech.loucianus.im.model.po.File

@Repository
interface FileRepository {

    @Select("select * from file where id=#{id}")
    fun findFile(@Param("id") id: Int): File

    @Select("select filepath from file where id=#{id}")
    fun findFilePathById(@Param("id") id: Int): String

    @Select("select * from file")
    fun findFiles(): Page<File>

    @Select("select * from file_list")
    fun findFileList(): Page<FileList>

    @Select("select * from file where filename like CONCAT('%',#{filename},'%')")
    fun findFileLikeFilename(@Param("filename") filename: String): Page<File>

    @Select("select filename from file where filepath=#{filepath}")
    fun findFileByPath(@Param("filepath") filepath: String): String

    @Insert("insert into file (id, filename, filepath, upload_date, " +
            "download_times, upload_worker) " +
            "values (#{id}, #{filename}, #{filepath}, " +
            "#{uploadDate}, #{downloadTimes}, #{uploadWorker})")
    fun saveFile(file: File): Int

    @Delete("delete from file where id=#{id}")
    fun deleteFileById(@Param("id") id: Int): Int
}