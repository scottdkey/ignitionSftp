package com.skm.sftp;

import com.inductiveautomation.ignition.common.BundleUtil;
import com.inductiveautomation.ignition.common.script.hints.ScriptArg;
import com.inductiveautomation.ignition.common.script.hints.ScriptFunction;

import java.io.IOException;

public abstract class AbstractScriptModule implements SFTPBlackBox {
  static {
    BundleUtil.get().addBundle(
        AbstractScriptModule.class.getSimpleName(),
        AbstractScriptModule.class.getClassLoader(),
        AbstractScriptModule.class.getName().replace('.', '/')
    );
  }
  @Override
  @ScriptFunction(docBundlePrefix = "AbstractScriptModule")
  public void upload(
      @ScriptArg ("remoteHost") String remoteHost,
      @ScriptArg ("username") String username,
      @ScriptArg ("password") String password,
      @ScriptArg ("localPath") String localPath,
      @ScriptArg ("remotePath") String remotePath) throws IOException {
    uploadImpl(remoteHost, username, password, localPath, remotePath);
  }

  protected abstract void uploadImpl(
      String remoteHost,
      String username,
      String password,
      String localPath,
      String remotePath) throws IOException;

  @Override
  @ScriptFunction(docBundlePrefix = "AbstractScriptModule")
  public void download(
      @ScriptArg ("remoteHost") String remoteHost,
      @ScriptArg ("username") String username,
      @ScriptArg ("password") String password,
      @ScriptArg ("localPath") String localPath,
      @ScriptArg ("remotePath") String remotePath) throws IOException {
    downloadImpl(remoteHost, username, password, localPath, remotePath);
  }

  protected abstract void downloadImpl(
      String remoteHost,
      String username,
      String password,
      String localPath,
      String remotePath) throws IOException;
}
