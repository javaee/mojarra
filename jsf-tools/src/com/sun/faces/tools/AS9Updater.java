/*
 * AS9Updater.java
 *
 * Created on February 24, 2006, 3:41 PM
 * $Id: AS9Updater.java,v 1.1 2006/02/27 15:33:11 edburns Exp $
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

package com.sun.faces.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.jar.JarEntry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.jar.JarOutputStream;
import java.util.jar.JarInputStream;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;

/**
 *
 * @author edburns
 */
public class AS9Updater extends GlassfishUpdater {
    
    /** Creates a new instance of AS9Updater */
    public AS9Updater() {
    }

    public static void main(String args[]) throws IOException {  
        GlassfishUpdater.main(args);
        
        if (printUsageCalled) {
            return;
        }
        
        updateAppservRT(libDir);
    }  
    
    public static void updateAppservRT(File libDir) throws IOException {
        File rtJarCopy = new File(libDir, "appserv-rt.jar.copy"),
            rtJar =  new File (libDir, "appserv-rt.jar"),
            rtJarOrig = new File(libDir, getBackupFilename(libDir,
                "appserv-rt.jar"));
        rtJarCopy.delete();
        JarInputStream origJarStream = new JarInputStream(new FileInputStream(rtJar));
        Manifest oldMan = null;
        
        oldMan = origJarStream.getManifest();
        JarOutputStream copyJarStream = new JarOutputStream(new FileOutputStream(rtJarCopy),
                oldMan);
        JarEntry newEntry = null, cur = null;
        Pattern pat = Pattern.compile("org/apache/jasper/compiler/TldLocationsCache.class");
        Matcher mat = null;
        byte[] buf = new byte[1024];
        int n = 0;    
        
        while (null != (cur = origJarStream.getNextJarEntry())) {
            mat = pat.matcher(cur.getName());
            // If the current entry does match jasper...
            if (!mat.matches()) {
                // copy it to the newJar.
                newEntry = new JarEntry(cur);
                copyJarStream.putNextEntry(newEntry);
                while((n = origJarStream.read(buf, 0, buf.length)) != -1) {
                    copyJarStream.write(buf, 0, n);
                }
            }
            else {
                InputStream is = Thread.currentThread().getContextClassLoader().
                        getResourceAsStream(cur.getName());

                // replace the TldLocationsCache.class
                newEntry = new JarEntry(cur.getName());
                copyJarStream.putNextEntry(newEntry);
                while ((n = is.read(buf, 0, buf.length)) != -1) {
                    copyJarStream.write(buf, 0, n);
                }
            }
        }
        
        origJarStream.close();
        copyJarStream.close();
        
        rtJar.renameTo(rtJarOrig);
        rtJarCopy.renameTo(rtJar);
        
    }

}
