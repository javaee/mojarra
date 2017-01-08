@echo off

::    DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
::
::    Copyright (c) 1997-2017 Oracle and/or its affiliates. All rights reserved.
::
::    The contents of this file are subject to the terms of either the GNU
::    General Public License Version 2 only ("GPL") or the Common Development
::    and Distribution License("CDDL") (collectively, the "License").  You
::    may not use this file except in compliance with the License.  You can
::    obtain a copy of the License at
::    https://glassfish.java.net/public/CDDL+GPL_1_1.html
::    or packager/legal/LICENSE.txt.  See the License for the specific
::    language governing permissions and limitations under the License.
::
::    When distributing the software, include this License Header Notice in each
::    file and include the License file at packager/legal/LICENSE.txt.
::
::    GPL Classpath Exception:
::    Oracle designates this particular file as subject to the "Classpath"
::    exception as provided by Oracle in the GPL Version 2 section of the License
::    file that accompanied this code.
::
::    Modifications:
::    If applicable, add the following below the License Header, with the fields
::    enclosed by brackets [] replaced by your own identifying information:
::    "Portions Copyright [year] [name of copyright owner]"
::
::    Contributor(s):
::    If you wish your version of this file to be governed by only the CDDL or
::    only the GPL Version 2, indicate your decision by adding "[Contributor]
::    elects to include this software in this distribution under the [CDDL or GPL
::    Version 2] license."  If you don't indicate a single choice of license, a
::    recipient has the option to distribute your version of this file under
::    either the CDDL, the GPL Version 2 or to extend the choice of license to
::    its licensees as provided above.  However, if you add GPL Version 2 code
::    and therefore, elected the GPL Version 2 license, then the option applies
::    only if the new code is made subject to such option by the copyright
::    holder.

echo.
echo Building Mojarra and installing in Maven
echo This should be run every time after source code changes are made
echo.
echo.
echo.
echo Note that this should be run from the [mojarra home]\test folder as .\bin\build.bat
echo.
echo.

call cd ..

echo Running initial ant build from %cd%

:: ant main clean main after release, e.g. m05 changed to m06
call ant clean main

if %ERRORLEVEL% neq 0 (
    echo.
    echo.
    echo.
    echo.
    echo EXITING BECAUSE OF FAILURES. SEE ABOVE!
    echo.
    echo.
    echo.
    echo.
    exit /b
)

echo.
echo Running ant initiated maven build and install
echo.
echo.

call ant mvn.deploy.snapshot.local

if %ERRORLEVEL% neq 0 ( 
    echo.
    echo.
    echo.
    echo.
    echo EXITING BECAUSE OF FAILURES. SEE ABOVE!
    echo.
    echo.
    echo.
    echo.
    exit /b
)

call cd test