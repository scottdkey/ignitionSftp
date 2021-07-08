package com.skm.sftp


import net.schmizz.sshj.SSHClient
import net.schmizz.sshj.sftp.SFTPClient
import net.schmizz.sshj.transport.verification.PromiscuousVerifier

class SftpUtil(
    private val remoteHost: String,
    private val username: String,
    private val password: String,
    private val localPath: String?,
    private val remotePath: String?
) {

    fun uploadSshj() {
        val sshClient: SSHClient = setupSshj()
        val sftpClient: SFTPClient = sshClient.newSFTPClient()
        sftpClient.put(localPath, remotePath)
        sftpClient.close()
        sshClient.disconnect()
    }

    fun downloadSshj() {
        val sshClient: SSHClient = setupSshj()
        val sftpClient: SFTPClient = sshClient.newSFTPClient()
        sftpClient.get(remotePath, localPath)
        sftpClient.close()
        sshClient.disconnect()
    }


    private fun setupSshj(): SSHClient {
        val client = SSHClient()
        client.addHostKeyVerifier(PromiscuousVerifier())
        client.connect(remoteHost)
        client.authPassword(username, password)
        return client
    }
}