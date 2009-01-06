/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 1997-2008 Sun Microsystems, Inc. All rights reserved.
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

package com.sun.faces.config;

import java.io.IOException;
import java.net.JarURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Map;
import java.util.HashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.lang.annotation.Annotation;

import javax.faces.FacesException;
import javax.faces.convert.FacesConverter;
import javax.faces.validator.FacesValidator;
import javax.faces.render.FacesRenderer;
import javax.faces.model.ManagedBean;
import javax.faces.event.NamedEvent;
import javax.faces.component.FacesComponent;
import javax.servlet.ServletContext;

import com.sun.faces.util.FacesLogger;
import com.sun.faces.util.Util;

/**
 * This class is responsible for scanning the class file bytes of
 * classes contained within the web application for any of the known
 * Faces configuration Annotations:
 * <ul>
 *  <li>javax.faces.component.FacesComponent</li>
 *  <li>javax.faces.convert.FacesConverter</li>
 *  <li>javax.faces.validator.FacesValidator</li>
 *  <li>javax.faces.render.FacesRenderer</li>
 *  <li>javax.faces.model.ManagedBean</li>
 *  <li>javax.faces.event.NamedEvent</li>
 * </ul>
 */
public class AnnotationScanner {

    private static final Logger LOGGER = FacesLogger.CONFIG.getLogger();

    private static final String WEB_INF_CLASSES = "/WEB-INF/classes/";
    private static final String WEB_INF_LIB = "/WEB-INF/lib/";
    private static final String FACES_CONFIG_XML = "META-INF/faces-config.xml";

    private static final Set<String> FACES_ANNOTATIONS;
    private static final Set<Class<? extends Annotation>> FACES_ANNOTATION_TYPE;

    static {
        HashSet<String> annotations = new HashSet<String>(6, 1.0f);
        Collections.addAll(annotations,
                           "Ljavax/faces/component/FacesComponent;",
                           "Ljavax/faces/convert/FacesConverter;",
                           "Ljavax/faces/validator/FacesValidator;",
                           "Ljavax/faces/render/FacesRenderer;",
                           "Ljavax/faces/model/ManagedBean;",
                           "Ljavax/faces/event/NamedEvent;");
        FACES_ANNOTATIONS = Collections.unmodifiableSet(annotations);
        HashSet<Class<? extends Annotation>> annotationInstances =
              new HashSet<Class<? extends Annotation>>(6, 1.0f);
        Collections.addAll(annotationInstances,
                           FacesComponent.class,
                           FacesConverter.class,
                           FacesValidator.class,
                           FacesRenderer.class,
                           ManagedBean.class,
                           NamedEvent.class);
        FACES_ANNOTATION_TYPE = Collections.unmodifiableSet(annotationInstances);
    }

    private ServletContext sc;
    private ClassFile classFileScanner;


    // ------------------------------------------------------------ Constructors


    /**
     * Creates a new <code>AnnotationScanner</code> instance.
     *
     * @param sc the <code>ServletContext</code> for the application to be
     *  scanned
     */
    public AnnotationScanner(ServletContext sc) {

        this.sc = sc;
        classFileScanner = new ClassFile();

    }


    // ---------------------------------------------------------- Public Methods


    /**
     * @return a <code>Map</code> of classes mapped to a specific annotation type.
     *  If no annotations are present, or the application is considered
     * <code>metadata-complete</code> <code>null</code> will be returned.
     */
    public Map<Class<? extends Annotation>,Set<Class<?>>> getAnnotatedClasses() {

        Set<String> classList = new HashSet<String>();

        processWebInfClasses(sc, classList);
        processWebInfLib(sc, classList);

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
                    }
                }
            }
        }

        return ((annotatedClasses != null)
                ? annotatedClasses
                : Collections.<Class<? extends Annotation>, Set<Class<?>>>emptyMap());

    }


    // --------------------------------------------------------- Private Methods


    /**
     * Called by {@link ConstantPoolInfo} when processing the bytes of the
     * class file.
     *
     * @String the String value as provided from {@link ConstantPoolInfo}
     * @return <code>true</code> if the value is one of the known
     *  Faces annotations, otherwise <code>false</code>
     */
    private static boolean isAnnotation(String value) {

        return FACES_ANNOTATIONS.contains(value);

    }


    /**
     * Process JAR files within <code>WEB-INF/lib</code>.
     *
     * @param sc the <code>ServletContext</code> for the application being
     *  scanned
     * @param classList the <code>Set</code> to which annotated classes
     *  will be added
     */
    private void processWebInfLib(ServletContext sc, Set<String> classList) {

        //noinspection unchecked
        Set<String> entries = sc.getResourcePaths(WEB_INF_LIB);
        List<JarFile> jars = getJars(sc, entries);
        if (jars != null) {
            for (JarFile jar : jars) {
                processJarEntries(jar, classList);
            }
        }

    }


    /**
     * Process the entries in the provided <code>JarFile</code> looking for
     * class files that may be annotated with any of the Faces configuration
     * annotations.
     *
     * @param jarFile the JAR to process
     * @param classList the <code>Set</code> to which annotated classes
     *  will be added
     */
    private void processJarEntries(JarFile jarFile, Set<String> classList) {

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
            if (name.startsWith("/META-INF")) {
                continue;
            }

            if (name.endsWith(".class")) {
                ReadableByteChannel channel = null;
                try {
                    channel = Channels.newChannel(jarFile.getInputStream(entry));
                    if (classFileScanner.containsAnnotation(channel, entry.getSize())) {
                        String cname = convertToClassName(name);
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
     * <p>
     * Return any JARs in <code>WEB-INF/lib</code> that contain
     * a <code>META-INF/faces-config.xml</code> file.
     * </p>
     *
     * @param sc the <code>ServletContext</code> for the application being
     *  scanned
     * @param entries the <code>Set</code> to which annotated classes
     *  will be added
     * @return a <code>List</code> of JAR files that should be scanned
     *  for annotations.
     */
    private List<JarFile> getJars(ServletContext sc, Set<String> entries) {
        List<JarFile> jars = null;
        if (entries != null && !entries.isEmpty()) {
            for (String entry : entries) {
                if (entry.endsWith(".jar")) {
                    try {
                        URL url = sc.getResource(entry);
                        StringBuilder sb = new StringBuilder(32);
                        sb.append("jar:").append(url.toString()).append("!/");
                        url = new URL(sb.toString());
                        JarFile jarFile =
                              ((JarURLConnection) url.openConnection())
                                    .getJarFile();
                        if (jarFile.getJarEntry(FACES_CONFIG_XML) != null) {
                            if (jars == null) {
                                jars = new ArrayList<JarFile>();
                            }
                            jars.add(jarFile);
                        }
                    } catch (Exception e) {
                        throw new FacesException(e);
                    }
                }
            }
        }
        return jars;

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
                        if (containsAnnotation(sc, pathElement)) {
                            String cname = convertToClassName(WEB_INF_CLASSES,
                                                              pathElement);
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
            URLConnection conn = url.openConnection();
            conn.setUseCaches(false);
            channel = Channels.newChannel(url.openStream());
            return classFileScanner.containsAnnotation(channel,
                                                       conn.getContentLength());
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
     */
    private String convertToClassName(String pathEntry) {

        return convertToClassName(null, pathEntry);

    }


    /**
     * Utility method for converting paths to fully qualified class names.
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
         */
        public boolean containsAnnotation(ReadableByteChannel in, long size)
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
            if (size != -1 && size > header.capacity()) {
                // time to expand...
                header = ByteBuffer.allocate((int) size);
            }
            long read = (long) in.read(header);
            if (size != -1 && read != size) {
                return false;
            }
            header.rewind();

            if (header.getInt() != magic) {
                return false;
            }

            majorVersion = header.getShort();
            minorVersion = header.getShort();
            int constantPoolSize = header.getShort();

            return constantPoolInfo
                  .containsAnnotation(constantPoolSize, header);

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
         */
        public boolean containsAnnotation(int constantPoolSize,
                                          final ByteBuffer buffer)
              throws IOException {

            for (int i = 1; i < constantPoolSize; i++) {
                final byte type = buffer.get();
                switch (type) {
                    case ASCIZ:
                    case UNICODE:
                        final short length = buffer.getShort();
                        if (length < 0 || length > Short.MAX_VALUE) {
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
                        buffer.getShort();
                        break;
                    case FIELDREF:
                    case METHODREF:
                    case INTERFACEMETHODREF:
                    case INTEGER:
                    case FLOAT:
                        buffer.position(buffer.position() + 4);
                        break;
                    case LONG:
                    case DOUBLE:
                        buffer.position(buffer.position() + 8);
                        // for long, and double, they use 2 constantPool
                        i++;
                        break;
                    case NAMEANDTYPE:
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

    } // END ConstantPoolInfo

}
