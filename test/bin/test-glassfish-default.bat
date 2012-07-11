call %~dp0\test-glassfish-specific.bat Development true server
if not "%ERRORLEVEL%" == "0" exit /b
