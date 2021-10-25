package ru.sber.notebook.filter

import javax.servlet.FilterChain
import javax.servlet.FilterConfig
import javax.servlet.ServletContext
import javax.servlet.annotation.WebFilter
import javax.servlet.http.HttpFilter
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@WebFilter(urlPatterns = ["*"])
class LogFilter: HttpFilter() {

    private lateinit var context: ServletContext

    override fun init(filterConfig: FilterConfig) {
        context = filterConfig.servletContext
        context.log("Log filter online")
    }

    override fun doFilter(request: HttpServletRequest?, response: HttpServletResponse?, chain: FilterChain?) {
        context.log("Method: ${request!!.method}")
        context.log("Uri: ${request.requestURI}")

        context.log("Request parameter:")

        for (parameter in request.parameterNames.toList()) {
            context.log("\t\t\t\t\t$parameter:\t${request.getParameter(parameter)}")
        }

        chain!!.doFilter(request, response)
    }
}