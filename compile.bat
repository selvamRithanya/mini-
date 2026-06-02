@echo off
setlocal EnableExtensions
cd /d "%~dp0"

if not exist "lib\mysql-connector-j-8.3.0.jar" (
    echo Downloading libraries...
    powershell -NoProfile -ExecutionPolicy Bypass -File "%~dp0download-libs.ps1"
    if errorlevel 1 exit /b 1
)

if not exist "target\classes" mkdir "target\classes"
if exist "src\main\resources" xcopy /Y /Q /E "src\main\resources\*" "target\classes\" >nul 2>&1

echo Compiling Java sources...
powershell -NoProfile -Command ^
  "$ErrorActionPreference='Stop';" ^
  "$files = Get-ChildItem -Path 'src\main\java' -Recurse -Filter '*.java' | ForEach-Object { $_.FullName };" ^
  "javac --release 17 -encoding UTF-8 -cp 'lib\*' -d 'target\classes' @files"

if errorlevel 1 (
    echo Compilation failed.
    exit /b 1
)

echo Build OK.
exit /b 0
