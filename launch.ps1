$root = Split-Path -Parent $MyInvocation.MyCommand.Path

function Compile-Module {
    param([string]$modulePath)

    $src     = "$modulePath\src\main\java"
    $out     = "$modulePath\target\classes"
    $sources = Get-ChildItem -Path $src -Filter "*.java" -Recurse | Select-Object -ExpandProperty FullName

    New-Item -ItemType Directory -Force -Path $out | Out-Null
    javac -d $out $sources

    if ($LASTEXITCODE -ne 0) {
        Write-Host "[ERROR] Build failed for $modulePath"
        Read-Host "Press Enter to exit"
        exit 1
    }
}

Write-Host "=== Building all modules ==="

Write-Host "Compiling Pam..."
Compile-Module "$root\Reciever_Pam"

Write-Host "Compiling Dwight..."
Compile-Module "$root\Dwight_Interceptor"

Write-Host "Compiling Jim..."
Compile-Module "$root\Sender_Jim"

Write-Host "=== Launching ==="

Write-Host "Starting Pam (port 9999)..."
Start-Process cmd -ArgumentList "/k title Pam Receiver && cd /d `"$root\Reciever_Pam`" && java -cp target\classes org.example.Pam"

Write-Host "Waiting for Pam to generate RSA keys and bind port..."
Start-Sleep -Seconds 2

Write-Host "Starting Dwight (port 8888)..."
Start-Process cmd -ArgumentList "/k title Dwight Interceptor && cd /d `"$root\Dwight_Interceptor`" && java -cp target\classes org.example.Dwight"

Start-Sleep -Seconds 2

Write-Host "Starting Jim..."
Start-Process cmd -ArgumentList "/k title Jim Sender && cd /d `"$root\Sender_Jim`" && java -cp target\classes org.example.Jim"

Write-Host "=== All three launched ==="
