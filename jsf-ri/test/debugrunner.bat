REM
REM DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
REM 
REM Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
REM 
REM The contents of this file are subject to the terms of either the GNU
REM General Public License Version 2 only ("GPL") or the Common Development
REM and Distribution License("CDDL") (collectively, the "License").  You
REM may not use this file except in compliance with the License. You can obtain
REM a copy of the License at https://glassfish.dev.java.net/public/CDDL+GPL.html
REM or glassfish/bootstrap/legal/LICENSE.txt.  See the License for the specific
REM language governing permissions and limitations under the License.
REM 
REM When distributing the software, include this License Header Notice in each
REM file and include the License file at glassfish/bootstrap/legal/LICENSE.txt.
REM Sun designates this particular file as subject to the "Classpath" exception
REM as provided by Sun in the GPL Version 2 section of the License file that
REM accompanied this code.  If applicable, add the following below the License
REM Header, with the fields enclosed by brackets [] replaced by your own
REM identifying information: "Portions Copyrighted [year]
REM [name of copyright owner]"
REM 
REM Contributor(s):
REM 
REM If you wish your version of this file to be governed by only the CDDL or
REM only the GPL Version 2, indicate your decision by adding "[Contributor]
REM elects to include this software in this distribution under the [CDDL or GPL
REM Version 2] license."  If you don't indicate a single choice of license, a
REM recipient has the option to distribute your version of this file under
REM either the CDDL, the GPL Version 2 or to extend the choice of license to
REM its licensees as provided above.  However, if you add GPL Version 2 code
REM and therefore, elected the GPL Version 2 license, then the option applies
REM only if the new code is made subject to such option by the copyright
REM holder.
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
