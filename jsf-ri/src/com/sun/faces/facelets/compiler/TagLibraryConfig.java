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
 *
 * This file incorporates work covered by the following copyright and
 * permission notice:
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sun.faces.facelets.compiler;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import com.sun.faces.facelets.tag.AbstractTagLibrary;
import com.sun.faces.facelets.tag.TagHandler;
import com.sun.faces.facelets.tag.TagLibrary;
import com.sun.faces.facelets.tag.jsf.CompositeComponentTagLibrary;
import com.sun.faces.facelets.util.ParameterCheck;
import com.sun.faces.facelets.util.Classpath;
import com.sun.faces.facelets.util.ReflectionUtil;

/**
 * Handles creating a {@link com.sun.facelets.tag.TagLibrary TagLibrary} from a
 * {@link java.net.URL URL} source.
 *
 * @author Jacob Hookom
 * @version $Id$
 */
public final class TagLibraryConfig {

    private final static String SUFFIX = ".taglib.xml";
    private final static String MOJARRA_SUFFIX = ".taglib.mojarra.xml";

    protected final static Logger log = Logger.getLogger("facelets.compiler");

    private static class TagLibraryImpl extends AbstractTagLibrary {
        public TagLibraryImpl(String namespace) {
            super(namespace);
        }

        public void putConverter(String name, String id) {
            ParameterCheck.notNull("name", name);
            ParameterCheck.notNull("id", id);
            this.addConverter(name, id);
        }

        public void putConverter(String name, String id, Class handlerClass) {
            ParameterCheck.notNull("name", name);
            ParameterCheck.notNull("id", id);
            ParameterCheck.notNull("handlerClass", handlerClass);
            this.addConverter(name, id, handlerClass);
        }


        public void putValidator(String name, String id) {
            ParameterCheck.notNull("name", name);
            ParameterCheck.notNull("id", id);
            this.addValidator(name, id);
        }

        public void putValidator(String name, String id, Class handlerClass) {
            ParameterCheck.notNull("name", name);
            ParameterCheck.notNull("id", id);
            ParameterCheck.notNull("handlerClass", handlerClass);
            this.addValidator(name, id, handlerClass);
        }

        public void putTagHandler(String name, Class type) {
            ParameterCheck.notNull("name", name);
            ParameterCheck.notNull("type", type);
            this.addTagHandler(name, type);
        }

        public void putComponent(String name, String componentType,
                String rendererType) {
            ParameterCheck.notNull("name", name);
            ParameterCheck.notNull("componentType", componentType);
            this.addComponent(name, componentType, rendererType);
        }

        public void putComponent(String name, String componentType,
                String rendererType, Class handlerClass) {
            ParameterCheck.notNull("name", name);
            ParameterCheck.notNull("componentType", componentType);
            ParameterCheck.notNull("handlerClass", handlerClass);
            this.addComponent(name, componentType, rendererType, handlerClass);
        }


        public void putUserTag(String name, URL source) {
            ParameterCheck.notNull("name", name);
            ParameterCheck.notNull("source", source);
            this.addUserTag(name, source);
        }

        public void putFunction(String name, Method method) {
            ParameterCheck.notNull("name", name);
            ParameterCheck.notNull("method", method);
            this.addFunction(name, method);
        }
    }

    private static class LibraryHandler extends DefaultHandler {
        private final String file;

        private final URL source;

        private TagLibrary library;

        private final StringBuffer buffer;

        private Locator locator;

        private String tagName;

        private String converterId;

        private String validatorId;

        private String componentClassName;

        private String componentType;

        private String rendererType;

        private String functionName;

        private Class handlerClass;

        private Class functionClass;

        private String functionSignature;

        private String compositeLibraryName;

        private String namespace;

        public LibraryHandler(URL source) {
            this.file = source.getFile();
            this.source = source;
            this.buffer = new StringBuffer(64);
        }

        public TagLibrary getLibrary() {
            TagLibrary result = null;
            if (null != compositeLibraryName) {
                this.library = new CompositeComponentTagLibrary(namespace, compositeLibraryName);
            }
            return this.library;
        }

        public void endElement(String uri, String localName, String qName)
                throws SAXException {
            try {
                if ("facelet-taglib".equals(qName)) {
                  ; // Nothing to do
                }
                else if ("library-class".equals(qName)) {
                    this.processLibraryClass();
                }
                else if ("namespace".equals(qName)) {
                    this.namespace = this.captureBuffer();
                    this.library = new TagLibraryImpl(this.namespace);
                }
                else if ("composite-library-name".equals(qName)) {
                    this.compositeLibraryName = this.captureBuffer();
                }
                else if ("component-type".equals(qName)) {
                    this.componentType = this.captureBuffer();
                }
                else if ("renderer-type".equals(qName)) {
                    this.rendererType = this.captureBuffer();
                }
                else if ("tag-name".equals(qName)) {
                    this.tagName = this.captureBuffer();
                }
                else if ("function-name".equals(qName)) {
                    this.functionName = this.captureBuffer();
                }
                else if ("function-class".equals(qName)) {
                    String className = this.captureBuffer();
                    this.functionClass = this.createClass(Object.class, className);
                }
                else
                {
                    // Make sure there we've seen a namespace element
                    // before trying any of the following elements to avoid
                    // obscure NPEs
                    if (this.library == null) {
                        throw new IllegalStateException("No <namespace> element");
                    }

                    TagLibraryImpl impl = (TagLibraryImpl) this.library;

                    if ("tag".equals(qName)) {
                        if (this.handlerClass != null) {
                            impl.putTagHandler(this.tagName, this.handlerClass);
                        }
                    }
                    else if ("handler-class".equals(qName)) {
                        String cName = this.captureBuffer();
                        this.handlerClass = this.createClass(
                                                  TagHandler.class, cName);
                    }
                    else if ("component".equals(qName)) {
                        if (this.handlerClass != null) {
                            impl.putComponent(this.tagName,
                                              this.componentType,
                                              this.rendererType,
                                              this.handlerClass);
                            this.handlerClass = null;
                        }
                        else {
                            impl.putComponent(this.tagName,
                                              this.componentType,
                                              this.rendererType);
                        }
                    }
                    else if ("converter-id".equals(qName)) {
                        this.converterId = this.captureBuffer();
                    }
                    else if ("converter".equals(qName)) {
                        if (this.handlerClass != null) {
                            impl.putConverter(this.tagName,
                                              this.converterId,
                                              handlerClass);
                            this.handlerClass = null;
                        }
                        else {
                            impl.putConverter(this.tagName,
                                              this.converterId);
                        }
                        this.converterId = null;
                    }
                    else if ("validator-id".equals(qName)) {
                        this.validatorId = this.captureBuffer();
                    }
                    else if ("validator".equals(qName)) {
                        if (this.handlerClass != null) {
                            impl.putValidator(this.tagName,
                                              this.validatorId,
                                              handlerClass);
                            this.handlerClass = null;
                        }
                        else {
                            impl.putValidator(this.tagName,
                                              this.validatorId);
                        }
                        this.validatorId = null;
                    }
                    else  if ("source".equals(qName)) {
                        String path = this.captureBuffer();
                        URL url = new URL(this.source, path);
                        impl.putUserTag(this.tagName, url);
                    }
                    else if ("function-signature".equals(qName)) {
                        this.functionSignature = this.captureBuffer();
                        Method m = this.createMethod(this.functionClass, this.functionSignature);
                        impl.putFunction(this.functionName, m);
                    }
                }
            } catch (Exception e) {
                SAXException saxe =
                   new SAXException("Error Handling [" + this.source + "@"
                        + this.locator.getLineNumber() + ","
                        + this.locator.getColumnNumber() + "] <" + qName
                                    + ">");
                saxe.initCause(e);
                throw saxe;
            }
        }

        private String captureBuffer() throws Exception {
            String s = this.buffer.toString().trim();
            if (s.length() == 0) {
                throw new Exception("Value Cannot be Empty");
            }
            this.buffer.setLength(0);
            return s;
        }

        private static Class createClass(Class type, String name) throws Exception {
            Class factory = ReflectionUtil.forName(name);
            if (!type.isAssignableFrom(factory)) {
                throw new Exception(name + " must be an instance of "
                        + type.getName());
            }
            return factory;
        }

        private static Method createMethod(Class type, String s) throws Exception {
            Method m = null;
            int pos = s.indexOf(' ');
            if (pos == -1) {
                throw new Exception("Must Provide Return Type: "+s);
            } else {
                String rt = s.substring(0, pos).trim();
                int pos2 = s.indexOf('(', pos+1);
                if (pos2 == -1) {
                    throw new Exception("Must provide a method name, followed by '(': "+s);
                } else {
                    String mn = s.substring(pos+1, pos2).trim();
                    pos = s.indexOf(')', pos2 + 1);
                    if (pos == -1) {
                        throw new Exception("Must close parentheses, ')' missing: "+s);
                    } else {
                        String[] ps = s.substring(pos2 + 1, pos).trim().split(",");
                        Class[] pc;
                        if (ps.length == 1 && "".equals(ps[0])) {
                            pc = new Class[0];
                        } else {
                            pc = new Class[ps.length];
                            for (int i = 0; i < pc.length; i ++) {
                                pc[i] = ReflectionUtil.forName(ps[i].trim());
                            }
                        }
                        try {
                            return type.getMethod(mn, pc);
                        } catch (NoSuchMethodException e) {
                            throw new Exception("No Function Found on type: "+type.getName()+" with signature: "+s);
                        }

                    }

                }
            }
        }

        private void processLibraryClass() throws Exception {
            String name = this.captureBuffer();
            Class type = this.createClass(TagLibrary.class, name);
            this.library = (TagLibrary) type.newInstance();
        }

        public InputSource resolveEntity(String publicId, String systemId)
                throws SAXException {
            if ("-//Sun Microsystems, Inc.//DTD Facelet Taglib 1.0//EN"
                    .equals(publicId)) {
                URL url = Thread.currentThread().getContextClassLoader()
                        .getResource("com/sun/faces/facelet-taglib_1_0.dtd");
                return new InputSource(url.toExternalForm());
            }
            return null;
        }

        public void characters(char[] ch, int start, int length)
                throws SAXException {
            this.buffer.append(ch, start, length);
        }

        public void startElement(String uri, String localName, String qName,
                Attributes attributes) throws SAXException {
            this.buffer.setLength(0);
            if ("tag".equals(qName)) {
                this.componentClassName = null;
                this.handlerClass = null;
                this.componentType = null;
                this.rendererType = null;
                this.tagName = null;
            } else if ("function".equals(qName)) {
                this.functionName = null;
                this.functionClass = null;
                this.functionSignature = null;
            }
        }

        public void error(SAXParseException e) throws SAXException {
            SAXException saxe =
                new SAXException("Error Handling [" + this.source + "@"
                      + e.getLineNumber() + "," + e.getColumnNumber() + "]");
            saxe.initCause(e);
            throw saxe;
        }

        public void setDocumentLocator(Locator locator) {
            this.locator = locator;
        }

        public void fatalError(SAXParseException e) throws SAXException {
            throw e;
        }

        public void warning(SAXParseException e) throws SAXException {
            throw e;
        }
    }

    public TagLibraryConfig() {
        super();
    }

    public static TagLibrary create(URL url) throws IOException {
        InputStream is = null;
        TagLibrary t = null;
        try {
            is = url.openStream();
            LibraryHandler handler = new LibraryHandler(url);
            SAXParser parser = createSAXParser(handler);
            parser.parse(is, handler);
            t = handler.getLibrary();
        } catch (SAXException e) {
          IOException ioe =
            new IOException("Error parsing [" + url + "]: ");
          ioe.initCause(e);
          throw ioe;
        } catch (ParserConfigurationException e) {
          IOException ioe =
            new IOException("Error parsing [" + url + "]: ");
          ioe.initCause(e);
          throw ioe;
        } finally {
            if (is != null)
                is.close();
        }
        return t;
    }



    public void loadMojarraImplicit(Compiler compiler) throws IOException {
        loadImplicit(compiler, MOJARRA_SUFFIX);
    }

    public void loadImplicit(Compiler compiler) throws IOException {

        loadImplicit(compiler, SUFFIX);

    }

    private static void loadImplicit(Compiler compiler, String suffix)
    throws IOException {

        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        URL[] urls = Classpath.search(cl, "META-INF/", suffix);
        for (int i = 0; i < urls.length; i++) {
            try {
                compiler.addTagLibrary(create(urls[i]));
                if (log.isLoggable(Level.FINE)) {
                    log.fine("Added Library from: " + urls[i]);
                }
            } catch (Exception e) {
                log.log(Level.SEVERE, "Error Loading Library: " + urls[i], e);
            }
        }

    }

    private static final SAXParser createSAXParser(LibraryHandler handler)
            throws SAXException, ParserConfigurationException {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        factory.setNamespaceAware(false);
        factory.setFeature("http://xml.org/sax/features/validation", true);
        factory.setValidating(true);
        SAXParser parser = factory.newSAXParser();
        XMLReader reader = parser.getXMLReader();
        reader.setErrorHandler(handler);
        reader.setEntityResolver(handler);
        return parser;
    }

}
