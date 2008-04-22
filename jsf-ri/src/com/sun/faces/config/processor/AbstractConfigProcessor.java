/*
 * $Id: AbstractConfigProcessor.java,v 1.5 2007/07/17 21:35:18 rlubke Exp $
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

package com.sun.faces.config.processor;

import com.sun.faces.application.ApplicationResourceBundle;
import com.sun.faces.config.ConfigurationException;
import com.sun.faces.config.WebConfiguration;
import com.sun.faces.config.WebConfiguration.WebContextInitParameter;
import com.sun.faces.util.ReflectionUtils;
import com.sun.faces.util.Util;
import com.sun.faces.scripting.GroovyHelper;
import com.sun.faces.scripting.RendererProxy;
import com.sun.faces.scripting.NavigationHandlerProxy;
import com.sun.faces.scripting.ELResolverProxy;
import com.sun.faces.scripting.PhaseListenerProxy;
import com.sun.faces.scripting.ViewHandlerProxy;
import com.sun.faces.scripting.ActionListenerProxy;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import javax.faces.FactoryFinder;
import javax.faces.event.ActionListener;
import javax.faces.event.PhaseListener;
import javax.faces.render.Renderer;
import javax.faces.application.Application;
import javax.faces.application.ApplicationFactory;
import javax.faces.application.NavigationHandler;
import javax.faces.application.ViewHandler;
import javax.el.ELResolver;

import java.lang.reflect.Constructor;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * <p>
 *   This is the base <code>ConfigProcessor</code> that all concrete
 *   <code>ConfigProcessor</code> implementations should extend.
 * </p> 
 */
public abstract class AbstractConfigProcessor implements ConfigProcessor {


    private ConfigProcessor nextProcessor;  


    // -------------------------------------------- Methods from ConfigProcessor


    /**
     * @see ConfigProcessor#setNext(ConfigProcessor)
     */
    public void setNext(ConfigProcessor nextProcessor) {

        this.nextProcessor = nextProcessor;
        
    }
    

    /**
     * @see ConfigProcessor#invokeNext(org.w3c.dom.Document[])
     */
    public void invokeNext(Document[] documents)
    throws Exception {

        if (nextProcessor != null) {
            nextProcessor.process(documents);
        }
        
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


    protected Object createInstance(String className, Node source) {
        return createInstance(className, null, null, source);
    }

    protected Object createInstance(String className,
                                    Class rootType,
                                    Object root,
                                    Node source) {
        Class clazz;
        Object returnObject = null;
        if (className != null) {
            GroovyHelper groovyHelper = GroovyHelper.getCurrentInstance();
            try {
                if (isDevModeEnabled() && groovyHelper != null && GroovyHelper
                      .isGroovyScript(className)) {
                    returnObject = createScriptProxy(rootType, className, root, groovyHelper);
                }
                if (returnObject == null) {
                    clazz = loadClass(className, returnObject, rootType, groovyHelper);
                    if (clazz != null) {
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
                }
            } catch (ClassNotFoundException cnfe) {
                throw new ConfigurationException(
                      buildMessage(MessageFormat.format("Unable to find class ''{0}''",
                                                        className),
                                   source));
            } catch (NoClassDefFoundError ncdfe) {
                throw new ConfigurationException(
                      buildMessage(MessageFormat.format("Class ''{0}'' is missing a runtime dependency: {1}",
                                                        className,
                                                        ncdfe.toString()),
                                   source));
            } catch (ClassCastException cce) {
                throw new ConfigurationException(
                      buildMessage(MessageFormat.format("Class ''{0}'' is not an instance of ''{1}''",
                                                        className,
                                                        rootType),
                                   source));
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


    protected Class<?> loadClass(String className,
                                 Object fallback,
                                 Class<?> expectedType,
                                 GroovyHelper groovyHelper)
    throws ClassNotFoundException {

        Class<?> clazz;
        if (groovyHelper != null && GroovyHelper.isGroovyScript(className)) {
            clazz = groovyHelper.loadScript(className);
        } else {
            clazz = Util.loadClass(className, fallback);
        }
        if (expectedType != null && !expectedType.isAssignableFrom(clazz)) {
                throw new ClassCastException();
        }
        return clazz;
        
    }


    // --------------------------------------------------------- Private Methods


    private String buildMessage(String cause, Node source) {

        return MessageFormat.format("\n  Source Document: {0}\n  Cause: {1}",
                                    source.getOwnerDocument().getDocumentURI(),
                                    cause);

    }


    private Object createScriptProxy(Class<?> artifactType,
                                     String scriptName,
                                     Object root,
                                     GroovyHelper groovyHelper) {
        if (Renderer.class.equals(artifactType)) {
            return new RendererProxy(scriptName, groovyHelper);
        } else if (PhaseListener.class.equals(artifactType)) {
            return new PhaseListenerProxy(scriptName, groovyHelper);
        } else if (ViewHandler.class.equals(artifactType)) {
            return new ViewHandlerProxy(scriptName,
                                              groovyHelper,
                                              (ViewHandler) root);
        } else if (NavigationHandler.class.equals(artifactType)) {
            return new NavigationHandlerProxy(scriptName,
                                              groovyHelper,
                                              (NavigationHandler) root);
        } else if (ActionListener.class.equals(artifactType)) {
            return new ActionListenerProxy(scriptName,
                                           groovyHelper,
                                           (ActionListener) root);
        } else if (ELResolver.class.equals(artifactType)) {
            return new ELResolverProxy(scriptName, groovyHelper);
        } else {
            return null;
        }
    }


    private boolean isDevModeEnabled() {
        WebConfiguration webconfig = WebConfiguration.getInstance();
        return (webconfig != null
                  && "Development".equals(webconfig.getOptionValue(WebContextInitParameter.JavaxFacesProjectStage)));
    }


}
