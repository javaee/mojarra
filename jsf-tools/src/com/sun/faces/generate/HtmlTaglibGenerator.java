/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.generate;

import com.sun.faces.config.beans.AttributeBean;
import com.sun.faces.config.beans.ComponentBean;
import com.sun.faces.config.beans.DescriptionBean;
import com.sun.faces.config.beans.FacesConfigBean;
import com.sun.faces.config.beans.PropertyBean;
import com.sun.faces.config.beans.RenderKitBean;
import com.sun.faces.config.beans.RendererBean;
import org.apache.commons.digester.Digester;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * This class generates tag handler class code that is special to the "html_basic"
 * package.
 */
public class HtmlTaglibGenerator extends AbstractGenerator {

    // -------------------------------------------------------- Static Variables
    
    // Log instance for this class
    protected static Log log = LogFactory.getLog(HtmlTaglibGenerator.class);

    // StringBuffer containing the copyright material
    private static String copyright;

    // The directory into which the output will be generated
    private static File directory;

    // Base object of the configuration beans
    private static FacesConfigBean fcb;

    // The Writer for each component class to be generated
    private static Writer writer;

    private static final String DEFAULT_RENDERKIT_ID = "DEFAULT";

    private static String renderKitId = DEFAULT_RENDERKIT_ID;

    // Defaults
    private static final String TAGCLASSPATH = "com.sun.faces.taglib.html_basic";
    private static final String TEICLASS = "com.sun.faces.taglib.FacesTagExtraInfo";
    private static final String BODYCONTENT = "JSP";
    private static final String REQUIRED = "false";
    private static final String RTEXPRVALUE = "false";

    // Maps used for generatng TLD and Tag Classes
    private static SortedMap componentsByComponentFamily;
    private static SortedMap renderersByComponentFamily;
    private static ComponentBean component = null;
    private static RendererBean renderer = null;

    // Tag Handler Class Name
    private static String tagClassName = null;

    // String containing optional tag definitions loaded from external file.
    // Currently "column" is the only tag existing in this file, because it
    // is a tag wth no renderer.
    private static String tagDef;

    // SPECIAL - Body Tags 
    private static List bodyTags = new ArrayList();
    static {
        bodyTags.add("CommandLinkTag");
	bodyTags.add("OutputLinkTag");
    }

    // SPECIAL - Components in this List are either a ValueHolder or
    // ConvertibleValueHolder;  This is used to determine if we generate ValueHolder
    // ConvertibleValueHolder code in "setProperties" method;
    private static List convertibleValueHolderComponents = new ArrayList();
    static {
        convertibleValueHolderComponents.add("UIOutput");
        convertibleValueHolderComponents.add("UIInput");
        convertibleValueHolderComponents.add("UISelectMany");
        convertibleValueHolderComponents.add("UISelectOne");
    }

    // SPECIAL - Value Binding Enabled Component Property Names
    private static List valueBindingEnabledProperties = new ArrayList();
    static {
        valueBindingEnabledProperties.add("immediate");
        valueBindingEnabledProperties.add("value");
        valueBindingEnabledProperties.add("first");
        valueBindingEnabledProperties.add("rows");
        valueBindingEnabledProperties.add("rowIndex");
        valueBindingEnabledProperties.add("required");
        valueBindingEnabledProperties.add("for");
        valueBindingEnabledProperties.add("showDetail");
        valueBindingEnabledProperties.add("showSummary");
        valueBindingEnabledProperties.add("globalOnly");
        valueBindingEnabledProperties.add("converter");
        valueBindingEnabledProperties.add("url");
    }
    
    // SPECIAL - Method Binding Enabled Component Property Names
    private static List methodBindingEnabledProperties = new ArrayList();
    static {
        methodBindingEnabledProperties = new ArrayList();
        methodBindingEnabledProperties.add("action");
        methodBindingEnabledProperties.add("actionListener");
        methodBindingEnabledProperties.add("validator");
        methodBindingEnabledProperties.add("valueChangeListener");
    }

    /**
     * The XML header for the TLD file.
     */
    private static void xmlHeader() throws Exception {
        writer.write("<?xml version="+'"'+"1.0"+'"'+" encoding="+'"'+"ISO-8859-1"+'"'+" ?>\n\n");
    }

    /**
     * <p>Load the copyright text for the top of each Java source file and TLD file.</p>
     *
     * @param path Pathname of the copyright file
     */
    private static void copyright(String path) throws Exception {

        if (log.isDebugEnabled()) {
            log.debug("Loading copyright text from '" + path + "'");
        }
        StringBuffer sb = new StringBuffer();
        BufferedReader reader =
            new BufferedReader(new FileReader(path));
        int ch;
        while ((ch = reader.read()) >= 0) {
            sb.append((char) ch);
        }
        reader.close();
        if (log.isDebugEnabled()) {
            log.debug("  Copyright text contains " + sb.length() +
                      " characters");
        }
        copyright = sb.toString();
    }

    /**
     * Copyright
     */
    private static void copyright() throws Exception {
        writer.write(copyright+"\n\n");
    }

    /**
     * TLD DOCTYPE
     */
    private static void tldDocType() throws Exception {
        writer.write("<!DOCTYPE taglib\n");
	writer.write("PUBLIC "+'"'+"-//Sun Microsystems, Inc.//DTD JSP Tag Library 1.2//EN"+'"'+'\n');
	writer.write('"'+"http://java.sun.com/dtd/web-jsptaglibrary_1_2.dtd"+'"'+">\n\n");
    }

    /**
     * The description element for this TLD.
     */
    private static void tldDescription() throws Exception {
	writer.write("<taglib>\n\n");
	writer.write("<!-- ============== Tag Library Description Elements ============= -->\n\n");
        writer.write("<tlib-version>1.0</tlib-version>\n<jsp-version>1.2</jsp-version>\n");
	writer.write("<short-name>h</short-name>\n");
	writer.write("<uri>http://java.sun.com/jsf/html</uri>\n");
	writer.write("<description>\n");
	writer.write("  This tag library contains JavaServer Faces component tags for all\n");
	writer.write("  UIComponent + HTML RenderKit Renderer combinations defined in the\n");
	writer.write("  JavaServer Faces Specification.\n");
	writer.write("</description>\n\n");
    }

    /**
     * The validator for this TLD.
     */
    private static void tldValidator() throws Exception {
        writer.write("<!-- ============== Tag Library Validator ============= -->\n\n");
	writer.write("<validator>\n  <validator-class>\n");
	writer.write("    com.sun.faces.taglib.html_basic.HtmlBasicValidator\n");
	writer.write("  </validator-class>\n</validator>\n\n");
    }

    /**
     * The tags for this TLD.
     */
    private static void tldTags() throws Exception {
	writer.write("<!-- ===================== HTML 4.0 basic tags ====================== -->\n\n");
	componentsByComponentFamily = getComponentFamilyComponentMap();
	renderersByComponentFamily = getComponentFamilyRendererMap(renderKitId);

	Iterator 
	    rendererIter = null,
	    keyIter = renderersByComponentFamily.keySet().iterator();
	String componentFamily = null;
	String rendererType = null;
	List renderers = null;
	DescriptionBean description = null;
	String descriptionText = null;
	String tagName = null;
	while (keyIter.hasNext()) {
	    componentFamily = (String) keyIter.next();
	    renderers = (List)renderersByComponentFamily.get(componentFamily);
	    rendererIter = renderers.iterator();
	    while (rendererIter.hasNext()) {
                renderer = (RendererBean) rendererIter.next();
		rendererType = renderer.getRendererType();
		writer.write("  <tag>\n");
		tagName = makeTldTagName(strip(componentFamily),
					 strip(rendererType));
		if (tagName == null) {
		    throw new IllegalStateException("Could not determine tag name");
		}
		writer.write("    <name>"+tagName+"</name>\n");
		if (makeTagClassName(strip(componentFamily), strip(rendererType)) == null) {
		    throw new IllegalStateException("Could not determine tag class name");
		}
		writer.write("    <tag-class>"+getTagClassPath(tagName)+"."+
			     makeTagClassName(strip(componentFamily), strip(rendererType))+"</tag-class>\n");
		writer.write("    <tei-class>"+getTeiClass(tagName)+"</tei-class>\n");
		writer.write("    <body-content>"+getBodyContent(tagName)+"</body-content>\n");
		description = renderer.getDescription("");
		if (description != null) {
		    descriptionText = description.getDescription();
		    writer.write("    <description>\n");
		    if (descriptionText != null) {
		        if (descriptionText.indexOf("<") < 0) {
			    writer.write("      "+descriptionText+"\n");
			} else {
		            writer.write("      <![CDATA["+descriptionText+"]]>\n");
			}
		    }
		    writer.write("    </description>\n");
		}

		// Generate tag attributes
		//

		// Component Properties first...
		//
		component = (ComponentBean)componentsByComponentFamily.get(componentFamily);
		
		PropertyBean[] properties = component.getProperties();
		PropertyBean property = null;
		for (int i = 0, len = properties.length; i < len; i++) {
		    if (null == (property = properties[i])) {
		        continue;
		    }
		    if (!property.isTagAttribute()) {
		        continue;
		    }
		    writer.write("    <attribute>\n");
		    writer.write("      <name>"+property.getPropertyName()+"</name>\n");
		    if (property.isRequired()) {
		        writer.write("      <required>true</required>\n");
		    } else {
		        writer.write("      <required>false</required>\n");
		    }
		    writer.write("      <rtexprvalue>"+
                        getRtexprvalue(tagName, property.getPropertyName())+
			"</rtexprvalue>\n");
		    description = property.getDescription("");
		    if (description != null) {
		        descriptionText = description.getDescription();
		        writer.write("      <description>\n");
		        if (descriptionText != null) {
		            if (descriptionText.indexOf("<") < 0) {
			        writer.write("      "+descriptionText+"\n");
			    } else {
		                writer.write("      <!CDATA["+descriptionText+"]]>\n");
			    }
			}
		        writer.write("      </description>\n");
	            }
		    writer.write("    </attribute>\n");
		}
		
		// Renderer Attributes Next...
		//
		AttributeBean[] attributes = renderer.getAttributes();
		AttributeBean attribute = null;
		for (int i = 0, len = attributes.length; i < len; i++) {
		    if (null == (attribute = attributes[i])) {
		        continue;
		    }
		    if (!attribute.isTagAttribute()) {
		        continue;
		    }
		    if (skipThisAttribute(renderer, 
					  attribute.getAttributeName())) {
			continue;
		    }
		    writer.write("    <attribute>\n");
		    writer.write("      <name>"+attribute.getAttributeName()+"</name>\n");
		    if (attribute.isRequired()) {
		        writer.write("      <required>true</required>\n");
		    } else {
		        writer.write("      <required>false</required>\n");
		    }
		    writer.write("      <rtexprvalue>"+
                        getRtexprvalue(tagName, attribute.getAttributeName())+
			"</rtexprvalue>\n");
		    description = attribute.getDescription("");
		    if (description != null) {
		        descriptionText = description.getDescription();
		        writer.write("      <description>\n");
		        if (descriptionText != null) {
		            if (descriptionText.indexOf("<") < 0) {
			        writer.write("      "+descriptionText+"\n");
			    } else {
		                writer.write("      <!CDATA["+descriptionText+"]]>\n");
			    }
			}
		        writer.write("      </description>\n");
		    }
		    writer.write("    </attribute>\n");
		}

		// SPECIAL: "Binding" needs to exist on every tag..
		writer.write("    <attribute>\n");
		writer.write("      <name>binding</name>\n");
		writer.write("      <required>false</required>\n");
		writer.write("      <rtexprvalue>false</rtexprvalue>\n");
		writer.write("      <description>\n");
		writer.write("         The value binding expression linking this component to a property in a backing bean\n");
		writer.write("      </description>\n");
		writer.write("    </attribute>\n");

	        writer.write("  </tag>\n");
	    }
	}

	//Include any other tags defined in the optional tag definition file.
	//These might be tags that were not picked up because they have no renderer
	//- for example "column".
	if (tagDef != null) {
	    writer.write(tagDef);
	}

	writer.write("</taglib>");
    }

    /**
     * <p>Load any additional tag definitions from the specified 
     * file.  This file might include tags such as "column" which
     * have no renderer, but need to be generated into the 
     * TLD file.</p>
     *
     * @param path Pathname of the tag definition file
     */
    private static void loadOptionalTags(String path) throws Exception {

        if (log.isDebugEnabled()) {
            log.debug("Loading optional tag text from '" + path + "'");
        }
        StringBuffer sb = new StringBuffer();
        BufferedReader reader =
            new BufferedReader(new FileReader(path));
        int ch;
        while ((ch = reader.read()) >= 0) {
            sb.append((char) ch);
        }
        reader.close();
        if (log.isDebugEnabled()) {
            log.debug("  Optional tag text contains " + sb.length() +
                      " characters");
        }
        tagDef = sb.toString();
    }

    /**
     * Build the tag name from componentFamily and rendererType.  The name will
     * be "camel case".
     *
     * @param componentFamily the component family
     * @param rendererType the renderer type
     */
    private static String makeTldTagName(String componentFamily, String rendererType) {
	if (componentFamily == null) {
	    return null;
	}
        String tagName =
            Character.toLowerCase(componentFamily.charAt(0)) +
            componentFamily.substring(1);
	if (rendererType == null) {
	    return tagName;
	}
	if (componentFamily.equals(rendererType)) {
	    return tagName;
	}
	if (rendererType != null) {
	    tagName = tagName + rendererType;
	}
	return tagName;
    }

    /**
     * Build the tag handler class name from componentFamily and rendererType.
     *
     * @param componentFamily the component family
     * @param rendererType the renderer type
     */
    private static String makeTagClassName(String componentFamily, String rendererType) {
        if (componentFamily == null) {
	    return null;
	}
	String tagClassName = componentFamily;
	if (rendererType != null) {
	    if (!componentFamily.equals(rendererType)) {
	        tagClassName = tagClassName + rendererType;
	    }
	}
	return tagClassName+"Tag";
    }

    /**
     * <p>Create directories as needed to contain the output. 
     *
     * @param path Pathname to the base directory
     */
    private static void directories(String path, boolean classesDir) throws Exception {

        directory = new File(path);
	if (classesDir) {
	    directory = new File(path, "com/sun/faces/taglib/html_basic");
	}
        if (log.isDebugEnabled()) {
            log.debug("Creating output directory " +
                      directory.getAbsolutePath());
        }
        directory.mkdirs();
    }

    // Note that the following methods simply return a global default value now.  When we
    // set up the class path, tag extra info, required, rtexprValue, bodycontext to be
    // configurable, then we can retrieve that info using the args.
    //

    /**
     * Return the class path for the given tag.
     */
    private static String getTagClassPath(String tagName) {
        return TAGCLASSPATH;
    }

    /**
     * Return the tag extra info information (if any) for a given tag.
     */
    private static String getTeiClass(String tagName) {
        return TEICLASS;
    }

    /**
     * Return the tag body content information (if any) for a given tag.
     */
    private static String getBodyContent(String tagName) {
        return BODYCONTENT;
    }

    /**
     * Return the "rtexprvalue" element value for the tag attribute.
     */
    private static String getRtexprvalue(String tagName, String attributeName) {
        return RTEXPRVALUE;
    }

    //
    // Following methods uses in tag class generation;
    //

    /**
     * Generate copyright, package declaration, import statements,
     * class declaration.
     */
    private static void tagHandlerPrefix() throws Exception {

        // Generate the copyright information
        writer.write(copyright);

        // Generate the package declaration
        writer.write("\npackage com.sun.faces.taglib.html_basic;\n");
        writer.write("\n\n");

        // Generate the imports
        writer.write("import com.sun.faces.util.Util;\n");
        writer.write("import java.io.IOException;\n");
        writer.write("import javax.faces.component.UIComponent;\n");
	writer.write("import javax.faces.component.UI" +
		     strip(component.getComponentType())+";\n");
        writer.write("import javax.faces.context.FacesContext;\n");
        writer.write("import javax.faces.event.ActionEvent;\n");
        writer.write("import javax.faces.event.ValueChangeEvent;\n");
        writer.write("import javax.faces.convert.Converter;\n");
        writer.write("import javax.faces.el.ValueBinding;\n");
        writer.write("import javax.faces.el.MethodBinding;\n");
        writer.write("import javax.faces.webapp.UIComponentTag;\n");
        writer.write("import javax.faces.webapp.UIComponentBodyTag;\n");
        writer.write("import javax.servlet.jsp.JspException;\n");
        writer.write("import org.apache.commons.logging.Log;\n");
        writer.write("import org.apache.commons.logging.LogFactory;\n");
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
                writer.write("/**\n");
                description(description, writer, 1);
                writer.write(" */\n");
            }
        }
	
        // Generate the class declaration
        writer.write("public class ");
        writer.write(tagClassName);
        writer.write(" extends ");
	if (isBodyTag()) {
	    writer.write("UIComponentBodyTag");
	} else {
	    writer.write("UIComponentTag");
	}
        writer.write(" {\n\n\n");

	// Generate Log declaration
	writer.write("    public static Log log = LogFactory.getLog("+tagClassName+".class);\n\n");
    }

    /**
     * Generate Tag Handler instance variables from component properties
     * and renderer attributes.
     */
    private static void tagHandlerIvars() throws Exception {

	writer.write("    //\n    // Instance Variables\n    //\n\n");

	// Generate from component properties
	//
        PropertyBean[] properties = component.getProperties();
	PropertyBean property = null;
	String propertyName = null;
	String propertyType = null;
	String ivar = null;
        for (int i = 0, len = properties.length; i < len; i++) {
            if (null == (property = properties[i])) {
                continue;
            }
    	    if (!property.isTagAttribute()) {
                continue;
            }
	    propertyName = property.getPropertyName();
	    propertyType = property.getPropertyClass();

	    // SPECIAL - Don't generate these properties 
	    if (propertyName.equals("binding") ||
		propertyName.equals("id") ||
		propertyName.equals("rendered")) {
		continue;
	    }

	    ivar = mangle(propertyName);
	    if (valueBindingEnabledProperties.contains(propertyName) ||
		methodBindingEnabledProperties.contains(propertyName)) {
                writer.write("    private java.lang.String " + ivar + ";\n");
	    } else {
	        writer.write("    private " + propertyType + " " + ivar + ";\n");
		if (primitive(propertyType)) {
		    writer.write(" = "+(String)defaults.get(propertyType)+";\n");
		}
	    }
	}

	writer.write("\n");

	// Generate from renderer attributes..
	//
        AttributeBean[] attributes = renderer.getAttributes();
	AttributeBean attribute = null;
	String attributeName = null;
	String attributeType = null;
        for (int i = 0, len = attributes.length; i < len; i++) {
            if (null == (attribute = attributes[i])) {
                continue;
            }
    	    if (!attribute.isTagAttribute()) {
                continue;
            }
	    attributeName = attribute.getAttributeName();
	    attributeType = attribute.getAttributeClass();

	    ivar = mangle(attributeName);
            writer.write("    private java.lang.String " + ivar + ";\n");
	}
	writer.write("\n");
    }

    /**
     * Generate Tag Handler setter methods from component properties
     * and renderer attributes.
     */
    private static void tagHandlerSetterMethods() throws Exception {

	writer.write("    //\n    // Setter Methods\n    //\n\n");

	// Generate from component properties
	//
        PropertyBean[] properties = component.getProperties();
	PropertyBean property = null;
	String propertyName = null;
	String propertyType = null;
	String setVar = null;
	String ivar = null;
        for (int i = 0, len = properties.length; i < len; i++) {
            if (null == (property = properties[i])) {
                continue;
            }
    	    if (!property.isTagAttribute()) {
                continue;
            }
	    propertyName = property.getPropertyName();
	    propertyType = property.getPropertyClass();

	    // SPECIAL - Don't generate these properties 
	    if (propertyName.equals("binding") ||
		propertyName.equals("id") ||
		propertyName.equals("rendered")) {
		continue;
	    }
	    ivar = mangle(propertyName);
	    setVar = capitalize(propertyName);
	    writer.write("    public void set" + setVar + "(");
	    if (valueBindingEnabledProperties.contains(propertyName) ||
		methodBindingEnabledProperties.contains(propertyName)) {
		writer.write("java.lang.String " + ivar + ") {\n");
	    } else {
		writer.write(propertyType + " " + ivar + ") {\n");
	    }
            writer.write("        this." + ivar + " = " + ivar + ";\n");
            writer.write("    }\n\n");
	}

	// Generate from renderer attributes..
	//
        AttributeBean[] attributes = renderer.getAttributes();
	AttributeBean attribute = null;
	String attributeName = null;
	String attributeType = null;
        for (int i = 0, len = attributes.length; i < len; i++) {
            if (null == (attribute = attributes[i])) {
                continue;
            }
    	    if (!attribute.isTagAttribute()) {
                continue;
            }
	    attributeName = attribute.getAttributeName();
	    attributeType = attribute.getAttributeClass();

	    ivar = mangle(attributeName);
            setVar = capitalize(attributeName);
            writer.write("    public void set" + setVar + "(");
            writer.write("java.lang.String " + ivar + ") {\n");
            writer.write("        this." + ivar + " = " + ivar + ";\n");
            writer.write("    }\n\n");
	}
	writer.write("\n");    
    }
 
    /**
     * Generate Tag Handler general methods from component properties
     * and renderer attributes.
     */
    private static void tagHandlerGeneralMethods() throws Exception {

	writer.write("    //\n    // General Methods\n    //\n\n");

	String componentType = component.getComponentType();
	String rendererType = renderer.getRendererType();
	writer.write("    public String getRendererType() { return ");
        writer.write("\""+rendererType+"\"; }\n");
	writer.write("    public String getComponentType() { return ");
	if (componentType.equals(rendererType)) {
	    writer.write("\"javax.faces.Html" + strip(componentType) + "\"; }\n");
	} else {
	    writer.write("\"javax.faces.Html" + strip(componentType) + strip(rendererType) + "\"; }\n");
	}
        writer.write("\n");
        writer.write("    protected void setProperties(UIComponent component) {\n");
        writer.write("        super.setProperties(component);\n");
        String uicomponent = "UI"+strip(componentType);
        writer.write("        "+uicomponent+" "+strip(componentType).toLowerCase()+
		     " = null;\n");

	writer.write("        try {\n");
        writer.write("            "+strip(componentType).toLowerCase()+
		     " = (UI"+strip(componentType)+ ")component;\n");
	writer.write("        }\n");
	writer.write("        catch (ClassCastException cce) {\n");
	writer.write("          throw new IllegalStateException(\"Component \" + component.toString() + \" not expected type.  Expected: " + uicomponent + ".  Perhaps you're missing a tag?\");\n");
	writer.write("        }\n\n");
        if (convertibleValueHolderComponents.contains(uicomponent)) {
            writer.write("        if (converter != null) {\n");
            writer.write("            if (isValueReference(converter)) {\n");
            writer.write("                      ValueBinding vb = \n");
            writer.write("                          Util.getValueBinding(converter);\n");
            writer.write("                  "+strip(componentType).toLowerCase()+".setValueBinding(");
            writer.write("\""+"converter\", vb);\n");
            writer.write("            } else {\n");
            writer.write("                Converter _converter = FacesContext.getCurrentInstance().\n");          
	    writer.write("                    getApplication().createConverter(converter);\n"); 
            writer.write("                " + strip(componentType).toLowerCase() + ".setConverter(_converter);\n");

            writer.write("            }\n");
            writer.write("        }\n\n");
        }

	// Generate "setProperties" method contents from component properties
	//
        PropertyBean[] properties = component.getProperties();
	PropertyBean property = null;
	String propertyName = null;
	String propertyType = null;
	String ivar = null;
	String vbKey = null;
        for (int i = 0, len = properties.length; i < len; i++) {
            if (null == (property = properties[i])) {
                continue;
            }
    	    if (!property.isTagAttribute()) {
                continue;
            }
	    propertyName = property.getPropertyName();
	    propertyType = property.getPropertyClass();

	    // SPECIAL - Don't generate these properties 
	    if (propertyName.equals("binding") ||
		propertyName.equals("id") ||
		propertyName.equals("rendered") ||
		propertyName.equals("converter")) {
		continue;
	    }
	    ivar = mangle(propertyName);
	    vbKey = ivar;

	    if (valueBindingEnabledProperties.contains(propertyName)) {
                writer.write("        if ("+ivar+" != null) {\n");
                writer.write("            if (isValueReference("+ivar+")) {\n");
                writer.write("                ValueBinding vb = ");
                writer.write("Util.getValueBinding("+ivar+");\n");

                writer.write("                "+strip(componentType).toLowerCase());
                writer.write(".setValueBinding(\""+vbKey+"\", vb);\n");
                writer.write("            } else {\n");
		if (primitive(propertyType)) {
	            writer.write("                "+propertyType+" _"+ivar+" ");
                    writer.write("= new "+wrappers.get(propertyType));
                    writer.write("("+ivar+")."+propertyType+"Value();\n");
                    writer.write("                "+strip(componentType).toLowerCase()+
                        ".set" + capitalize(propertyName) + "(_" + ivar + ");\n");
                } else {
                    writer.write("                "+strip(componentType).toLowerCase()+
		        ".set" + capitalize(propertyName) + "(" + ivar + ");\n");
                }
                writer.write("            }\n");
                writer.write("        }\n");
	    } else if (methodBindingEnabledProperties.contains(propertyName)) {
                if (ivar.equals("action")) {
                    writer.write("        if ("+ivar+" != null) {\n");
                    writer.write("            if (isValueReference("+ivar+")) {\n");
                    writer.write("                MethodBinding vb = FacesContext.getCurrentInstance().");
                    writer.write("getApplication().createMethodBinding("+ivar+", null);\n");
                    writer.write("                "+strip(componentType).toLowerCase());
                    writer.write(".setAction(vb);\n");
                    writer.write("            } else {\n");
                    writer.write("                final String outcome = " + ivar + ";\n");
                    writer.write("                MethodBinding vb = Util.createConstantMethodBinding("+ivar+");\n");
                    writer.write("                "+strip(componentType).toLowerCase()+".setAction(vb);\n");
                    writer.write("            }\n");
                    writer.write("        }\n");
                } else {
                    HashMap signatureMap = new HashMap(3);
                    signatureMap.put("actionListener", 
		        "Class args[] = { ActionEvent.class };");
                    signatureMap.put("validator",
                        "Class args[] = { FacesContext.class, UIComponent.class, Object.class };");
                    signatureMap.put("valueChangeListener",
                        "Class args[] = { ValueChangeEvent.class };");
                    writer.write("        if ("+ivar+" != null) {\n");
                    writer.write("            if (isValueReference("+ivar+")) {\n");
                    writer.write("                " + signatureMap.get(ivar) + "\n");
                    writer.write("                MethodBinding vb = FacesContext.getCurrentInstance().");
                    writer.write("getApplication().createMethodBinding("+ivar+", args);\n");
                    writer.write("                "+strip(componentType).toLowerCase());
                    writer.write(".set" + capitalize(ivar) + "(vb);\n");
                    writer.write("            } else {\n");
                    writer.write("              Object params [] = {" + ivar + "};\n");
                    writer.write("              throw new javax.faces.FacesException(Util.getExceptionMessageString(Util.INVALID_EXPRESSION_ID, params));\n");
                    writer.write("            }\n");
                    writer.write("            }\n");
                } 
	    } else {
                writer.write("        "+strip(componentType).toLowerCase()+".set" +
                    capitalize(propertyName) + "(" + ivar + ");\n");
	    }
	}

	// Generate "setProperties" method contents from renderer attributes 
	//
        AttributeBean[] attributes = renderer.getAttributes();
	AttributeBean attribute = null;
	String attributeName = null;
	String attributeType = null;
	ivar = null;
	vbKey = null;
        for (int i = 0, len = attributes.length; i < len; i++) {
            if (null == (attribute = attributes[i])) {
                continue;
            }
    	    if (!attribute.isTagAttribute()) {
                continue;
            }
	    attributeName = attribute.getAttributeName();
	    attributeType = attribute.getAttributeClass();

	    ivar = mangle(attributeName);
	    vbKey = ivar;

	    writer.write("        if ("+ivar+" != null) {\n");
            writer.write("            if (isValueReference("+ivar+")) {\n");
	    writer.write("                ValueBinding vb = ");
	    writer.write("Util.getValueBinding("+ivar+");\n");
            writer.write("                "+strip(componentType).toLowerCase());
	    if (ivar.equals("_for")) {
	        writer.write(".setValueBinding(\""+"_"+vbKey+"\", vb);\n");
	    } else {
	        writer.write(".setValueBinding(\""+vbKey+"\", vb);\n");
	    }
	    writer.write("            } else {\n");
	    if (primitive(attributeType)) {
	        writer.write("                "+attributeType+" _"+ivar+" ");
		writer.write("= new "+wrappers.get(attributeType));
		writer.write("("+ivar+")."+attributeType+"Value();\n");
		if (attributeType.equals("boolean")) {
		    writer.write("                "+strip(componentType).toLowerCase()+
			".getAttributes().put(\""+ivar+"\", ");
		    writer.write("_"+ivar+" ? Boolean.TRUE : Boolean.FALSE);\n");
		} else {
		    writer.write("                if (_"+ivar+" != ");
		    writer.write(defaults.get(attributeType)+") {\n");
		    writer.write("                    "+strip(componentType).toLowerCase()+
		        ".getAttributes().put(\""+ivar+"\", new ");
		    writer.write(wrappers.get(attributeType)+"(_"+ivar+"));\n");
		    writer.write("                }\n");
		}
	    } else {
	        if (ivar.equals("bundle")) {
		    writer.write("                "+strip(componentType).toLowerCase()+
			".getAttributes().put(com.sun.faces.RIConstants.BUNDLE_ATTR, ");
		} else if (ivar.equals("_for")) {
		    writer.write("                "+strip(componentType).toLowerCase()+
			".getAttributes().put(\"for\", ");
		} else {
		    writer.write("                "+strip(componentType).toLowerCase()+
			".getAttributes().put(\""+ivar+"\", ");
		}
		writer.write(ivar+");\n");
	    }
	    writer.write("            }\n");
	    writer.write("        }\n");
	}
	writer.write("    }\n\n");
    }

    /**
     * Generate Tag Handler support methods
     */
    private static void tagHandlerSupportMethods() throws Exception {
        writer.write("    //\n    // Methods From TagSupport\n    //\n\n");
	writer.write("    public int doStartTag() throws JspException {\n");
	writer.write("        int rc = 0;\n");
	writer.write("        try {\n");
	writer.write("            rc = super.doStartTag();\n");
	writer.write("        } catch (JspException e) {\n");
	writer.write("            if (log.isDebugEnabled()) {\n");
	writer.write("                log.debug(getDebugString(), e);\n");
	writer.write("            }\n");
	writer.write("            throw e;\n");
	writer.write("        } catch (Throwable t) {\n");
	writer.write("            if (log.isDebugEnabled()) {\n");
	writer.write("                log.debug(getDebugString(), t);\n");
	writer.write("            }\n");
	writer.write("            throw new JspException(t);\n");
	writer.write("        }\n");
	writer.write("        return rc;\n");
	writer.write("    }\n\n");
	writer.write("    public int doEndTag() throws JspException {\n");
	writer.write("        int rc = 0;\n");
	writer.write("        try {\n");
	writer.write("            rc = super.doEndTag();\n");
	writer.write("        } catch (JspException e) {\n");
	writer.write("            if (log.isDebugEnabled()) {\n");
	writer.write("                log.debug(getDebugString(), e);\n");
	writer.write("            }\n");
	writer.write("            throw e;\n");
	writer.write("        } catch (Throwable t) {\n");
	writer.write("            if (log.isDebugEnabled()) {\n");
	writer.write("                log.debug(getDebugString(), t);\n");
	writer.write("            }\n");
	writer.write("            throw new JspException(t);\n");
	writer.write("        }\n");
	writer.write("        return rc;\n");
	writer.write("    }\n\n");
    }

    /**
     * Generate Body Tag Handler support methods
     */
    private static void tagHandlerBodySupportMethods() throws Exception {
        writer.write("    //\n    // Methods From TagSupport\n    //\n\n");
        writer.write("    public int doStartTag() throws JspException {\n");
        writer.write("        int rc = 0;\n");
        writer.write("        try {\n");
        writer.write("            rc = super.doStartTag();\n");
        writer.write("        } catch (JspException e) {\n");
        writer.write("            if (log.isDebugEnabled()) {\n");
        writer.write("                log.debug(getDebugString(), e);\n");
        writer.write("            }\n");
        writer.write("            throw e;\n");
        writer.write("        } catch (Throwable t) {\n");
        writer.write("            if (log.isDebugEnabled()) {\n");
        writer.write("                log.debug(getDebugString(), t);\n");
        writer.write("            }\n");
        writer.write("            throw new JspException(t);\n");
        writer.write("        }\n");
        writer.write("        return rc;\n");
        writer.write("    }\n\n");
        writer.write("    public int doEndTag() throws JspException {\n");
        writer.write("        String content = null;\n");
        writer.write("        try {\n");
        writer.write("            if (null != (bodyContent = getBodyContent())) {\n");
        writer.write("                content = bodyContent.getString();\n");
        writer.write("                getPreviousOut().write(content);\n");
        writer.write("            }\n");
        writer.write("        } catch (IOException iox) {\n");
        writer.write("            throw new JspException(iox);\n");
        writer.write("        }\n"); 
        writer.write("        int rc = super.doEndTag();\n"); 
        writer.write("        return rc;\n"); 
        writer.write("    }\n\n"); 
    }
 

    /**
     * Generate remaining Tag Handler methods 
     */
    private static void tagHandlerSuffix() throws Exception {

        // generate general purpose method used in logging.
        //
        writer.write("    public String getDebugString() {\n");
        String res = "\"id: \"+this.getId()+\" class: \"+this.getClass().getName()";
        writer.write("        String result = "+res+";\n");
        writer.write("        return result;\n");
        writer.write("    }\n\n");

        writer.write("}\n");
    }

    //
    // Helper methods
    //
    //

    /**
     * Is the tag handler we're building a body tag variety?
     */
    private static boolean isBodyTag() {
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
    private static SortedMap getComponentFamilyRendererMap(String rkId) 
	throws IllegalStateException {
	RenderKitBean renderKit = null;
	RendererBean [] renderers = null;
	RendererBean renderer = null;
	TreeMap result = null;
	ArrayList list = null;
	String componentFamily = null;

	if (null == (renderKit = fcb.getRenderKit(rkId))) {
	    RenderKitBean [] kits = null;
	    if (null == (kits = fcb.getRenderKits())) {
		throw new IllegalStateException("no RenderKits");
	    }
	    if (null == (renderKit = kits[0])) {
		throw new IllegalStateException("no RenderKits");
	    }
	}

	if (null == (renderers = renderKit.getRenderers())) {
	    throw new IllegalStateException("no Renderers");
	}

	result = new TreeMap();
	
	for (int i = 0, len = renderers.length; i < len; i++) {
	    if (null == (renderer = renderers[i])) {
		throw new IllegalStateException("no Renderer");
	    }
	    // if this is the first time we've encountered this
	    // componentFamily
	    if (null == (list = (ArrayList)
			 result.get(componentFamily = 
				    renderer.getComponentFamily()))) {
		// create a list for it
		list = new ArrayList();
		list.add(renderer);
		result.put(componentFamily, list);
	    }
	    else {
		list.add(renderer);
	    }
	}
	return result;
    }

    /**
     * @return a SortedMap, where the keys are component-family String
     * entries, and the values are {@link ComponentBean} instances
     * Only include components that do not have a base component type.
     */
    private static SortedMap getComponentFamilyComponentMap() 
        throws IllegalStateException {
	TreeMap result = new TreeMap();
	ComponentBean[] components = fcb.getComponents();
	for (int i = 0, len = components.length; i < len; i++) {
	    if (null == (component = components[i])) {
	        throw new IllegalStateException("No Components Found");
	    }
	    if (component.getBaseComponentType() != null) {
	        continue;
	    }
	    String componentFamily = component.getComponentFamily();
	    String componentType = component.getComponentType();
	    result.put(componentFamily, component);
	}
	return result;
    }


    /**
     * Generate the TLD file.
     */
    private static void generateTld() throws Exception {
        // Create and open a Writer for generating our output
        File file = new File(directory, "html_basic.tld");
        writer = new BufferedWriter(new FileWriter(file));

	// Generate each section of the TLD consecutively
	xmlHeader();
	// copyright();
	tldDocType();
	tldDescription();
	tldValidator();
	tldTags();

        // Flush and close the Writer 
        writer.flush();
        writer.close();
    }

    /**
     * Generate the tag handler class files.
     */
    private static void generateTagClasses() throws Exception {
	Iterator 
	    rendererIter = null,
	    keyIter = renderersByComponentFamily.keySet().iterator();
	String componentFamily = null;
	String rendererType = null;
	List renderers = null;
	while (keyIter.hasNext()) {
	    componentFamily = (String) keyIter.next();
	    renderers = (List)renderersByComponentFamily.get(componentFamily);
            component = (ComponentBean)componentsByComponentFamily.get(componentFamily);
	    rendererIter = renderers.iterator();
	    while (rendererIter.hasNext()) {
                renderer = (RendererBean) rendererIter.next();
		rendererType = renderer.getRendererType();
                if ((tagClassName = makeTagClassName(strip(componentFamily), strip(rendererType))) == null) {
		    throw new IllegalStateException("Could not determine tag class name");
		}
                if (log.isInfoEnabled()) {
                    log.info("Generating " + tagClassName + "...");
                }
                File file = new File(directory, tagClassName + ".java");
                writer = new BufferedWriter(new FileWriter(file));

		tagHandlerPrefix();
		tagHandlerIvars();
		tagHandlerSetterMethods();
		tagHandlerGeneralMethods();
                if (isBodyTag()) {
                    tagHandlerBodySupportMethods();
                } else {
		    tagHandlerSupportMethods();
                }
		tagHandlerSuffix();
	    
                // Flush and close the Writer 
                writer.flush();
                writer.close();
            }	
	}
    }



    private static final String PREFIX = "javax.faces.";

    /**
     * <p>Strip any "javax.faces." prefix from the beginning of the specified
     * identifier, and return it.</p>
     *
     * @param identifier Identifier to be stripped
     */
    private static String strip(String identifier) {
	if (identifier.startsWith(PREFIX)) {
	    return (identifier.substring(PREFIX.length()));
	} else {
	    return (identifier);
	}
    }

    /**
     * @return true if this attribute is in the set of attributes to be
     * excluded by this renderer.
     *
     */ 
    private static boolean skipThisAttribute(RendererBean renderer, String attribute) {
	String excludeAttributes = null;
	boolean skip = false;

	if (null != (excludeAttributes = renderer.getExcludeAttributes())) {
	    skip = (null != attribute && 
		    -1 != excludeAttributes.indexOf(attribute));
	}
	return skip;
    }


    /**
     * Main routine.
     */
    public static void main(String args[]) throws Exception {

        try {
            // Perform setup operations
            if (log.isDebugEnabled()) {
                log.debug("Processing command line options");
            }
            Map options = options(args);

            copyright((String) options.get("--copyright"));
            directories((String) options.get("--tlddir"), false);
            Digester digester = digester(false, true, false);
            String config = (String) options.get("--config");
	    loadOptionalTags((String) options.get("--tagdef"));
            if (log.isDebugEnabled()) {
                log.debug("Parsing configuration file '" + config + "'");
            }
            digester.push(new FacesConfigBean());
            fcb = parse(digester, config);
            if (log.isInfoEnabled()) {
                log.info("Generating Tag Library Descriptor file");
            }

	    // Generate TLD File
	    generateTld();

	    // Generate Tag Handler Classes
            directories((String) options.get("--dir"), true);
	    generateTagClasses();

	} catch(Exception e) {
            e.printStackTrace();
            System.exit(1);
	}
	System.exit(0);
    }
}
