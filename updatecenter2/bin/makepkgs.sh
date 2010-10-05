#!/bin/sh
#
# DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
#
# Copyright (c) 2010 Oracle and/or its affiliates. All rights reserved.
#
# The contents of this file are subject to the terms of either the GNU
# General Public License Version 2 only ("GPL") or the Common Development
# and Distribution License("CDDL") (collectively, the "License").  You
# may not use this file except in compliance with the License.  You can
# obtain a copy of the License at
# https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
# or packager/legal/LICENSE.txt.  See the License for the specific
# language governing permissions and limitations under the License.
#
# When distributing the software, include this License Header Notice in each
# file and include the License file at packager/legal/LICENSE.txt.
#
# GPL Classpath Exception:
# Oracle designates this particular file as subject to the "Classpath"
# exception as provided by Oracle in the GPL Version 2 section of the License
# file that accompanied this code.
#
# Modifications:
# If applicable, add the following below the License Header, with the fields
# enclosed by brackets [] replaced by your own identifying information:
# "Portions Copyright [year] [name of copyright owner]"
#
# Contributor(s):
# If you wish your version of this file to be governed by only the CDDL or
# only the GPL Version 2, indicate your decision by adding "[Contributor]
# elects to include this software in this distribution under the [CDDL or GPL
# Version 2] license."  If you don't indicate a single choice of license, a
# recipient has the option to distribute your version of this file under
# either the CDDL, the GPL Version 2 or to extend the choice of license to
# its licensees as provided above.  However, if you add GPL Version 2 code
# and therefore, elected the GPL Version 2 license, then the option applies
# only if the new code is made subject to such option by the copyright
# holder.
#

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

