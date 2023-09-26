# THL Project - Teclado Helena

***

# Prerequisites

### Windows

Windows 10.x        (https://www.microsoft.com/pt-br/software-download/windows10)

Win32 Api Docs      (https://learn.microsoft.com/en-us/windows/win32/apiindex/api-index-portal)

Windows 10.x SDK    (https://developer.microsoft.com/en-us/windows/downloads/windows-sdk/)

***

## Java

Java 21.x           (https://jdk.java.net/21/)

Java 21.x Docs      (https://docs.oracle.com/en/java/javase/21/docs/api/index.html)

***

## Java FX

JavaFX 20.x Docs    (https://openjfx.io/javadoc/20/)

***

## Maven

Maven 3.x        (https://maven.apache.org/download.cgi)

Maven 3.x Docs   (https://maven.apache.org/guides/index.html)

***

## Git

Git 2.x         (https://git-scm.com/downloads)

Git 2.x Docs    (https://git-scm.com/doc)

## Node

Node 18.x       (https://nodejs.org/en/download/)

Node 18.x Docs  (https://nodejs.org/dist/latest-v18.x/docs/api/)


# Generate MSIX Resources Pri

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

## Build project from source

The build process requires the `signtool.exe` and `makeappx.exe` tools from the Windows SDK.

Ensure that `C:\Program Files (x86)\Windows Kits\10\bin\<win build>\x64` is in the PATH environment variable.

The code signing certificate must be installed in the Local Machine certificate store. 
The certificate thumbprint must be updated in pom.xml.

For dev purposes you can install the certificate located at ```server\jpackage\certificate\THL.pfx``` in your local machine.

The Maven ```install``` task must be executed as administrator to allow the signing certificate to be read from the Windows certificate store.

```
git clone git@github.com:iptsp/teclado-helena.git
cd teclado-helena
mvn clean install 
```

After the build, the Windows MSIX Installer will be available in the target directory.


***

## Running client project from IDE

```
cd client
npm install
npm run start
```

***

## Running server project from IDE

Import the project as maven project in your favorite IDE.

Run ```br.ipt.th.Entrypoint``` class as java application with vm
options: ```-Dthl.version=dev -Dspring.profiles.active=dev -Djavafx.preloader=br.ipt.thl.UiApplicationSplashScreen -Xms512m -Xmx1g```


***

## Create a self-signed package signing certificate for Windows

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

Copy the generated PFX file from `$user_home\THL.pfx` to `assets\jpackage\certificate` in the project folder.

Add `C:\Program Files (x86)\Windows Kits\10\bin\<win build>\x64` to the PATH environment variable to use `signtool.exe`