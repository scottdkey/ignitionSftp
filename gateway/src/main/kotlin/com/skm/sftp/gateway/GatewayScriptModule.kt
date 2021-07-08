package com.skm.sftp.gateway

import com.skm.sftp.AbstractScriptModule
import com.skm.sftp.SftpUtil

class GatewayScriptModule : AbstractScriptModule() {
    override fun uploadImpl(
        remoteHost: String,
        username: String,
        password: String,
        localPath: String,
        remotePath: String
    ) {
       return SftpUtil(remoteHost, username, password, localPath, remotePath).uploadSshj()
    }

    override fun downloadImpl(
        remoteHost: String,
        username: String,
        password: String,
        localPath: String,
        remotePath: String
    ) {
        return SftpUtil(remoteHost, username, password, localPath, remotePath).downloadSshj()
    }

}