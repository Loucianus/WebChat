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
    fun findFileById(@Param("id") id: Int): File

    @Select("select filepath from file where id=#{id}")
    fun findFilePathById(@Param("id") id: Int): String

    @Select("select * from file_list where filename like CONCAT('%',#{filename},'%') and ( to_id=#{uid} or to_id=0 or upload_worker=#{uid} )")
    fun findFileLikeFilename(@Param("filename") filename: String,@Param("uid") uid: Int): Page<FileList>

    @Select("select * from file where filepath=#{filepath}")
    fun findFileByFullPath(@Param("filepath") filepath: String): File

    @Insert("insert into file (id, filename, filepath, upload_date, " +
            "download_times, upload_worker, to_id) " +
            "values (#{id}, #{filename}, #{filepath}, " +
            "#{uploadDate}, #{downloadTimes}, #{uploadWorker}, #{toId})")
    fun saveFile(file: File): Int

    @Delete("delete from file where id=#{id}")
    fun deleteFileById(@Param("id") id: Int): Int
}