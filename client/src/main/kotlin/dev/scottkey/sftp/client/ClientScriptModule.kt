package dev.scottkey.sftp.client

import com.inductiveautomation.ignition.client.gateway_interface.ModuleRPCFactory
import dev.scottkey.sftp.AbstractScriptModule
import dev.scottkey.sftp.SFTPBlackBox
import dev.scottkey.sftp.moduleId


class ClientScriptModule: AbstractScriptModule() {
    private val rpc: SFTPBlackBox = ModuleRPCFactory.create(
        moduleId,
        SFTPBlackBox::class.java
    )

    override fun uploadImpl(
        remoteHost: String,
        username: String,
        password: String,
        localPath: String,
        remotePath: String
    ) {
        rpc.upload(remoteHost, username, password, localPath, remotePath)
    }


    override fun downloadImpl(
        remoteHost: String,
        username: String,
        password: String,
        localPath: String,
        remotePath: String
    ) {
        rpc.download(remoteHost, username, password, localPath, remotePath)
    }


}