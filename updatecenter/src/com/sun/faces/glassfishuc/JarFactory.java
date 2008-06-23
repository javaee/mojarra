/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
 * 
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License. You can obtain
 * a copy of the License at https://glassfish.dev.java.net/public/CDDL+GPL.html
 * or glassfish/bootstrap/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 * 
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at glassfish/bootstrap/legal/LICENSE.txt.
 * Sun designates this particular file as subject to the "Classpath" exception
 * as provided by Sun in the GPL Version 2 section of the License file that
 * accompanied this code.  If applicable, add the following below the License
 * Header, with the fields enclosed by brackets [] replaced by your own
 * identifying information: "Portions Copyrighted [year]
 * [name of copyright owner]"
 * 
 * Contributor(s):
 * 
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


package com.sun.faces.glassfishuc;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.IOException;

import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarException;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;
import java.util.jar.Pack200;
import java.util.jar.Pack200.*;
import java.io.File;

import java.util.zip.GZIPInputStream;
import java.util.zip.ZipFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;

/*
 * A utility class to manipulate Jar files.
 */
public class JarFactory
{
    // Buffer to read data 
    private byte[] mBuffer;
    
    // Unzip Directory 
    private String mDir;
    
    // Extension of a packed file
    String ext = ".pack.gz";

    // unpacker
    Unpacker unpacker = Pack200.newUnpacker();

    /**
      * @param dir Directory to unjar
      */
    public JarFactory (String dir)
    {
        
        this.mDir = dir;
        this.mBuffer = new byte[8092];
        
    }
    
    /**
     * Unjars a file.
     *
     * @param jarFile File to be unjared
     * @throws IOException if error trying to read entry.
     * @throws java.util.jar.JarException if error trying to read jar file.
     */
    public void unJar (File jarFile) throws IOException, JarException, ZipException
    {
        
        // process all entries in that JAR file
        JarFile jar = new JarFile (jarFile);
        unJar(jar);
        jar.close();
    }
    
    /**
     * Unjars a file.
     *
     * @param jar JarFile to be unjared
     * @throws IOException if error trying to read entry.
     * @throws java.util.jar.JarException if error trying to read jar file.
     */
    public void unJar (java.util.zip.ZipFile jar) throws IOException, ZipException
    {
        
        // process all entries in that JAR file
        Enumeration all = jar.entries ();
        while (all.hasMoreElements ())
        {
            getEntry (jar, ((ZipEntry) (all.nextElement ())));
        }
    }
    
    /**
     * Gets one file <code>entry</code> from <code>jarFile</code>.
     *
     * @param jarFile the JAR file reference to retrieve <code>entry</code> from.
     * @param entry the file from the JAR to extract.
     * @return the ZipEntry name
     * @throws IOException if error trying to read entry.
     */
    public String getEntry (ZipFile jarFile, ZipEntry entry) throws IOException
    {
        
        String entryName = entry.getName ();

        // if a directory, mkdir it (remember to create 
        // intervening subdirectories if needed!)
        if (entryName.endsWith ("/"))
        {
            new File (mDir, entryName).mkdirs ();
            return entryName;
        }
        
        File f = new File (mDir, entryName);
        
        if (!f.getParentFile ().exists ())
        {
            f.getParentFile ().mkdirs ();
        }
        
        // Must be a file; create output stream to the file
        FileOutputStream fostream = new FileOutputStream (f);
        InputStream istream = jarFile.getInputStream (entry);
        
        // extract files
        int n = 0;
        while ((n = istream.read (mBuffer)) > 0)
        {
            fostream.write (mBuffer, 0, n);
        }
        
        try
        {
            istream.close ();
            fostream.close ();
        }
        catch (IOException e)
        {
            // do nothing
        }

        // unpack file if needed
        if (entryName.endsWith(ext)) {
            unpackFile(mDir, entryName);
        }

        return entryName;
    }


    public void unpackFile(String dir, String fileName) throws FileNotFoundException, IOException {

        int dotIndex = fileName.length() - ext.length();

        String jarName = fileName.substring(0, dotIndex);
        String jarPath = dir + File.separator + jarName;
        String packedPath = dir + File.separator + fileName;
        File packedFile = new File(packedPath);

        FileOutputStream fostream = new FileOutputStream(jarPath);
        JarOutputStream jostream = new JarOutputStream(
                new BufferedOutputStream(fostream));

        FileInputStream fis = new FileInputStream(packedPath);
        InputStream is = new BufferedInputStream(new GZIPInputStream(fis));
        unpacker.unpack(is, jostream);
        fis.close();
        jostream.close();
        packedFile.delete();
    }


}
