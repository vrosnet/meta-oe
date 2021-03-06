DESCRIPTION = "Systemd serial config"
LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://${WORKDIR}/LICENSE;md5=751419260aa954499f7abaabaa882bbe"

PR = "r1"

SERIAL_CONSOLE ?= "115200 ttyS0"

SRC_URI = "file://LICENSE \
           file://serial-getty@.service"

def get_baudrate(bb, d):
    return bb.data.getVar('SERIAL_CONSOLE', d, 1).split()[0]

def get_console(bb, d):
    return bb.data.getVar('SERIAL_CONSOLE', d, 1).split()[1]

do_install() {
	if [ ! ${@get_baudrate(bb, d)} = "" ]; then
		sed -i -e s/\@BAUDRATE\@/${@get_baudrate(bb, d)}/g ${WORKDIR}/serial-getty@.service
		install -d ${D}${base_libdir}/systemd/system/
		install -d ${D}${sysconfdir}/systemd/system/getty.target.wants/
		install ${WORKDIR}/serial-getty@.service ${D}${base_libdir}/systemd/system/

		# enable the service
		ln -sf ${base_libdir}/systemd/system/serial-getty@.service \
			${D}${sysconfdir}/systemd/system/getty.target.wants/serial-getty@${@get_console(bb, d)}.service
	fi
}

PACKAGES = "${PN} ${PN}-dbg ${PN}-dev ${PN}-doc"

RRECOMMENDS_${PN} = ""
RDEPENDS_${PN} = "systemd"

# This is a machine specific file
FILES_${PN} = "${base_libdir}/systemd/system/serial-getty@.service ${sysconfdir}"
PACKAGE_ARCH = "${MACHINE_ARCH}"
