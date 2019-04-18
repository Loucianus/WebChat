package tech.loucianus.im.model.vo

import java.io.Serializable
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

/**
 * UserEntity Dto
 */
data class Account (

    /**
     * The email of account
     * Not Blank; Must be email style
     */
    @field: NotBlank(message = "{username.required}")
    @field: Email(message = "{username.invalid}")
    val username: String,

    /**
     * The password of account
     * Not Blank; Size <= 20 & >= 8
     */
    @field: NotBlank(message = "{password.required}")
    @field: Size(max = 20 ,min = 8, message = "{password.invalid}")
    val password: String

): Serializable