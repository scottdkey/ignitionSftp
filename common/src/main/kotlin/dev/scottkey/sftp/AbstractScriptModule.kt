package dev.scottkey.sftp

import com.inductiveautomation.ignition.common.BundleUtil
import com.inductiveautomation.ignition.common.script.hints.ScriptArg
import com.inductiveautomation.ignition.common.script.hints.ScriptFunction

const val modulePath = "system.sftp"
const val moduleId = "dev.scottkey.sftp"

abstract class AbstractScriptModule : SFTPBlackBox {
    companion object {
        init {
            BundleUtil.get().addBundle(
                AbstractScriptModule::class.java.simpleName,
                AbstractScriptModule::class.java.classLoader,
                AbstractScriptModule::class.java.name.replace('.', '/')
            )
        }
    }

    @ScriptFunction(docBundlePrefix = "AbstractScriptModule")
    override fun upload(
        @ScriptArg("remoteHost") remoteHost: String,
        @ScriptArg("username") username: String,
        @ScriptArg("password") password: String,
        @ScriptArg("localPath") localPath: String,
        @ScriptArg("remotePath") remotePath: String
    ) {
        uploadImpl(remoteHost, username, password, localPath, remotePath)
    }

    protected abstract fun uploadImpl(
        remoteHost: String,
        username: String,
        password: String,
        localPath: String,
        remotePath: String
    )

    @ScriptFunction(docBundlePrefix = "AbstractScriptModule")
    override fun download(
        @ScriptArg("remoteHost") remoteHost: String,
        @ScriptArg("username") username: String,
        @ScriptArg("password") password: String,
        @ScriptArg("localPath") localPath: String,
        @ScriptArg("remotePath") remotePath: String
    ) {
        downloadImpl(remoteHost, username, password, localPath, remotePath)
    }

    protected abstract fun downloadImpl(
        remoteHost: String,
        username: String,
        password: String,
        localPath: String,
        remotePath: String
    )
}