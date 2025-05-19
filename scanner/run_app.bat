@echo off
echo Building and running Smart Stopwatch app...
echo.

echo Step 1: Building debug APK...
call gradlew assembleDebug
if %ERRORLEVEL% NEQ 0 (
    echo Error building APK. Exiting.
    exit /b %ERRORLEVEL%
)
echo APK built successfully.
echo.

echo Step 2: Installing APK on device...
adb install -r app\build\outputs\apk\debug\app-debug.apk
if %ERRORLEVEL% NEQ 0 (
    echo Error installing APK. Make sure a device is connected and USB debugging is enabled.
    exit /b %ERRORLEVEL%
)
echo APK installed successfully.
echo.

echo Step 3: Launching app...
adb shell am start -n com.example.smartstopwatch/.MainActivity
if %ERRORLEVEL% NEQ 0 (
    echo Error launching app.
    exit /b %ERRORLEVEL%
)
echo App launched successfully.
echo.

echo Showing logs (press Ctrl+C to stop)...
adb logcat -s "SmartStopwatch:*"
