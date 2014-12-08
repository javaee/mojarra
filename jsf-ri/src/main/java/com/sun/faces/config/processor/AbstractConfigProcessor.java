/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2010 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.faces.config.processor;

import com.sun.faces.application.ApplicationAssociate;
import com.sun.faces.application.ApplicationResourceBundle;
import com.sun.faces.application.ApplicationInstanceFactoryMetadataMap;
import com.sun.faces.application.annotation.AnnotationManager;
import com.sun.faces.config.ConfigurationException;
import com.sun.faces.config.WebConfiguration;
import com.sun.faces.config.ConfigManager;
import com.sun.faces.config.DocumentInfo;
import com.sun.faces.config.WebConfiguration.WebContextInitParameter;
import com.sun.faces.util.ReflectionUtils;
import com.sun.faces.util.Util;
import com.sun.faces.scripting.groovy.RendererProxy;
import com.sun.faces.scripting.groovy.NavigationHandlerProxy;
import com.sun.faces.scripting.groovy.ELResolverProxy;
import com.sun.faces.scripting.groovy.PhaseListenerProxy;
import com.sun.faces.scripting.groovy.ViewHandlerProxy;
import com.sun.faces.scripting.groovy.ActionListenerProxy;
import com.sun.faces.spi.InjectionProvider;
import com.sun.faces.spi.InjectionProviderException;
import com.sun.faces.util.FacesLogger;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import javax.faces.FactoryFinder;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionListener;
import javax.faces.event.PhaseListener;
import javax.faces.render.Renderer;
import javax.faces.application.Application;
import javax.faces.application.ApplicationFactory;
import javax.faces.application.NavigationHandler;
import javax.faces.application.ViewHandler;
import javax.el.ELResolver;
import javax.servlet.ServletContext;

import java.lang.reflect.Constructor;
import java.lang.annotation.Annotation;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.FacesException;
import javax.faces.application.ProjectStage;


/**
 * <p>
 *   This is the base <code>ConfigProcessor</code> that all concrete
 *   <code>ConfigProcessor</code> implementations should extend.
 * </p> 
 */
public abstract class AbstractConfigProcessor implements ConfigProcessor {


    private ConfigProcessor nextProcessor;  
    private static final Logger LOGGER = FacesLogger.CONFIG.getLogger();

    // -------------------------------------------- Methods from ConfigProcessor
    
    public AbstractConfigProcessor() {
    }

    private ApplicationInstanceFactoryMetadataMap<String,Object> getClassMetadataMap(ServletContext sc) {
        ApplicationInstanceFactoryMetadataMap<String,Object> classMetadataMap = 
                (ApplicationInstanceFactoryMetadataMap<String,Object>) sc.getAttribute(getClassMetadataMapKey());
        if (null == classMetadataMap) {
            classMetadataMap = new ApplicationInstanceFactoryMetadataMap(new ConcurrentHashMap<String, Object>());
            sc.setAttribute(getClassMetadataMapKey(), classMetadataMap);
        }
        
        return classMetadataMap;
    }

    @Override
    public void initializeClassMetadataMap(ServletContext sc) {
        getClassMetadataMap(sc);
    }
    
    protected String getClassMetadataMapKey() {
        return this.getClass().getName() + CLASS_METADATA_MAP_KEY_SUFFIX;
    }
    
    private static final String CLASS_METADATA_MAP_KEY_SUFFIX = ".METADATA";

    /**
     * @see ConfigProcessor#setNext(ConfigProcessor)
     */
    public void setNext(ConfigProcessor nextProcessor) {

        this.nextProcessor = nextProcessor;
        
    }
    
    @Override
    public ConfigProcessor getNext() {
        return this.nextProcessor;
    }
    

    /**
     * @see ConfigProcessor#invokeNext(javax.servlet.ServletContext,com.sun.faces.config.DocumentInfo[])
     */
    public void invokeNext(ServletContext sc, DocumentInfo[] documentInfos)
    throws Exception {

        if (nextProcessor != null) {
            nextProcessor.process(sc, documentInfos);
        }
        
    }

    @Override
    public void destroyNext(ServletContext sc) {
        if (nextProcessor != null) {
            nextProcessor.destroy(sc);
        }
    }
    
    

    @Override
    public void destroy(ServletContext sc) {
        destroyNext(sc);
    }

    // ------------------------------------------------------- Protected Methods


    /**
     * @return return the Application instance for this context.
     */
    protected Application getApplication() {

        ApplicationFactory afactory = (ApplicationFactory)
             FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY);
        return afactory.getApplication();

    }


    /**
     * <p>Return the text of the specified <code>Node</code>,
     * if any.
     * @param node the <code>Node</code>
     * @return the text of the <code>Node</code>  If the length
     *  of the text is zero, this method will return <code>null</code>
     */
    protected String getNodeText(Node node) {

        String res = null;
        if (node != null) {
            res = node.getTextContent();
            if (res != null) {
                res = res.trim();
            }
        }

        return ((res != null && res.length() != 0) ? res : null);

    }


    /**
     * @return a <code>Map</code> of of textual values keyed off the values
     * of any lang or xml:lang attributes specified on an attribute.  If no
     * such attribute exists, then the key {@link ApplicationResourceBundle#DEFAULT_KEY}
     * will be used (i.e. this represents the default Locale).
     * @param list a list of nodes representing textual elements such as
     *  description or display-name     
     */
    protected Map<String, String> getTextMap(List<Node> list) {

        if (list != null && !list.isEmpty()) {
            int len = list.size();
            HashMap<String, String> names =
                    new HashMap<String, String>(len, 1.0f);
            for (int i = 0; i < len; i++) {
                Node node = list.get(i);
                String textValue = getNodeText(node);
                if (textValue != null) {
                    if (node.hasAttributes()) {
                        NamedNodeMap attributes = node
                                .getAttributes();
                        String lang
                                = getNodeText(attributes.getNamedItem(
                                     "lang"));
                        if (lang == null) {
                            lang =
                                    getNodeText(attributes.getNamedItem(
                                         "xml:lang"));
                        }
                        if (lang != null) {
                            names.put(lang, textValue);
                        } else {                                                     
                            names.put(ApplicationResourceBundle.DEFAULT_KEY,
                                      textValue);
                        }
                    } else {
                        names.put(ApplicationResourceBundle.DEFAULT_KEY,
                                  textValue);
                    }
                }
            }

            return names;
        }

        return null;

    }

    protected Class<?> findRootType(ServletContext sc, String source,
                                    Node sourceNode,
                                    Class<?>[] ctorArguments) {

        try {
            Class<?> sourceClass = loadClass(sc, source, this, null);
            for (Class<?> ctorArg : ctorArguments) {
                if (ReflectionUtils.lookupConstructor(sourceClass, ctorArg) != null) {
                    return ctorArg;
                }
            }
        } catch (ClassNotFoundException cnfe) {
            throw new ConfigurationException(
                      buildMessage(MessageFormat.format("Unable to find class ''{0}''",
                                                        source), sourceNode),
                                                        cnfe);
        }

        return null;
    }


    protected Object createInstance(ServletContext sc, String className, Node source) {
        return createInstance(sc, className, null, null, source);
    }

    protected Object createInstance(ServletContext sc, String className,
                                    Class rootType,
                                    Object root,
                                    Node source) {
        boolean [] didPerformInjection = { false };
        Object result = createInstance(sc, className, rootType, root, source, true, didPerformInjection);
        return result;
    }

    protected Object createInstance(ServletContext sc, String className,
                                    Class rootType,
                                    Object root,
                                    Node source,
                                    boolean performInjection, boolean [] didPerformInjection) {
        Class clazz;
        Object returnObject = null;
        if (className != null) {
            try {
                clazz = loadClass(sc, className, returnObject, null);
                if (clazz != null) {
                    if (isDevModeEnabled(sc)) {
                        Class<?>[] interfaces = clazz.getInterfaces();
                        if (interfaces != null) {
                            for (Class<?> c : interfaces) {
                                if ("groovy.lang.GroovyObject".equals(c.getName())) {
                                    // all groovy classes will implement this interface
                                    returnObject = createScriptProxy(rootType, className, root);
                                    break;
                                }
                            }
                        }
                    }
                    if (returnObject == null) {
                        // Look for an adapter constructor if we've got
                        // an object to adapt
                        if ((rootType != null) && (root != null)) {
                            Constructor construct =
                                  ReflectionUtils.lookupConstructor(
                                        clazz,
                                        rootType);
                            if (construct != null) {
                                returnObject = construct.newInstance(root);
                            }
                        }
                    }
                    if (clazz != null && returnObject == null) {
                        returnObject = clazz.newInstance();
                    }
                    
                    ApplicationInstanceFactoryMetadataMap<String,Object>
                            classMetadataMap = getClassMetadataMap(sc);

                    if (classMetadataMap.hasAnnotations(className) && performInjection) {
                        InjectionProvider injectionProvider = (InjectionProvider) FacesContext.getCurrentInstance().getAttributes().get(ConfigManager.INJECTION_PROVIDER_KEY);

                        try {
                            injectionProvider.inject(returnObject);
                        } catch (InjectionProviderException ex) {
                            LOGGER.log(Level.SEVERE, "Unable to inject instance" + className, ex);
                            throw new FacesException(ex);
                        }

                        try {
                            injectionProvider.invokePostConstruct(returnObject);
                        } catch (InjectionProviderException ex) {
                            LOGGER.log(Level.SEVERE, "Unable to invoke @PostConstruct annotated method on instance " + className, ex);
                            throw new FacesException(ex);
                        }

                        didPerformInjection[0] = true;
                    }
                    
                }

            } catch (ClassNotFoundException cnfe) {
                throw new ConfigurationException(
                      buildMessage(MessageFormat.format("Unable to find class ''{0}''",
                                                        className),
                                   source),
                                   cnfe);
            } catch (NoClassDefFoundError ncdfe) {
                throw new ConfigurationException(
                      buildMessage(MessageFormat.format("Class ''{0}'' is missing a runtime dependency: {1}",
                                                        className,
                                                        ncdfe.toString()),
                                   source),
                                   ncdfe);
            } catch (ClassCastException cce) {
                throw new ConfigurationException(
                      buildMessage(MessageFormat.format("Class ''{0}'' is not an instance of ''{1}''",
                                                        className,
                                                        rootType),
                                   source),
                                   cce);
            } catch (Exception e) {
                throw new ConfigurationException(
                      buildMessage(MessageFormat.format("Unable to create a new instance of ''{0}'': {1}",
                                                        className,
                                                        e.toString()),
                                   source), e);
            }
        }

        return returnObject;
        
    }

    protected void destroyInstance(ServletContext sc, String className, Object instance) {
        if (null != instance) {
            ApplicationInstanceFactoryMetadataMap<String,Object>
                    classMetadataMap = getClassMetadataMap(sc);
            
            if (classMetadataMap.hasAnnotations(className)) {
                InjectionProvider injectionProvider = (InjectionProvider) FacesContext.getCurrentInstance().getAttributes().get(ConfigManager.INJECTION_PROVIDER_KEY);
                
                if (null != injectionProvider) {
                    try {
                        injectionProvider.invokePreDestroy(instance);
                    } catch (InjectionProviderException ex) {
                        LOGGER.log(Level.SEVERE, "Unable to invoke @PreDestroy annotated method on instance " + className, ex);
                        throw new FacesException(ex);
                    }
                }
            }
        }
    }

    protected Class<?> loadClass(ServletContext sc, String className,
                                 Object fallback,
                                 Class<?> expectedType)
    throws ClassNotFoundException {
        ApplicationInstanceFactoryMetadataMap<String,Object>
                classMetadataMap = getClassMetadataMap(sc);

        Class<?> clazz = (Class<?>) classMetadataMap.get(className);
        if (null == clazz) {
            try {
                clazz =  Util.loadClass(className, fallback);
                if (!this.isDevModeEnabled(sc)) {
                    classMetadataMap.put(className, clazz);    
                } else {
                    classMetadataMap.scanForAnnotations(className, clazz);
                }
                assert (clazz != null);
            } catch (Exception e) {
                throw new FacesException(e.getMessage(), e);
            }
            
        }
        if (expectedType != null && !expectedType.isAssignableFrom(clazz)) {
                throw new ClassCastException();
        }
        return clazz;
        
    }


    protected void processAnnotations(Class<? extends Annotation> annotationType) {

        FacesContext ctx = FacesContext.getCurrentInstance();
        ApplicationAssociate associate =
              ApplicationAssociate.getInstance(ctx.getExternalContext());
        AnnotationManager manager = associate.getAnnotationManager();
        manager.applyConfigAnnotations(ctx,
                                      annotationType,
                                      ConfigManager.getAnnotatedClasses(ctx).get(annotationType));
        
    }



    // --------------------------------------------------------- Private Methods


    private String buildMessage(String cause, Node source) {

        return MessageFormat.format("\n  Source Document: {0}\n  Cause: {1}",
                                    source.getOwnerDocument().getDocumentURI(),
                                    cause);

    }


    private Object createScriptProxy(Class<?> artifactType,
                                     String scriptName,
                                     Object root) {
        if (Renderer.class.equals(artifactType)) {
            return new RendererProxy(scriptName);
        } else if (PhaseListener.class.equals(artifactType)) {
            return new PhaseListenerProxy(scriptName);
        } else if (ViewHandler.class.equals(artifactType)) {
            return new ViewHandlerProxy(scriptName, (ViewHandler) root);
        } else if (NavigationHandler.class.equals(artifactType)) {
            return new NavigationHandlerProxy(scriptName, (NavigationHandler) root);
        } else if (ActionListener.class.equals(artifactType)) {
            return new ActionListenerProxy(scriptName, (ActionListener) root);
        } else if (ELResolver.class.equals(artifactType)) {
            return new ELResolverProxy(scriptName);
        } else {
            return null;
        }
    }


    private boolean isDevModeEnabled(ServletContext sc) {
        return getProjectStage(sc).equals(ProjectStage.Development);
    }
    
    private ProjectStage getProjectStage(ServletContext sc) {
        ProjectStage projectStage = null;
        final String projectStageKey = AbstractConfigProcessor.class.getName() + ".PROJECTSTAGE";
        projectStage = (ProjectStage) sc.getAttribute(projectStageKey);
        
        if (projectStage == null) {
            WebConfiguration webConfig =
                  WebConfiguration.getInstance(
                        FacesContext.getCurrentInstance().getExternalContext());
            String value = webConfig.getEnvironmentEntry(WebConfiguration.WebEnvironmentEntry.ProjectStage);
            if (value != null) {
                if (LOGGER.isLoggable(Level.FINE)) {
                    LOGGER.log(Level.FINE,
                               "ProjectStage configured via JNDI: {0}",
                               value);
                }
            } else {
                value = webConfig.getOptionValue(WebContextInitParameter.JavaxFacesProjectStage);
                if (value != null) {
                    if (LOGGER.isLoggable(Level.FINE)) {
                        LOGGER.log(Level.FINE,
                               "ProjectStage configured via servlet context init parameter: {0}", 
                               value);
                    }
                }
            }
            if (value != null) {
                try {
                    projectStage = ProjectStage.valueOf(value);
                } catch (IllegalArgumentException iae) {
                    if (LOGGER.isLoggable(Level.INFO)) {
                        LOGGER.log(Level.INFO,
                                   "Unable to discern ProjectStage for value {0}.",
                                   value);
                    }
                }
            }
            if (projectStage == null) {
                projectStage = ProjectStage.Production;
            }
            sc.setAttribute(projectStageKey, projectStage);
        }
        return projectStage;

    }
    


}
