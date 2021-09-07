package com.skm.sftp.client

import com.inductiveautomation.ignition.client.gateway_interface.ModuleRPCFactory
import com.skm.sftp.AbstractScriptModule
import com.skm.sftp.SFTPBlackBox


class ClientScriptModule: AbstractScriptModule() {
    private val rpc: SFTPBlackBox = ModuleRPCFactory.create(
        "com.skm.sftp",
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