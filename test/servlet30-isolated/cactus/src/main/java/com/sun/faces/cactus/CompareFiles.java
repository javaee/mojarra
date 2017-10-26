/*
 * $Id: CompareFiles.java,v 1.1 2005/10/18 16:41:30 edburns Exp $
 */

/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2017 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */

package com.sun.faces.cactus;

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
                if (oldLine.contains("javax.faces.Token")) {
                    break;
                }
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
