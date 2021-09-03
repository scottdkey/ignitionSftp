package com.skm.sftp.gateway;

import com.skm.sftp.AbstractScriptModule;
import com.skm.sftp.SftpUtil;

import java.io.IOException;

public class GatewayScriptModule extends AbstractScriptModule {
  @Override
  public void uploadImpl(String remoteHost, String username, String password, String localPath, String remotePath) throws IOException {
    new SftpUtil().uploadSshj(remoteHost, username, password, localPath, remotePath);
  }

  @Override
  public void downloadImpl(String remoteHost, String username, String password, String localPath, String remotePath) throws IOException {
    new SftpUtil().downloadSshj(remoteHost, username, password, localPath, remotePath);
  }
}
