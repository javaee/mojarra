@echo off
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

cvs -n update -dP 2>&1 | grep -v "^cvs server:" > %FILE%

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

echo -- ADD DESCRIPTION HERE -- > %CB%
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

cvs diff -u 2>&1 | grep -v "^cvs server:" | grep -v "^\?"  >> %CB%

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
	type %ADD_FILE% | cut -c3- | zip %ZIP% -@
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
