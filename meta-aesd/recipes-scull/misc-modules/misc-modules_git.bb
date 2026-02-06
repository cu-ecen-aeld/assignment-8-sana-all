# Recipe created by recipetool
# This is the basis of a recipe and may need further editing in order to be fully functional.
# (Feel free to remove these comments when editing.)

# WARNING: the following LICENSE and LIC_FILES_CHKSUM values are best guesses - it is
# your responsibility to verify that the values are complete and correct.
#
# The following license files were not able to be identified and are
# represented as "Unknown" below, you will need to check them yourself:
#   LICENSE
SUMMARY = "Misc kernel modules"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://LICENSE;md5=f098732a73b5f6f3430472f5b094ffdb"

SRC_URI = "git://github.com/cu-ecen-aeld/assignment-7-sana-all.git;protocol=https;branch=main \
           file://0001-Trim-Makefile-to-only-build-scull-and-misc-modules.patch \
           file://S98miscmodules \
           file://misc-modules-fix-headers.patch \
           "
           


# Modify these as desired
PV = "1.0+git${SRCPV}"
SRCREV = "4c641ed43405a0bae97b9d8eebad15595e590cb0"

S = "${WORKDIR}/git"

inherit module update-rc.d
EXTRA_OEMAKE += "M=${S}/misc-modules"
#EXTRA_OEMAKE:append:task-install = " -C ${STAGING_KERNEL_DIR} M=${S}"
#EXTRA_OEMAKE += "KERNELDIR=${STAGING_KERNEL_DIR}"

DEPENDS += "virtual/kernel"

INITSCRIPT_NAME = "S98miscmodules"
INITSCRIPT_PARAMS = "defaults"

do_compile[depends] += "virtual/kernel:do_shared_workdir"

do_compile() {
    oe_runmake -C ${S}/misc-modules \
        KERNEL_SRC=${STAGING_KERNEL_BUILDDIR} \
        KERNELDIR=${STAGING_KERNEL_BUILDDIR} \
        modules
}

do_install() {
    install -d ${D}/lib/modules/${KERNEL_RELEASE}/extra
    install -m 0644 ${S}/misc-modules/*.ko ${D}/lib/modules/${KERNEL_RELEASE}/extra || true

    install -d ${D}${sysconfdir}/init.d
    install -m 0755 ${WORKDIR}/S98miscmodules ${D}${sysconfdir}/init.d/
}

FILES:${PN} += "/lib/modules/extra/*.ko"
FILES:${PN} += "/etc/init.d/S98miscmodules"
