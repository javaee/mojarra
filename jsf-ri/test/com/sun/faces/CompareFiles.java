/*
 * $Id: CompareFiles.java,v 1.3 2002/05/31 19:34:14 jvisvanathan Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces;

import java.io.*;

import java.util.List;
import java.util.Iterator;

public class CompareFiles {

    public CompareFiles() {
    }

    /**
     * This method compares the input files character by character. 
     * Skips whitespaces and comparison is not case sensitive.
     */
    public static boolean filesIdentical (String newFileName, String oldFileName, List oldLinesToIgnore) 
            throws IOException {

        boolean same = true;

        File newFile = new File(newFileName);
        File oldFile = new File(oldFileName);

        FileReader newFileReader = new FileReader(newFile);
        FileReader oldFileReader = new FileReader(oldFile);
	LineNumberReader newReader = new LineNumberReader(newFileReader);
	LineNumberReader oldReader = new LineNumberReader(oldFileReader);

	String newLine, oldLine;

	newLine = newReader.readLine();
	oldLine = oldReader.readLine();

	// if one of the lines is null, but not the other
	if (((null == newLine) && (null != oldLine)) ||
	    ((null != newLine) && (null == oldLine))) {
	    same = false;
	}

	while (null != newLine && null != oldLine) {
	    if (!newLine.equalsIgnoreCase(oldLine)) {

		if (null != oldLinesToIgnore) {
		    // go thru the list of oldLinesToIgnore and see if
		    // the current oldLine matches any of them.
		    Iterator ignoreLines = oldLinesToIgnore.iterator();
		    boolean foundMatch = false;
		    while (ignoreLines.hasNext()) {
			if (oldLine.equalsIgnoreCase((String) 
						     ignoreLines.next())) {
			    foundMatch = true;
			    break;
			}
		    }
		    // If we haven't found a match, then this mismatch is
		    // important
		    if (!foundMatch) {
			same = false;
			break;
		    }
		}
		else {
		    same = false;
		    break;
		}
	    }
	    
	    newLine = newReader.readLine();
	    oldLine = oldReader.readLine();

	    // if one of the lines is null, but not the other
	    if (((null == newLine) && (null != oldLine)) ||
		((null != newLine) && (null == oldLine))) {
		same = false;
		break;
	    }
	}

        newReader.close();
        oldReader.close();

        // if same is true and both files have reached eof, then
        // files are identical
        if (same == true) {
            return true;  
        } 
        return false;
    }
}
