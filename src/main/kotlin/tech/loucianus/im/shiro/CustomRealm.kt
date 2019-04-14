package tech.loucianus.im.shiro

import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory
import org.apache.shiro.SecurityUtils
import org.apache.shiro.authz.SimpleAuthorizationInfo
import org.apache.shiro.authc.*
import org.apache.shiro.subject.PrincipalCollection
import org.apache.shiro.authz.AuthorizationInfo
import org.springframework.beans.factory.annotation.Autowired
import org.apache.shiro.realm.AuthorizingRealm
import org.apache.shiro.authc.SimpleAuthenticationInfo
import org.springframework.stereotype.Component
import org.apache.shiro.authc.AuthenticationToken
import java.util.HashSet
import org.apache.shiro.authc.UsernamePasswordToken
import org.springframework.context.annotation.Lazy
import tech.loucianus.im.service.WorkerService

/**
 * The implement of AuthorizingRealm.
 *
 * Shiro authenticates the user's identity via Realm.
 */
@Component
class CustomRealm : AuthorizingRealm() {
    companion object {
        private val log: Log = LogFactory.getLog(CustomRealm::class.java)
    }

    @Autowired @Lazy private lateinit var workerService: WorkerService

    /**
     * Get User identity Information.
     *
     * In Shiro，Get identity and Authentication via 'Realm' in application.
     * The password that UsernamePasswordToken got from html for landing js.
     * @param authenticationToken Token of Authentication
     * @return User Authentication Info packaged instance.
     * @throws AuthenticationException An error during the Authentication process.
     */
    override fun doGetAuthenticationInfo(authenticationToken: AuthenticationToken): AuthenticationInfo {
        if(log.isInfoEnabled) log.info("身份认证.....")

        val token = authenticationToken as UsernamePasswordToken

        val password: String? = workerService.getPassword(token.username)

        password?.let {
            if (workerService.verify(token.username, it)) {
                return SimpleAuthenticationInfo(token.principal, password, name)
            }
        }

        throw AuthenticationException("Account invalid!")
    }

    /**
     * Grant the authority to active user, such as check Role or Permission.
     * @param principals
     * @return AuthorizationInfo packaged instance.
     */
    override fun doGetAuthorizationInfo(principals: PrincipalCollection): AuthorizationInfo {
        if(log.isInfoEnabled) log.info("权限认证......")
        val email: String = SecurityUtils.getSubject().principal as String

        val authority = workerService.getAuthority(email)

        val info = SimpleAuthorizationInfo()

        val roleSet = HashSet<String>()
        val permissionSet = HashSet<String>()
        for (role in authority.role.split("#")) {
            roleSet.add(role)
        }
        for (permission in authority.permission.split("#")) {
            permissionSet.add(permission)
        }

        info.roles = roleSet
        info.stringPermissions = permissionSet

        return info
    }
}