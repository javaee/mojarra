/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2011 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
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

package com.sun.faces.config;

import com.sun.faces.scripting.ScriptManager;
import com.sun.faces.scripting.groovy.GroovyHelper;
import com.sun.faces.scripting.groovy.GroovyScriptManager;
import com.sun.faces.spi.AnnotationProvider;
import com.sun.faces.util.FacesLogger;
import com.sun.faces.util.Util;

import javax.faces.bean.ManagedBean;
import javax.faces.component.FacesComponent;
import javax.faces.component.behavior.FacesBehavior;
import javax.faces.context.FacesContext;
import javax.faces.convert.FacesConverter;
import javax.faces.event.NamedEvent;
import javax.faces.render.FacesBehaviorRenderer;
import javax.faces.render.FacesRenderer;
import javax.faces.validator.FacesValidator;
import javax.servlet.ServletContext;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.JarURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.sun.faces.config.WebConfiguration.WebContextInitParameter.AnnotationScanPackages;

/**
 * This class is responsible for scanning the class file bytes of
 * classes contained within the web application for any of the known
 * Faces configuration Annotations:
 * <ul>
 *  <li>javax.faces.component.FacesBehavior</li>
 *  <li>javax.faces.render.FacesBehaviorRenderer</li>
 *  <li>javax.faces.component.FacesComponent</li>
 *  <li>javax.faces.convert.FacesConverter</li>
 *  <li>javax.faces.validator.FacesValidator</li>
 *  <li>javax.faces.render.FacesRenderer</li>
 *  <li>javax.faces.bean.ManagedBean</li>
 *  <li>javax.faces.event.NamedEvent</li>
 * </ul>
 */
public class AnnotationScanner extends AnnotationProvider {

    private static final Logger LOGGER = FacesLogger.CONFIG.getLogger();

    // Matcher.group(1) == the URL to the JAR file itself.
    // Matcher.group(2) == the name of the JAR.
    private static final Pattern JAR_PATTERN = Pattern.compile("(.*/(\\S*\\.jar)).*");

    private static final String WEB_INF_CLASSES = "/WEB-INF/classes/";
    private static final String WILDCARD = "*";

    private static final Set<String> FACES_ANNOTATIONS;
    private static final Set<Class<? extends Annotation>> FACES_ANNOTATION_TYPE;

    static {
        HashSet<String> annotations = new HashSet<String>(8, 1.0f);
        Collections.addAll(annotations,
                           "Ljavax/faces/component/FacesComponent;",
                           "Ljavax/faces/convert/FacesConverter;",
                           "Ljavax/faces/validator/FacesValidator;",
                           "Ljavax/faces/render/FacesRenderer;",
                           "Ljavax/faces/bean/ManagedBean;",
                           "Ljavax/faces/event/NamedEvent;",
                           "Ljavax/faces/component/behavior/FacesBehavior;",
                           "Ljavax/faces/render/FacesBehaviorRenderer;");
        FACES_ANNOTATIONS = Collections.unmodifiableSet(annotations);
        HashSet<Class<? extends Annotation>> annotationInstances =
              new HashSet<Class<? extends Annotation>>(8, 1.0f);
        Collections.addAll(annotationInstances,
                           FacesComponent.class,
                           FacesConverter.class,
                           FacesValidator.class,
                           FacesRenderer.class,
                           ManagedBean.class,
                           NamedEvent.class,
                           FacesBehavior.class,
                           FacesBehaviorRenderer.class);
        FACES_ANNOTATION_TYPE = Collections.unmodifiableSet(annotationInstances);
    }

    private ClassFile classFileScanner;
    private String[] webInfClassesPackages;
    private Map<String,String[]> classpathPackages;
    private List<ScriptManager> scriptManagers = new ArrayList<ScriptManager>();

    // ------------------------------------------------------------ Constructors


    /**
     * Creates a new <code>AnnotationScanner</code> instance.
     *
     * @param sc the <code>ServletContext</code> for the application to be
     *  scanned
     */
    public AnnotationScanner(ServletContext sc) {
        super(sc);

        classFileScanner = new ClassFile();
        
        if (GroovyHelper.isGroovyAvailable(FacesContext.getCurrentInstance())) {
            scriptManagers.add(new GroovyScriptManager(sc));
        }

        WebConfiguration webConfig = WebConfiguration.getInstance(sc);
        if (webConfig.isSet(AnnotationScanPackages)) {
            classpathPackages = new HashMap<String,String[]>(4);
            webInfClassesPackages = new String[0];
            String[] options = webConfig.getOptionValue(AnnotationScanPackages, "\\s+");
            List<String> packages = new ArrayList<String>(4);
            for (String option : options) {
                if (option.length() == 0) {
                    continue;
                }
                if (option.startsWith("jar:")) {
                    String[] parts = Util.split(option, ":");
                    if (parts.length != 3) {
                        if (LOGGER.isLoggable(Level.WARNING)) {
                            LOGGER.log(Level.WARNING,
                                       "jsf.annotation.scanner.configuration.invalid",
                                       new String[] { AnnotationScanPackages.getQualifiedName(), option });
                        }
                    } else {
                        if (WILDCARD.equals(parts[1]) && !classpathPackages.containsKey(WILDCARD)) {
                            classpathPackages.clear();
                            classpathPackages.put(WILDCARD, normalizeJarPackages(Util.split(parts[2], ",")));
                        } else if (WILDCARD.equals(parts[1]) && classpathPackages.containsKey(WILDCARD)) {
                            if (LOGGER.isLoggable(Level.WARNING)) {
                                LOGGER.log(Level.WARNING,
                                           "jsf.annotation.scanner.configuration.duplicate.wildcard",
                                           new String[] { AnnotationScanPackages.getQualifiedName(), option });
                            }
                        } else {
                            if (!classpathPackages.containsKey(WILDCARD)) {
                                classpathPackages.put(parts[1], normalizeJarPackages(Util.split(parts[2], ",")));
                            }
                        }
                    }
                } else {
                    if (WILDCARD.equals(option) && !packages.contains(WILDCARD)) {
                        packages.clear();
                        packages.add(WILDCARD);
                    } else {
                        if (!packages.contains(WILDCARD)) {
                            packages.add(option);
                        }
                    }
                }
            }
            webInfClassesPackages = packages.toArray(new String[packages.size()]);
        }


    }


    // ---------------------------------------------------------- Public Methods


    /**
     * @return a <code>Map</code> of classes mapped to a specific annotation type.
     *  If no annotations are present, or the application is considered
     * <code>metadata-complete</code> <code>null</code> will be returned.
     */
    public Map<Class<? extends Annotation>,Set<Class<?>>> getAnnotatedClasses(Set<URL> urls) {

        Set<String> classList = new HashSet<String>();

        processWebInfClasses(sc, classList);
        processClasspath(urls, classList);
        processScripts(classList);

        Map<Class<? extends Annotation>,Set<Class<?>>> annotatedClasses = null;
        if (classList.size() > 0) {
            annotatedClasses = new HashMap<Class<? extends Annotation>,Set<Class<?>>>(6, 1.0f);
            for (String className : classList) {
                try {
                    Class<?> clazz = Util.loadClass(className, this);
                    Annotation[] annotations = clazz.getAnnotations();
                    for (Annotation annotation : annotations) {
                        Class<? extends Annotation> annoType =
                              annotation.annotationType();
                        if (FACES_ANNOTATION_TYPE.contains(annoType)) {
                            Set<Class<?>> classes = annotatedClasses.get(annoType);
                            if (classes == null) {
                                classes = new HashSet<Class<?>>();
                                annotatedClasses.put(annoType, classes);
                            }
                            classes.add(clazz);
                        }
                    }
                } catch (ClassNotFoundException cnfe) {
                    // shouldn't happen..
                    if (LOGGER.isLoggable(Level.SEVERE)) {
                        LOGGER.log(Level.SEVERE,
                                   "Unable to load annotated class: {0}",
                                   className);
                        LOGGER.log(Level.SEVERE, "", cnfe);
                    }
                } catch (NoClassDefFoundError ncdfe) {
                    // this is more likely
                    if (LOGGER.isLoggable(Level.SEVERE)) {
                        LOGGER.log(Level.SEVERE,
                                   "Unable to load annotated class: {0}, reason: {1}",
                                   new Object[] { className, ncdfe.toString()});
                    }
                }
            }
        }

        return ((annotatedClasses != null)
                ? annotatedClasses
                : Collections.<Class<? extends Annotation>, Set<Class<?>>>emptyMap());

    }


    // --------------------------------------------------------- Private Methods

    private void processScripts(Set<String> classList) {
        for (ScriptManager sm : scriptManagers) {
            classList.addAll(sm.getScripts());
        }
    }

    /**
     * Scans for annotations on classes within JAR files on the classpath.
     *
     * @param urls to a faces-config documents that allow us to refer to
     *  unique jar files on the classpath
     * @param classList the <code>Set</code> to which annotated classes
     *  will be added
     */
    private void processClasspath(Set<URL> urls, Set<String> classList) {

        for (URL url : urls) {
            try {
                Matcher m = JAR_PATTERN.matcher(url.toString());
                if (m.matches()) {
                    String jarName = m.group(2);
                    if (!processJar(jarName)) {
                        continue;
                    }
                    StringBuilder sb = new StringBuilder(32);
                    String us = m.group(1);
                    if (!us.startsWith("jar:")) {
                        sb.append("jar:");
                    }
                    sb.append(us).append("!/");
                    URL u = new URL(sb.toString());
                    JarFile jarFile =
                          ((JarURLConnection) u.openConnection()).getJarFile();
                    processJarEntries(jarFile,
                                      ((classpathPackages != null)
                                       ? classpathPackages.get(jarName)
                                       : null),
                                      classList);
                } else {
                    if (LOGGER.isLoggable(Level.FINE)) {
                        LOGGER.fine("Unable to match URL to a jar file: " + url
                              .toString());
                    }
                }
            } catch (Exception e) {
                if (LOGGER.isLoggable(Level.SEVERE)) {
                    LOGGER.log(Level.SEVERE,
                               "Unable to process annotations for url, {0}.  Reason: "
                               + e.toString(),
                               new Object[]{url});
                    LOGGER.log(Level.SEVERE, "", e);
                }
            }
        }

    }


    /**
     * Called by {@link ConstantPoolInfo} when processing the bytes of the
     * class file.
     *
     * @param value the String value as provided from {@link ConstantPoolInfo}
     * @return <code>true</code> if the value is one of the known
     *  Faces annotations, otherwise <code>false</code>
     */
    private static boolean isAnnotation(String value) {

        return FACES_ANNOTATIONS.contains(value);

    }


    /**
     * Process the entries in the provided <code>JarFile</code> looking for
     * class files that may be annotated with any of the Faces configuration
     * annotations.
     *
     * @param jarFile the JAR to process
     * @param allowedPackages the packages that should be scanned within the jar
     * @param classList the <code>Set</code> to which annotated classes
     *  will be added
     */
    private void processJarEntries(JarFile jarFile, String[] allowedPackages, Set<String> classList) {

        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.log(Level.FINE,
                        "Scanning JAR {0} for annotations...",
                        jarFile.getName());
        }

        for (Enumeration<JarEntry> entries = jarFile.entries(); entries.hasMoreElements(); ) {
            JarEntry entry = entries.nextElement();
            if (entry.isDirectory()) {
                continue;
            }

            String name = entry.getName();
            if (name.startsWith("META-INF")) {
                continue;
            }

            if (name.endsWith(".class")) {
                String cname = convertToClassName(name);
                if (!processClass(cname, allowedPackages)) {
                    continue;
                }
                ReadableByteChannel channel = null;
                try {
                    channel = Channels.newChannel(jarFile.getInputStream(entry));
                    if (classFileScanner.containsAnnotation(channel)) {
                        if (LOGGER.isLoggable(Level.FINE)) {
                            LOGGER.log(Level.FINE,
                                       "[JAR] Found annotated Class: {0}",
                                       cname);
                        }
                        classList.add(cname);
                    }
                } catch (IOException e) {
                    if (LOGGER.isLoggable(Level.SEVERE)) {
                        LOGGER.log(Level.SEVERE,
                                   "Unexpected exception scanning JAR {0} for annotations",
                                   jarFile.getName());
                        LOGGER.log(Level.SEVERE,
                                   e.toString(),
                                   e);
                    }
                } finally {
                    if (channel != null) {
                        try {
                            channel.close();
                        } catch (IOException ignored) {
                            if (LOGGER.isLoggable(Level.FINE)) {
                                LOGGER.log(Level.FINE,
                                           ignored.toString(),
                                           ignored);
                            }
                        }
                    }
                }
            }
        }

    }


    /**
     * Scan <code>WEB-INF/classes</code> for classes that may be annotated
     * with any of the Faces configuration annotations.
     *
     * @param sc the <code>ServletContext</code> for the application being
     *  scanned
     * @param classList the <code>Set</code> to which annotated classes
     *  will be added
     */
    private void processWebInfClasses(ServletContext sc, Set<String> classList) {

        processWebInfClasses(sc, WEB_INF_CLASSES, classList);

    }


    /**
     * Scan <code>WEB-INF/classes</code> for classes that may be annotated
     * with any of the Faces configuration annotations.
     *
     * @param sc the <code>ServletContext</code> for the application being
     *  scanned
     * @param path the path to start the scan from
     * @param classList the <code>Set</code> to which annotated classes
     *  will be added
     */
    private void processWebInfClasses(ServletContext sc,
                                      String path,
                                      Set<String> classList) {

        //noinspection unchecked
        Set<String> paths = sc.getResourcePaths(path);
        processWebInfClasses(sc, paths, classList);

    }


    /**
     * Scan <code>WEB-INF/classes</code> for classes that may be annotated
     * with any of the Faces configuration annotations.
     *
     * @param sc the <code>ServletContext</code> for the application being
     *  scanned
     * @param paths a set of paths to process
     * @param classList the <code>Set</code> to which annotated classes
     *  will be added
     */
    private void processWebInfClasses(ServletContext sc,
                                      Set<String> paths,
                                      Set<String> classList) {

        if (paths != null && !paths.isEmpty()) {
            for (String pathElement : paths) {
                if (pathElement.endsWith("/")) {
                    processWebInfClasses(sc, pathElement, classList);
                } else {
                    if (pathElement.endsWith(".class")) {
                        String cname = convertToClassName(WEB_INF_CLASSES,
                                                              pathElement);
                        if (!processClass(cname, webInfClassesPackages)) {
                            continue;
                        }
                        if (containsAnnotation(sc, pathElement)) {
                            if (LOGGER.isLoggable(Level.FINE)) {
                                LOGGER.log(Level.FINE,
                                           "[WEB-INF/classes] Found annotated Class: {0}",
                                           cname);
                            }
                            classList.add(cname);
                        }
                    }
                }
            }
        }

    }


    private String[] normalizeJarPackages(String[] packages) {

        if (packages.length == 0) {
            return packages;
        }
        List<String> normalizedPackages = new ArrayList<String>(packages.length);
        for (String pkg : packages) {
            if (WILDCARD.equals(pkg)) {
                normalizedPackages.clear();
                normalizedPackages.add(WILDCARD);
                break;
            } else {
                normalizedPackages.add(pkg);
            }
        }
        return normalizedPackages.toArray(new String[normalizedPackages.size()]);

    }


    /**
     * @param sc the <code>ServletContext</code> for the application being
     *  scanned
     * @param pathElement the full path to the classfile to be scanned
     * @return <code>true</code> if the class contains one of the Faces
     *  configuration annotations
     */
    private boolean containsAnnotation(ServletContext sc, String pathElement) {

        ReadableByteChannel channel = null;
        try {
            URL url = sc.getResource(pathElement);
            channel = Channels.newChannel(url.openStream());
            return classFileScanner.containsAnnotation(channel);
        } catch (MalformedURLException e) {
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE,
                           e.toString(),
                           e);
            }
        } catch (IOException ioe) {
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE,
                           ioe.toString(),
                           ioe);
            }
        } finally {
            if (channel != null) {
                try {
                    channel.close();
                } catch (IOException ignored) {
                    if (LOGGER.isLoggable(Level.FINE)) {
                        LOGGER.log(Level.FINE,
                                   ignored.toString(),
                                   ignored);
                    }
                }
            }
        }
        return false;

    }


    /**
     * Utility method for converting paths to fully qualified class names.
     *
     * @param pathEntry a path entry to a class file
     *
     * @return a fully qualfied class name using dot notation
     */
    private String convertToClassName(String pathEntry) {

        return convertToClassName(null, pathEntry);

    }


    /**
     * Utility method for converting paths to fully qualified class names.
     *
     * @param prefix the prefix that should be stripped from the class name
     *  before converting it
     * @param pathEntry a path to a class file
     *
     * @return a fully qualfied class name using dot notation
     */
    private String convertToClassName(String prefix, String pathEntry) {

        String className = pathEntry;

        if (prefix != null) {
            // remove the prefix
            className = className.substring(prefix.length());
        }
        // remove the .class suffix
        className = className.substring(0, (className.length() - 6));

        return className.replace('/', '.');

    }


    private boolean processJar(String entry) {

        return (classpathPackages == null
                  || (classpathPackages.containsKey(entry)
                         || classpathPackages.containsKey(WILDCARD)));

    }


    /**
     * @param candidate the class that should be processed
     * @param packages the packages of classes that are allowed to be processed
     * @return <code>true</code> if the class should be processed further,
     *  otherwise, <code>false</code>
     */
    private boolean processClass(String candidate, String[] packages) {

        if (packages == null) {
            return true;
        }

        for (String packageName : packages) {
            if (candidate.startsWith(packageName) || WILDCARD.equals(packageName)) {
                return true;
            }
        }
        return false;
        
    }


    // ----------------------------------------------------------- Inner Classes


    /**
     * This class is encapsulating binary .class file information as defined at
     * http://java.sun.com/docs/books/vmspec/2nd-edition/html/ClassFile.doc.html
     * <p/>
     * This is used by the annotation frameworks to quickly scan .class files
     * for the presence of annotations. This avoid the annotation framework
     * having to load each .class file in the class loader.
     * <p/>
     * Taken from the GlassFish V2 source base.
     */
    @SuppressWarnings({"UnusedDeclaration"})
    private static final class ClassFile {

        private static final int magic = 0xCAFEBABE;

        public static final int ACC_PUBLIC = 0x1;
        public static final int ACC_PRIVATE = 0x2;
        public static final int ACC_PROTECTED = 0x4;
        public static final int ACC_STATIC = 0x8;
        public static final int ACC_FINAL = 0x10;
        public static final int ACC_SYNCHRONIZED = 0x20;
        public static final int ACC_THREADSAFE = 0x40;
        public static final int ACC_TRANSIENT = 0x80;
        public static final int ACC_NATIVE = 0x100;
        public static final int ACC_INTERFACE = 0x200;
        public static final int ACC_ABSTRACT = 0x400;

        public short majorVersion;
        public short minorVersion;
        public ConstantPoolInfo constantPool[];
        public short accessFlags;
        public ConstantPoolInfo thisClass;
        public ConstantPoolInfo superClass;
        public ConstantPoolInfo interfaces[];

        /**
         * bunch of stuff I really don't care too much for now.
         * <p/>
         * FieldInfo           fields[]; MethodInfo          methods[];
         * AttributeInfo       attributes[];
         */

        ByteBuffer header;
        ConstantPoolInfo constantPoolInfo = new ConstantPoolInfo();

        // ------------------------------------------------------------ Constructors


        /**
         * Creates a new instance of ClassFile
         */
        public ClassFile() {
            header = ByteBuffer.allocate(12000);
        }

        // ---------------------------------------------------------- Public Methods


        public void setConstantPoolInfo(ConstantPoolInfo poolInfo) {
            constantPoolInfo = poolInfo;
        }


        /**
         * Read the input channel and initialize instance data structure.
         *
         * @param in a <code>ReadableByteChannel</code> that provides the bytes
         *  of the classfile
         *
         * @return <code>true</code> if the bytes representing this classfile include
         *  one of the annotations we're looking for.
         *
         * @throws IOException if an I/O error occurs while reading the class
         */
        public boolean containsAnnotation(ReadableByteChannel in)
              throws IOException {

            /**
             * this is the .class file layout
             *
             ClassFile {
             u4 magic;
             u2 minor_version;
             u2 major_version;
             u2 constant_pool_count;
             cp_info constant_pool[constant_pool_count-1];
             u2 access_flags;
             u2 this_class;
             u2 super_class;
             u2 interfaces_count;
             u2 interfaces[interfaces_count];
             u2 fields_count;
             field_info fields[fields_count];
             u2 methods_count;
             method_info methods[methods_count];
             u2 attributes_count;
             attribute_info attributes[attributes_count];
             }
             **/
            header.clear();
            long read = (long) in.read(header);
            if (read == -1) {
                return false;
            }
            header.rewind();

            if (header.getInt() != magic) {
                return false;
            }

            minorVersion = header.getShort();
            majorVersion = header.getShort();
            int constantPoolSize = header.getShort();

            return constantPoolInfo
                  .containsAnnotation(constantPoolSize, header, in);

        }

    } // END ClassFile


    private static class ConstantPoolInfo {

        private static final Logger LOGGER = FacesLogger.CONFIG.getLogger();

        public static final byte CLASS = 7;
        public static final int FIELDREF = 9;
        public static final int METHODREF = 10;
        public static final int STRING = 8;
        public static final int INTEGER = 3;
        public static final int FLOAT = 4;
        public static final int LONG = 5;
        public static final int DOUBLE = 6;
        public static final int INTERFACEMETHODREF = 11;
        public static final int NAMEANDTYPE = 12;
        public static final int ASCIZ = 1;
        public static final int UNICODE = 2;

        byte[] bytes = new byte[Short.MAX_VALUE];


        // ------------------------------------------------------------ Constructors


        /**
         * Creates a new instance of ConstantPoolInfo
         */
        public ConstantPoolInfo() {
        }


        // ---------------------------------------------------------- Public Methods


        /**
         * Read the input channel and initialize instance data structure.
         *
         * @param constantPoolSize the constant pool size for this class file
         * @param buffer the ByteBuffer used to store the bytes from <code>in</code>
         * @param in ReadableByteChannel from which the class file bytes are
         *  read
         *
         * @return <code>true</code> if the bytes representing this classfile include
         *  one of the annotations we're looking for.
         *
         * @throws IOException if an I/O error occurs while reading the class
         */
        public boolean containsAnnotation(int constantPoolSize,
                                          final ByteBuffer buffer,
                                          final ReadableByteChannel in)
        throws IOException {

            for (int i = 1; i < constantPoolSize; i++) {
                if (!refill(buffer, in, 1)) {
                    return true;
                }
                final byte type = buffer.get();
                switch (type) {
                    case ASCIZ:
                    case UNICODE:
                        if (!refill(buffer, in, 2)) {
                            return true;
                        }
                        final short length = buffer.getShort();
                        if (length < 0 || length > Short.MAX_VALUE) {
                            return true;
                        }
                        if (length > buffer.capacity()) {
                            return true;
                        }
                        if (!refill(buffer, in, length)) {
                            return true;
                        }
                        buffer.get(bytes, 0, length);
                        /* to speed up the process, I am comparing the first few
                         * bytes to Ljava since all annotations are in the java
                         * package, the reduces dramatically the number or String
                         * construction
                         */
                        if (bytes[0] == 'L' && bytes[1] == 'j' && bytes[2] == 'a') {
                            String stringValue;
                            if (type == ASCIZ) {
                                stringValue =
                                      new String(bytes, 0, length, "US-ASCII");
                            } else {
                                stringValue = new String(bytes, 0, length);
                            }
                            if (AnnotationScanner.isAnnotation(stringValue)) {
                                return true;
                            }
                        }
                        break;
                    case CLASS:
                    case STRING:
                        if (!refill(buffer, in, 2)) {
                            return true;
                        }
                        buffer.getShort();
                        break;
                    case FIELDREF:
                    case METHODREF:
                    case INTERFACEMETHODREF:
                    case INTEGER:
                    case FLOAT:
                        if (!refill(buffer, in, 4)) {
                            return true;
                        }
                        buffer.position(buffer.position() + 4);
                        break;
                    case LONG:
                    case DOUBLE:
                        if (!refill(buffer, in, 8)) {
                            return true;
                        }
                        buffer.position(buffer.position() + 8);
                        // for long, and double, they use 2 constantPool
                        i++;
                        break;
                    case NAMEANDTYPE:
                        if (!refill(buffer, in, 4)) {
                            return true;
                        }
                        buffer.getShort();
                        buffer.getShort();
                        break;
                    default:
                        if (LOGGER.isLoggable(Level.SEVERE)) {
                            LOGGER.log(Level.SEVERE,
                                       "Unknow type constant pool {0} at position {1}",
                                       new Object[]{type, i});
                        }
                        break;
                }
            }
            return false;
        }


        // ----------------------------------------------------- Private Methods

        private boolean refill(ByteBuffer buffer,
                               ReadableByteChannel in,
                               int requestLen) throws IOException {
            
            int cap = buffer.capacity();
            if (buffer.position() + requestLen > cap) {
                buffer.compact();
                int read = in.read(buffer);
                if (read < 0) {
                    return false;
                }
                buffer.rewind();
            }
            return true;

        }

    } // END ConstantPoolInfo

}
