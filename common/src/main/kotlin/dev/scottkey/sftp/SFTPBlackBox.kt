package dev.scottkey.sftp

interface SFTPBlackBox {
    fun upload(
        remoteHost: String,
        username: String,
        password: String,
        localPath: String,
        remotePath: String
    )

    fun download(
        remoteHost: String,
        username: String,
        password: String,
        localPath: String,
        remotePath: String
    )
}