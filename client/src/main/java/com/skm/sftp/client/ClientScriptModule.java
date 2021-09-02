package com.skm.sftp.client;

import com.inductiveautomation.ignition.client.gateway_interface.ModuleRPCFactory;
import com.skm.sftp.AbstractScriptModule;
import com.skm.sftp.SFTPBlackBox;

import java.io.IOException;

public class ClientScriptModule extends AbstractScriptModule {
  private final SFTPBlackBox rpc;

  public ClientScriptModule() {
    rpc = ModuleRPCFactory.create(
        "com.skm.sftp",
        SFTPBlackBox.class
    );
  }

  @Override
  protected void uploadImpl(
      String remoteHost,
      String username,
      String password,
      String localPath,
      String remotePath) throws IOException {
    rpc.upload(remoteHost, username, password, localPath, remotePath);
  }

  @Override
  protected void downloadImpl(
      String remoteHost,
      String username,
      String password,
      String localPath,
      String remotePath) throws IOException {
    rpc.download(remoteHost, username, password, localPath, remotePath);
  }
}
