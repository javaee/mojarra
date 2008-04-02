#!/bin/bash

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
