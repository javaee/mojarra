@if "%TOMCAT_HOME%" == "" goto printUsage
@if "%JSF_RI_HOME%" == "" goto printUsage
@if "%CACTUS_HOME%" == "" goto printUsage
@if "%JUNIT_HOME%" == "" goto printUsage
@if "%1" == "" goto printUsage

@if not "%2" == "" goto setDebug
@if "%2" == "" goto setCp

:setDebug
set DO_DEBUG=-Xdebug -Xrunjdwp:transport=dt_socket,address=8000,server=y,suspend=y

:setCp
@set CP=%TOMCAT_HOME%\common\lib\servlet.jar;%TOMCAT_HOME%\server\lib\catalina.jar;%TOMCAT_HOME%\bin\bootstrap.jar;%TOMCAT_HOME%\common\lib\naming-common.jar;%TOMCAT_HOME%\common\lib\naming-resources.jar;%TOMCAT_HOME%\common\lib\xerces.jar;%JSF_RI_HOME%\build\test\servers\tomcat40\webapps\test\WEB-INF\classes;%JSF_RI_HOME%\conf\test;%CACTUS_HOME%\lib\cactus.jar;%CACTUS_HOME%\lib\httpclient.jar;%JUNIT_HOME%\junit.jar;

@cls
java %DO_DEBUG% -classpath %CP% -Dcatalina.home=%TOMCAT_HOME% -Dserver.xml=%JSF_RI_HOME%\build\test\servers\tomcat40\conf\server.xml DebugRunner "%1%"                      
goto doExit

:printUsage
@cls
@echo Run a junit or cactus test class from the command line,
@echo with the option of allowing a debugger to attach.
@echo You must the have the following env vars set to the correct
@echo values for your system: 
@echo TOMCAT_HOME to your tomcat 4.0 installation
@echo JSF_RI_HOME to the top level of your jsf-ri workarea
@echo CACTUS_HOME to your cactus 23-1.3 installation
@echo JUNIT_HOME to your junit 3.7 installation
@echo usage:
@echo      debugrunner.bat fully.qual.test.class [debug]
@echo If you specify a debug argument, the vm will be started 
@echo with arguments for attaching a debugger

:doExit
@set CP=
@set DO_DEBUG=
