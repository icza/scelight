#!/bin/bash
cd "${0%/*}"
java -Xmx1024m -Dfile.encoding=UTF-8 -Dhu.scelight.launched-with=Scelight-linux.sh -cp mod/launcher/3.1.3/scelight-launcher.sldat:mod/launcher/3.1.3/jl1.0.1.jar:mod/launcher/3.1.3/mp3spi1.9.5.jar:mod/launcher/3.1.3/tritonus_share.jar hu/sllauncher/ScelightLauncher "$@"