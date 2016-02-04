
========================================================================================
SCELIGHT
========================================================================================

Home page
https://sites.google.com/site/scelight/

Copyright
Copyright (c) Andras Belicza <iczaaa@gmail.com>. All rights reserved.

License
See inside Scelight.


========================================================================================
INSTALLATION INSTRUCTIONS
========================================================================================

Just download the zip file and extract it to anywhere you like. You're done.
It is STRONGLY advised not to put Scelight in special folders like "C:\Program Files"
where normal users do not have write permission therefore Scelight would not be able
to perform updates or save settings.

On MAC OS-X you can start Scelight by double clicking on the file
"Scelight-os-x-command".

Note that on Linux and MAC OS-X you (might) have to set executable permission on the
starter script. Open a console/terminal, navigate to the Scelight folder where you
have extracted it, and execute the following command:

OS-X:  "chmod +x Scelight-os-x.command"
Linux: "chmod +x Scelight-linux.sh"

This only has to be done once. After that you can start Scelight with the following
command:

Windows: double click on the "Scelight.exe"
Linux:   "./Scelight-linux.sh"
OS-X:    "./Scelight-os-x.command"


========================================================================================
SCELIGHT FILE LIST
========================================================================================

Scelight.exe
    Primary Scelight starter application for Windows.

Scelight-admin.exe
    Primary Scelight starter application for Windows which runs Scelight with
    administrator privileges.

Scelight-win.cmd
    Secondary Scelight starter script for Windows, it also allocates and displays
    a console window.

Scelight-os-x.command
    Primary Scelight starter script for MAC OS-X.

Scelight-linux.sh
    Primary Scelight starter script for Linux.

README.txt
    This file.

mod/
    This folder is reserved for internal modules and data of Scelight.

mod-x/
    This folder is for external modules of Scelight (both official and unofficial).

mod-x/README.txt
    Basic info file about external modules.

jre/
    Optional prepared / bundled JRE (Java Runtime Environment). It can be downloaded
    from the Scelight Downloads page. If present, it will be used to run Scelight.
