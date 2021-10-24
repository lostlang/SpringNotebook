package ru.sber.notebook.servlet

import java.math.BigInteger
import java.security.MessageDigest
import javax.servlet.annotation.WebServlet
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@WebServlet(name = "loginServlet", urlPatterns = ["/login"])
class LoginServlet: HttpServlet() {
    private val usernameAdmin = "lostlang"
    private val passwordAdmin = "21232f297a57a5a743894a0e4a801fc3" // admin

    fun md5(input:String): String {
        val md = MessageDigest.getInstance("MD5")
        return BigInteger(1, md.digest(input.toByteArray())).toString(16).padStart(32, '0')
    }

    override fun doGet(req: HttpServletRequest?, resp: HttpServletResponse?) {
        req!!.getRequestDispatcher("/login.html").forward(req, resp)
    }

    override fun doPost(req: HttpServletRequest?, resp: HttpServletResponse?) {
        if (req?.getParameter("username") == usernameAdmin && md5(req.getParameter("password")) == passwordAdmin) {
            val cookie = Cookie("auth", passwordAdmin)
            resp!!.addCookie(cookie)
            resp.sendRedirect("/menu.html")
        } else {
            resp!!.sendRedirect("/login.html")
        }
    }
}