package tech.loucianus.im.controller

import org.apache.shiro.SecurityUtils
import org.apache.shiro.authz.annotation.Logical
import org.apache.shiro.authz.annotation.RequiresPermissions
import org.apache.shiro.authz.annotation.RequiresRoles
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import tech.loucianus.im.service.WorkerService

/**
 * Web page redirecting
 */
@Controller
class IndexController {

    @Autowired lateinit var workerService: WorkerService

    /**
     * Index page
     */
    @RequiresRoles(value = ["worker", "manager"],logical =  Logical.OR)
    @RequiresPermissions(value = ["view"])
    @GetMapping("/index")
    fun indexPage(model: Model) : String {
        val email = SecurityUtils.getSubject().principal as String
        val worker = workerService.getWorker(email)
        model.addAttribute("worker", worker)
        return "index"
    }

    /**
     * Login page
     */
    @GetMapping("/")
    fun loginPage() = "login"

}