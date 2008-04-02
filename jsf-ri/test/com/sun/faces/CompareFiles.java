/*
 * $Id: CompareFiles.java,v 1.16 2005/08/22 22:11:05 ofung Exp $
 */

/*
 * The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the License). You may not use this file except in
 * compliance with the License.
 * 
 * You can obtain a copy of the License at
 * https://javaserverfaces.dev.java.net/CDDL.html or
 * legal/CDDLv1.0.txt. 
 * See the License for the specific language governing
 * permission and limitations under the License.
 * 
 * When distributing Covered Code, include this CDDL
 * Header Notice in each file and include the License file
 * at legal/CDDLv1.0.txt.    
 * If applicable, add the following below the CDDL Header,
 * with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 * 
 * [Name of File] [ver.__] [Date]
 * 
 * Copyright 2005 Sun Microsystems Inc. All Rights Reserved
 */

package com.sun.faces;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.Iterator;
import java.util.List;

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
    public static boolean filesIdentical(String newFileName, String oldFileName, List oldLinesToIgnore)
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
                } else {
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
