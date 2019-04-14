package tech.loucianus.im.model

import org.springframework.http.HttpStatus
import java.io.Serializable


/**
 * <p>Response Body</p>
 *
 * Http status code
 * Http status reason
 * Message
 */
object JsonResponse : Serializable {

    private lateinit var meta: Meta
    private lateinit var data: Any

    /**
     * Code: 200
     * Message: OK.
     * Methods: GET, PUT, PATCH
     *
     * Operation succeeded.
     */
    fun ok(): JsonResponse {
        this.meta = Meta(HttpStatus.OK)
        return this
    }

    /**
     * Code: 201
     * Message: Created.
     * Methods: POST
     *
     * Operation succeeded
     */
    fun created(): JsonResponse {
        this.meta = Meta(HttpStatus.CREATED)
        return this
    }

    /**
     * Code: 204
     * Message: No Content.
     * Methods: DELETE
     *
     * Operation succeeded
     */
    fun noContent() : JsonResponse {
        this.meta = Meta(HttpStatus.NO_CONTENT)
        return this
    }

    /**
     * Code: 303
     * Message: See Other.
     * Methods: POS, PUT, DELETE
     *
     * Refer to another URL
     */
    fun seeOther(): JsonResponse {
        this.meta = Meta(HttpStatus.SEE_OTHER)
        return this
    }

    /**
     * Code: 307
     * Message: Temporary Redirect
     * Methods: Get
     *
     * Refer to another URL
     */
    fun redirect(): JsonResponse {
        this.meta = Meta(HttpStatus.TEMPORARY_REDIRECT)
        return this
    }


    /**
     * Code: 400
     * Message: Bad request.
     */
    fun badRequest(): JsonResponse {
        meta = Meta(HttpStatus.BAD_REQUEST)
        return this
    }

    /**
     * Code: 401
     * Message: Unauthorized.
     */
    fun unauthorized(): JsonResponse {
        meta = Meta(HttpStatus.UNAUTHORIZED)
        return this
    }

    /**
     * Code: 403
     * Message: Forbidden.
     */
    fun forbidden(): JsonResponse {
        meta = Meta(HttpStatus.FORBIDDEN)
        return this
    }

    /**
     * Code: 404
     * Message: Not Found.
     */
    fun notFound(): JsonResponse {
        meta = Meta(HttpStatus.NOT_FOUND)
        return this
    }

    /**
     * Code: 405
     * Message: Method Not Allowed.
     */
    fun methodNotAllowed(): JsonResponse {
        meta = Meta(HttpStatus.METHOD_NOT_ALLOWED)
        return this
    }

    /**
     * Code: 410
     * Message: Gone.
     */
    fun gone(): JsonResponse {
        meta = Meta(HttpStatus.GONE)
        return this
    }

    /**
     * Code: 415
     * Message: Unsupported Media Type.
     */
    fun unsupportedMediaType(): JsonResponse {
        meta = Meta(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
        return this
    }

    /**
     * Code: 422
     * Message: Unprocessable Entity
     */
    fun unprocessableEntity(): JsonResponse {
        meta = Meta(HttpStatus.UNPROCESSABLE_ENTITY)
        return this
    }

    /**
     * Code: 429
     * Message: Too Many Requests
     */
    fun tooManyRequests(): JsonResponse {
        meta = Meta(HttpStatus.TOO_MANY_REQUESTS)
        return this
    }

    /**
     * Code: 500
     * Message: Internal server error.
     */
    fun internalServerError(): JsonResponse {
        meta = Meta(HttpStatus.INTERNAL_SERVER_ERROR)
        return this
    }

    /**
     * Code: 503
     * Message: Service Unavailable
     */
    fun serviceUnavailable(): JsonResponse {
        meta = Meta(HttpStatus.SERVICE_UNAVAILABLE)
        return this
    }

    /**
     * Code:
     */
    fun gotNull(): JsonResponse {
        meta = Meta(HttpStatus.OK)
        return this
    }

    /**
     * The response data.
     *
     * @param data
     */
    fun message(data: Any): JsonResponse {
        this.data = data
        return this
    }

    fun getMeta() = meta

    fun getData() = data

    /**
     * Meta:
     *      Code: Status code value.
     *      Message: Reason phrase.
     */
    class Meta(private val httpStatus: HttpStatus) {

        fun getTimestamp() = System.currentTimeMillis()

        fun getStatus() = this.httpStatus.value()

        fun getMessage() = this.httpStatus.reasonPhrase
    }
}