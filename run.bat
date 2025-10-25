@echo off
echo ========================================
echo Health Camp Records - Startup Script
echo ========================================
echo.

echo Step 1: Checking Maven...
where mvn >nul 2>&1
if errorlevel 1 (
    echo Maven not found in PATH. Using full path...
    set MVN_CMD="C:\Program Files\Apache\apache-maven-3.9.11\bin\mvn.cmd"
) else (
    set MVN_CMD=mvn
)

echo Step 2: Starting application...
echo.
%MVN_CMD% clean javafx:run

if errorlevel 1 (
    echo.
    echo ========================================
    echo ERROR: Application failed to start!
    echo ========================================
    echo.
    echo Please check:
    echo 1. MySQL is running (check XAMPP or MySQL service)
    echo 2. Database 'healthcamp_db' exists
    echo 3. Database connection settings in DatabaseManager.java
    echo.
    pause
) else (
    echo.
    echo Application closed successfully.
)
