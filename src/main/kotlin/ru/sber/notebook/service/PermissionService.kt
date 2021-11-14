package ru.sber.notebook.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.acls.domain.GrantedAuthoritySid
import org.springframework.security.acls.domain.ObjectIdentityImpl
import org.springframework.security.acls.domain.PrincipalSid
import org.springframework.security.acls.model.*
import org.springframework.stereotype.Service
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.TransactionStatus
import org.springframework.transaction.annotation.Transactional
import org.springframework.transaction.support.TransactionCallbackWithoutResult
import org.springframework.transaction.support.TransactionTemplate

interface WithGetValue {
    fun getValue(): Long
}

@Service
@Transactional
class PermissionService {

    @Autowired
    private val aclService: MutableAclService? = null

    @Autowired
    private val transactionManager: PlatformTransactionManager? = null

    fun addPermissionForUser(targetObj: WithGetValue, permission: Permission, username: String) {
        val sid: Sid = PrincipalSid(username)
        addPermissionForSid(targetObj, permission, sid)
    }

    fun addPermissionForAuthority(targetObj: WithGetValue, permission: Permission, authority: String) {
        val sid: Sid = GrantedAuthoritySid(authority)
        addPermissionForSid(targetObj, permission, sid)
    }

    private fun addPermissionForSid(targetObj: WithGetValue, permission: Permission, sid: Sid) {
        val tt = TransactionTemplate(transactionManager!!)
        tt.execute(object : TransactionCallbackWithoutResult() {
            override fun doInTransactionWithoutResult(status: TransactionStatus) {
                val oi: ObjectIdentity = ObjectIdentityImpl(targetObj.javaClass, targetObj.getValue())
                var acl: MutableAcl? = null
                acl = try {
                    aclService!!.readAclById(oi) as MutableAcl
                } catch (nfe: NotFoundException) {
                    aclService!!.createAcl(oi)
                }
                acl!!.insertAce(
                    acl.entries
                        .size, permission, sid, true
                )
                aclService!!.updateAcl(acl)
            }
        })
    }
}