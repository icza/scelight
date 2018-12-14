# This module is adapted from Canonical's gradle Snapcraft plugin:
# https://github.com/snapcore/snapcraft/blob/master/snapcraft/plugins/gradle.py
#
# Canonical's gradle plugin only produces a .jar or .war file.
# This plugin is for producing a gradle java application distribution.

import logging
import os
import tarfile
import urllib.parse
from glob import glob
from typing import Sequence

import snapcraft
from snapcraft import file_utils, formatting_utils
from snapcraft.internal import errors, sources

logger = logging.getLogger(__name__)


_DEFAULT_GRADLE_VERSION = "4.10.2"
_DEFAULT_GRADLE_CHECKSUM = (
    "sha256/b49c6da1b2cb67a0caf6c7480630b51c70a11ca2016ff2f555eaeda863143a29"
)
_GRADLE_URL = "https://services.gradle.org/distributions/gradle-{version}-bin.zip"


class UnsupportedJDKVersionError(errors.SnapcraftError):

    fmt = (
        "The gradle-openjdk-version plugin property was set to {version!r}.\n"
        "Valid values for the {base!r} base are: {valid_versions}."
    )

    def __init__(
            self, *, base: str, version: str, valid_versions: Sequence[str]
    ) -> None:
        super().__init__(
            base=base,
            version=version,
            valid_versions=formatting_utils.humanize_list(
                valid_versions, conjunction="or"
            ),
        )


class GradlePlugin(snapcraft.BasePlugin):
    @classmethod
    def schema(cls):
        schema = super().schema()
        schema["properties"]["gradle-options"] = {
            "type": "array",
            "minitems": 1,
            "uniqueItems": True,
            "items": {"type": "string"},
            "default": [],
        }
        schema["properties"]["gradle-output-dir"] = {
            "type": "string",
            "default": "build/distributions",
        }

        schema["properties"]["gradle-version"] = {"type": "string"}

        schema["properties"]["gradle-version-checksum"] = {"type": "string"}

        schema["properties"]["gradle-openjdk-version"] = {
            "type": "string",
            "default": "",
        }
        
        schema["required"] = []
        # schema["required"] = ["source"]

        return schema

    @classmethod
    def get_pull_properties(cls):
        # Inform Snapcraft of the properties associated with pulling. If these
        # change in the YAML Snapcraft will consider the pull step dirty.
        return ["gradle-version", "gradle-version-checksum", "gradle-openjdk-version"]

    @classmethod
    def get_build_properties(cls):
        # Inform Snapcraft of the properties associated with building. If these
        # change in the YAML Snapcraft will consider the build step dirty.
        return super().get_build_properties() + ["gradle-options", "gradle-output-dir"]

    @property
    def _gradle_tar(self):
        if self._gradle_tar_handle is None:
            gradle_uri = _GRADLE_URL.format(version=self._gradle_version)
            self._gradle_tar_handle = sources.Zip(
                gradle_uri, self._gradle_dir, source_checksum=self._gradle_checksum
            )
        return self._gradle_tar_handle

    def __init__(self, name, options, project):
        super().__init__(name, options, project)

        self._setup_gradle()
        self._setup_base_tools(project.info.base)
        
    def _get_openjdk_version(self, base):
        if base not in ("core16", "core18"):
            raise errors.PluginBaseError(
                part_name=self.name, base=self.project.info.base
            )
        
        if base == "core16":
            valid_versions = ["8", "9"]
        elif base == "core18":
            valid_versions = ["8", "11"]
    
        version = self.options.gradle_openjdk_version
        if version and version not in valid_versions:
            raise UnsupportedJDKVersionError(
                version=version, base=base, valid_versions=valid_versions
            )
        elif not version:
            # Get the latest version from the slice
            version = valid_versions[-1]
            
        return version

    def _setup_base_tools(self, base):
        version = self._get_openjdk_version(base)
        self.stage_packages.append("openjdk-{}-jre-headless".format(version))
        self.build_packages.append("openjdk-{}-jdk-headless".format(version))
        self.build_packages.append("ca-certificates-java")

    def _using_gradlew(self) -> bool:
        return os.path.isfile(os.path.join(self.sourcedir, "gradlew"))

    def _setup_gradle(self):
        self._gradle_tar_handle = None
        self._gradle_dir = os.path.join(self.partdir, "gradle")
        if self.options.gradle_version:
            self._gradle_version = self.options.gradle_version
            self._gradle_checksum = self.options.gradle_version_checksum
        else:
            self._gradle_version = _DEFAULT_GRADLE_VERSION
            self._gradle_checksum = _DEFAULT_GRADLE_CHECKSUM

    def pull(self):
        super().pull()

        if self._using_gradlew():
            logger.info("Found gradlew, skipping gradle setup.")
            return

        os.makedirs(self._gradle_dir, exist_ok=True)
        self._gradle_tar.download()

    def build(self):
        super().build()

        if self._using_gradlew():
            gradle_cmd = ["./gradlew"]
        else:
            self._gradle_tar.provision(self._gradle_dir, keep_zip=True)
            gradle_cmd = ["gradle"]
        self.run(
            gradle_cmd
            + self._get_proxy_options()
            + self.options.gradle_options
            + ["build"],
            rootdir=self.builddir,
            )

        src = os.path.join(self.builddir, self.options.gradle_output_dir)
        tarFiles = glob(os.path.join(src, "*.tar"))

        if len(tarFiles) >= 1:
            tarFile = tarFiles[0]
        else:
            raise RuntimeError("Could not find any built tar or zip files for part")
        
        tarFile = tarFiles[0]
        tar = tarfile.open(tarFile)
        distribution_dir = os.path.join(self.builddir, "distribution")
        tar.extractall(path = distribution_dir)
        tar.close()

        file_utils.link_or_copy_tree(
            os.path.join(distribution_dir, self.project.info.name),
            os.path.join(self.installdir + "/"),
        )
        
    def run(self, cmd, rootdir):
        super().run(cmd, cwd=rootdir, env=self._build_environment())

    def _build_environment(self):
        if self._using_gradlew():
            return

        env = os.environ.copy()
        gradle_bin = os.path.join(
            self._gradle_dir, "gradle-{}".format(self._gradle_version), "bin"
        )
        print("gradle_bin")
        print(gradle_bin)

        if env.get("PATH"):
            new_path = "{}:{}".format(gradle_bin, env.get("PATH"))
        else:
            new_path = gradle_bin

        env["PATH"] = new_path
        return env

    def _get_proxy_options(self):
        proxy_options = []
        for var in ("http", "https"):
            proxy = os.environ.get("{}_proxy".format(var), False)
            if proxy:
                parsed_url = urllib.parse.urlparse(proxy)
                proxy_options.append(
                    "-D{}.proxyHost={}".format(var, parsed_url.hostname)
                )
                if parsed_url.port:
                    proxy_options.append(
                        "-D{}.proxyPort={}".format(var, parsed_url.port)
                    )
                if parsed_url.username:
                    proxy_options.append(
                        "-D{}.proxyUser={}".format(var, parsed_url.username)
                    )
                if parsed_url.password:
                    proxy_options.append(
                        "-D{}.proxyPassword={}".format(var, parsed_url.password)
                    )
        return proxy_options
