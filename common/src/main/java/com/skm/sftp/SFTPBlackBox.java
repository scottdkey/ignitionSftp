package com.skm.sftp;


import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;

import java.io.IOException;

public interface SFTPBlackBox {
  void upload(
      String remoteHost,
      String username,
      String password,
      String localPath,
      String remotePath) throws IOException, JSchException, SftpException;
  void download(
      String remoteHost,
      String username,
      String password,
      String localPath,
      String remotePath) throws IOException, JSchException, SftpException;
}
