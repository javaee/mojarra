REM
REM The contents of this file are subject to the terms
REM of the Common Development and Distribution License
REM (the License). You may not use this file except in
REM compliance with the License.
REM 
REM You can obtain a copy of the License at
REM https://javaserverfaces.dev.java.net/CDDL.html or
REM legal/CDDLv1.0.txt. 
REM See the License for the specific language governing
REM permission and limitations under the License.
REM 
REM When distributing Covered Code, include this CDDL
REM Header Notice in each file and include the License file
REM at legal/CDDLv1.0.txt.    
REM If applicable, add the following below the CDDL Header,
REM with the fields enclosed by brackets [] replaced by
REM your own identifying information:
REM "Portions Copyrighted [year] [name of copyright owner]"
REM 
REM [Name of File] [ver.__] [Date]
REM 
REM Copyright 2005 Sun Microsystems Inc. All Rights Reserved
REM

@if "%TOMCAT_HOME%" == "" goto printUsage
@if "%JSF_RI_HOME%" == "" goto printUsage
@if "%CACTUS_HOME%" == "" goto printUsage
@if "%JUNIT_HOME%" == "" goto printUsage
@if "%1" == "" goto printUsage

@if not "%2" == "" goto setDebug
@if "%2" == "" goto setCp

:setDebug
set DO_DEBUG=-Xdebug -Xrunjdwp:transport=dt_shmem,address=jdbconn,server=y,suspend=n

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
