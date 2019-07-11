#!/bin/bash

ZULU_PACK=zulu-11-azure-jdk_11.31.11-11.0.3-linux_musl_x64.tar.gz
INSTALL_DIR=/usr/lib/jvm
BIN_DIR=/usr/bin
MAN_DIR=/usr/share/man/man1
ZULU_DIR=$( basename ${ZULU_PACK} .tar.gz )

apk --no-cache add ca-certificates libgcc libstdc++ ttf-dejavu wget

echo "Build to wget"
wget -q http://repos.azul.com/azure-only/zulu/packages/zulu-11/11.0.3/$ZULU_PACK

#wget --show-progress http://repos.azul.com/azure-only/zulu/packages/zulu-11/11.0.3/$ZULU_PACK
echo "Build  2"
mkdir -p ${INSTALL_DIR}
echo "Build  3"
tar -xf ./${ZULU_PACK} -C ${INSTALL_DIR} && rm -f ${ZULU_PACK}
echo "Build  4"
cd ${BIN_DIR}
echo "Build  5"

find ${INSTALL_DIR}/${ZULU_DIR}/bin -type f -perm -a=x -exec ln -s {} . \;

echo "Build  6"

mkdir -p ${MAN_DIR}
echo "Build  7"
cd ${MAN_DIR}

echo "Build 8"

find ${INSTALL_DIR}/${ZULU_DIR}/man/man1 -type f -name "*.1" -exec ln -s {} . \;
echo "Done and done!"