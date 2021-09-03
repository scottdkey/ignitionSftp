package com.skm.sftp;

import com.jcraft.jsch.*;

public class SftpUtil {
  private ChannelSftp setupJsch(String username, String password, String remoteHost) throws JSchException {
    JSch jsch = new JSch();
    Session jschSession = jsch.getSession(username, remoteHost);
    jschSession.setPassword(password);
    jschSession.connect();
    return (ChannelSftp) jschSession.openChannel("sftp");
  }

  public void uploadSshj(String remoteHost, String username, String password, String localPath, String remotePath) throws JSchException, SftpException {
    ChannelSftp client = setupJsch(username, password, remoteHost);
    client.connect();
    client.put(localPath, remotePath);
    client.exit();
  }

  public void downloadSshj(String remoteHost, String username, String password, String localPath, String remotePath) throws JSchException, SftpException {
    ChannelSftp client = setupJsch(username, password, remoteHost);
    client.connect();
    client.get(localPath, remotePath);
    client.exit();
  }
}
