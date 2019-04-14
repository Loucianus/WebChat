package tech.loucianus.im.thymeleaf

import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.ComponentScan.Filter
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.FilterType
import org.springframework.web.servlet.config.annotation.EnableWebMvc

@Configuration
@ComponentScan(basePackages = ["tech.loucianus.im"],
    excludeFilters = (
            arrayOf(Filter(type = FilterType.ANNOTATION, value = [EnableWebMvc::class]))))
class RootConfig