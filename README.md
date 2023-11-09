# THL Project - Teclado Helena

## Dependencies
To run and build this project, the following dependencies must be installed in your machine:
* Java 21 JDK (https://adoptium.net/temurin/releases/?package=jdk&version=21)
* Maven 3.x (https://maven.apache.org/download.cgi)
* Node 20.x (https://nodejs.org/en/download/)
* Windows SDK (https://developer.microsoft.com/en-us/windows/downloads/windows-sdk/)
* Git (https://git-scm.com/downloads)

## Develop
### Running the client
```
cd client/ui
npm install
npm run start
```

### Running the server
Import the project as maven project in your favorite IDE.

Run ```br.ipt.th.Entrypoint``` class as java application with vm
options: ```-Dthl.version=dev -Dspring.profiles.active=dev -Djavafx.preloader=br.ipt.thl.UiApplicationSplashScreen -Xms512m -Xmx1g```

## Build
Building the project will generate an MSIX package that can be used as an installer for Windows computers.

As a general rule, all MSIX packages must be signed with a valid code signing certificate before they can be installed.

For development purposes, a self-signed certificate is included in this repository.
If there is a need to generate another self-signed certificate, refer to [Generating a self-signed code signing certificate](#Generating-a-self-signed-code-signing-certificate).

1. Ensure that the `signtool.exe` and `makeappx.exe` tools are in the PATH.
These are installed with the Windows SDK and can be found at `C:\Program Files (x86)\Windows Kits\10\bin\<win build>\x64`.

2. Ensure that the code signing certificate is installed in the Local Machine certificate store.
For detailed instructions on how to install the included self-signed certificate, refer to [Installing the included self-signed certificate](#Installing-the-included-self-signed-certificate). 

3. Update the `certificate.fingerprint` property in the `server\pom.xml` file to match the fingerprint of the installed code signing certificate.
If using the included self-signed certificate, this value does not need to be changed.

4. If the `server\jpackage\launcher.png` icon has been modified, image assets must be regenerated. Refer to [Regenerating image assets and Resources.pri](#Regenerating-image-assets-and-Resourcespri).

5. As an administrator, run ```mvn clean install```. Running without administrative privileges will cause the signing tool to be unable to find the certificate.

If the MSIX package has been signed with a self-signed certificate, the certificate must also be installed in the target machine.
Refer to [Installing a self-signed certificate from an MSIX package](#Installing-a-self-signed-certificate-from-an-MSIX-package).

> ❗ The included self-signed certificate is for development purposes only. It should not be used in production.

***

## Optional steps

### Generating a self-signed code signing certificate

The build process requires a code signing certificate to sign the executables and the MSIX package.

If a valid code signing certificate is not available, a self-signed certificate can be created through the steps below for development purposes.

Run the following commands in a PowerShell terminal:

```
New-SelfSignedCertificate -Type Custom -Subject "CN=THL, O=IPT, C=BR" -KeyUsage DigitalSignature -FriendlyName "THL Certificate" -CertStoreLocation "Cert:\CurrentUser\My" -TextExtension @("2.5.29.37={text}1.3.6.1.5.5.7.3.3", "2.5.29.19={text}")
$password = ConvertTo-SecureString -String "!thl!321#" -Force -AsPlainText
$thumbprint = (Get-ChildItem -Path Cert:\CurrentUser\My | Where-Object {$_.Subject -eq "CN=THL, O=IPT, C=BR"} |Sort-Object LastWriteTime -Descending | Select-Object -first 1).Thumbprint
$user_home = [Environment]::GetFolderPath("UserProfile")
Export-PfxCertificate -Cert "Cert:\CurrentUser\My\$thumbprint" -FilePath "$user_home\THL.pfx" -Password $password
```

Copy the generated PFX file from `$user_home\THL.pfx` to `server\jpackage\certificate` in the project folder.

Add `C:\Program Files (x86)\Windows Kits\10\bin\<win build>\x64` to the PATH environment variable to use `signtool.exe`

### Installing the included self-signed certificate

<details open>
<summary><b>Option 1: From the CLI</b></summary>

1. From an elevated PowerShell prompt, run ```Import-PfxCertificate -FilePath '.\server\jpackage\certificate\THL.pfx' -CertStoreLocation 'Cert:\LocalMachine\My' -Password (ConvertTo-SecureString '!thl!321#' -AsPlainText -Force)```
</details>

<details open>
<summary><b>Option 2: From File Explorer</b></summary>

1. Double click the file `server\jpackage\certificate\THL.pfx` to open the Certificate Import Wizard.
2. Select "Local Machine" as the store location, click Next.
3. Click Next.
4. Enter `!thl!321#` as the password, click Next.
5. Select "Automatically select the certificate store based on the type of certificate", click Next.
6. Click Finish.

</details>

### Regenerating image assets and Resources.pri

Assets images will be generated based on ```server/jpackage/launcher.ico``` file. 
Change it to change the icon assets.


Run ```br.ipt.thl.assets.MsixAssetGenerator``` class as java application 
with program arguments: ```"yourprojectlocation"```


Generate Resources.pri requires the `makepri.exe` tools from the Windows SDK.

Navigate to ```server/jpackage``` folder

Run the following commands in a PowerShell terminal:
```
MakePri.exe createconfig /cf Resources.pri.xml /df lang-pt-BR /o /pv 10.0.0
MakePri.exe new /cf Resources.pri.xml /pr Assets /mn AppXManifest.xml /o /of Resources.pri
```

To Debug generated Pri Dump to XML with following command
```
MakePri.exe dump /if Resources.pri /of Resources.Debug.xml /dt Detailed
```

***

# Reference docs

* Win32 Api Docs      (https://learn.microsoft.com/en-us/windows/win32/apiindex/api-index-portal)
* Java 21.x Docs      (https://docs.oracle.com/en/java/javase/21/docs/api/index.html)
* JavaFX 20.x Docs    (https://openjfx.io/javadoc/20/)
* Maven 3.x Docs   (https://maven.apache.org/guides/index.html)
* Git 2.x Docs    (https://git-scm.com/doc)
* Node 20.x Docs  (https://nodejs.org/dist/latest-v18.x/docs/api/)
