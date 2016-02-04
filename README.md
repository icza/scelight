# Scelight

The source code of the Scelight project with all its modules. 

Scelight home page: https://sites.google.com/site/scelight/

(The Github project counts [127,000 lines](https://github.com/icza/scelight/graphs/contributors). And this is without development history, just  the snapshot of the current state of the project.)

## Directory Structure

[directory-info.html](https://github.com/icza/scelight/blob/master/directory-info.html) in the root explains / details the main folders / files:

<table>
	<tr><th colspan=2>File/folder</th><th>Description</th>
	<tr><td colspan=2><a href="https://github.com/icza/scelight/tree/master/src-launcher/hu">/src-launcher</a></td><td>Source folder of the Scelight Launcher module.</td>
	<tr><td colspan=2><a href="https://github.com/icza/scelight/tree/master/src-sc2-textures/hu/slsc2textures">/src-sc2-textures</a></td><td>Source folder of the SC2 Textures module.</td>
	<tr><td colspan=2><a href="https://github.com/icza/scelight/tree/master/src-sc2-balance-data/hu/slsc2balancedata">/src-sc2-balance-data</a></td><td>Source folder of the SC2 Balance Data module.</td>
	<tr><td colspan=2><a href="https://github.com/icza/scelight/tree/master/src-app-libs">/src-app-libs</a></td><td>Source folder of the Scelight Libs module.</td>
	<tr><td colspan=2><a href="https://github.com/icza/scelight/tree/master/src-app">/src-app</a></td><td>Source folder of the (main) Scelight module.</td>
	<tr><td colspan=2><a href="https://github.com/icza/scelight/tree/master/src-ext-mod-api">/src-ext-mod-api</a></td><td>Source folder of the External Module API.</td>
	<tr><td colspan=2><a href="https://github.com/icza/scelight/tree/master/src-tool/hu/sltool">/src-tool</a></td><td>Source folder of utility tools.</td>
	<tr><td colspan=2><a href="https://github.com/icza/scelight/tree/master/app-folder">/app-folder</a></td><td>Contains the off-line files and static files/folders required to run Scelight from Eclipse.</td>
	<tr><td rowspan=3><a href="https://github.com/icza/scelight/tree/master/dev-data">/dev-data</a></td><td><a href="https://github.com/icza/scelight/tree/master/dev-data">/</a></td><td>Contains files related to development history and metrics</td>
	<tr><td><a href="https://github.com/icza/scelight/tree/master/dev-data/source-stats">/source-stats</a></td><td>Metrics of the Scelight project.</td>
	<tr><td>/*-build-history.txt</td><td>Build history of different modules.</td>
	<tr><td rowspan=4><a href="https://github.com/icza/scelight/tree/master/docs">/docs</a></td><td><a href="https://github.com/icza/scelight/tree/master/docs">/</a></td><td>Contains some documentation of the project.</td>
	<tr><td><a href="https://github.com/icza/scelight/tree/master/docs/app-logo">/app-logo</a></td><td>First version of the app logo.</td>
	<tr><td><a href="https://github.com/icza/scelight/tree/master/docs/change-history">/change-history</a></td><td>Changes and new features of all the public releases.</td>
	<tr><td>/generated</td><td>Target folder of generated javadoc.</td>
	<tr><td colspan=2><a href="https://github.com/icza/scelight/tree/master/eclipse">/eclipse</a></td><td>Contains Eclipse configuration files (code templates and source file formatting rules).</td>
	<tr><td rowspan=7><a href="https://github.com/icza/scelight/tree/master/release">/release</a></td><td><a href="https://github.com/icza/scelight/tree/master/release">/</a></td><td>Target folder for creating releases.</td>
	<tr><td><a href="https://github.com/icza/scelight/tree/master/release/resources">/resources</a></td><td>Static resource files needed to create releases.</td>
	<tr><td>/compiled-classes</td><td>Target folder for compiled classes of releases (used during module build).</td>
	<tr><td>/deployment-dev</td><td>Deployment files (packaed modules) of the development version.</td>
	<tr><td>/deployment-pub</td><td>Deployment files (packaed modules) of the public version.</td>
	<tr><td>/Scelight</td><td>Complete Scelight version assembled after module builds.</td>
	<tr><td>/*.properties</td><td>*.properties files holding latest build numbers and build timestamps of different modules.</td>
	<tr><td colspan=2><a href="https://github.com/icza/scelight/tree/master/war/news">/war/news</a></td><td>News content for the Scelight Operator web application.</td>
	<tr><td colspan=2><a href="https://github.com/icza/scelight/blob/master/build.xml">/build.xml</a></td><td>Scelight Builder. Can build releases and modules (with deployments), generate dev javadoc and make project backup.</td>
	<tr><td colspan=2><a href="https://github.com/icza/scelight/blob/master/directory-info.html">/directory-info.html</a></td><td>This document.</td>
	<tr><td colspan=2><a href="https://github.com/icza/scelight/blob/master/Scelight.launch">/Scelight.launch</a></td><td>Eclipse launch configuration to run Scelight.</td>
	<tr><td colspan=2><a href="https://github.com/icza/scelight/blob/master/Scelight-dev.launch">/Scelight-dev.launch</a></td><td>Eclipse launch configuration to run Scelight in development mode.</td>
</table>


## Project Status

Project is active and under development.
