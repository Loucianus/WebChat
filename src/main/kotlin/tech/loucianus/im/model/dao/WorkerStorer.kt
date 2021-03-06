package tech.loucianus.im.model.dao

import java.io.Serializable
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank

data class WorkerStorer (

    /**
     * email
     */
    @field: NotBlank(message = "{email.required}")
    @field: Email(message = "{email.invalid}")
    val email: String,


    /**
     * name of worker
     */
    @field: NotBlank(message = "{name.required}")
    val name: String,

    /**
     * Gender
     */
    @field: NotBlank(message = "{gender.required}")
    val gender: String,

    /**
     * role: worker | manager
     */
    @field: NotBlank(message = "{role.required}")
    val role: String,

    /**
     * permission: view | edict | download | upload
     */
    val permission: String = "view#edict#download#upload",

    @field: NotBlank(message = "{idCard.required}")
    val idCard: String

) : Serializable