/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.generate;

import java.io.File;
import java.io.FileWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.sun.faces.config.beans.AttributeBean;
import com.sun.faces.config.beans.ComponentBean;
import com.sun.faces.config.beans.DescriptionBean;
import com.sun.faces.config.beans.FacesConfigBean;
import com.sun.faces.config.beans.PropertyBean;
import com.sun.faces.config.beans.RendererBean;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This class generates tag handler class code that is special to the
 * "html_basic" package.
 */
public class HtmlTaglibGenerator extends AbstractGenerator {

    // -------------------------------------------------------- Static Variables
    
    // Log instance for this class
    private static Log log = LogFactory.getLog(HtmlTaglibGenerator.class);

    // The Writer for each component class to be generated
    private CodeWriter writer;


    // Maps used for generatng Tag Classes
    private ComponentBean component = null;
    private RendererBean renderer = null;

    // Tag Handler Class Name
    private static String tagClassName = null;


    // SPECIAL - Body Tags 
    private List bodyTags;

    // SPECIAL - Components in this List are either a ValueHolder or
    // ConvertibleValueHolder;  This is used to determine if we generate ValueHolder
    // ConvertibleValueHolder code in "setProperties" method;
    private List convertibleValueHolderComponents;

    // SPECIAL - Value Binding Enabled Component Property Names
    private List valueBindingEnabledProperties;


    // SPECIAL - Method Binding Enabled Component Property Names
    private List methodBindingEnabledProperties;

    private PropertyManager propManager;
    private FacesConfigBean configBean;

    private Generator tldGenerator;

    private File outputDir;


    // ------------------------------------------------------------ Constructors

    public HtmlTaglibGenerator(PropertyManager propManager) {

        this.propManager = propManager;
        tldGenerator = new JspTLD12Generator(propManager);

        // initialize structures from the data in propManager

        // body tags
        bodyTags = Arrays.asList(propManager.getProperty(
                PropertyManager.TAGLIB_BODY_TAGS));

        // ValueHolder and ConvertibleValueHolder components
        convertibleValueHolderComponents =
            Arrays.asList(propManager.getProperty(
                PropertyManager.VALUE_HOLDER_COMPONENTS));

        // ValueBinding enabled properties
        valueBindingEnabledProperties = Arrays.asList(propManager.getProperty(
                PropertyManager.VALUE_BINDING_PROPERTIES));


        // MethodBinding enabled properties
        methodBindingEnabledProperties = Arrays.asList(propManager.getProperty(
                PropertyManager.METHOD_BINDING_PROPERTIES));

        String packagePath =
            propManager.getProperty(PropertyManager.TARGET_PACKAGE)[0].
            replace('.', File.separatorChar);
        outputDir = new File(System.getProperty("user.dir") +
            File.separatorChar +
            propManager.getProperty(PropertyManager.BASE_OUTPUT_DIR)[0] +
            File.separatorChar + packagePath);

        if (!outputDir.exists()) {
            outputDir.mkdirs();
        }

        setTldGenerator(GeneratorUtil.getTldGenerator(propManager));

    } // END HtmlTaglibGenerator


    // ---------------------------------------------------------- Public Methods


    public void generate(FacesConfigBean configBean) {

        this.configBean = configBean;
        try {
            generateTagClasses();
            tldGenerator.generate(configBean);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    } // END generate


    public static void main(String[] args) {

        PropertyManager manager = PropertyManager.newInstance(args[0]);
        try {
            Generator generator = new HtmlTaglibGenerator(manager);
            generator.generate(GeneratorUtil.getConfigBean(args[1]));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    } // END main


    // ------------------------------------------------------- Protected Methods


    /**
     * <p>Set the <code>JspTLDGenerator</code> to be used by the taglib
     * generator.</p>
     * @param tldGenerator <code>JspTLDGenerator</code> instance
     */
    protected void setTldGenerator(JspTLDGenerator tldGenerator) {

        this.tldGenerator = tldGenerator;

    } // END setTldGenerator


    // --------------------------------------------------------- Private Methods


    /**
     * Generate copyright, package declaration, import statements, class
     * declaration.
     */
    private void tagHandlerPrefix() throws Exception {

        // Generate the copyright information
        writer.writeBlockComment(
            propManager.getProperty(PropertyManager.COPYRIGHT)[0]);

        writer.write('\n');

        // Generate the package declaration
        writer.writePackage(
            propManager.getProperty(PropertyManager.TARGET_PACKAGE)[0]);

        writer.write('\n');

        // Generate the imports
        writer.writeImport("com.sun.faces.util.Util");
        writer.write('\n');
        writer.writeImport("java.io.IOException");
        writer.write('\n');
        writer.writeImport("javax.faces.component.UIComponent");
        writer.writeImport(
            "javax.faces.component.UI" +
            GeneratorUtil.stripJavaxFacesPrefix(component.getComponentType()));

        writer.writeImport("javax.faces.context.FacesContext");
        writer.writeImport("javax.faces.event.ActionEvent");
        writer.writeImport("javax.faces.event.ValueChangeEvent");
        writer.writeImport("javax.faces.convert.Converter");
        writer.writeImport("javax.faces.el.ValueBinding");
        writer.writeImport("javax.faces.el.MethodBinding");
        writer.writeImport("javax.faces.webapp.UIComponentTag");
        writer.writeImport("javax.faces.webapp.UIComponentBodyTag");
        writer.writeImport("javax.servlet.jsp.JspException");
        writer.write('\n');
        writer.writeImport("org.apache.commons.logging.Log");
        writer.writeImport("org.apache.commons.logging.LogFactory");
        writer.write("\n\n");

        writer.writeBlockComment("******* GENERATED CODE - DO NOT EDIT *******");
        writer.write("\n\n");


        // Generate the class JavaDocs (if any)
        DescriptionBean db = component.getDescription("");
        if (db != null) {
            String description = db.getDescription();
            if (description == null) {
                description = "";
            }
            description = description.trim();
            if (description.length() > 0) {
                writer.writeJavadocComment(description);
            }
        }

        // Generate the class declaration
        writer.writePublicClassDeclaration(tagClassName,
                                           isBodyTag() ?
                                               "UIComponentBodyTag" :
                                               "UIComponentTag",
                                           null, false);

        writer.write('\n');

        writer.indent();
        // Generate Log declaration
        writer.fwrite("private static Log log = LogFactory.getLog(" +
            tagClassName + ".class);\n\n");
    }


    private void tagHandlerReleaseMethod() throws Exception {

        writer.writeLineComment("RELEASE");

        writer.fwrite("public void release() {\n");
        writer.indent();
        writer.fwrite("super.release();\n\n");
        writer.writeLineComment("component properties");

        // Generate from component properties
        //
        PropertyBean[] properties = component.getProperties();
        for (int i = 0, len = properties.length; i < len; i++) {
            PropertyBean property = properties[i];
            if (property == null) {
                continue;
            }

            if (!property.isTagAttribute()) {
                continue;
            }

            String propertyName = property.getPropertyName();
            String propertyType = property.getPropertyClass();

            // SPECIAL - Don't generate these properties
            if ("binding".equals(propertyName) || "id".equals(propertyName)
                || "rendered".equals(propertyName)) {
                continue;
            }

            String ivar = mangle(propertyName);
            writer.fwrite("this." + ivar + " = ");
            if (primitive(propertyType) && !(valueBindingEnabledProperties.contains(
                propertyName)
                || methodBindingEnabledProperties.contains(propertyName))) {
                writer.write((String) TYPE_DEFAULTS.get(propertyType));
            } else {
                writer.write("null");
            }
            writer.write(";\n");
        }

        writer.write("\n");
        writer.writeLineComment("rendered attributes");

        // Generate from renderer attributes..
        //
        AttributeBean[] attributes = renderer.getAttributes();

        for (int i = 0, len = attributes.length; i < len; i++) {
            AttributeBean attribute = attributes[i];

            if (attribute == null) {
                continue;
            }
            if (!attribute.isTagAttribute()) {
                continue;
            }

            String attributeName = attribute.getAttributeName();

            writer.fwrite("this." + mangle(attributeName) + " = null;\n");
        }

        writer.outdent();
        writer.fwrite("}\n\n");
    }

    /**
     * Generate Tag Handler setter methods from component properties and
     * renderer attributes.
     */
    private void tagHandlerSetterMethods() throws Exception {

        writer.writeLineComment("Setter Methods");

        // Generate from component properties
        //
        PropertyBean[] properties = component.getProperties();

        for (int i = 0, len = properties.length; i < len; i++) {
            PropertyBean property = properties[i];

            if (property == null) {
                continue;
            }
            if (!property.isTagAttribute()) {
                continue;
            }

            String propertyName = property.getPropertyName();
            String propertyType = property.getPropertyClass();

            // SPECIAL - Don't generate these properties
            if ("binding".equals(propertyName) ||
                "id".equals(propertyName) ||
                "rendered".equals(propertyName)) {
                continue;
            }

            if (valueBindingEnabledProperties.contains(propertyName) ||
                methodBindingEnabledProperties.contains(propertyName)) {
                writer.writeWriteOnlyProperty(propertyName, "java.lang.String");
            } else {
                writer.writeWriteOnlyProperty(propertyName, propertyType);
            }
        }

        // Generate from renderer attributes..
        //
        AttributeBean[] attributes = renderer.getAttributes();
        for (int i = 0, len = attributes.length; i < len; i++) {
            AttributeBean attribute = attributes[i];

            if (attribute == null) {
                continue;
            }
            if (!attribute.isTagAttribute()) {
                continue;
            }
            String attributeName = attribute.getAttributeName();

            writer.writeWriteOnlyProperty(attributeName,
                "java.lang.String");

        }
        writer.write("\n");
    }


    /**
     * Generate Tag Handler general methods from component properties and
     * renderer attributes.
     */
    private void tagHandlerGeneralMethods() throws Exception {

        writer.writeLineComment("General Methods");

        String componentType = component.getComponentType();
        String rendererType = renderer.getRendererType();
        writer.fwrite("public String getRendererType() {\n");
        writer.indent();
        writer.fwrite("return ");
        writer.write('\"' + rendererType + "\";\n");
        writer.outdent();
        writer.fwrite("}\n\n");

        writer.fwrite("public String getComponentType() {\n");
        writer.indent();
        writer.fwrite("return ");
        if (componentType.equals(rendererType)) {
            writer.write(
                "\"javax.faces.Html" +
                GeneratorUtil.stripJavaxFacesPrefix(componentType) +
                "\";\n");
        } else {
            writer.write(
                "\"javax.faces.Html" +
                GeneratorUtil.stripJavaxFacesPrefix(componentType) +
                GeneratorUtil.stripJavaxFacesPrefix(rendererType) +
                "\";\n");
        }
        writer.outdent();
        writer.fwrite("}\n\n");

        writer.fwrite("protected void setProperties(UIComponent component) {\n");
        writer.indent();
        writer.fwrite("super.setProperties(component);\n");

        String uicomponent = "UI" +
            GeneratorUtil.stripJavaxFacesPrefix(componentType);
        String iVar =
            GeneratorUtil.stripJavaxFacesPrefix(componentType).toLowerCase();

        writer.fwrite(uicomponent + ' ' + iVar + " = null;\n");

        writer.fwrite("try {\n");
        writer.indent();
        writer.fwrite(iVar + " = (" + uicomponent + ") component;\n");
        writer.outdent();
        writer.fwrite("} catch (ClassCastException cce) {\n");
        writer.indent();
        writer.fwrite("throw new IllegalStateException(\"Component \" + " +
            "component.toString() + \" not expected type.  Expected: " +
            uicomponent +
            ".  Perhaps you're missing a tag?\");\n");
        writer.outdent();
        writer.fwrite("}\n\n");

        if (convertibleValueHolderComponents.contains(uicomponent)) {
            writer.fwrite("if (converter != null) {\n");
            writer.indent();
            writer.fwrite("if (isValueReference(converter)) {\n");
            writer.indent();
            writer.fwrite("ValueBinding vb = Util.getValueBinding(converter);\n");
            writer.fwrite(iVar + ".setValueBinding(\"converter\", vb);\n");
            writer.outdent();
            writer.fwrite("} else {\n");
            writer.indent();
            writer.fwrite("Converter _converter = FacesContext.getCurrent" +
                "Instance().getApplication().createConverter(converter);\n");
            writer.fwrite(iVar + ".setConverter(_converter);\n");
            writer.outdent();
            writer.fwrite("}\n");
            writer.outdent();
            writer.fwrite("}\n\n");
        }

        // Generate "setProperties" method contents from component properties
        //
        PropertyBean[] properties = component.getProperties();
        for (int i = 0, len = properties.length; i < len; i++) {
            PropertyBean property = properties[i];

            if (property == null) {
                continue;
            }
            if (!property.isTagAttribute()) {
                continue;
            }

            String propertyName = property.getPropertyName();
            String propertyType = property.getPropertyClass();

            // SPECIAL - Don't generate these properties
            if ("binding".equals(propertyName) ||
                "id".equals(propertyName) ||
                "rendered".equals(propertyName) ||
                "converter".equals(propertyName)) {
                continue;
            }
            String ivar = mangle(propertyName);
            String vbKey = ivar;
            String comp =
                GeneratorUtil.stripJavaxFacesPrefix(componentType).toLowerCase();
            String capPropName = capitalize(propertyName);

            if (valueBindingEnabledProperties.contains(propertyName)) {
                writer.fwrite("if (" + ivar + " != null) {\n");
                writer.indent();
                writer.fwrite("if (isValueReference(" + ivar + ")) {\n");
                writer.indent();
                writer.fwrite("ValueBinding vb = Util.getValueBinding(" +
                    ivar + ");\n");

                writer.fwrite(comp + ".setValueBinding(\"" + vbKey + "\", vb);\n");
                writer.outdent();
                writer.fwrite("} else {\n");
                writer.indent();
                if (primitive(propertyType)) {
                    writer.fwrite(propertyType + " _" + ivar);
                    writer.write(" = new " + WRAPPERS.get(propertyType));
                    writer.write("(" + ivar + ")." + propertyType + "Value();\n");
                    writer.fwrite(comp + ".set" + capPropName +
                        "(_" + ivar + ");\n");
                } else {
                    writer.fwrite(comp + ".set" + capPropName + '(' + ivar +
                        ");\n");
                }
                writer.outdent();
                writer.fwrite("}\n");
                writer.outdent();
                writer.fwrite("}\n\n");
            } else if (methodBindingEnabledProperties.contains(propertyName)) {
                if ("action".equals(ivar)) {
                    writer.fwrite("if (" + ivar + " != null) {\n");
                    writer.indent();
                    writer.fwrite("if (isValueReference(" + ivar + ")) {\n");
                    writer.indent();
                    writer.fwrite("MethodBinding vb = FacesContext.getCurrentInstance().");
                    writer.write("getApplication().createMethodBinding(" +
                        ivar + ", null);\n");
                    writer.fwrite(comp + ".setAction(vb);\n");
                    writer.outdent();
                    writer.fwrite("} else {\n");
                    writer.indent();
                    writer.fwrite("final String outcome = " + ivar + ";\n");
                    writer.fwrite("MethodBinding vb = Util.createConstantMethodBinding(" +
                        ivar + ");\n");
                    writer.fwrite(comp + ".setAction(vb);\n");
                    writer.outdent();
                    writer.fwrite("}\n");
                    writer.outdent();
                    writer.fwrite("}\n");
                } else {
                    HashMap signatureMap = new HashMap(3);
                    signatureMap.put("actionListener",
                        "Class args[] = { ActionEvent.class };");
                    signatureMap.put("validator",
                        "Class args[] = { FacesContext.class, UIComponent.class, Object.class };");
                    signatureMap.put("valueChangeListener",
                        "Class args[] = { ValueChangeEvent.class };");

                    writer.fwrite("if (" + ivar + " != null) {\n");
                    writer.indent();
                    writer.fwrite("if (isValueReference(" + ivar + ")) {\n");
                    writer.indent();
                    writer.fwrite(signatureMap.get(ivar) + "\n");
                    writer.fwrite("MethodBinding vb = FacesContext.getCurrentInstance().");
                    writer.write("getApplication().createMethodBinding(" +
                        ivar + ", args);\n");
                    writer.fwrite(comp + ".set" + capitalize(ivar) + "(vb);\n");
                    writer.outdent();
                    writer.fwrite("} else {\n");
                    writer.indent();
                    writer.fwrite("Object params [] = {" + ivar + "};\n");
                    writer.fwrite("throw new javax.faces.FacesException(Util." +
                        "getExceptionMessageString(Util.INVALID_EXPRESSION_ID, " +
                        "params));\n");
                    writer.outdent();
                    writer.fwrite("}\n");
                    writer.outdent();
                    writer.fwrite("}\n");
                }
            } else {
                writer.fwrite(comp + ".set" + capPropName + "(" + ivar + ");\n");
            }
        }

        // Generate "setProperties" method contents from renderer attributes
        //
        AttributeBean[] attributes = renderer.getAttributes();
        for (int i = 0, len = attributes.length; i < len; i++) {
            AttributeBean attribute = attributes[i];
            if (attribute == null) {
                continue;
            }
            if (!attribute.isTagAttribute()) {
                continue;
            }
            String attributeName = attribute.getAttributeName();
            String attributeType = attribute.getAttributeClass();

            String ivar = mangle(attributeName);
            String vbKey = ivar;
            String comp =
                GeneratorUtil.stripJavaxFacesPrefix(componentType).toLowerCase();

            writer.fwrite("if (" + ivar + " != null) {\n");
            writer.indent();
            writer.fwrite("if (isValueReference(" + ivar + ")) {\n");
            writer.indent();
            writer.fwrite("ValueBinding vb = Util.getValueBinding(" + ivar +
                ");\n");
            writer.fwrite(comp);
            if ("_for".equals(ivar)) {
                writer.write(".setValueBinding(\"" + '_' + vbKey + "\", vb);\n");
            } else {
                writer.write(".setValueBinding(\"" + vbKey + "\", vb);\n");
            }
            writer.outdent();
            writer.fwrite("} else {\n");
            writer.indent();
            if (primitive(attributeType)) {
                writer.fwrite(attributeType + " _" + ivar + ' ');
                writer.write("= new " + WRAPPERS.get(attributeType));
                writer.write("(" + ivar + ")." + attributeType + "Value();\n");
                if ("boolean".equals(attributeType)) {
                    writer.fwrite(comp +
                        ".getAttributes().put(\"" + ivar + "\", ");
                    writer.write(
                        "_" + ivar + " ? Boolean.TRUE : Boolean.FALSE);\n");
                } else {
                    writer.fwrite("if (_" + ivar + " != ");
                    writer.write(TYPE_DEFAULTS.get(attributeType) + ") {\n");
                    writer.indent();
                    writer.fwrite(comp + ".getAttributes().put(\"" + ivar +
                        "\", new ");
                    writer.write(WRAPPERS.get(attributeType) + "(_" + ivar +
                        "));\n");
                    writer.outdent();
                    writer.write("}\n");
                }
            } else {
                if ("bundle".equals(ivar)) {
                    writer.fwrite(comp +
                        ".getAttributes().put(com.sun.faces.RIConstants.BUNDLE_ATTR, ");
                } else if ("_for".equals(ivar)) {
                    writer.fwrite(comp +
                        ".getAttributes().put(\"for\", ");
                } else {
                    writer.fwrite(comp +
                        ".getAttributes().put(\"" + ivar + "\", ");
                }
                writer.write(ivar + ");\n");
            }
            writer.outdent();
            writer.fwrite("}\n");
            writer.outdent();
            writer.fwrite("}\n");
        }
        writer.outdent();
        writer.fwrite("}\n\n");
    }


    /**
     * Generate Tag Handler support methods
     */
    private void tagHandlerSupportMethods() throws Exception {

        writer.writeLineComment("Methods From TagSupport");

        writer.fwrite("public int doStartTag() throws JspException {\n");
        writer.indent();
        writer.fwrite("int rc = 0;\n");
        writer.fwrite("try {\n");
        writer.indent();
        writer.fwrite("rc = super.doStartTag();\n");
        writer.outdent();
        writer.fwrite("} catch (JspException e) {\n");
        writer.indent();
        writer.fwrite("if (log.isDebugEnabled()) {\n");
        writer.indent();
        writer.fwrite("log.debug(getDebugString(), e);\n");
        writer.outdent();
        writer.fwrite("}\n");
        writer.fwrite("throw e;\n");
        writer.outdent();
        writer.fwrite("} catch (Throwable t) {\n");
        writer.indent();
        writer.fwrite("if (log.isDebugEnabled()) {\n");
        writer.indent();
        writer.fwrite("log.debug(getDebugString(), t);\n");
        writer.outdent();
        writer.fwrite("}\n");
        writer.fwrite("throw new JspException(t);\n");
        writer.outdent();
        writer.fwrite("}\n");
        writer.fwrite("return rc;\n");
        writer.outdent();
        writer.fwrite("}\n\n");

        writer.fwrite("public int doEndTag() throws JspException {\n");
        writer.indent();
        writer.fwrite("int rc = 0;\n");
        writer.fwrite("try {\n");
        writer.indent();
        writer.fwrite("rc = super.doEndTag();\n");
        writer.outdent();
        writer.fwrite("} catch (JspException e) {\n");
        writer.indent();
        writer.fwrite("if (log.isDebugEnabled()) {\n");
        writer.indent();
        writer.fwrite("log.debug(getDebugString(), e);\n");
        writer.outdent();
        writer.fwrite("}\n");
        writer.fwrite("throw e;\n");
        writer.outdent();
        writer.fwrite("} catch (Throwable t) {\n");
        writer.indent();
        writer.fwrite("if (log.isDebugEnabled()) {\n");
        writer.indent();
        writer.fwrite("log.debug(getDebugString(), t);\n");
        writer.outdent();
        writer.fwrite("}\n");
        writer.fwrite("throw new JspException(t);\n");
        writer.outdent();
        writer.fwrite("}\n");
        writer.fwrite("return rc;\n");
        writer.outdent();
        writer.fwrite("}\n\n");

    }


    /**
     * Generate Body Tag Handler support methods
     */
    private void tagHandlerBodySupportMethods() throws Exception {

        writer.writeLineComment("Methods from TagSupport");

        writer.fwrite("public int doStartTag() throws JspException {\n");
        writer.indent();
        writer.fwrite("int rc = 0;\n");
        writer.fwrite("try {\n");
        writer.indent();
        writer.fwrite("rc = super.doStartTag();\n");
        writer.outdent();
        writer.fwrite("} catch (JspException e) {\n");
        writer.indent();
        writer.fwrite("if (log.isDebugEnabled()) {\n");
        writer.indent();
        writer.fwrite("log.debug(getDebugString(), e);\n");
        writer.outdent();
        writer.fwrite("}\n");
        writer.fwrite("throw e;\n");
        writer.outdent();
        writer.fwrite("} catch (Throwable t) {\n");
        writer.indent();
        writer.fwrite("if (log.isDebugEnabled()) {\n");
        writer.indent();
        writer.fwrite("log.debug(getDebugString(), t);\n");
        writer.outdent();
        writer.fwrite("}\n");
        writer.fwrite("throw new JspException(t);\n");
        writer.outdent();
        writer.fwrite("}\n");
        writer.fwrite("return rc;\n");
        writer.outdent();
        writer.fwrite("}\n\n");
        writer.fwrite("public int doEndTag() throws JspException {\n");
        writer.indent();
        writer.fwrite("String content = null;\n");
        writer.fwrite("try {\n");
        writer.indent();
        writer.fwrite("if (null != (bodyContent = getBodyContent())) {\n");
        writer.indent();
        writer.fwrite("content = bodyContent.getString();\n");
        writer.fwrite("getPreviousOut().write(content);\n");
        writer.outdent();
        writer.fwrite("}\n");
        writer.outdent();
        writer.fwrite("} catch (IOException iox) {\n");
        writer.indent();
        writer.fwrite("throw new JspException(iox);\n");
        writer.outdent();
        writer.fwrite("}\n");
        writer.fwrite("int rc = super.doEndTag();\n");
        writer.fwrite("return rc;\n");
        writer.outdent();
        writer.fwrite("}\n\n");
    }


    /**
     * Generate remaining Tag Handler methods
     */
    private void tagHandlerSuffix() throws Exception {

        // generate general purpose method used in logging.
        //
        writer.fwrite("public String getDebugString() {\n");
        writer.indent();
        String res = "\"id: \" + this.getId() + \" class: \" + this.getClass().getName()";
        writer.fwrite("String result = " + res + ";\n");
        writer.fwrite("return result;\n");
        writer.outdent();
        writer.fwrite("}\n\n");

        writer.outdent();
        writer.fwrite("}\n");

    }

    //
    // Helper methods
    //
    //

    /**
     * Is the tag handler we're building a body tag variety?
     */
    private boolean isBodyTag() {
        if (bodyTags.contains(tagClassName)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     *
     * @return a SortedMap, where the keys are component-family String
     * entries, and the values are {@link RendererBean} instances
     */


    /**
     * Generate the tag handler class files.
     */
    private void generateTagClasses() throws Exception {

        Map renderersByComponentFamily =
            GeneratorUtil.getComponentFamilyRendererMap(configBean,
                propManager.getProperty(PropertyManager.RENDERKIT_ID)[0]);
        Map componentsByComponentFamily =
            GeneratorUtil.getComponentFamilyComponentMap(configBean);

        for (Iterator keyIter = renderersByComponentFamily.keySet().iterator();
             keyIter.hasNext(); ) {

            String componentFamily = (String) keyIter.next();
            List renderers = (List) renderersByComponentFamily.get(componentFamily);

            component = (ComponentBean)
                componentsByComponentFamily.get(componentFamily);

            for (Iterator rendererIter = renderers.iterator();
                 rendererIter.hasNext(); ) {

                renderer = (RendererBean) rendererIter.next();
                String rendererType = renderer.getRendererType();

                tagClassName = GeneratorUtil.makeTagClassName(
                         GeneratorUtil.stripJavaxFacesPrefix(componentFamily),
                         GeneratorUtil.stripJavaxFacesPrefix(rendererType));

                if (tagClassName == null) {
                    throw new IllegalStateException(
                        "Could not determine tag class name");
                }

                if (log.isInfoEnabled()) {
                    log.info("Generating " + tagClassName + "...");
                }


                File file = new File(outputDir, tagClassName + ".java");
                writer = new CodeWriter(new FileWriter(file));

                tagHandlerPrefix();
                tagHandlerSetterMethods();
                tagHandlerGeneralMethods();
                if (isBodyTag()) {
                    tagHandlerBodySupportMethods();
                } else {
                    tagHandlerSupportMethods();
                }
                tagHandlerReleaseMethod();
                tagHandlerSuffix();

                // Flush and close the Writer
                writer.flush();
                writer.close();
            }
        }
    }

    // ----------------------------------------------------------- Inner Classes


}
