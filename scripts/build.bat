@echo off
REM ========================================================
REM Build Script - Compagnie Aerienne
REM ========================================================
REM Configure paths below, then run: build.bat
REM ========================================================

REM === CONFIGURATION (Edit these paths) ===
set PROJECT_NAME=projet
set TOMCAT_HOME=C:\apache-tomcat-10.1.28
set JAVA_HOME=C:\Program Files\Java\jdk-17

REM === Build Process ===
cd ..

echo.
echo [1/5] Cleaning previous build...
if exist build rmdir /s /q build
if exist %PROJECT_NAME%.war del /q %PROJECT_NAME%.war

echo [2/5] Creating directories...
mkdir build\classes
mkdir build\web\WEB-INF\classes
mkdir build\web\WEB-INF\lib
mkdir build\web\jsp

echo [3/5] Compiling Java sources...
"%JAVA_HOME%\bin\javac.exe" -encoding UTF-8 -d build\classes -cp "lib\*;%TOMCAT_HOME%\lib\*" ^
    src\model\*.java ^
    src\util\*.java ^
    src\dao\*.java ^
    src\controller\*.java

if errorlevel 1 (
    echo ERROR: Compilation failed!
    exit /b 1
)

echo [4/5] Packaging WAR...
xcopy /E /I /Q build\classes\* build\web\WEB-INF\classes\ >nul
xcopy /Y /Q lib\*.jar build\web\WEB-INF\lib\ >nul
copy /Y WebContent\WEB-INF\web.xml build\web\WEB-INF\ >nul
xcopy /E /I /Q WebContent\jsp\* build\web\jsp\ >nul
if exist WebContent\*.html copy /Y WebContent\*.html build\web\ >nul
if exist WebContent\css\* xcopy /E /I /Q WebContent\css\* build\web\css\ >nul

cd build\web
"%JAVA_HOME%\bin\jar.exe" -cvf ..\..\%PROJECT_NAME%.war * >nul
cd ..\..

echo [5/5] Deploying to Tomcat...
if exist "%TOMCAT_HOME%\webapps\%PROJECT_NAME%" rmdir /s /q "%TOMCAT_HOME%\webapps\%PROJECT_NAME%"
if exist "%TOMCAT_HOME%\webapps\%PROJECT_NAME%.war" del /q "%TOMCAT_HOME%\webapps\%PROJECT_NAME%.war"
copy /Y %PROJECT_NAME%.war "%TOMCAT_HOME%\webapps\" >nul

echo.
echo ========================================
echo   BUILD SUCCESSFUL
echo ========================================
echo WAR: %PROJECT_NAME%.war
echo Deployed to: %TOMCAT_HOME%\webapps\
echo.
echo Start Tomcat: %TOMCAT_HOME%\bin\startup.bat
echo Access: http://localhost:9090/%PROJECT_NAME%/home
echo.
