/*
 * $Id: CompareFiles.java,v 1.11 2004/01/27 21:05:46 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces;

import java.io.*;

import java.util.List;
import java.util.Iterator;

public class CompareFiles {

    public CompareFiles() {
    }

    public static String stripJsessionidFromLine(String line) {
	if (null == line) {
	    return line;
	}
	int 
	    start = 0,
	    end = 0;
	String result = line;

	if (-1 == (start = line.indexOf(";jsessionid="))) {
	    return result;
	}
	
	if (-1 == (end = line.indexOf("?", start))) {
	    if (-1 == (end = line.indexOf("\"", start))) {
		throw new IllegalStateException();
	    }
	}
	result = stripJsessionidFromLine(line.substring(0, start) + 
					 line.substring(end));
	return result;
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

	newLine = newReader.readLine().trim();
	oldLine = oldReader.readLine().trim();

	// if one of the lines is null, but not the other
	if (((null == newLine) && (null != oldLine)) ||
	    ((null != newLine) && (null == oldLine))) {
            System.out.println("1OLD=" + oldLine);
            System.out.println("1NEW=" + newLine);
	    same = false;
	}

	while (null != newLine && null != oldLine) {
	    if (!newLine.equals(oldLine)) {

		if (null != oldLinesToIgnore && oldLinesToIgnore.size() > 0) {
		    // go thru the list of oldLinesToIgnore and see if
		    // the current oldLine matches any of them.
		    Iterator ignoreLines = oldLinesToIgnore.iterator();
		    boolean foundMatch = false;
		    while (ignoreLines.hasNext()) {
			String newTrim = ((String) ignoreLines.next()).trim();
			if (oldLine.equals(newTrim)) {
			    foundMatch = true;
			    break;
			}
		    }
		    // If we haven't found a match, then this mismatch is
		    // important
		    if (!foundMatch) {
                        System.out.println("2OLD=" + oldLine);
                        System.out.println("2NEW=" + newLine);
			same = false;
			break;
		    }
		}
		else {
		    newLine = stripJsessionidFromLine(newLine);
		    oldLine = stripJsessionidFromLine(oldLine);
		    if (!newLine.equals(oldLine)) {
                        System.out.println("3OLD=" + oldLine);
                        System.out.println("3NEW=" + newLine);
			same = false;
			break;
		    }
		}
	    }
	    
	    newLine = newReader.readLine();
	    oldLine = oldReader.readLine();

	    // if one of the lines is null, but not the other
	    if (((null == newLine) && (null != oldLine)) ||
		((null != newLine) && (null == oldLine))) {
                System.out.println("4OLD=" + oldLine);
                System.out.println("4NEW=" + newLine);
		same = false;
		break;
	    }
	    if (null != newLine) {
		newLine = newLine.trim();
	    }
	    if (null != oldLine) {
		oldLine = oldLine.trim();
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
