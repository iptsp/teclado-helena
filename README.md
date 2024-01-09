# THL Project - Teclado Helena

## Prerequisites
To run and build this project, the following dependencies must be installed in your machine:
* Java 21 JDK (https://jdk.java.net/21/)
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
Import this repository as a Maven project in your preferred IDE.

Run ```br.ipt.thl.Entrypoint``` class as java application with vm
options: ```-Dthl.version=dev -Dspring.profiles.active=dev -Djavafx.preloader=br.ipt.thl.UiApplicationSplashScreen -Xms512m -Xmx1g```

## Build
Building the project will generate an MSIX package that can be used as an installer for Windows computers.

As a general rule, all MSIX packages must be signed with a valid code signing certificate before they can be installed.

For development purposes, a self-signed certificate is included in this repository.
If there is a need to generate another self-signed certificate, refer to [Generating a self-signed code signing certificate](#Generating-a-self-signed-code-signing-certificate).

> [!WARNING]
> The included self-signed certificate is for development purposes only. It should not be used in production.

1. Ensure that the `signtool.exe`, `makeappx.exe` and `makepri.exe` tools are in the PATH.
These are installed with the Windows SDK and can be found at `C:\Program Files (x86)\Windows Kits\10\bin\<win build>\x64`.

2. Ensure that the code signing certificate is installed in the Local Machine certificate store.
For detailed instructions on how to install the included self-signed certificate, refer to [Installing the included self-signed certificate](#Installing-the-included-self-signed-certificate). 

3. Update the `certificate.fingerprint` property in the `server\pom.xml` file to match the fingerprint of the installed code signing certificate.
If using the included self-signed certificate, this value does not need to be changed.

4. As an administrator, run ```mvn clean install```. Running without administrative privileges will cause the signing tool to be unable to find the certificate.

> [!IMPORTANT]
> If the MSIX package has been signed with a self-signed certificate, the certificate must also be installed in the target machine.
>
> Refer to [Installing a self-signed certificate from an MSIX package](#Installing-a-self-signed-certificate-from-an-MSIX-package).

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

### Installing the included self-signed certificate

<details open>
<summary><b>Option 1: From the CLI</b></summary>

1. From an elevated PowerShell prompt, run the following command:
   ```
   Import-PfxCertificate -FilePath '.\server\jpackage\certificate\THL.pfx' -CertStoreLocation 'Cert:\LocalMachine\My' -Password (ConvertTo-SecureString '!thl!321#' -AsPlainText -Force)
   ```
</details>

<details open>
<summary><b>Option 2: From the GUI</b></summary>

1. Double-click the file `server\jpackage\certificate\THL.pfx` to open the Certificate Import Wizard.
2. Select "Local Machine" as the store location, click Next.
3. Click Next.
4. Enter `!thl!321#` as the password, click Next.
5. Select "Automatically select the certificate store based on the type of certificate", click Next.
6. Click Finish.

</details>

### Installing a self-signed certificate from an MSIX package

To install MSIX packages that have been signed with a self-signed certificate, it is necessary to trust this signature by installing the certificate in the ```Cert:\LocalMachine\TrustedPeople``` certificate store.

> [!WARNING]
> Installing a self-signed certificate in Windows will cause the system to trust all packages signed with that certificate, which may include potentially malicious ones.
> 
> It is strongly recommended to uninstall the certificate after testing to ensure the security of your system.

<details open>
<summary><b>Option 1: From the CLI</b></summary>

1. Run the following command from an elevated PowerShell prompt. Replace ```THL-0.2.msix``` with the path to the MSIX file.
   ```
   Get-AuthenticodeSignature 'THL-0.2.msix' | Select-Object -ExpandProperty SignerCertificate | Export-Certificate -FilePath ($TempFile = New-TemporaryFile).FullName; Import-Certificate -FilePath $TempFile.FullName -CertStoreLocation 'Cert:\LocalMachine\TrustedPeople'; Remove-Item -Path $TempFile.FullName
   ```
</details>

<details open>
<summary><b>Option 2: From the GUI</b></summary>

1. Open the context menu of the .msix file and open the Properties window.
2. In the Digital Signatures tab, select the THL certificate and click Details.
3. In the Digital Signature Details window, click Show Certificate.
4. In the Certificate window, click Install Certificate.
5. In the Certificate Import Wizard, select "Local Machine" as the store location, click Next.
6. Select "Place all certificates in the following store" and click Browse...
7. Select the "Trusted People" repository and click OK.
8. Click Next.
9. Click Finish.

</details>

***

# Reference docs

* Win32 Api Docs      (https://learn.microsoft.com/en-us/windows/win32/apiindex/api-index-portal)
* Java 21.x Docs      (https://docs.oracle.com/en/java/javase/21/docs/api/index.html)
* JavaFX 20.x Docs    (https://openjfx.io/javadoc/20/)
* Maven 3.x Docs      (https://maven.apache.org/guides/index.html)
* Git 2.x Docs        (https://git-scm.com/doc)
* Node 20.x Docs      (https://nodejs.org/dist/latest-v20.x/docs/api/)
