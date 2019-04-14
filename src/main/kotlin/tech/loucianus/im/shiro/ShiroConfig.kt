package tech.loucianus.im.shiro

import net.sf.ehcache.hibernate.EhCacheRegionFactory
import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory
import org.apache.shiro.cache.ehcache.EhCacheManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.apache.shiro.spring.web.ShiroFilterFactoryBean
import org.apache.shiro.web.mgt.DefaultWebSecurityManager
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor
import org.apache.shiro.spring.LifecycleBeanPostProcessor
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.DependsOn
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator
import org.apache.shiro.mgt.DefaultSubjectDAO

@Configuration
class ShiroConfig {
    companion object {
        private val log: Log = LogFactory.getLog(CustomRealm::class.java)
    }

    /**
     * Injecting Shiro Filter Bean.
     */
    @Bean
    fun shiroFilter(@Qualifier("securityManager")securityManager: DefaultWebSecurityManager): ShiroFilterFactoryBean {

        val factoryBean = ShiroFilterFactoryBean()

        //添加securityManager
        factoryBean.securityManager = securityManager

        if(log.isInfoEnabled) log.info("Shiro Bean Configuration Completed.")

        return factoryBean
    }

    /**
     * Injecting Security Manager.
     */
    @Bean
    fun securityManager(): DefaultWebSecurityManager {

        val manager = DefaultWebSecurityManager()
        //使用自定义Realm
        manager.setRealm(customRealm())

        val subjectDAO = DefaultSubjectDAO()

//        val defaultSessionStorageEvaluator = DefaultSessionStorageEvaluator()

//        defaultSessionStorageEvaluator.isSessionStorageEnabled = false

//        subjectDAO.sessionStorageEvaluator = defaultSessionStorageEvaluator

        manager.subjectDAO = subjectDAO

        manager.cacheManager = ehcacheManager()

        return manager
    }

    @Bean
    fun customRealm(): CustomRealm {
        return CustomRealm()
    }

    @Bean
    fun ehcacheManager(): EhCacheManager {
        val ehcacheManager = EhCacheManager()
        ehcacheManager.cacheManagerConfigFile = "classpath:ehcache-shiro.xml"
        return ehcacheManager
    }

    /**
     * Support annotation.
     */
    @Bean
    @DependsOn("lifecycleBeanPostProcessor")
    fun defaultAdvisorAutoProxyCreator(): DefaultAdvisorAutoProxyCreator {

        val defaultAdvisorAutoProxyCreator = DefaultAdvisorAutoProxyCreator()

        defaultAdvisorAutoProxyCreator.isProxyTargetClass = true

        return defaultAdvisorAutoProxyCreator
    }

    /**
     * Lifecycle
     */
    @Bean
    fun lifecycleBeanPostProcessor(): LifecycleBeanPostProcessor {

        return LifecycleBeanPostProcessor()
    }

    @Bean
    fun authorizationAttributeSourceAdvisor(securityManager: DefaultWebSecurityManager): AuthorizationAttributeSourceAdvisor {

        val advisor = AuthorizationAttributeSourceAdvisor()

        advisor.securityManager = securityManager

        return advisor
    }
}