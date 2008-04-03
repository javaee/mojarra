/*
 * GlassfishUpdater.java
 *
 * Created on January 25, 2006, 4:29 PM
 */
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

package com.sun.faces.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 *
 * @author edburns
 */
public class GlassfishUpdater {

    private static final String AGREE_TO_LICENCE_QUESTION =
          "Do you accept the above license terms?";
    private static final String SERVER_MUST_BE_RUNNING_STATEMENT =
          "Ensure the domain is running before continuing.";
    private static final String ASADMIN_USERNAME_QUESTION =
          "What is the asadmin administrative user name?";
    private static final String ASADMIN_USER_PASSWORD_QUESTION =
          "What is path to the asadmin administrative password file (press enter if a password isn't necessary)?";
    private static final String PATH_TO_APPSERVER_QUESTION =
          "What is the path to the GlassFish/SJSAS installation?";
    private static final String INSTALL_ROOT = "${com.sun.aas.installRoot}";
    private static final String NEW_LINE =
            System.getProperty("line.separator", "\n");

    private enum JARS {
        API("jsf-api.jar"),
        IMPL("jsf-impl.jar");

        private String jarname;
        JARS(String jarname) {
            this.jarname = jarname;
        }

        public String getName() {
            return this.jarname;
        }

        public void unpackTo(File installRoot) throws IOException {
            File libDir = new File(installRoot, "lib");
            System.out.println("Unpacking " + jarname + " to " + libDir.getAbsolutePath());
            InputStream is = Thread.currentThread().getContextClassLoader()
                  .getResourceAsStream(jarname);
            FileOutputStream fos =
                  new FileOutputStream(new File(libDir, jarname));
            if (is != null) {
                for (int b = is.read(); b != -1; b = is.read()) {
                    fos.write(b);
                }
                fos.flush();
                fos.close();
                is.close();
            }
        }
    }
    

    /** Creates a new instance of GlassfishUpdater */
    protected GlassfishUpdater() { }

    protected static File libDir = null;
    protected static Properties props;
    
    public static void main(String args[]) throws Exception {
        // For statefile operations.
        if (args.length != 0) {
            if ("-statefile".equals(args[0])) {
                if (args.length == 2) {
                    setStateProps(args[1]);
                    String adminUser = getProp("admin.user");
                    String passwd = getProp("passwd.file");
                    File appHome = new File(getProp("appserver.home"));

                    displayLicense();
                    if (askYesOrNoQuestion(AGREE_TO_LICENCE_QUESTION, "yes")) {
                        unpackJars(appHome);
                        updateClassPathPrefix(appHome, adminUser, passwd);
                        System.out.println("Upgrade complete." + NEW_LINE +
                                "Restart the server for the changes to take " +
                                "effect.");
                    }
                } else {
                    System.out.println("Provide a properties file with the " +
                            "following props!" + NEW_LINE + "admin.user" +
                            NEW_LINE + "passwd.file" + NEW_LINE +
                            "appserver.home" + NEW_LINE + "Exiting...");
                    System.exit(1);
                }
            } else {
                System.out.println("Illegal Argument!" + NEW_LINE + "Exiting...");
                System.exit(1);
            }
        } else {
            // Default no command line args operation.
        displayLicense();
        if (askYesOrNoQuestion(AGREE_TO_LICENCE_QUESTION, "yes")) {
            File installDir = null;
            while (installDir == null) {
                String path = askQuestion(PATH_TO_APPSERVER_QUESTION, null);
                installDir = validateServerDirectory(path);
                if (installDir == null) {
                    System.out.println('\'' + path + "' is invalid.  Please try again.\n");
                }
            }

            displayContinuationStatement(SERVER_MUST_BE_RUNNING_STATEMENT);
            String user = askQuestion(ASADMIN_USERNAME_QUESTION, "admin");
            String pass = askQuestion(ASADMIN_USER_PASSWORD_QUESTION, null);
            unpackJars(installDir);
            updateClassPathPrefix(installDir, user, pass);
            System.out.println("Upgrade complete.  Restart the server for the changes to take effect.");
        }
        }
    }


    public static void updateClassPathPrefix(File installDir,
                                            String user,
                                            String pathToPasswordFile)
    throws Exception {
        File binDir = new File(installDir, "bin");
        Runtime rt = Runtime.getRuntime();
        Process p = rt.exec(buildCommand(binDir, user, pathToPasswordFile, "get", null),
                            null,
                            binDir);
        failUpdateIfNecessary(p);
        BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String result = reader.readLine();
        String[] parts;
        if (result == null || result.length() == 0) {
            result = "";
            parts = new String[0];
        } else {
            parts = result.split(" = ");
        }
        String path;
        if (parts.length != 2) {
            System.out.println("Setting classpath-prefix....");
            path = INSTALL_ROOT + "/lib/" + JARS.API.getName();

        } else {
            if (result.contains(JARS.API.getName())) {
                // existing prefix includes the update jars already - do nothing
                return;
            }
            System.out.println("Updating classpath-prefix....");
            path = parts[1].trim() + '/' + INSTALL_ROOT + "/lib/" + JARS.API.getName();
        }
        p = rt.exec(buildCommand(binDir, user, pathToPasswordFile, "set", path),
                    null,
                    binDir);
        failUpdateIfNecessary(p);
        
    }

    public static void failUpdateIfNecessary(Process p) throws Exception {

        // failed asadmin get or set commands don't exit with a non-zero code
        // What a pain.
        p.waitFor();
        BufferedReader reader =
              new BufferedReader(new InputStreamReader(p.getErrorStream()));
        boolean failed = false;
        for (String l = reader.readLine(); l != null; l = reader.readLine()) {
            if (l.contains("CLI137")) {
                failed = true;
                break;
            }
        }
        if (failed) {
            System.out.println("Update FAILED.  See below for the cause...");
            reader =
                  new BufferedReader(new InputStreamReader(p.getInputStream()));
            for (String l = reader.readLine(); l != null; l = reader.readLine())
            {
                System.out.println("   " + l);
            }            
            System.exit(1);
        }

    }


    public static void backupJsfImplIfNecessary(File installDir) throws IOException {
        File libDir = new File(installDir, "lib");
        File jsfImpl = new File(libDir, JARS.IMPL.getName());
        File jsfImplOrig = new File(libDir, JARS.IMPL.getName() + ".orig");
        if (!jsfImplOrig.exists()) {
            System.out.println("Backing up original jsf-impl.jar to " + JARS.IMPL.getName() + ".orig");
            jsfImpl.renameTo(new File(libDir, JARS.IMPL.getName() + ".orig"));
        }
    }

    public static String buildCommand(File binDir,
                                      String user,
                                      String pathToPasswordFile,
                                      String baseCommand,
                                      String updateInfo) throws IOException {
        StringBuilder sb = new StringBuilder(256);
        sb.append(binDir.getCanonicalPath());
        sb.append(File.separatorChar);
        String os = System.getProperty("os.name");
        if (os.contains("Windows") || os.contains("windows")) {
            sb.append("asadmin.bat ");
        } else {
            sb.append("asadmin ");
        }
        sb.append(baseCommand);
        sb.append(" --user ");
        sb.append(user.trim());
        if (pathToPasswordFile != null && pathToPasswordFile.trim().length() > 0) {
            sb.append(" --passwordfile \"");
            sb.append(pathToPasswordFile.trim());
            sb.append('"');
        }
        sb.append(" server.java-config.classpath-prefix");
        if (updateInfo != null) {
            sb.append('=');
            sb.append(updateInfo);
        }
        return sb.toString();
    }


    public static void unpackJars(File baseDir) throws IOException {
        backupJsfImplIfNecessary(baseDir);
        for (JARS jar : JARS.values()) {
            jar.unpackTo(baseDir);
        }
    }

    public static File validateServerDirectory(String path) {

        if (path == null || path.length() == 0) {
            return null;
        }
        
        File gfInstallDir = new File(path);

        // Have we been given a directory?
        if (!gfInstallDir.isDirectory()) {
            return null;
        }

        File testFile = new File(gfInstallDir, "bin");
        if (testFile.exists() && testFile.isDirectory()) {
            File[] files = testFile.listFiles();
            for (File cur : files) {
                if (-1 != cur.getName().indexOf("asadmin")) {
                    return gfInstallDir;
                }
            }
        }

        return null;

    }

    public static void displayLicense() throws IOException {
        InputStream is = Thread.currentThread().getContextClassLoader().
                getResourceAsStream("BINARY_LICENSE.txt");
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String line;
        while (null != (line = br.readLine())) {
            System.out.println(line);
        }
    }

    public static String askQuestion(String question, String defaultValue)
    throws IOException {
        if (defaultValue != null) {
            System.out.print(question + " [" + defaultValue + "] : " );
        } else {
            System.out.print(question + " : ");
        }
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String response = br.readLine();
        if (response == null || response.trim().length() == 0) {
            response = defaultValue;
        }
        return response;
    }

    public static boolean askYesOrNoQuestion(String question,
                                             String defaultValue)
    throws IOException {
        System.out.print(question
                         + " (type yes or no) "
                         + '['
                         + defaultValue
                         + "] : ");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String response = br.readLine();
        if (response == null || response.trim().length() == 0) {
            response = defaultValue;
        }
        return response.trim().equalsIgnoreCase("yes");
    }

    public static boolean displayContinuationStatement(String question) throws IOException {
        System.out.print(question + " (press Enter to continue) ");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String response = br.readLine();
        return (response != null) && response.trim().equalsIgnoreCase("yes");
    }

    /*
     * Set and validate the Properties file.
     */
    public static void setStateProps(String path) {

        props = new Properties();

        // Read in the properties file.
        try {
            props.load(new FileInputStream(path));
        } catch (IOException e) {
            System.out.println("Failed to read properties file: " + path +
                    NEW_LINE + "Exiting...");
            System.exit(1);
        } catch (IllegalArgumentException iae) {
            System.out.println(path + " File is corrupt!" + NEW_LINE +
                    "Exiting...");
            System.exit(1);
        }
    }

    /*
     * Get and validate a given property.
     */
    public static String getProp(String prop) {
        String value = props.getProperty(prop);

        if (value == null) {
            System.out.println(prop + " Not found in the following props " +
                    "list: " + props + NEW_LINE + "This prop must be set!" +
                    NEW_LINE + "Exiting...");
            System.exit(1);
        }
        if (value.length() == 0) {
            System.out.println(prop + " NOT SET!" + NEW_LINE +
                    "This prop must be set!" + NEW_LINE + "Exiting...");
            System.exit(1);
        }

        return value;
    }
}
