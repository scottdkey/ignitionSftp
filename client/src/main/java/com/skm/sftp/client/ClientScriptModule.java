package com.skm.sftp.client;
import com.skm.sftp.AbstractScriptModule;
import com.skm.sftp.SftpUtil;

import java.io.IOException;

public class ClientScriptModule extends AbstractScriptModule {
  @Override
  protected void uploadImpl(String remoteHost, String username, String password, String localPath, String remotePath) throws IOException {
   new SftpUtil().uploadSshj(remoteHost, username, password, localPath, remotePath);
  }

  @Override
  protected void downloadImpl(String remoteHost, String username, String password, String localPath, String remotePath) throws IOException {
    new SftpUtil().downloadSshj(remoteHost, username, password, localPath, remotePath);
  }
}
