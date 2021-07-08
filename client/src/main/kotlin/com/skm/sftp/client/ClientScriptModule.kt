package com.skm.sftp.client

import com.skm.sftp.AbstractScriptModule
import com.skm.sftp.SFTPBlackBox
import com.inductiveautomation.ignition.client.gateway_interface.ModuleRPCFactory

class ClientScriptModule : AbstractScriptModule() {
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
        return rpc.upload(remoteHost, username, password, localPath, remotePath)
    }

    override fun downloadImpl(
        remoteHost: String,
        username: String,
        password: String,
        localPath: String,
        remotePath: String
    ) {
        return rpc.download(remoteHost, username, password, localPath, remotePath)
    }

}