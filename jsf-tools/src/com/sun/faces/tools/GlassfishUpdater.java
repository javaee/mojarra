/*
 * GlassfishUpdater.java
 *
 * Created on January 25, 2006, 4:29 PM
 *
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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.jar.JarOutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author edburns
 */
public class GlassfishUpdater {
    
    private static final String ASADMIN_NAME = "asadmin";
    private static final String BACKUP_SUFFIX = "jsfbak";
    
    /** Creates a new instance of GlassfishUpdater */
    protected GlassfishUpdater() {
    }
    
    protected static File libDir = null;
    
    public static void main(String args[]) throws IOException {
        if (0 == args.length) {
            printUsage();
            return;
            
        }
        File testFile = null,
             gfInstallDir = new File(args[0]);

        // Have we been given a directory?
        if (!gfInstallDir.isDirectory()) {
            printUsage();
            return;
        }
        // Is it a glassfish install directory?
        testFile = new File(gfInstallDir,"bin");
        File [] files = testFile.listFiles();
        boolean foundAsdmin = false;
        for (File cur : files) {
            if (-1 != cur.getName().indexOf("asadmin")) {
                foundAsdmin = true;
                break;
            }
        }
        if (!foundAsdmin) {
            printUsage();
            return;
        }
        
        // Get the glassfish lib directory
        libDir = new File(gfInstallDir, "lib");
        if (!libDir.isDirectory()) {
            printUsage();
            return;
        }
        
        if (licenseAccepted()) {
            System.out.println("Updating glassfish at\n" + gfInstallDir.toString());
            System.out.println("with new JSF jars.");
            stripJsfFromJavaEEJar(libDir);
            unpackJsfJarsToLib(libDir);
        }
        
    }
    
    public static boolean licenseAccepted() throws IOException {
        boolean result = false;
        InputStream is = Thread.currentThread().getContextClassLoader().
                getResourceAsStream("BINARY_LICENSE.txt");
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String line = null;
        while (null != (line = br.readLine())) {
            System.out.println(line);
        }
        System.out.print("Do you accept the above license terms? (type yes or no):");
        br = new BufferedReader(new InputStreamReader(System.in));
        line = br.readLine();
        result = (null != line) && line.equals("yes");
        return result;
    }
    
    protected static boolean printUsageCalled = false;
    
    public static void printUsage() {
        printUsageCalled = true;
        System.err.println("Usage: java -jar glassfish-jsf-update.jar <glassfish install directory>");
        System.err.println("\t<glassfish install directory> is the path to the\n\tglassfish binary install.");
    }
    
    public static void stripJsfFromJavaEEJar(File libDir) throws IOException {
        if (!javaEEJarHasJsfClasses(libDir)) {
            return;
        }
        
        File javaEEJarCopy = new File(libDir, "javaee.jar.copy"),
            javaEEJar =  new File (libDir, "javaee.jar"),
            javaEEJarOrig = new File(libDir, getBackupFilename(libDir,
                "javaee.jar"));
        javaEEJarCopy.delete();
        JarInputStream origJarStream = new JarInputStream(new FileInputStream(javaEEJar));
        JarOutputStream copyJarStream = new JarOutputStream(new FileOutputStream(javaEEJarCopy));
        JarEntry newEntry = null, cur = null;
        Pattern pat = Pattern.compile(".*javax.faces.*");
        Matcher mat = null;
        byte[] buf = new byte[1024];
        int n = 0;        
        while (null != (cur = origJarStream.getNextJarEntry())) {
            mat = pat.matcher(cur.getName());
            // If the current entry does not include javax.faces...
            if (!mat.matches()) {
                // copy it to the newJar.
                newEntry = new JarEntry(cur);
                copyJarStream.putNextEntry(newEntry);
                while((n = origJarStream.read(buf, 0, buf.length)) != -1) {
                    copyJarStream.write(buf, 0, n);
                }
            }
        }
        origJarStream.close();
        copyJarStream.close();
        
        javaEEJar.renameTo(javaEEJarOrig);
        javaEEJarCopy.renameTo(javaEEJar);
                
    }
    
    public static boolean javaEEJarHasJsfClasses(File libDir) throws IOException {
        boolean result = false;
        File javaEEJar =  new File (libDir, "javaee.jar");
        JarInputStream origJarStream = new JarInputStream(new FileInputStream(javaEEJar));
        JarEntry newEntry = null, cur = null;
        Pattern pat = Pattern.compile(".*javax.faces.*");
        Matcher mat = null;
        byte[] buf = new byte[1024];
        int n = 0;        
        while (null != (cur = origJarStream.getNextJarEntry())) {
            mat = pat.matcher(cur.getName());
            // If the current entry does not include javax.faces...
            if (mat.matches()) {
                result = true;
            }
        }
        origJarStream.close();
        return result;
    }
    
    public static void unpackJsfJarsToLib(File libDir) throws IOException {
        InputStream is = Thread.currentThread().getContextClassLoader().
                getResourceAsStream("jsf-api.jar");
        File 
             jsfApiCopy = new File(libDir, getBackupFilename(libDir,
                "jsf-api.jar")),
             jsfApi = new File(libDir, "jsf-api.jar"),
             jsfImplCopy = new File(libDir, getBackupFilename(libDir,
                "jsf-impl.jar")),
             jsfImpl = new File(libDir, "jsf-impl.jar");
        jsfApiCopy.delete();
        jsfImplCopy.delete();
        jsfApi.renameTo(jsfApiCopy);
        jsfImpl.renameTo(jsfImplCopy);
        FileOutputStream fos = new FileOutputStream(jsfApi);
        int n = 0;
        while (-1 != (n = is.read())) {
            fos.write(n);
        }
        is.close();
        fos.close();
        is = Thread.currentThread().getContextClassLoader().
               getResourceAsStream("jsf-impl.jar");
        fos = new FileOutputStream(jsfImpl);
        while (-1 != (n = is.read())) {
            fos.write(n);
        }
        is.close();
        fos.close();
    }
    
    protected static String getBackupFilename(File libDir, String filename) {
       File [] files = libDir.listFiles();
       Pattern pat = Pattern.compile(".*" + BACKUP_SUFFIX + "[0-9]*");
       Matcher mat = null;
       ArrayList<File> backupFiles = new ArrayList<File>();
       for (File cur : files) {
           mat = pat.matcher(cur.getName());
           // If the current entry is a backup file...
           if (mat.matches()) {
               // add it to the list.
               backupFiles.add(cur);
           }
       }
       int i = 1;
       int curInt = 0;
       String curSuffix = null, curName = null;
       if (0 < backupFiles.size()) {
           // Find the highest backup number and go one more.
           for (File cur : backupFiles) {
               curName = cur.getName();
               curSuffix = curName.substring(curName.indexOf(BACKUP_SUFFIX));
               if (BACKUP_SUFFIX.length() < curSuffix.length()) {
                   
                   curInt = Integer.valueOf(curSuffix.
                           substring(BACKUP_SUFFIX.length())).intValue();
                   if (i == curInt) {
                       i = curInt + 1;
                   }
                   else if (i < curInt) { 
                       i = curInt;
                   }
               }
           }
       }
       
       return filename + BACKUP_SUFFIX + i;
    }
    
}
