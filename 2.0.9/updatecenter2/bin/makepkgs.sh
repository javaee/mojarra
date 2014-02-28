#!/bin/sh

# Remember to set pkg.dir in build.properties!  
# This value is passed to this program as $1

# set this to the location of the Updatecenter2 tools
UCHOME=$1
export UCHOME

# add OpenSSL path for Solaris
LD_LIBRARY_PATH=$UCHOME/python2.4-minimal/lib
export LD_LIBRARY_PATH 

# add the necessary libraries to python
PYTHONPATH=$UCHOME/vendor-packages:.
export PYTHONPATH

# execute the bundled python
PYTHONBIN=$UCHOME/python2.4-minimal/bin/python
export PYTHONBIN

# create the repo
# new version 
#$PYTHONBIN $UCHOME/bin/makepkgs.py -s file://`pwd`/repo -b dist conf/jsf_proto.py
# old version
#$PYTHONBIN $UCHOME/bin/makepkgs.py -d `pwd`/repo -b dist conf/jsf_proto.py

# Or, optionally, put the repo files into a local repository
#$PYTHONBIN $UCHOME/bin/makepkgs.py -s http://localhost:10000 -b dist conf/jsf_proto.py

# Or, optionally, put the repo files into the dev test repository
$PYTHONBIN $UCHOME/bin/makepkgs.py -s http://eflat.sfbay.sun.com:60000 -b dist conf/jsf_proto.py

