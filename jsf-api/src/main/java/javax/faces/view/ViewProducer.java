/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2014 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.dev.java.net/public/CDDLGPL_1_1.html
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
package javax.faces.view;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import static java.util.Arrays.asList;
import static java.util.Collections.emptySet;
import static java.util.Collections.singleton;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.Default;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.enterprise.util.AnnotationLiteral;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.xml.namespace.NamespaceContext;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.xml.sax.InputSource;

/**
 * <p class="changed_added_2_3">
 *  The ViewProducer is the CDI producer that allows you to inject the 
 *  UIViewRoot and to do EL resolving of #{view}
 * </p>
 *
 * @since 2.3
 * @see UIViewRoot
 */
public class ViewProducer implements Bean<UIViewRoot> {

    /**
     * Stores the active flag.
     */
    protected Boolean active;

    /**
     * Check if we are active.
     */
    protected void checkActive() {
        if (active == null) {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            active = getWebXmlVersion(facesContext).equals("4.0")
                    || getFacesConfigXmlVersion(facesContext).equals("2.3");
        }

        if (!active) {
            throw new IllegalStateException("Cannot use @Inject without stating JSF 2.3 version or Servlet 4.0 version");
        }
    }    
    
    /**
     * Inner class defining an annotation literal for @Default.
     */
    public class DefaultAnnotationLiteral
            extends AnnotationLiteral<Default> {

        private static final long serialVersionUID = 1L;
    }

    /**
     * Create the actual instance.
     *
     * @param creationalContext the creational context.
     * @return the Faces context.
     */
    @Override
    public UIViewRoot create(CreationalContext<UIViewRoot> creationalContext) {
        checkActive();
        return FacesContext.getCurrentInstance().getViewRoot();
    }

    /**
     * Destroy the instance.
     *
     * @param instance the instance.
     * @param creationalContext the creational context.
     */
    @Override
    public void destroy(UIViewRoot instance, CreationalContext<UIViewRoot> creationalContext) {
    }

    /**
     * Get the bean class.
     *
     * @return the bean class.
     */
    @Override
    public Class<?> getBeanClass() {
        return UIViewRoot.class;
    }

    /**
     * Get the injection points.
     *
     * @return the injection points.
     */
    @Override
    public Set<InjectionPoint> getInjectionPoints() {
        return emptySet();
    }

    /**
     * Get the name.
     *
     * @return the name.
     */
    @Override
    public String getName() {
        return "view";
    }

    /**
     * Get the qualifiers.
     *
     * @return the qualifiers.
     */
    @Override
    public Set<Annotation> getQualifiers() {
        return singleton((Annotation) new DefaultAnnotationLiteral());
    }

    /**
     * Get the scope.
     *
     * @return the scope.
     */
    @Override
    public Class<? extends Annotation> getScope() {
        return RequestScoped.class;
    }

    /**
     * Get the stereotypes.
     *
     * @return the stereotypes.
     */
    @Override
    public Set<Class<? extends Annotation>> getStereotypes() {
        return emptySet();
    }

    /**
     * Get the types.
     *
     * @return the types.
     */
    @Override
    public Set<Type> getTypes() {
        return new HashSet<>(asList(UIViewRoot.class));
    }

    /**
     * Is this an alternative.
     *
     * @return false.
     */
    @Override
    public boolean isAlternative() {
        return false;
    }

    /**
     * Is this nullable.
     *
     * @return false.
     */
    @Override
    public boolean isNullable() {
        return true;
    }

    /**
     * Get the web.xml version (if any).
     *
     * @param facesContext the Faces context.
     * @return the version found, or "" if none found.
     */
    protected String getWebXmlVersion(FacesContext facesContext) {
        String result = "";
        InputStream stream = null;
        try {
            URL url = facesContext.getExternalContext().getResource("/WEB-INF/web.xml");
            if (url != null) {
                XPathFactory factory = XPathFactory.newInstance();
                XPath xpath = factory.newXPath();
                xpath.setNamespaceContext(new JavaeeNamespaceContext());
                stream = url.openStream();
                result = xpath.evaluate("string(/javaee:web-app/@version)", new InputSource(stream));
            }
        } catch (MalformedURLException mue) {
        } catch (XPathExpressionException | IOException xpee) {
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException ioe) {
                }
            }
        }
        return result;
    }

    /**
     * Get the faces-config.xml version (if any).
     *
     * @param facesContext the Faces context.
     * @return the version found, or "" if none found.
     */
    protected String getFacesConfigXmlVersion(FacesContext facesContext) {
        String result = "";
        InputStream stream = null;
        try {
            URL url = facesContext.getExternalContext().getResource("/WEB-INF/faces-config.xml");
            if (url != null) {
                XPathFactory factory = XPathFactory.newInstance();
                XPath xpath = factory.newXPath();
                xpath.setNamespaceContext(new JavaeeNamespaceContext());
                stream = url.openStream();
                result = xpath.evaluate("string(/javaee:faces-config/@version)", new InputSource(stream));
            }
        } catch (MalformedURLException mue) {
        } catch (XPathExpressionException | IOException xpee) {
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException ioe) {
                }
            }
        }
        return result;
    }

    public class JavaeeNamespaceContext implements NamespaceContext {

        @Override
        public String getNamespaceURI(String prefix) {
            return "http://xmlns.jcp.org/xml/ns/javaee";
        }

        @Override
        public String getPrefix(String namespaceURI) {
            return "javaee";
        }

        @Override
        public Iterator getPrefixes(String namespaceURI) {
            return null;
        }
    }
}
