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

import com.sun.appserv.addons.AddonVersion;
import com.sun.appserv.addons.InstallationContext;
import com.sun.appserv.addons.Installer;
import com.sun.appserv.addons.AddonFatalException;
import com.sun.appserv.addons.AddonException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.ZipException;

/**
 * Installer implementation as required by Glassfish Update Center.
 */
public class GFInstaller implements Installer {
    
    static final String IMPL_JAR = "jsf-impl.jar";

    static final String API_JAR = "jsf-api.jar";
    
    static final String uc_version = "01_00_00";
    
    static final String INSTALLER_JAR = "jsf2.0_installer.jar";
    
    static final String CONFIGURATOR_JAR = "jsf2.0_configurator_" + uc_version + ".jar";

    /**
     * The main method of the components Addons installer.
     * This method takes the appserver installation dir as the
     * first argument
     * @param args, the first argument should be appserver install dir
     *
     */
    public static void main(String[] args) {
        GFInstaller installer = null;
        try {
            
            boolean isInstall = true;
            installer = new GFInstaller();
            
            if (args.length <1 || args[0] == null || args[0].equals("") ) {
                System.out.println("No args");
            } else {
                System.out.println("First arg is:" + args[0]);
            }
        } catch (Exception ex) {
            System.out.println("Installation failed :" + ex.getMessage());
        }
    }
    
    public void install(InstallationContext ic) throws AddonException, AddonFatalException {
        System.out.println("JSF2.0: invoked Installer:install ...");
        String timeStamp = "";
        try {
            String pattern = "yyyyMMddHHmm";
            String underscore = "_";
            SimpleDateFormat df = new SimpleDateFormat(pattern);
            String result = df.format(new Date());
            timeStamp = underscore + result;
        } catch (Exception e) {
            System.out.println("JSF2.0:  Warning, could not generate a timestamp for temporary file.  Continuing...");
        }
        
        File appserverDir = ic.getInstallationDirectory();
        String appServerInstallRoot = appserverDir.getAbsolutePath();

        String appServerLibDir = appServerInstallRoot + File.separator + "lib";
        
        String addOnsDir =
                appServerInstallRoot + File.separator + "addons";
        
        String addOnsLibDir =
                appServerInstallRoot + File.separator+ "lib" + File.separator + "addons";
  
        //Dir location of roller addon installer jar
        String installerJar =
                addOnsDir + File.separator + INSTALLER_JAR;
        
        // Create a tmp working directory for the installer
        String tmpWorkDir = System.getProperty("java.io.tmpdir") +
                File.separator + "jsf2.0-components" + timeStamp;
        
        File baseDir = new File(tmpWorkDir);
        if(!baseDir.exists()) {
            baseDir.mkdirs();
        }
        
        try {
            JarFactory tmpJar = new JarFactory(tmpWorkDir);
            tmpJar.unJar(new File(installerJar));
        } catch (ZipException zipEx) {
            throw new AddonFatalException("Could not unbundle jar "+installerJar, zipEx);
        } catch (IOException ioEx) {
            throw new AddonFatalException("Could not unbundle jar "+installerJar, ioEx);
        }


        
        File apiFrom = new File(tmpWorkDir + File.separator + API_JAR);
        File apiTo = new File(appServerLibDir + File.separator + API_JAR);

        File implNew = new File(tmpWorkDir + File.separator + IMPL_JAR);
        File implOrig = new File(appServerLibDir + File.separator + IMPL_JAR);
        File implOld = new File(appServerLibDir + File.separator + IMPL_JAR + ".old");

        // check if api jar already exists.  If it does, that's an error.
        if (apiTo.exists()) {
            throw new AddonFatalException("JSF API jar already exists - have you installed a different version already?  If so, uninstall it before running this upgrade.");
        }

        // check if a backup impl file already exists, If it does, that's an error.
        if (implOld.exists()) {
            throw new AddonFatalException("JSF Impl backup jar already exists - have you installed a different version already?  If so, uninstall it before running this upgrade.");
        }

        System.out.println("Installing new api file "+apiTo);
        copyFile(apiFrom, apiTo);

        if (!implOrig.exists()) {
            throw new AddonFatalException("JSF implementation jar does not exist at: "+implOrig);
        }

        System.out.println("Backing up old impl file to "+implOld);
        if (!implOrig.renameTo(implOld)) {
            throw new AddonFatalException("Could not backup JSF impl jar from "+implOrig+" to "+implOld);
        }

        System.out.println("Installing new impl file "+implOrig);
        copyFile(implNew, implOrig);

        // Copy a configurator plugin to AS_HOME/lib/addons directory.
        File confJarFrom = new File(tmpWorkDir +  File.separator + CONFIGURATOR_JAR);
        File confJarTo = new File(addOnsLibDir +  File.separator + CONFIGURATOR_JAR);
        
        System.out.println("Installing configurator at "+confJarTo);
        copyFile(confJarFrom, confJarTo);

    }
    
    public void uninstall(InstallationContext ic) throws AddonException {
        System.out.println("JSF2.0: invoked JSF2.0 Installer:uninstall ...");

        File appserverDir = ic.getInstallationDirectory();
        String appServerInstallRoot = appserverDir.getAbsolutePath();
 
        String appServerLibDir = appServerInstallRoot + File.separator + "lib";
                
        String addOnsDir =
                appServerInstallRoot + File.separator + "addons";
        
        String addOnsLibDir =
                appServerInstallRoot + File.separator+ "lib" + File.separator + "addons";
        
        // delete installer jar
        File installerJar = new File(addOnsDir + File.separator + INSTALLER_JAR);
        installerJar.deleteOnExit();
            
        // delete configurator jar
        File configJar = new File(addOnsLibDir +  File.separator + CONFIGURATOR_JAR);
        configJar.deleteOnExit();
        
        // delete API jar
        File apiJar = new File(appServerLibDir + File.separator + API_JAR);
        apiJar.deleteOnExit();

        // delete impl jar immedately, replace with backed up jar
        File implJar = new File(appServerLibDir + File.separator + IMPL_JAR);
        implJar.delete();
        File implJarOld = new File(appServerLibDir + File.separator + IMPL_JAR + ".old");
        if (!implJarOld.renameTo(implJar)) {
            throw new AddonFatalException("SEVERE ERROR: Could not restore old version of JSF impl, App Server is now in an inconsistent state, required immediate attention.");
        }

    }

    
    public void upgrade(InstallationContext ic, AddonVersion av) throws AddonFatalException {
        System.out.println("JSF2.0 Installer: Nothing to upgrade ...");

        throw new AddonFatalException("This code should never be called...  There is no upgrade available.");

    }
    
    /**
     * This method is used to copy a file
     */
    public static void copyFile(File sourceFile, File destinationFile) throws AddonFatalException {
        try {
            InputStream in = new FileInputStream(sourceFile);
            OutputStream out = new FileOutputStream(destinationFile);
            
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
        } catch (Exception e){
            throw new AddonFatalException("Exception copying file from "+sourceFile+" to "+destinationFile,  e);
        }
    }

   /**
    *  This method deletes a tree of files.
    */
   public static void deleteDirectory(File path) {
       if( path.exists() ) {
           File[] files = path.listFiles();
           for(int i=0; i<files.length; i++) {
               if(files[i].isDirectory()) {
                   deleteDirectory(files[i]);
               } else {
                   files[i].delete();
               }
           }
       }
       path.delete();
   }

}
