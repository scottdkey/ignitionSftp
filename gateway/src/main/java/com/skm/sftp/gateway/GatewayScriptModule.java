package com.skm.sftp.gateway;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import com.skm.sftp.AbstractScriptModule;
import com.skm.sftp.SftpUtil;

public class GatewayScriptModule extends AbstractScriptModule {
  @Override
  public void uploadImpl(String remoteHost, String username, String password, String localPath, String remotePath) throws JSchException, SftpException {
    new SftpUtil(remoteHost, username, password, localPath, remotePath).uploadSshj();
  }

  @Override
  public void downloadImpl(String remoteHost, String username, String password, String localPath, String remotePath) throws JSchException, SftpException {
    new SftpUtil(remoteHost, username, password, localPath, remotePath).downloadSshj();
  }
}
