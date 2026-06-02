@echo off
title Download Maven (one-time setup)
cd /d "%~dp0"

set "TOOLS=%~dp0tools"
set "ZIP=%TOOLS%\maven.zip"
set "MAVEN_DIR=%TOOLS%\apache-maven-3.9.6"

if exist "%MAVEN_DIR%\bin\mvn.cmd" (
    echo Maven already installed in tools folder.
    goto :done
)

echo Downloading Apache Maven 3.9.6...
if not exist "%TOOLS%" mkdir "%TOOLS%"

powershell -NoProfile -Command "[Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12; Invoke-WebRequest -Uri 'https://archive.apache.org/dist/maven/maven-3/3.9.6/binaries/apache-maven-3.9.6-bin.zip' -OutFile '%ZIP%' -UseBasicParsing; Expand-Archive -Path '%ZIP%' -DestinationPath '%TOOLS%' -Force"

if not exist "%MAVEN_DIR%\bin\mvn.cmd" (
    echo Download failed. Check your internet connection.
    pause
    exit /b 1
)

:done
echo.
echo Maven ready. Now double-click start-server.bat
pause
