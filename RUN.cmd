@echo off
echo Spring Boot creating JAR file for production...
call mvnw -U clean package -DskipTests -Pproduction
IF NOT %ERRORLEVEL% EQU 0 (
    echo "Error: Maven build failed."
    pause
    exit /b %ERRORLEVEL%
)
cls
SET "ENV_PATH=.env"
SETLOCAL
FOR /F "tokens=*" %%i IN ('type %ENV_PATH%') DO SET %%i
@title %application_name_and_port%
cd /d target
java -jar ai-0.0.1-SNAPSHOT.jar
ENDLOCAL
pause