package com.skm.sftp.gateway

import com.jcraft.jsch.*
import com.skm.sftp.AbstractScriptModule
import net.schmizz.sshj.SSHClient
import net.schmizz.sshj.transport.verification.PromiscuousVerifier
import org.slf4j.LoggerFactory
import java.io.IOException


class GatewayScriptModule : AbstractScriptModule() {
    private val logger = LoggerFactory.getLogger(javaClass)

    @Throws(IOException::class)
    private fun setupSshj(username:String, password: String, remoteHost: String): SSHClient {
        val client = SSHClient()
        client.addHostKeyVerifier(PromiscuousVerifier())
        client.connect(remoteHost)
        client.authPassword(username, password)
        return client
    }
    @Throws(JSchException::class)
    private fun setupJsch(username: String, password: String, remoteHost: String): ChannelSftp {
        val jsch = JSch()
        val jschSession = jsch.getSession(username, remoteHost)
        val config = java.util.Properties()
        config["StrictHostKeyChecking"] = "no"
        jschSession.setConfig(config)
        jschSession.setPassword(password)
        jschSession.connect()
        return jschSession.openChannel("sftp") as ChannelSftp
    }
    private fun altUpload(
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
    override fun uploadImpl(
        remoteHost: String,
        username: String,
        password: String,
        localPath: String,
        remotePath: String
    ) {
    var channelSftp: ChannelSftp? = null
        try {
            channelSftp = setupJsch(username, password, remoteHost)
        } catch (e: JSchException){
            logger.info("jsch error", e)
        }
        try {
            channelSftp?.connect()
        } catch(e: JSchException){
            logger.info("jsch unable to connect", e)
            logger.info("alt attempt", "trying sshj library")
            altUpload(remoteHost, username, password, localPath, remotePath)
        }
        try {
            channelSftp?.put(localPath, remotePath)
            println("Upload Complete")
        }catch (e: SftpException){
            logger.info("sftp upload failed", e)
        }

    }

    private fun altDownload(
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

    override fun downloadImpl(
        remoteHost: String,
        username: String,
        password: String,
        localPath: String,
        remotePath: String
    ) {
        var channelSftp: ChannelSftp? = null
        try {
            channelSftp = setupJsch(username, password, remoteHost)
        } catch (e: JSchException){
            logger.info("jsch error", e)

        }
        try {
            channelSftp?.connect()
        } catch(e: JSchException){
            logger.info("jsch unable to connect", e)
            logger.info("alt attempt", "trying sshj library")
            altDownload(remoteHost, username, password, localPath, remotePath)
        }
        try {
            channelSftp?.get(localPath, remotePath)
            println("Upload Complete")
        }catch (e: SftpException){
            logger.info("sftp upload failed", e)
        }
    }
}