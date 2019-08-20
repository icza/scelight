#!/bin/bash
cd "${0%/*}"
java -Xmx1024m -Dfile.encoding=UTF-8 -Dhu.scelight.launched-with=Scelight-os-x.command -cp mod/launcher/3.1.4/scelight-launcher.sldat:mod/launcher/3.1.4/jl1.0.1.jar:mod/launcher/3.1.4/mp3spi1.9.5.jar:mod/launcher/3.1.4/tritonus_share.jar hu/sllauncher/ScelightLauncher "$@"