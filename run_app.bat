@echo off
echo Compiling the Java code...
javac -encoding UTF-8 -sourcepath src src/main/MainApp.java -d bin

IF %ERRORLEVEL% NEQ 0 (
    echo Compilation Failed!
    pause
    exit /b %ERRORLEVEL%
)

echo Compilation Successful. Starting the Application...
java -cp bin main.MainApp
pause
