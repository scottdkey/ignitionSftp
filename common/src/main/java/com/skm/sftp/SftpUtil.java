package com.skm.sftp;

import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.sftp.SFTPClient;
import net.schmizz.sshj.transport.verification.PromiscuousVerifier;
import java.io.IOException;


public class SftpUtil {

  public void uploadSshj(String remoteHost, String username, String password, String localPath, String remotePath) throws IOException {
    SSHClient client = new SSHClient();
    client.addHostKeyVerifier(new PromiscuousVerifier());
    client.authPassword(username, password);
    client.connect(remoteHost);
    SFTPClient sftpClient = client.newSFTPClient();
    sftpClient.put(localPath, remotePath);
    sftpClient.close();
    client.disconnect();
  }

  public void downloadSshj(String remoteHost, String username, String password, String localPath, String remotePath) throws IOException {
    SSHClient client = new SSHClient();
    client.addHostKeyVerifier(new PromiscuousVerifier());
    client.authPassword(username, password);
    client.connect(remoteHost);
    SFTPClient sftpClient = client.newSFTPClient();
    sftpClient.get(localPath, remotePath);
    sftpClient.close();
    client.disconnect();
  }
}
