#!/bin/bash

#
# DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
# 
# Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
# 
# The contents of this file are subject to the terms of either the GNU
# General Public License Version 2 only ("GPL") or the Common Development
# and Distribution License("CDDL") (collectively, the "License").  You
# may not use this file except in compliance with the License. You can obtain
# a copy of the License at https://glassfish.dev.java.net/public/CDDL+GPL.html
# or glassfish/bootstrap/legal/LICENSE.txt.  See the License for the specific
# language governing permissions and limitations under the License.
# 
# When distributing the software, include this License Header Notice in each
# file and include the License file at glassfish/bootstrap/legal/LICENSE.txt.
# Sun designates this particular file as subject to the "Classpath" exception
# as provided by Sun in the GPL Version 2 section of the License file that
# accompanied this code.  If applicable, add the following below the License
# Header, with the fields enclosed by brackets [] replaced by your own
# identifying information: "Portions Copyrighted [year]
# [name of copyright owner]"
# 
# Contributor(s):
# 
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


#
# List all files added, modified, and removed from a cvs repository
#

FILE=/tmp/lmtxt
CB=$PWD/changebundle.txt
DIFF=/tmp/diff.txt
ZIP=newfiles.zip

echo "Scanning for modifications..."

cvs -n update -dP > $FILE 2>&1

modifiedFiles=`grep ^"M " $FILE`
addedFiles=`grep ^"A " $FILE`
removedFiles=`grep ^"R " $FILE`

if [[ -z "$modifiedFiles" && -z "$addedFiles" && -z "$removedFiles" ]] 
then
    echo "No modifications - change bundle creation not necessary."
    exit 0;
fi 

echo -n "Modifications found.  Input issue or CR number: "
read issue

echo "" > $CB
if [[ -z "${issue}" ]] 
then 
    echo "<< ADD DESCRIPTION HERE >>" >> $CB
else
    echo "<< https://javaserverfaces.dev.java.net/issues/show_bug.cgi?id=${issue} >>" >> $CB
fi
echo "" >> $CB
echo "" >> $CB

if [[ -n "$modifiedFiles" || -n "$addedFiles" || -n "$removedFiles" ]] 
then
    echo "SECTION: Modified Files" >> $CB
    echo "----------------------------" >> $CB

    if [ -n "$modifiedFiles" ]; then
        grep ^"M " $FILE >> $CB
        echo "" >> $CB
        echo "" >> $CB
    fi

    if [ -n "$addedFiles" ]; then
        grep ^"A " $FILE >> $CB
        echo "" >> $CB
        echo "" >> $CB
    fi

    if [ -n "$removedFiles" ]; then
        grep ^"R " $FILE >> $CB
        echo "" >> $CB
        echo "" >> $CB
    fi
    
    if [ -n "$modifiedFiles" ]; then
        echo "SECTION: Diffs" >> $CB
        echo "----------------------------" >> $CB
        cvs diff -u > $DIFF
        grep -v ^\? $DIFF >> $CB
    fi

    echo "" >> $CB
    echo "" >> $CB
fi


if [ -n "$addedFiles" ]; then
    echo "SECTION: New Files" >> $CB
    echo "----------------------------" >> $CB
    echo "SEE ATTACHMENTS" >> $CB
    
    echo ""
    echo "Creating ZIP file with new files..."
    echo ""
    if [ -e "$ZIP" ]; then
        rm -rf newfiles.zip
    fi

    touch test.txt
    zip newfiles.zip test.txt
    for line in `cat $FILE | grep ^"A " | awk '{print $2}'`; 
    do
        zip -u newfiles.zip $line
    done
    zip -d newfiles.zip test.txt
    rm test.txt
    echo ""
    echo "ZIP file, newfiles.zip, created."
fi

rm $FILE
rm $DIFF

echo ""

echo "Change bundle complete."
