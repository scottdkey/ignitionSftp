package com.skm.sftp.gateway

import com.skm.sftp.AbstractScriptModule
import net.schmizz.sshj.SSHClient
import net.schmizz.sshj.transport.verification.PromiscuousVerifier
import java.io.IOException


class GatewayScriptModule : AbstractScriptModule() {
    @Throws(IOException::class)
    private fun setupSshj(username:String, password: String, remoteHost: String): SSHClient {
        val client = SSHClient()
        client.addHostKeyVerifier(PromiscuousVerifier())
        client.connect(remoteHost)
        client.authPassword(username, password)
        return client
    }



    override fun uploadImpl(
        remoteHost: String,
        username: String,
        password: String,
        localPath: String,
        remotePath: String
    ) {
        val sshClient = setupSshj(username, password, remoteHost)
        val sftpClient = sshClient.newSFTPClient()
        sftpClient?.put(localPath, remotePath)
        sftpClient?.close()
        sshClient.disconnect()
    }

    override fun downloadImpl(
        remoteHost: String,
        username: String,
        password: String,
        localPath: String,
        remotePath: String
    ) {
        val sshClient = setupSshj(username, password, remoteHost)
        val sftpClient = sshClient.newSFTPClient()
        sftpClient?.get(remotePath, localPath)
        sftpClient?.close()
        sshClient.disconnect()
    }
}