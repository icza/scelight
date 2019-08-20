@echo off
%~dd0
cd "%~dp0"
set PATH=c:\Program Files (x86)\Java\jre8\bin\;c:\Program Files\Java\jre8\bin\;c:\Program Files (x86)\Java\jre7\bin\;c:\Program Files\Java\jre7\bin\;%PATH%

IF EXIST jre\bin\java.exe (
	set _JAVA_EXE_=jre\bin\java.exe
) ELSE (
	set _JAVA_EXE_=java.exe
)

%_JAVA_EXE_% -Xmx1024m -Dfile.encoding=UTF-8 -Dhu.scelight.launched-with=Scelight-win.cmd -cp mod/launcher/3.1.4/scelight-launcher.sldat;mod/launcher/3.1.4/jl1.0.1.jar;mod/launcher/3.1.4/mp3spi1.9.5.jar;mod/launcher/3.1.4/tritonus_share.jar hu/sllauncher/ScelightLauncher %*
