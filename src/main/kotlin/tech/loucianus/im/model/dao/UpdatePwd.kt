package tech.loucianus.im.model.dao

import java.io.Serializable
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

data class UpdatePwd (

        @field: NotBlank(message = "{username.required}")
        @field: Email(message = "{username.invalid}")
        val email: String,

        @field: NotBlank(message = "{password.required}")
        @field: Size(max = 20 ,min = 8, message = "{password.invalid}")
        val password: String,

        @field: NotBlank(message = "{idCard.required}")
        @field: Size(max = 22 ,min = 22, message = "{idCard.invalid}")
        val idCard: String

): Serializable