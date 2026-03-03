#!/bin/bash
# Script to build image for qemu.
# Author: Siddhant Jajoo.

git submodule init
git submodule sync
git submodule update

# local.conf won't exist until this step on first execution
source poky/oe-init-build-env

CONFLINE="MACHINE = \"qemuarm64\""

cat conf/local.conf | grep "${CONFLINE}" > /dev/null
local_conf_info=$?

if [ $local_conf_info -ne 0 ];then
	echo "Append ${CONFLINE} in the local.conf file"
	echo ${CONFLINE} >> conf/local.conf
	
else
	echo "${CONFLINE} already exists in the local.conf file"
fi

echo "INHERIT += \"rm_work\"" >> conf/local.conf
#echo "PARALLEL_MAKE = \"-j 2\"" >> conf/local.conf
#echo "BB_NUMBER_THREADS = \"2\"" >> conf/local.conf
echo "rm_work added for disk space issue and decreased cores for ram issue"

bitbake-layers show-layers | grep "meta-aesd" > /dev/null
layer_info=$?

if [ $layer_info -ne 0 ];then
	echo "Adding meta-aesd layer"
	bitbake-layers add-layer ../meta-aesd
else
	echo "meta-aesd layer already exists"
fi

set -e
bitbake -c clean cmake-native itstool-native
bitbake cmake-native itstool-native
bitbake -c clean gcc-cross-aarch64
bitbake gcc-cross-aarch64
bitbake core-image-aesd
