package ru.sber.notebook.filter

import java.time.Instant
import javax.servlet.FilterChain
import javax.servlet.FilterConfig
import javax.servlet.ServletContext
import javax.servlet.annotation.WebFilter
import javax.servlet.http.HttpFilter
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


@WebFilter(urlPatterns = ["/api/*", "/app/*"])
class LoginFilter: HttpFilter() {

    private lateinit var context: ServletContext

    override fun init(filterConfig: FilterConfig) {
        context = filterConfig.servletContext
        context.log("Login filter online")
    }

    override fun doFilter(request: HttpServletRequest?, response: HttpServletResponse?, chain: FilterChain?) {
        val cookies = request!!.cookies

        if (cookies == null) {
            context.log("Cookie not found")
            response!!.sendRedirect("/login")
        }
        else {
            val passwordAdmin = "21232f297a57a5a743894a0e4a801fc3"
            var logMessage = ""

            for (cookie in cookies) {
                if (cookie.name == "auth") {
                    if (cookie.value == passwordAdmin) {
                        logMessage = "Cookie is valid"
                        context.log(logMessage)
                        chain!!.doFilter(request, response)
                    } else {
                        logMessage = "Wrong cookie value: cookie value - ${cookie.value}"
                        context.log(logMessage)
                        response!!.sendRedirect("/login")
                    }
                }
            }

            if (logMessage.isEmpty()) {
                context.log("Cookie not found")
                response!!.sendRedirect("/login")
            }
        }
    }
}