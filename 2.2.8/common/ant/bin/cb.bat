@echo off

REM DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
REM
REM Copyright (c) 1997-2010 Oracle and/or its affiliates. All rights reserved.
REM
REM The contents of this file are subject to the terms of either the GNU
REM General Public License Version 2 only ("GPL") or the Common Development
REM and Distribution License("CDDL") (collectively, the "License").  You
REM may not use this file except in compliance with the License.  You can
REM obtain a copy of the License at
REM https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
REM or packager/legal/LICENSE.txt.  See the License for the specific
REM language governing permissions and limitations under the License.
REM
REM When distributing the software, include this License Header Notice in each
REM file and include the License file at packager/legal/LICENSE.txt.
REM
REM GPL Classpath Exception:
REM Oracle designates this particular file as subject to the "Classpath"
REM exception as provided by Oracle in the GPL Version 2 section of the License
REM file that accompanied this code.
REM
REM Modifications:
REM If applicable, add the following below the License Header, with the fields
REM enclosed by brackets [] replaced by your own identifying information:
REM "Portions Copyright [year] [name of copyright owner]"
REM
REM Contributor(s):
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
REM List all files added, modified, and removed from a cvs repository
REM 
REM For this to work, you will need to make sure the following utilities
REM are either in your path or in the current working directory:
REM     cut.exe
REM     grep.exe
REM     wc.exe
REM     zip.exe
REM These files can be obtained here: http://unxutils.sourceforge.net/

set FILE=cb.temp.txt
set MOD_FILE=cb.chk.mods.txt
set ADD_FILE=cb.chk.add.txt
set REM_FILE=cb.chk.rem.txt
set CB=changebundle.txt
set ZIP=newfiles.zip
set DUMMY=zipdummy.txt

echo Scanning for modifications...

svn status 2>&1 | grep -v "^cvs server:" > %FILE%

type %FILE% | grep "^M " > %MOD_FILE%
type %FILE% | grep "^A " > %ADD_FILE%
type %FILE% | grep "^D " > %REM_FILE%

for /f "tokens=*" %%F in ('wc -l cb.chk.*.txt ^| grep -v "^      0 "') do (
	goto :CHANGES
)

echo No modifications - change bundle creation not necessary.
goto :END

:CHANGES
echo Modifications found.  Generating change bundle...

echo -- ADD DESCRIPTION HERE http://java.net/jira/browse/JAVASERVERFACES-XXXX -- > %CB%
echo. >> %CB%
echo. >> %CB%

echo ******************************************************************* >> %CB%
echo * SECTION: Modified Files >> %CB%
echo ******************************************************************* >> %CB%
for /f "tokens=*" %%F in ('wc -l %MOD_FILE% ^| grep -v "      0"') do (
	type %MOD_FILE% >> %CB%
	echo. >> %CB%
	echo. >> %CB%
)
for /f "tokens=*" %%F in ('wc -l %ADD_FILE% ^| grep -v "      0"') do (
	type %ADD_FILE% >> %CB%
	echo. >> %CB%
	echo. >> %CB%
)
for /f "tokens=*" %%F in ('wc -l %REM_FILE% ^| grep -v "      0"') do (
	type %REM_FILE% >> %CB%
	echo. >> %CB%
	echo. >> %CB%
)

echo. >> %CB%
echo ******************************************************************* >> %CB%
echo * SECTION: Diffs >> %CB%
echo ******************************************************************* >> %CB%

svn diff -x --ignore-eol-style 2>&1 | grep -v "^cvs server:" | grep -v "^\?"  >> %CB%

for /f "tokens=*" %%F in ('wc -l %ADD_FILE% ^| grep -v "      0"') do (
	echo ******************************************************************* >> %CB%
	echo * SECTION: New Files >> %CB%
	echo ******************************************************************* >> %CB%
	echo SEE ATTACHMENTS >> %CB%
	echo. >> %CB%
	echo Creating ZIP file with new files...
	del %ZIP%
	echo. > %DUMMY%
	zip %ZIP% %DUMMY%
	type %ADD_FILE% | cut -c9- | zip %ZIP% -@
	zip -d %ZIP% %DUMMY%
	del %DUMMY%
	echo.
	echo ZIP file, newfiles.zip, created.
)

rem "C:\Program Files\Windows NT\Accessories\wordpad.exe" %CB%

:END
del %FILE%
del %MOD_FILE%
del %ADD_FILE%
del %REM_FILE%
