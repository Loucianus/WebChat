package tech.loucianus.im.thymeleaf

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer

class WebAppInitializer: AbstractAnnotationConfigDispatcherServletInitializer() {
    override fun getRootConfigClasses(): Array<Class<*>>? = arrayOf(RootConfig()::class.java)

    override fun getServletMappings(): Array<String> = arrayOf("/")

    override fun getServletConfigClasses(): Array<Class<*>>? = arrayOf(WebConfig::class.java)
}
