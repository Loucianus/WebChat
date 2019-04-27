package tech.loucianus.im.service

import org.springframework.web.multipart.MultipartFile
import tech.loucianus.im.model.vo.Contacts
import tech.loucianus.im.model.dao.Permission
import tech.loucianus.im.model.dao.WorkerStorer
import tech.loucianus.im.model.dao.WorkerUpdater
import tech.loucianus.im.model.po.Worker

interface WorkerService {

    fun getWorker(email: String): Worker

    fun getWorker(id: Int): Worker

    fun getPassword(email: String): String?

    fun updateWorkerPermission(workerUpdater: WorkerUpdater): Boolean

    fun updateWorkerInfo( uid: Int,
                         name: String,
                         gender: String,
                         portrait: MultipartFile?): Boolean

    fun setWorker(workerStorer: WorkerStorer): Boolean

    fun verify(username: String, password: String): Boolean

    fun getContacts(email: String): List<Contacts>

    fun getAuthority(email: String): Permission
}