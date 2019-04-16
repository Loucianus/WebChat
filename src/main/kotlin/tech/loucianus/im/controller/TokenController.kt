package tech.loucianus.im.controller

import org.apache.commons.logging.LogFactory
import org.springframework.validation.Errors
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import tech.loucianus.im.exception.CustomNotFoundException
import tech.loucianus.im.model.JsonResponse
import tech.loucianus.im.model.dto.Account
import org.apache.shiro.SecurityUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.apache.shiro.authc.UsernamePasswordToken
import org.apache.shiro.authz.annotation.Logical
import org.apache.shiro.authz.annotation.RequiresPermissions
import org.apache.shiro.authz.annotation.RequiresRoles
import tech.loucianus.im.service.WorkerService
import javax.servlet.http.HttpSession

@RestController
@RequestMapping("/token")
class TokenController {

    companion object {
        private val log = LogFactory.getLog(this::class.java)
    }

    @Autowired lateinit var workerService: WorkerService
    /**
     * <p>Performing Landing Operation.</p>
     *
     * Determine the validity of account. If valid, would execute authorization to login.
     * Subject was Created from SecurityUtils and put a token created by UsernamePasswordToken.
     * Finally, execute to land.
     * @param account Only username and password.
     * @param errors Determine if the the account was invalid.
     * @return Json of land Succeed.
     * @throws CustomNotFoundException "Account error.(Username or Password Format Invalid.)"
     */
    @PostMapping(consumes = ["application/json"], produces = ["application/json"])
    fun login(
        @RequestBody @Validated account: Account,
        errors: Errors
    ): JsonResponse {

        if (log.isInfoEnabled) log.info("Landing:${account.username}")

        if (!errors.hasErrors()) {

            // 从SecurityUtils里边创建一个 subject
            val subject = SecurityUtils.getSubject()

            // 在认证提交前准备 token（令牌）
            val token = UsernamePasswordToken(account.username, account.password)

            // 执行认证登陆
            subject.login(token)

            return JsonResponse.ok().message(workerService.getWorker(account.username))
        }

        throw CustomNotFoundException("Account error.(Username or Password Invalid.)")
    }

    /**
     * <p>Performing Logout Operation.</p>
     *
     * Get the Subject of user and execute to logout.
     */
    @RequiresRoles(value= ["worker", "manager"], logical = Logical.OR)
    @RequiresPermissions(value = ["view"])
    @DeleteMapping
    fun logout(): JsonResponse {

        SecurityUtils.getSubject().logout()

        return JsonResponse.ok().message("Logout.")
    }
}