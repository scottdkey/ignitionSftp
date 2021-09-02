package com.skm.sftp;


import java.io.IOException;

public interface SFTPBlackBox {
  void upload(
      String remoteHost,
      String username,
      String password,
      String localPath,
      String remotePath) throws IOException;
  void download(
      String remoteHost,
      String username,
      String password,
      String localPath,
      String remotePath) throws IOException;
}
