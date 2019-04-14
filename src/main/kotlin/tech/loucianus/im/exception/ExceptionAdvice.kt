package tech.loucianus.im.exception

import org.apache.catalina.LifecycleException
import org.apache.ibatis.binding.BindingException
import org.apache.shiro.ShiroException
import org.apache.shiro.authc.AuthenticationException
import org.apache.shiro.authz.UnauthenticatedException
import org.apache.shiro.authz.UnauthorizedException
import org.springframework.messaging.handler.annotation.support.MethodArgumentNotValidException
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.ModelAndView
import tech.loucianus.im.model.JsonResponse
import org.springframework.web.servlet.NoHandlerFoundException
import java.io.FileNotFoundException
import java.net.SocketException

@RestControllerAdvice
class ExceptionAdvice {

    /**
     * Catch all Shiro's exception.
     */
    @ExceptionHandler(ShiroException::class)
    fun handle401(ex: ShiroException)
            = JsonResponse.unauthorized().message("${ex.message}")

    /**
     * Shiro(Authentication Exception)
     */
    @ExceptionHandler(AuthenticationException::class)
    fun authenticationException(ex: Exception)
            = JsonResponse.unauthorized().message("${ex.message}")

    /**
     * Shiro(Unauthenticated Exception)
     */
    @ExceptionHandler(UnauthenticatedException::class)
    fun unauthenticatedException(ex: UnauthenticatedException)
            = "Not to Login <a href='/'>To Landing Page</a>"

    /**
     * Catch 403
     */
    @ExceptionHandler(UnauthorizedException::class)
    fun unauthorizedException(ex: UnauthorizedException)
            = JsonResponse.unauthorized().message("${ex.message}")

    /**
     * Catch 404
     */
    @ExceptionHandler(NoHandlerFoundException::class)
    fun handle(ex: NoHandlerFoundException)
            = JsonResponse.notFound().message("Not Found.")

    /**
     * Custom Unauthorized Exception
     */
    @ExceptionHandler(CustomUnauthorizedException::class)
    fun customUnauthorizedException(ex: CustomUnauthorizedException)
            = JsonResponse.unauthorized().message("Cus:${ex.message}")

    /**
     * Custom Not Found Exception
     */
    @ExceptionHandler(CustomNotFoundException::class)
    fun customNotFoundException(ex: CustomNotFoundException)
            = JsonResponse.notFound().message("${ex.message}")

    /**
     * Custom Internal Exception
     */
    @ExceptionHandler(CustomInternalException::class)
    fun customInternalException(ex: CustomInternalException)
            = JsonResponse.internalServerError().message("${ex.message}")

    /**
     * Illegal State Exception
     */
    @ExceptionHandler(IllegalStateException::class)
    fun illegalStateException(ex: IllegalStateException)
            = JsonResponse.badRequest().message("${ex.message}")

    @ExceptionHandler(HttpRequestMethodNotSupportedException::class)
    fun httpRequestMethodNotSupportedException(ex: IllegalStateException)
            = JsonResponse.badRequest().message("${ex.message}")

    /**
     * LifecycleException
     */
    @ExceptionHandler(LifecycleException::class)
    fun lifecycleException(ex: LifecycleException)
            = JsonResponse.internalServerError().message("${ex.message}")

    /**
     * SocketException
     */
    @ExceptionHandler(SocketException::class)
    fun socketException(ex: SocketException)
            = JsonResponse.internalServerError().message("${ex.message}")

    /**
     * BindingException
     */
    @ExceptionHandler(BindingException::class)
    fun bindingException(ex: BindingException)
            = JsonResponse.badRequest().message("${ex.message}")

    /**
     * Not Valid
     */
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun methodArgumentNotValidException(ex: MethodArgumentNotValidException)
            = JsonResponse.badRequest().message("${ex.message}")

    /**
     * File not found.
     */
    @ExceptionHandler(FileNotFoundException::class)
    fun fileNotFoundException(ex: FileNotFoundException)
            = JsonResponse.badRequest().message("${ex.message}")
    /**
     * Others.
     */
    @ExceptionHandler(Exception::class)
    fun globalException(ex: Exception): JsonResponse {
        ex.printStackTrace()
        return JsonResponse.badRequest().message("${ex.message}")
    }
}