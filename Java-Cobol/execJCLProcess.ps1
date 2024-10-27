$LogDIR  = ".\LOG\"
$LogFile  = $LogDIR  + "execJCLProcess" + "_"+ $(date -Format 'yyyyMMdd') + ".log"

$ErrorActionPreference = "Stop"

Write-Host ("execProcessStartTime : " + (Get-Date).ToString("yyyy-MM-dd HH:mm:ss.fff"))| Add-Content $LogFile

$processFile = ""
if($Args.Length -gt 0){$processFile = $Args[0]}
$isAsyc = $false
if($Args.Length -gt 1 -and $Args[1] -eq "1"){$isAsyc = $true}

$errorMessage = ""
$exitcode=0 

try{
    
    if($processFile -eq ""){
        throw [string] "target File is not set"  
    }

    # Set process information
    $pinfo = New-Object System.Diagnostics.ProcessStartInfo
    $pinfo.FileName = "powershell.exe"
    $pinfo.RedirectStandardError = $true
    $pinfo.RedirectStandardOutput = $true
    $pinfo.UseShellExecute = $false
    $pinfo.Arguments = "-ExecutionPolicy Bypass -File `"$processFile`""

    # Create the process object
    $p = New-Object System.Diagnostics.Process
    $p.StartInfo = $pinfo
    
    # Start the process
    if($isAsyc){
        # Register an event to automatically dispose of the process upon completion
        $null = Register-ObjectEvent -InputObject $p -EventName Exited -Action {
            $p.Dispose()
            Write-Host "Process completed and disposed."
        }

        $p.EnableRaisingEvents = $true  # Enable Exited event to occur
    }
    $p.Start() | Out-Null
    Write-Host "processIDÅF$($p.Id)"

    # Wait for the process to complete
    if(-not $isAsyc){
        $p.WaitForExit()
        # Capture standard output and standard error
        $stdout = $p.StandardOutput.ReadToEnd()
        $stderr = $p.StandardError.ReadToEnd()
        $exitcode = $p.ExitCode

        Write-Host ($stdout)| Add-Content $LogFile

        if($exitcode -ne "0"){
            throw [string]"ProcessError: $stderr"
        }

        # Release the process resources
        $p.Dispose()
    }

}catch{
    $exitcode = "1"
    $errorMessage =  $_
}

Write-Host "exit code: $exitcode" | Add-Content $LogFile

if($errorMessage -ne ""){
    Write-Host $errorMessage | Add-Content $LogFile
}

# End
Write-Host ("execProcessEndTime : " + (Get-Date).ToString("yyyy-MM-dd HH:mm:ss.fff")) | Add-Content $LogFile
exit $exitcode
