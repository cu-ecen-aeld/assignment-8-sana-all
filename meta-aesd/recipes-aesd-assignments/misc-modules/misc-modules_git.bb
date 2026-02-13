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
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-2.0-only;md5=801f80980d171dd6425610833a22dbe6"


inherit module update-rc.d
#inherit update-rc.d
INITSCRIPT_PACKAGES = "${PN}"
INITSCRIPT_NAME:${PN} = "S98miscmodules"
INITSCRIPT_PARAMS:${PN} = "defaults 20"

SRC_URI = "git://github.com/cu-ecen-aeld/assignment-7-sana-all.git;protocol=https;branch=main \
           file://S98miscmodules"


# Modify these as desired
PV = "1.0+git${SRCPV}"
SRCREV = "4c641ed43405a0bae97b9d8eebad15595e590cb0"


S = "${WORKDIR}/git/misc-modules"

EXTRA_OEMAKE = "KERNEL_SRC=${STAGING_KERNEL_BUILDDIR} M=${S}"

do_compile() {
    oe_runmake -C ${STAGING_KERNEL_BUILDDIR} M=${S} modules
}


do_install() {
    install -d ${D}/lib/modules/${KERNEL_VERSION}/extra
    install -m 0644 ${S}/*.ko ${D}/lib/modules/${KERNEL_VERSION}/extra || true
    install -d ${D}${sysconfdir}/init.d
    install -m 0755 ${WORKDIR}/S98miscmodules ${D}${sysconfdir}/init.d/
    
}

FILES:${PN} += "${sysconfdir}/init.d/S98miscmodules"

#EXTRA_OEMAKE:append:task-install = " -C ${STAGING_KERNEL_DIR} M=${S}"
#EXTRA_OEMAKE += "KERNELDIR=${STAGING_KERNEL_DIR}"
# 5.15.194-yocto-standard
# ${KERNEL_RELEASE}
# install -d ${D}${sysconfdir}/init.d
# install -d ${D}${sysconfdir}/init.d
# install -m 0755 ${WORKDIR}/S98miscmodules ${D}${sysconfdir}/init.d/
# install -m 0755 ${WORKDIR}/S98miscmodules ${D}${sysconfdir}/init.d/
