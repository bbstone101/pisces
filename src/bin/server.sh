#!/bin/bash

scriptdir="`dirname ${0}`";
cd "${scriptdir}/..";
wd="`pwd`";
libdir="${wd}/lib";

classpath="${libdir}/*:${wd}/conf";
echo "using classpath: ${classpath}";

mainclass="com.bbstone.pisces.server.ServerStarter";


java \
  -Dappdir=${wd} \
  -cp ${classpath} \
  ${mainclass}

