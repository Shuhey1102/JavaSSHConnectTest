
$LogDIR  = ".\LOG\"
$LogFile  = $LogDIR  + "execSample2" + "_"+ $(date -Format 'yyyyMMdd') + ".log"

Write-Host ("StartTime : " + (Get-Date).ToString("yyyy-MM-dd HH:mm:ss.fff"))| Add-Content $LogFile
ipconfig
sleep(10)
Write-Host ("EndTime : " + (Get-Date).ToString("yyyy-MM-dd HH:mm:ss.fff"))| Add-Content $LogFile
exit 1