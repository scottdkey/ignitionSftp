package com.skm.sftp

import com.inductiveautomation.ignition.common.BundleUtil
import com.inductiveautomation.ignition.common.script.hints.ScriptArg
import com.inductiveautomation.ignition.common.script.hints.ScriptFunction
import org.slf4j.Logger
import org.slf4j.LoggerFactory

abstract class AbstractScriptModule: SFTPBlackBox {
    companion object {
        const val moduleId = "sftp"
        const val modulePath = "skm.sftp"

        @Suppress("unused")
        val logger: Logger = LoggerFactory.getLogger(moduleId)

        init {
            BundleUtil.get().addBundle(
                AbstractScriptModule::class.java.simpleName,
                AbstractScriptModule::class.java.classLoader,
                AbstractScriptModule::class.java.name.replace('.', '/')
            )
        }
    }


    @ScriptFunction(docBundlePrefix = "ScriptModule")
    override fun upload(
        @ScriptArg("remoteHost") remoteHost: String,
        @ScriptArg("username") username: String,
        @ScriptArg("password") password: String,
        @ScriptArg("localPath") localPath: String,
        @ScriptArg("remotePath") remotePath: String
    ) {
        return uploadImpl(remoteHost, username, password, localPath, remotePath)
    }

    @ScriptFunction(docBundlePrefix = "ScriptModule")
    override fun download(
        @ScriptArg("remoteHost") remoteHost: String,
        @ScriptArg("username") username: String,
        @ScriptArg("password") password: String,
        @ScriptArg("localPath") localPath: String,
        @ScriptArg("remotePath") remotePath: String
    ) {
        return downloadImpl(remoteHost, username, password, localPath, remotePath)
    }


    protected abstract fun uploadImpl(remoteHost: String, username: String, password: String, localPath: String, remotePath: String)
    protected abstract fun downloadImpl(remoteHost: String, username: String, password: String, localPath: String, remotePath: String)
}