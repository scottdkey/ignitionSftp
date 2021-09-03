package com.skm.sftp;

import com.jcraft.jsch.*;

public class SftpUtil {
  private final String remoteHost;
  private final String username;
  private final String password;
  private final String localPath;
  private final String remotePath;

  public SftpUtil(String remoteHost, String username, String password, String localPath, String remotePath){
    this.remoteHost = remoteHost;
    this.username = username;
    this.password = password;
    this.localPath = localPath;
    this.remotePath = remotePath;
  }

  private ChannelSftp setupJsch() throws JSchException {
    JSch jsch = new JSch();
    Session jschSession = jsch.getSession(username, remoteHost);
    jschSession.setPassword(password);
    jschSession.connect();
    return (ChannelSftp) jschSession.openChannel("sftp");
  }

  public void uploadSshj() throws JSchException, SftpException {
    ChannelSftp client = setupJsch();
    client.connect();
    client.put(localPath, remotePath);
    client.exit();
  }

  public void downloadSshj() throws JSchException, SftpException {
    ChannelSftp client = setupJsch();
    client.connect();
    client.get(localPath, remotePath);
    client.exit();
  }
}
