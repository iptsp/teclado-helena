$ScriptPath = $pwd.Path

$AppObject = [PSCustomObject]@{
    DisplayName = ''
    Executable = ''
    Description = ''
}
$appArray = [System.Collections.ArrayList]@()

[xml]$manifest = Get-Content ($ScriptPath + "\AppxManifest.xml")
$appsInManifest = $manifest.Package.Applications.Application
foreach ($app in $appsInManifest)
{
    if ($app.VisualElements.AppListEntry -eq "none")
    {
        continue
    }
    $object = [PSCustomObject]@{
        DisplayName = ''
        Executable = ''
        Description = ''
    }
    $object.DisplayName = $app.VisualElements.DisplayName;
    $object.Description = $app.VisualElements.Description;
    $appArray.Add($object);
}

$config = Get-Content ($ScriptPath + "\config.json") | ConvertFrom-Json
$cnt = 0
foreach ($app in $config.applications)
{
    $appArray[$cnt].Executable = $app.executable
    $cnt++;
}
Function CreateShortcut
{
    param (
        $app = [PSCustomObject]@{
        DisplayName = ''
        Executable = ''
        Description = ''
    },
        [string]$destinationDesktopPath
    )
    $WshShell = New-Object -comObject WScript.Shell
    $lnkPath = ($destinationDesktopPath + "\" + $app.DisplayName + ".lnk")
    $Shortcut = $WshShell.CreateShortcut($lnkPath)
    $Shortcut.TargetPath = $( Split-Path $app.Executable -Leaf )
    $Shortcut.IconLocation = ($env:APPDATA + "\" + $app.DisplayName + ".ico")
    $Shortcut.WorkingDirectory = ($ENV:LOCALAPPDATA + "\Microsoft\WindowsApps")
    $Shortcut.Description = ($app.Description)
    $Shortcut.Save()
}

$desinationIconPath = $env:APPDATA

$destinationDesktopPath = [Environment]::GetFolderPath("Desktop")

Function CopyIcon
{
    Param (
        [Parameter(Mandatory = $true)]
        [string]$iconSourcePath,
        [string]$iconDestinationPath
    )

    Copy-Item -Path $iconSourcePath -Destination $iconDestinationPath
}

$date = Get-Date

Foreach ($app in $appArray)
{
    try
    {
        CopyIcon $( "Icons\" + $app.DisplayName + ".ico" ) $desinationIconPath
    }
    catch
    {
        Write-Host "Failed to copy icon"
    }
    CreateShortcut -app $app -destinationDesktopPath $destinationDesktopPath
}
