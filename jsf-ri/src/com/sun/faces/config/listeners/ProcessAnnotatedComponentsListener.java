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

package com.sun.faces.config.listeners;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.Map;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Enumeration;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.jar.JarFile;
import java.util.jar.JarEntry;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.Channels;
import java.net.URL;
import java.net.MalformedURLException;
import java.net.URLConnection;
import java.net.JarURLConnection;
import java.io.IOException;
import java.lang.annotation.Annotation;

import javax.faces.event.SystemEventListener;
import javax.faces.event.SystemEvent;
import javax.faces.event.AbortProcessingException;
import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import javax.faces.context.ExternalContext;
import javax.faces.FacesException;
import javax.faces.FactoryFinder;
import javax.faces.render.FacesRenderKit;
import javax.faces.render.RenderKitFactory;
import javax.faces.render.RenderKit;
import javax.faces.render.Renderer;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.convert.FacesConverter;
import javax.faces.convert.Converter;
import javax.faces.component.FacesComponent;
import javax.faces.component.UIComponent;
import javax.faces.render.FacesRenderer;

import com.sun.faces.RIConstants;
import com.sun.faces.util.FacesLogger;

/**
 * RELEASE_PENDING (rlubke,driscoll) Document; Clean up error handling.
 */
public class ProcessAnnotatedComponentsListener implements SystemEventListener {

    private static final Logger LOGGER = FacesLogger.CONFIG.getLogger();

    private static final String WEB_INF_CLASSES = "/WEB-INF/classes/";
    private static final String WEB_INF_LIB = "/WEB-INF/lib/";
    private static final String FACES_CONFIG_XML = "META-INF/faces-config.xml";

    ClassFile classFileScanner = new ClassFile();


    // ---------------------------------------- Methods from SystemEventListener


    public void processEvent(SystemEvent event) throws AbortProcessingException {

        FacesContext ctx = FacesContext.getCurrentInstance();
        assert (ctx != null);

        // if the web application has been marked as 'metadata-complete'
        // do not process annotations
        if (Boolean.TRUE.equals(ctx.getExternalContext()
              .getApplicationMap().get(RIConstants.METADATA_COMPLETE))) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.fine("Application considered metadata-complete.  Configuration annotations will not be processed");
            }
            return;
        }


        Set<String> classList = new HashSet<String>();

        processWebInfClasses(ctx, classList);
        processWebInfLib(ctx, classList);

        // all annotated classes found now process them.
        if (!classList.isEmpty()) {
            processAnnotations(ctx, classList);
        }

    }



    public boolean isListenerForSource(Object source) {

        return (source instanceof Application);

    }


    // --------------------------------------------------------- Private Methods


    private void processWebInfLib(FacesContext ctx, Set<String> classList) {

        ExternalContext extCtx = ctx.getExternalContext();
        Set<String> entries = extCtx.getResourcePaths(WEB_INF_LIB);
        List<JarFile> jars = getJars(ctx, entries);
        if (jars != null) {
            for (JarFile jar : jars) {
                processJarEntries(jar, classList);
            }
        }

    }


    private void processJarEntries(JarFile jarFile, Set<String> classList) {

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
                        classList.add(convertToClassName(name));
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (channel != null) {
                        try {
                            channel.close();
                        } catch (IOException ignored) {

                        }
                    }
                }
            }
        }
        
    }

    private List<JarFile> getJars(FacesContext ctx, Set<String> entries) {
        List<JarFile> jars = null;
        if (entries != null && !entries.isEmpty()) {
            ExternalContext extCtx = ctx.getExternalContext();
            for (String entry : entries) {
                if (entry.endsWith(".jar")) {
                    try {
                        URL url = extCtx.getResource(entry);
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


    private void processWebInfClasses(FacesContext ctx, Set<String> classList) {

        processWebInfClasses(ctx, WEB_INF_CLASSES, classList);

    }


    private void processWebInfClasses(FacesContext ctx,
                                      String path,
                                      Set<String> classList) {

        ExternalContext extContext = ctx.getExternalContext();
        Set<String> paths = extContext.getResourcePaths(path);
        processWebInfClasses(ctx, paths, classList);

    }


    private void processWebInfClasses(FacesContext ctx,
                                      Set<String> paths,
                                      Set<String> classList) {
        if (paths != null && !paths.isEmpty()) {
            for (String pathElement : paths) {
                if (pathElement.endsWith("/")) {
                    processWebInfClasses(ctx, pathElement, classList);
                } else {
                    if (pathElement.endsWith(".class")) {
                        if (containsAnnotation(ctx, pathElement)) {
                            classList.add(convertToClassName(WEB_INF_CLASSES,
                                                             pathElement));
                        }
                    }
                }
            }
        }
    }


    private boolean containsAnnotation(FacesContext ctx, String pathElement) {

        ReadableByteChannel channel = null;
        try {
            URL url = ctx.getExternalContext().getResource(pathElement);
            URLConnection conn = url.openConnection();
            conn.setUseCaches(false);
            channel = Channels.newChannel(url.openStream());
            return classFileScanner.containsAnnotation(channel,
                                                       conn.getContentLength());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            if (channel != null) {
                try {
                    channel.close();
                } catch (IOException ignored) {
                }
            }
        }
        return false;

    }


    private String convertToClassName(String pathEntry) {

        return convertToClassName(null, pathEntry);

    }


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


     private void processAnnotations(FacesContext ctx,
                                    Set<String> classList) {

        Application app = ctx.getApplication();
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        Map<String,FacesRenderer> annotatedRenderers = new HashMap<String,FacesRenderer>();
        Map<String, FacesRenderKit> annotatedRenderKits = new HashMap<String,FacesRenderKit>();
        for (String className : classList) {
            try {
                Class<?> clazz = Class.forName(className, false, loader);
                Annotation[] annotations = clazz.getAnnotations();
                for (Annotation annotation : annotations) {
                    if (annotation.annotationType().equals(FacesComponent.class)) {
                        if (!UIComponent.class.isAssignableFrom(clazz)) {
                            throw new FacesException("Class, " + className + ", annotated with FacesComponent annotation is not an instance of UIComponent");
                        }
                        app.addComponent(((FacesComponent) annotation).value(), className);
                    }
                    if (annotation.annotationType().equals(FacesConverter.class)) {
                        if (!Converter.class.isAssignableFrom(clazz)) {
                            throw new FacesException("Class, " + className + ", annotated with FacesConverter annotation is not an instance of Converter");
                        }
                        app.addConverter(((FacesConverter) annotation).value(), className);
                    }
                    if (annotation.annotationType().equals(FacesValidator.class)) {
                        if (!Validator.class.isAssignableFrom(clazz)) {
                            throw new FacesException("Class, " + className + ", annotated with FacesValidator annotation is not an instance of Validator");
                        }
                        app.addValidator(((FacesValidator) annotation).value(), className);
                    }
                    if (annotation.annotationType().equals(FacesRenderer.class)) {
                        if (!Renderer.class.isAssignableFrom(clazz)) {
                            throw new FacesException("Class, " + className + ", annotated with FacesRenderer annotation is not an instance of Renderer");
                        }
                        annotatedRenderers.put(className, (FacesRenderer) annotation);
                    }
                    if (annotation.annotationType().equals(FacesRenderKit.class)) {
                        if (!RenderKit.class.isAssignableFrom(clazz)) {
                            throw new FacesException("Class, " + className + ", annotated with FacesRenderKit annotation is not an instance of RenderKit");
                        }
                        annotatedRenderKits.put(className, (FacesRenderKit) annotation);
                    }
                }
            } catch (ClassNotFoundException cnfe) {
                throw new FacesException(cnfe);
            }
        }

        // special handling for renderers and renderkits.  Make sure all
        // of the RenderKits are processed before the renderers.
        if (!annotatedRenderKits.isEmpty()) {
            RenderKitFactory factory = (RenderKitFactory) FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
            for (Map.Entry<String,FacesRenderKit> entry : annotatedRenderKits.entrySet()) {
                String className = entry.getKey();
                FacesRenderKit annotation = entry.getValue();
                try {
                    Class rkClass = Class.forName(className, false, loader);
                    factory.addRenderKit(annotation.value(), (RenderKit) rkClass.newInstance());
                } catch (Exception e) {
                    throw new FacesException(e);
                }
            }
        }
        if (!annotatedRenderers.isEmpty()) {
            RenderKitFactory factory = (RenderKitFactory) FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
            for (Map.Entry<String,FacesRenderer> entry : annotatedRenderers.entrySet()) {
                String className = entry.getKey();
                FacesRenderer annotation = entry.getValue();
                try {
                    Class rClass = Class.forName(className, false, loader);
                    RenderKit rk = factory.getRenderKit(ctx, annotation.renderKitId());
                    assert (rk != null);
                    rk.addRenderer(annotation.componentFamily(),
                                   annotation.rendererType(),
                                   (Renderer) rClass.newInstance());
                } catch (Exception e) {
                    throw new FacesException(e);
                }
            }
        }
    }

}
