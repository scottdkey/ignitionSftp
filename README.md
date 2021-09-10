# ignitionSFTP

**for use with ignition systems 8.1.0 and greater, untested past 8.1.6**

This is a basic module to add SFTP functionality and usage within ignition scripting with the signatures of:


## Basic Usage
`system.sftp.download(remoteHost, username, password, localPath, remotePath)`

or

`system.sftp.upload(remoteHost, username, password, localPath, remotePath)`

All of these parameters must be a string. An example of what this will look like within ignition scripting is:

`system.sftp.download("ftpServer.host.com", "username", "password", "C:/fileFolder/fileName.txt", "/reports/fileName.txt")`

This will download a file called `fileName.txt` to the ignition server in the folder `C:/fileFolder`(Windows file path).

### Some caveats to keep in mind when defining these parameters:

- **remoteHost:** this must be a fully qualified domain name or IP address without prefix.
  - `ftpServer.host.com` is correct usage
  - `sftp://ftpServer.host.com` will not work
- **username:** plaintext username
- **password:** plaintext password
- **localPath:** must not include backslashes `\` as that is an escape character within ignition scripting strings. Must be a full path to a local file with extension.
  - **example:** `C:/fileFolder/fileName.txt`
- **remotePath:** must not include backslashes `\` see above.
  - **example:** `/reports/fileName.txt`

### To create a build of the module using presets
- set up a keystore.jks and certificate.p7b in someplace accessible by the project(Not covered in this readme). My preference is in a folder within the project called 'private' as that will not be included in git by default(.gitignore)
- refactor `sign.props.example` to `sign.props` and fill out each of the fields with the correct information
- run `./gradlew buildSignedModule` from commandline or use an IDE built in Gradle builds via GUI
- The Build artifact will be available in the folder `{project root}/build/ignitionSFTP.modl`

### To Install in an ignition system:
- navigate to Config > Modules
- scroll to the bottom and click **Install or Upgrade a Module**
- Choose the file location from your computer.
- accept the terms and conditions
- if using a self-signed Cert accept
- this module should now be available to use in scripting.

There is some metadata that will be available when this module is used, for instance you will be able to see the signature during usage.
