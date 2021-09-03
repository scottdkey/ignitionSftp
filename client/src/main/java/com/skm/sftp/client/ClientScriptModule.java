package com.skm.sftp.client;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import com.skm.sftp.AbstractScriptModule;
import com.skm.sftp.SftpUtil;

public class ClientScriptModule extends AbstractScriptModule {
  @Override
  protected void uploadImpl(String remoteHost, String username, String password, String localPath, String remotePath) throws JSchException, SftpException {
   new SftpUtil(remoteHost, username, password, localPath, remotePath).uploadSshj();
  }

  @Override
  protected void downloadImpl(String remoteHost, String username, String password, String localPath, String remotePath) throws JSchException, SftpException {
    new SftpUtil(remoteHost, username, password, localPath, remotePath).downloadSshj();
  }
}
