package tech.loucianus.im.thymeleaf

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.*

import org.thymeleaf.spring5.SpringTemplateEngine
import org.thymeleaf.templatemode.TemplateMode
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver
import org.thymeleaf.templateresolver.ITemplateResolver

/**
 * Class WebConfig
 * Extends WebMvcConfigurer,to configure the view resolver and resources
 *
 * @author loucianus
 * @property WebConfig
 * @see org.springframework.web.servlet.config.annotation.WebMvcConfigurer
 */

@Configuration
@EnableWebMvc
@ComponentScan("tech.loucianus.im.controller")
class WebConfig: WebMvcConfigurer {

    @Value("\${web.img.path}") private lateinit var imgPath: String

    /**
     * Configure view resolver. Set the Thymeleaf as default.
     */
    @Bean
    fun templateResolver(): ITemplateResolver {
        val resolver = SpringResourceTemplateResolver()
        resolver.prefix = "classpath:/templates/"
        resolver.suffix = ".html"
        resolver.templateMode = TemplateMode.HTML
        resolver.characterEncoding = "UTF-8"
        resolver.order = 1
        resolver.isCacheable = false

        return resolver
    }

    /**
     * Configure web template engine
     */
    @Bean
    fun templateEngine(): SpringTemplateEngine{
        val templateEngine = SpringTemplateEngine()
        templateEngine.setTemplateResolver(templateResolver())
        return templateEngine
    }

    /**
     * Configure the static resources path.
     * @param registry
     */
    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        registry
            .addResourceHandler("/static/**", "/images/**")
            .addResourceLocations("classpath:/static/", imgPath)
    }

}