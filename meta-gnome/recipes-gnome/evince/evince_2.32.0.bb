DESCRIPTION = "Evince is a document viewer for document formats like pdf, ps, djvu."
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=96f2f8d5ee576a2163977938ea36fa7b"
SECTION = "x11/office"
DEPENDS = "gnome-icon-theme gnome-doc-utils-native libgnome-keyring nautilus tiff libxt ghostscript poppler libxml2 gtk+ gconf libglade"

PR = "r1"

inherit gnome pkgconfig gtk-icon-cache

SRC_URI += "file://cross-compile-fix.patch"

SRC_URI[archive.md5sum] = "ebc3ce6df8dcbf29cb9492f8dd031319"
SRC_URI[archive.sha256sum] = "2a4c91ae38f8b5028cebb91b9da9ddc50ea8ae3f3d429df89ba351da2d787ff7"

EXTRA_OECONF = " --enable-thumbnailer \
                 --enable-nautilus \
                 --disable-scrollkeeper \
                 --enable-pixbuf \
               "

do_install_append() {
	install -d install -d ${D}${datadir}/pixmaps
	install -m 0755 ${S}/data/icons/48x48/apps/evince.png ${D}${datadir}/pixmaps/
}

RDEPENDS_${PN} += "glib-2.0-utils"

PACKAGES =+ "${PN}-nautilus-extension"
FILES_${PN} += "${datadir}/dbus-1"
FILES_${PN}-dbg += "${libdir}/*/*/.debug \
                    ${libdir}/*/*/*/.debug"
FILES_${PN}-dev += "${libdir}/nautilus/extensions-2.0/*.la"
FILES_${PN}-staticdev += "${libdir}/nautilus/extensions-2.0/*.a"
FILES_${PN}-nautilus-extension = "${libdir}/nautilus/*/*so"

pkg_postinst_${PN} () {
if [ -n "$D" ]; then
    exit 1
fi

glib-compile-schemas ${datadir}/glib-2.0/schemas
}
