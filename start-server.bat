@echo off
title Employee Management System - Server
cd /d "%~dp0"

echo ============================================
echo  Employee Management System
echo  http://localhost:8083
echo ============================================
echo.

call "%~dp0compile.bat"
if errorlevel 1 exit /b 1

set "CP=target\classes;lib\*"

echo.
echo Starting server...
echo   Login:        http://localhost:8083/login.html
echo   Employees:    http://localhost:8083/employees/
echo.
echo   Press Ctrl+C to stop.
echo.

java -cp "%CP%" com.ems.EmbeddedServer
if "%~1"=="" pause
