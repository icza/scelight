#!/bin/bash
cd "${0%/*}"
java -Xmx1024m -Dfile.encoding=UTF-8 -Dhu.scelight.launched-with=Scelight-linux.sh -cp mod/launcher/@launcherVer@/scelight-launcher.sldat:mod/launcher/@launcherVer@/jl1.0.1.jar:mod/launcher/@launcherVer@/mp3spi1.9.5.jar:mod/launcher/@launcherVer@/tritonus_share.jar hu/sllauncher/ScelightLauncher "$@"