package tech.loucianus.im.service.impl

import org.apache.commons.logging.LogFactory
import org.jasypt.encryption.StringEncryptor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import tech.loucianus.im.model.vo.Contacts
import tech.loucianus.im.model.dao.Permission
import tech.loucianus.im.model.dao.WorkerStorer
import tech.loucianus.im.model.dao.WorkerUpdater
import tech.loucianus.im.model.vo.*
import tech.loucianus.im.model.po.Worker
import tech.loucianus.im.repository.WorkerRepository
import tech.loucianus.im.service.WorkerService

@Service
class WorkerServiceImpl: WorkerService {

    companion object {
        private val log = LogFactory.getLog(this::class.java)
    }

    @Autowired lateinit var workerRepository: WorkerRepository
    @Autowired lateinit var cipher: StringEncryptor

    @Cacheable(cacheNames = ["users"], key = "#email")
    override fun getWorker(email: String): Worker {
        return workerRepository.findWorkerByEmail(email)
    }

    @Cacheable(cacheNames = ["users"], key = "#id")
    override fun getWorker(id: Int): Worker {
        return workerRepository.findWorkerById(id)
    }

    override fun getWorkersById(ids: List<Any>): List<Worker> {
        val idList = mutableListOf<Int>()
        ids.forEach{
            idList.add(it as Int)
        }
        return workerRepository.findWorkersById(idList)
    }

    @Cacheable(cacheNames = ["password"], key = "#email")
    override fun getPassword(email: String): String? {
        return workerRepository.findPasswordByEmail(email)?.let { password ->
            if (log.isInfoEnabled) log.info("get password:: password->$password")
            return cipher.decrypt(password)
        }
    }

    override fun getId(email: String): Int {
        return workerRepository.findIdByEmail(email)
    }

    override fun setWorker(workerStorer: WorkerStorer): Boolean {
        return if (workerStorer.permission == "") {
            workerRepository.saveWorker(
                WorkerStorer(
                    email = workerStorer.email,
                    name = workerStorer.name,
                    role = workerStorer.role,
                    gender = workerStorer.gender)
            ) == 1
        } else {
            workerRepository.saveWorker(workerStorer) == 1
        }

    }

    override fun setWorkers(workerStorerList: List<WorkerStorer>): Boolean {
        return workerRepository.saveWorkers(workerStorerList) == workerStorerList.size
    }

    override fun updateWorkerByAdmin(workerUpdater: WorkerUpdater): Boolean {
        if (getWorker(workerUpdater.id).permission == "view#edict#download#upload#update#delete")
            return false

        return if (workerUpdater.permission == "") {
            workerRepository.updateWorkerByAdmin(
                    WorkerUpdater(id = workerUpdater.id,
                            status = workerUpdater.status,
                            role = workerUpdater.role)
            ) == 1
        } else {
            workerRepository.updateWorkerByAdmin(workerUpdater) == 1
        }
    }

    override fun verify(account: Account): Boolean {
        getPassword(account.username)?.let {
            return account.password == it
        }
        return false
    }
    
    override fun verify(username: String, password: String): Boolean {
        getPassword(username)?.let {
            if (log.isInfoEnabled) log.info("verify:: it->$it; password->$password")
            return password == it
        }
        return false
    }

    @Cacheable( cacheNames = ["Contacts"], key = "#uid")
    override fun getContacts(uid: Int): List<Contacts> {
        val contacts = workerRepository.findContactsById(uid) as MutableList
        contacts.add(
            index = 0,
            element = Contacts(
                id = 0,
                name = "Group Message.",
                portrait = "static/img/portrait/group.jpg",
                email = "citrine@loucianus.tech",
                role = "Group Message."
            )
        )

        return contacts
    }

    override fun getContacts(email: String): List<Contacts> {
        val contacts = workerRepository.findContactsByEmail(email) as MutableList
        contacts.add(
            index = 0,
            element = Contacts(
                id = 0,
                name = "Group Message.",
                portrait = "static/img/portrait/group.jpg",
                email = "citrine@loucianus.tech",
                role = "Group Message."
            )
        )

        return contacts
    }

    @Cacheable(cacheNames = ["authority"], key = "#email")
    override fun getAuthority(email: String): Permission {
        return workerRepository.getAuthority(email)
    }
}