package tech.loucianus.im.model.dto

import java.io.Serializable
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

data class MessageGetter (

    @field: NotNull(message = "{username.required}")
    val to_id: Int,

    @field: NotNull(message = "{username.required}")
    val from_id: Int,

    @field: Email(message = "{username.invalid}")
    @field: NotBlank(message = "{username.required}")
    val to_email: String,

    @field: NotBlank(message = "{username.required}")
    var data: String,

    @field: NotBlank(message = "{username.required}")
    val type: String,

    @field: NotBlank(message = "{username.required}")
    val name: String

) :Serializable {
    override fun toString(): String {
        return "{" +
                "'to_id',$to_id" +
                "'from_id':$from_id," +
                "'to_email':$to_email," +
                "'data':$data," +
                "'type':$type" +
                "'name':$name" +
                "}"
    }
}