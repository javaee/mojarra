/*
 * $Id: BootstrapperGenerator.java,v 1.1 2006/09/19 21:13:33 jdlee Exp $
 */

/*
 * The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the License). You may not use this file except in
 * compliance with the License.
 * 
 * You can obtain a copy of the License at
 * https://javaserverfaces.dev.java.net/CDDL.html or
 * legal/CDDLv1.0.txt. 
 * See the License for the specific language governing
 * permission and limitations under the License.
 * 
 * When distributing Covered Code, include this CDDL
 * Header Notice in each file and include the License file
 * at legal/CDDLv1.0.txt.    
 * If applicable, add the following below the CDDL Header,
 * with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 * 
 * [Name of File] [ver.__] [Date]
 * 
 * Copyright 2005 Sun Microsystems Inc. All Rights Reserved
 */
package com.sun.faces.generate;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import com.sun.faces.util.ToolsUtil;

/**
 * This class reads in the file jsf-ri-runtime.xml and creates a "bootstrap"
 * class that will be used by the ConfigureListener class to load and instantiate
 * the default components, validators, etc.
 * 
 * @author Jason Lee (jdlee)
 *
 */
public class BootstrapperGenerator {
    private final String CLASS_PACKAGE = "com.sun.faces.config";
    private static final Logger LOGGER = Logger.getLogger(ToolsUtil.FACES_LOGGER +
            ToolsUtil.GENERATE_LOGGER, ToolsUtil.TOOLS_LOG_STRINGS);    
    private AbstractGenerator.CodeWriter writer;
    private String copyright;
    private String runtimeConfig;
    private String outputDir;
    private Document document;
    private XPath xpath;
    
    public void init () {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(false);
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
            builder.setErrorHandler(new ParsingErrorHandler());
            document = builder.parse(runtimeConfig);
            xpath = XPathFactory.newInstance().newXPath();
        } catch (Exception e) {
            LOGGER.severe(e.getMessage());
        }
    }

    public void execute(String runtimeConfig, String copyright, String outputDir) {
        this.copyright = copyright;
        this.runtimeConfig = runtimeConfig;
        this.outputDir = outputDir;
        try {
            init();
            generateClass();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    private void generateClass() throws Exception {
        String packagePath = CLASS_PACKAGE.replace('.', File.separatorChar);
        File dir = new File(outputDir + File.separator + packagePath);
        System.out.println(dir);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        String fileName = "Bootstrapper.java";
        File file = new File(dir, fileName);
        writer = new AbstractGenerator.CodeWriter(new FileWriter(file));
        // Generate the various portions of the class
        prefix();
        properties();
        constructor();
        suffix();
        writer.flush();
        writer.close();
    }

    /**
     * <p>Generate the prefix for this component class, down to (and including)
     * the class declaration.</p>
     */
    private void prefix() throws Exception {
        // Generate the copyright information
        writer.write(getCopyrightText());

        // Generate the package declaration
        writer.writePackage(CLASS_PACKAGE);

        writer.write('\n');

        // Generate the imports
        writer.writeImport("java.util.ArrayList");
        writer.writeImport("java.util.List");
        writer.write('\n');
        writer.writeImport("com.sun.faces.config.beans.ApplicationBean");
        writer.writeImport("com.sun.faces.config.beans.ComponentBean");
        writer.writeImport("com.sun.faces.config.beans.ConverterBean");
        writer.writeImport("com.sun.faces.config.beans.FacesConfigBean");
        writer.writeImport("com.sun.faces.config.beans.FactoryBean");
        writer.writeImport("com.sun.faces.config.beans.LifecycleBean");
        writer.writeImport("com.sun.faces.config.beans.RenderKitBean");
        writer.writeImport("com.sun.faces.config.beans.RendererBean");
        writer.writeImport("com.sun.faces.config.beans.ValidatorBean");
        writer.write("\n\n");

        writer.writeBlockComment("******* GENERATED CODE - DO NOT EDIT *******");
        writer.write("\n\n");

        writer.writePublicClassDeclaration("Bootstrapper", null, 
                null, false, true);
        writer.indent();
    }

    private void properties() throws Exception {
        writer.fwrite("private FactoryBean factoryBean;\n");
        writer.fwrite("private ApplicationBean applicationBean;\n");
        writer.fwrite("private LifecycleBean lifecycleBean;\n");
        writer.fwrite("private RenderKitBean renderKitBean;\n");
        writer.fwrite("private List<ConverterBean> converters = new ArrayList<ConverterBean>();\n");
        writer.fwrite("private List<ValidatorBean> validators = new ArrayList<ValidatorBean>();\n");
        writer.fwrite("private List<ComponentBean> components = new ArrayList<ComponentBean>();\n");
    }

    private void constructor() throws Exception {
        writer.write('\n');
        writer.fwrite("public Bootstrapper() {\n");
        writer.indent();
        createFactoryBean();
        createApplicationBean();
        createLifecycleBean();
        loadConverters();
        loadValidators();
        loadComponents();
        loadRenderKit();
        writer.outdent();
        writer.fwrite("}\n\n");
    }


    private void createFactoryBean() throws Exception {
        writer.write('\n');
        writer.fwrite("factoryBean = new FactoryBean();\n");
        outputAdderCalls((NodeList)xpath.evaluate("/faces-config/factory/application-factory/text()", document, XPathConstants.NODESET),
            "factoryBean.addApplicationFactory");
        outputAdderCalls((NodeList)xpath.evaluate("/faces-config/factory/faces-context-factory/text()", document, XPathConstants.NODESET),
            "factoryBean.addFacesContextFactory");
        outputAdderCalls((NodeList)xpath.evaluate("/faces-config/factory/lifecycle-factory/text()", document, XPathConstants.NODESET),
            "factoryBean.addLifecycleFactory");
        outputAdderCalls((NodeList)xpath.evaluate("/faces-config/factory/render-kit-factory/text()", document, XPathConstants.NODESET),
            "factoryBean.addRenderKitFactory");
    }

    private void createApplicationBean() throws Exception {
        writer.write('\n');
        writer.fwrite("applicationBean = new ApplicationBean();\n");
        outputAdderCalls((NodeList)xpath.evaluate("/faces-config/application/action-listener/text()", document, XPathConstants.NODESET),
            "applicationBean.addActionListener");
        outputAdderCalls((NodeList)xpath.evaluate("/faces-config/application/navigation-handler/text()", document, XPathConstants.NODESET),
            "applicationBean.addNavigationHandler");
        outputAdderCalls((NodeList)xpath.evaluate("/faces-config/application/state-manager/text()", document, XPathConstants.NODESET),
            "applicationBean.addStateManager");
        outputAdderCalls((NodeList)xpath.evaluate("/faces-config/application/view-handler/text()", document, XPathConstants.NODESET),
            "applicationBean.addViewHandler");
    }
    
    private void createLifecycleBean() throws Exception {
        writer.write('\n');
        writer.fwrite("lifecycleBean = new LifecycleBean();\n");
        outputAdderCalls((NodeList)xpath.evaluate("/faces-config/lifecycle/phase-listener/text()", document, XPathConstants.NODESET),
            "lifecycleBean.addPhaseListener");
    }
    
    private void loadConverters() throws Exception {
        writer.write('\n');
        NodeList nl = (NodeList)xpath.evaluate("/faces-config/converter", document, XPathConstants.NODESET);
        writer.writeBlockComment("Wrap these in braces to allow repetition of the ivar name");
        for (int i = 0; i < nl.getLength(); i++) {
            NodeList children = nl.item(i).getChildNodes();
            writer.fwrite("{\n");
            writer.indent();
            writer.fwrite("ConverterBean converter = new ConverterBean();\n");
            for (int j = 0; j < children.getLength(); j++) {
                Node node = children.item(j);
                if ("converter-id".equals(node.getNodeName())) {
                    writer.fwrite("converter.setConverterId(\""+ node.getTextContent().trim() + "\");\n");
                } else if ("converter-class".equals(node.getNodeName())) {
                    writer.fwrite("converter.setConverterClass(\""+ node.getTextContent().trim() + "\");\n");
                } else if ("converter-for-class".equals(node.getNodeName())) {
                    writer.fwrite("converter.setConverterForClass(\""+ node.getTextContent().trim() + "\");\n");
                }
            }
            writer.fwrite ("converters.add(converter);\n");
            writer.outdent();
            writer.fwrite("}\n");
        }
    }
    
    private void loadValidators() throws Exception {
        writer.write('\n');
        NodeList nl = (NodeList)xpath.evaluate("/faces-config/validator", document, XPathConstants.NODESET);
        writer.writeBlockComment("Wrap these in braces to allow repetition of the ivar name");
        for (int i = 0; i < nl.getLength(); i++) {
            NodeList children = nl.item(i).getChildNodes();
            writer.fwrite("{\n");
            writer.indent();
            writer.fwrite("ValidatorBean validator = new ValidatorBean();\n");
            for (int j = 0; j < children.getLength(); j++) {
                Node node = children.item(j);
                if ("validator-id".equals(node.getNodeName())) {
                    writer.fwrite("validator.setValidatorId(\""+ node.getTextContent().trim() + "\");\n");
                } else if ("validator-class".equals(node.getNodeName())) {
                    writer.fwrite("validator.setValidatorClass(\""+ node.getTextContent().trim() + "\");\n");
                }
            }
            writer.fwrite ("validators.add(validator);\n");
            writer.outdent();
            writer.fwrite("}\n");
        }
    }

    private void loadComponents() throws Exception {
        writer.write('\n');
        NodeList nl = (NodeList)xpath.evaluate("/faces-config/component", document, XPathConstants.NODESET);
        writer.writeBlockComment("Wrap these in braces to allow repetition of the ivar name");
        for (int i = 0; i < nl.getLength(); i++) {
            NodeList children = nl.item(i).getChildNodes();
            writer.fwrite("{\n");
            writer.indent();
            writer.fwrite("ComponentBean component = new ComponentBean();\n");
            for (int j = 0; j < children.getLength(); j++) {
                Node node = children.item(j);
                if ("component-type".equals(node.getNodeName())) {
                    writer.fwrite("component.setComponentType(\""+ node.getTextContent().trim() + "\");\n");
                } else if ("component-class".equals(node.getNodeName())) {
                    writer.fwrite("component.setComponentClass(\""+ node.getTextContent().trim() + "\");\n");
                }
            }
            writer.fwrite ("components.add(component);\n");
            writer.outdent();
            writer.fwrite("}\n");
        }
    }

    private void loadRenderKit() throws Exception {
        writer.write('\n');
        writer.fwrite("renderKitBean = new RenderKitBean();\n");
        NodeList nl = (NodeList)xpath.evaluate("/faces-config/render-kit/renderer", document, XPathConstants.NODESET);
        writer.writeBlockComment("Wrap these in braces to allow repetition of the ivar name");
        for (int i = 0; i < nl.getLength(); i++) {
            NodeList children = nl.item(i).getChildNodes();
            writer.fwrite("{\n");
            writer.indent();
            writer.fwrite("RendererBean rendererBean = new RendererBean();\n");
            for (int j = 0; j < children.getLength(); j++) {
                Node node = children.item(j);
                if ("component-family".equals(node.getNodeName())) {
                    writer.fwrite("rendererBean.setComponentFamily(\""+ node.getTextContent().trim() + "\");\n");
                } else if ("renderer-type".equals(node.getNodeName())) {
                    writer.fwrite("rendererBean.setRendererType(\""+ node.getTextContent().trim() + "\");\n");
                } else if ("renderer-class".equals(node.getNodeName())) {
                    writer.fwrite("rendererBean.setRendererClass(\""+ node.getTextContent().trim() + "\");\n");
                }
            }
            writer.fwrite ("renderKitBean.addRenderer(rendererBean);\n");
            writer.outdent();
            writer.fwrite("}\n");
        }
    }

    private void outputAdderCalls(NodeList nl, String method) throws Exception {
        for (int i = 0; i < nl.getLength(); i++) {
            Node node = nl.item(i);

            writer.fwrite(method +"(\"" + node.getTextContent().trim() + "\");\n");
        }
    }

    private void suffix() throws Exception {
        writer.fwrite("public void populateFacesConfigBean(FacesConfigBean fcb) {\n");
        writer.indent();
        writer.fwrite("fcb.setFactory(factoryBean);\n");
        writer.fwrite("fcb.setApplication(applicationBean);\n");
        writer.fwrite("fcb.setLifecycle(lifecycleBean);\n");
        writer.fwrite("fcb.addRenderKit(renderKitBean);\n");

        writer.fwrite("for (ComponentBean component : components) {\n");
        writer.indent();
        writer.fwrite("fcb.addComponent(component);\n");
        writer.outdent();
        writer.fwrite("}\n");

        writer.fwrite("for (ConverterBean converter : converters) {\n");
        writer.indent();
        writer.fwrite("fcb.addConverter(converter);\n");
        writer.outdent();
        writer.fwrite("}\n");

        writer.fwrite("for (ValidatorBean validator : validators) {\n");
        writer.indent();
        writer.fwrite("fcb.addValidator(validator);\n");
        writer.outdent();
        writer.fwrite("}\n");

        writer.outdent();
        writer.fwrite("}\n"); // end populateFacesConfigBean()
        writer.outdent();
        writer.fwrite("}\n"); // end class
    }
    
    private String getCopyrightText() {
        StringBuilder text = new StringBuilder();
        
        BufferedReader in = null;
        try {
            in = new BufferedReader(new FileReader(copyright));
            String line;
            while ((line = in.readLine()) != null) {
                text.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (in != null) { 
                try { 
                    in.close();
                } catch (IOException e) { 
                    //
                } 
            }
        }
        
        return text.toString();
    }

    private class ParsingErrorHandler implements org.xml.sax.ErrorHandler {

        public void warning(SAXParseException arg0) throws SAXException {
            LOGGER.severe(arg0.getMessage());
        }

        public void error(SAXParseException arg0) throws SAXException {
            fatalError(arg0);
        }

        public void fatalError(SAXParseException arg0) throws SAXException {
            LOGGER.severe(arg0.getMessage());
            System.err.println (arg0.getMessage());
            System.exit(-1);
        }
    }
}
