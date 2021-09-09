package com.skm.sftp.gateway

import com.jcraft.jsch.ChannelSftp
import com.jcraft.jsch.JSch
import com.jcraft.jsch.JSchException
import com.jcraft.jsch.SftpException
import com.skm.sftp.AbstractScriptModule
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
            println("setup error")
            println(e)
        }
        try {
            channelSftp?.connect()
        } catch (e: JSchException) {
            logger.info("jsch unable to connect", e)
            println("unable to connect")
            println(e)
        }
        try {
            channelSftp?.put(localPath, remotePath)
            println("Upload Complete")
        } catch (e: SftpException) {
            logger.info("sftp upload failed", e)
            println("upload Failed")
            println(e)
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
            println("setup error")
            println(e)
        }
        try {
            channelSftp?.connect()
        } catch(e: JSchException){
            logger.info("jsch unable to connect", e)
            println("connection error")
            println(e)
        }
        try {
            channelSftp?.get(localPath, remotePath)
            println("Upload Complete")
        }catch (e: SftpException){
            logger.info("sftp upload failed", e)
            println("upload failed")
            println(e)
        }
    }
}