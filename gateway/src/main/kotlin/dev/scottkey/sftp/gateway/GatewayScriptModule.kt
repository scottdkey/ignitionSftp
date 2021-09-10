package dev.scottkey.sftp.gateway

import com.jcraft.jsch.ChannelSftp
import com.jcraft.jsch.JSch
import com.jcraft.jsch.JSchException
import com.jcraft.jsch.SftpException
import dev.scottkey.sftp.AbstractScriptModule
import org.slf4j.LoggerFactory


class GatewayScriptModule : AbstractScriptModule() {
    private val logger = LoggerFactory.getLogger(javaClass)


    @Throws(JSchException::class)
    private fun setupJsch(remoteHost: String, username: String, password: String): ChannelSftp {
        val jsch = JSch()
        val jschSession = jsch.getSession(username, remoteHost)
        jschSession.setConfig("StrictHostKeyChecking", "no")
        jschSession.setConfig("StrictHostChecking", "no")
        jschSession.setPassword(password)
        jschSession.connect()
        return jschSession.openChannel("sftp") as ChannelSftp
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
            channelSftp = setupJsch(remoteHost, username, password)
        } catch (e: JSchException) {
            logger.info("jsch error", e)
        }
        try {
            channelSftp?.connect()
        } catch (e: JSchException) {
            logger.info("jsch unable to connect", e)
        }
        try {
            channelSftp?.put(localPath, remotePath)
            println("Upload Complete")
        } catch (e: SftpException) {
            logger.info("sftp upload failed", e)
        }

    }

    override fun downloadImpl(
        remoteHost: String,
        username: String,
        password: String,
        localPath: String,
        remotePath: String
    ){
        var channelSftp: ChannelSftp? = null
        try {
            channelSftp = setupJsch(remoteHost, username, password)
        } catch (e: JSchException){
            logger.info("jsch error", e)
        }
        try {
            channelSftp?.connect()
        } catch(e: JSchException){
            logger.info("jsch unable to connect", e)
        }
        try {
            channelSftp?.get(localPath, remotePath)
            println("Upload Complete")
        }catch (e: SftpException){
            logger.info("sftp upload failed", e)
        }
    }
}