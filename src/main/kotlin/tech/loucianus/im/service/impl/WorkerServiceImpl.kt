package tech.loucianus.im.service.impl

import org.apache.commons.logging.LogFactory
import org.jasypt.encryption.StringEncryptor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import tech.loucianus.im.model.dto.Account
import tech.loucianus.im.model.dto.Contact
import tech.loucianus.im.model.dto.Contacts
import tech.loucianus.im.model.dto.Permission
import tech.loucianus.im.model.entity.Worker
import tech.loucianus.im.repository.MessageRepository
import tech.loucianus.im.repository.WorkerRepository
import tech.loucianus.im.service.WorkerService

@Service
class WorkerServiceImpl: WorkerService {

    companion object {
        private val log = LogFactory.getLog(this::class.java)
    }

    @Autowired lateinit var messageRepository: MessageRepository
    @Autowired lateinit var workerRepository: WorkerRepository
    @Autowired lateinit var cipher: StringEncryptor

    @Cacheable(cacheNames = ["users"], key = "#email")
    override fun getWorker(email: String): Worker
            = workerRepository.findWorkerByEmail(email)

    @Cacheable(cacheNames = ["users"], key = "#id")
    override fun getWorker(id: Int): Worker
            = workerRepository.findWorkerById(id)

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

    override fun getContacts(uid: Int): List<Contact> {
        return  workerRepository.findContactsById(uid)
    }

    override fun getContacts(email: String): List<Contact> {
        return  workerRepository.findContactsByEmail(email)
    }

    override fun getCurrentMessageAndContactList(uid: Int): List<Contacts> {
        val messageList = mutableListOf<Contacts>()
        getContacts(uid).forEach{
            val message = messageRepository.findCurrentMessage(uid, it.id)
            if (message == null) {
                messageList.add(
                    Contacts(
                        id = it.id,
                        name = it.name,
                        portrait = it.portrait,
                        email = it.email,
                        role = it.role
                    )
                )
            } else {
                messageList.add(
                    Contacts(
                        id = it.id,
                        name = it.name,
                        portrait = it.portrait,
                        content = message.content,
                        type = message.type,
                        email = it.email,
                        date = message.date.toString(),
                        role = it.role
                    )
                )
            }
        }
        return messageList
    }

    override fun getCurrentMessageAndContactList(email: String): List<Contacts> {
        val messageList = mutableListOf<Contacts>()
        val worker = getWorker(email)
        getContacts(worker.id).forEach{
            val message = messageRepository.findCurrentMessage(worker.id, it.id)
            if (message == null) {
                messageList.add(
                    Contacts(
                        id = it.id,
                        name = it.name,
                        portrait = it.portrait,
                        email = it.email,
                        role = it.role
                    )
                )
            } else {
                messageList.add(
                    Contacts(
                        id = it.id,
                        name = it.name,
                        portrait = it.portrait,
                        content = message.content,
                        type = message.type,
                        date = message.date.toString(),
                        email = it.email,
                        role = it.role
                    )
                )
            }
        }
        return messageList
    }


    @Cacheable(cacheNames = ["authority"], key = "#email")
    override fun getAuthority(email: String): Permission {
        return workerRepository.getAuthority(email)
    }
}