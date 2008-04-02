/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.generate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This class generates tag handler class code that is special to the "html_basic"
 * package.
 */
public class HtmlTaglibGenerator extends GenerateTagBase implements TaglibGenerator {

    // Log instance for this class
    protected static Log log = LogFactory.getLog(GenerateTaglib.class);

    // This List will hold component property names which are "value binding [VB] enabled;
    //
    private List valueBindingEnabledProperties = null;

    // A component in these lists signifies that the component is a ValueHolder or
    // ConvertibleValueHolder;  This is used to determine if we generate ValueHolder
    // ConvertibleValueHolder code in "overrideProperties" method;
    //
    private List valueHolderComponents;
    private List convertibleValueHolderComponents;

    // global defaults for html basic tld.  They should be read from config
    // file.
    //
    public static final String TAGCLASSPATH = "com.sun.faces.taglib.html_basic";
    public static final String TEICLASS = "com.sun.faces.taglib.FacesTagExtraInfo";
    public static final String BODYCONTENT = "JSP";
    public static final String REQUIRED = "false";
    public static final String RTEXPRVALUE = "false";

    public HtmlTaglibGenerator() {
        valueHolderComponents = new ArrayList();
	valueHolderComponents.add("UICommand");
	valueHolderComponents.add("UIData");
	valueHolderComponents.add("UIGraphic");
	valueHolderComponents.add("UIOutput");
	valueHolderComponents.add("UIPanel");
	valueHolderComponents.add("UIInput");
	valueHolderComponents.add("UISelectMany");
	valueHolderComponents.add("UISelectOne");
	convertibleValueHolderComponents = new ArrayList();
	convertibleValueHolderComponents.add("UIOutput");

	valueBindingEnabledProperties = new ArrayList();
	valueBindingEnabledProperties.add("action");
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

    // Not that the following methods simply return a global default value now.  When we
    // set up the class path, tag extra info, required, rtexprValue, bodycontext to be
    // configurable, then we can retrieve that info using the args.
    //

    /**
     * Return the class path for the given tag.
     */
    public String getTagClassPath(String tagName) {
        return TAGCLASSPATH;
    }

    /**
     * Return the tag extra info information (if any) for a given tag.
     */
    public String getTeiClass(String tagName) {
        return TEICLASS;
    }

    /**
     * Return the tag body content information (if any) for a given tag.
     */
    public String getBodyContent(String tagName) {
        return BODYCONTENT;
    }

    /**
     * Return the "required" element value for the tag attribute.
     */
    public String getRequired(String tagName, String attributeName) {
        return REQUIRED;
    }

    /**
     * Return the "rtexprvalue" element value for the tag attribute.
     */
    public String getRtexprvalue(String tagName, String attributeName) {
        return RTEXPRVALUE;
    }

    //
    // Following methods uses in tag class generation;
    //

    /**
     * Generate any import statements when generating tag classes.
     */
    public String getImportInfo() {
        StringBuffer sb = new StringBuffer();
	sb.append("import com.sun.faces.util.*;\n");
	sb.append("import javax.faces.component.*;\n");
	sb.append("import javax.faces.context.FacesContext;\n");
	sb.append("import javax.faces.convert.Converter;\n");
	sb.append("import javax.faces.el.ValueBinding;\n");
	sb.append("import javax.faces.webapp.UIComponentTag;\n");
	sb.append("import javax.servlet.jsp.JspException;\n");
	sb.append("import org.apache.commons.logging.*;\n");
	return sb.toString();
    }

    /**
     * Generate class declaration statement. 
     */
    public String getClassDeclaration(String tagName) {
        return "public class "+tagName+" extends UIComponentTag {";
    }

    /**
     * Generate instance variables
     */
    public String generateIvars(List attributeNames, String type, Map generated) {
        StringBuffer result = new StringBuffer(40000);
	if (attributeNames != null) {
	    String info = generateIvarsFromAttributes(attributeNames, type, generated);
	    if (info != null) {
	        result.append(info);
	    }
	}
	return result.toString();
    }

    /**
     * Generate accessor methods
     */
    public String generateAccessorMethods(List attributeNames, String type, Map generated) {
        StringBuffer result = new StringBuffer(40000);
	if (attributeNames != null) {
	    String info = generateAccessorMethodsFromAttributes(attributeNames, type, generated);
	    if (info != null) {
	        result.append(info);
	    }
	}
	return result.toString();
    }

    /**
     * Generate general methods that may be used in the tag handler (ex: overrideProperties);
     */
    public String generateGeneralMethods(List rendererAttributeNames, 
	List componentAttributeNames, String rendererType, String componentRendererType) {
        StringBuffer result = new StringBuffer(40000);
	String info = null;
	String componentType = determineComponentType(componentRendererType, rendererType);

	result.append("    //\n    // General Methods\n    //\n");
	result.append("    public String getRendererType() { return ");
	result.append("\""+rendererType+"\"; }\n");
	result.append("    public String getComponentType() { return ");
	result.append("\""+componentRendererType+"\"; }\n\n");

	result.append("    public void overrideProperties(UIComponent component) {\n");
	result.append("        super.overrideProperties(component);\n");
	String component = "UI"+componentType;
	result.append("        "+component+" "+componentType.toLowerCase()+
	    " = (UI"+componentType+ ")component;\n\n");
	if (valueHolderComponents.contains(component)) {
	    result.append("        if (component instanceof ValueHolder) {\n");
	    result.append("            if (value != null) {\n");
	    result.append("                if (isValueReference(value)) {\n");
	    result.append("                    ValueBinding vb = \n");
	    result.append("                        FacesContext.getCurrentInstance().getApplication().\n");
	    result.append("                        getValueBinding(value);\n");
	    result.append("                    "+componentType.toLowerCase()+".setValueBinding(");
	    result.append("\""+"value\", vb);\n"); 
	    result.append("                } else {\n"); 
	    result.append("                    ((ValueHolder)component).setValue(value);\n");
	    result.append("                }\n");
	    result.append("            }\n");
	    result.append("        }\n\n");
	}
	if (convertibleValueHolderComponents.contains(component)) {
	    result.append("        if (component instanceof ConvertibleValueHolder) {\n");
	    result.append("            if (converter != null) {\n");
	    result.append("                if (isValueReference(converter)) {\n");
	    result.append("                    ValueBinding vb = \n");
	    result.append("                        FacesContext.getCurrentInstance().getApplication().\n");
	    result.append("                        getValueBinding(converter);\n");
	    result.append("                    "+componentType.toLowerCase()+".setValueBinding(");
	    result.append("\""+"converter\", vb);\n"); 
	    result.append("                } else {\n"); 
	    result.append("                    Converter _converter = (Converter)Util.createInstance("+
	        "converter);\n");
	    result.append("                    ((ConvertibleValueHolder)component).setConverter(_converter);\n");
	    result.append("                }\n");
	    result.append("            }\n");
	    result.append("        }\n\n");
	}

	Map generated = new HashMap();
	info = generateOverrideMethodFromAttributes(rendererAttributeNames, rendererType, 
	    componentRendererType, "renderer", generated);
	if (info != null) {
	    result.append(info);
	}
	info = generateOverrideMethodFromAttributes(componentAttributeNames, rendererType,
	    componentRendererType, "component", generated);
	if (info != null) {
	    result.append(info);
	}
	result.append("    }\n\n");

	// generate TagSupport Methods
	//
	info = generateTagSupportMethods();
	if (info != null) {
	    result.append(info);
	}
	return result.toString();
    }

    /**
     * Generate general methods that may be used in the tag handler (ex: overrideProperties);
     * This method designed to handle generation based on component type only.  It is used
     * when generating tag class information from components not associated with any renderer.
     */
    public String generateGeneralMethods(List componentAttributeNames, String componentType) {
        StringBuffer result = new StringBuffer(40000);
	String info = null;

	result.append("    //\n    // General Methods\n    //\n");
	result.append("    public String getRendererType() { return null; }\n");
	result.append("    public String getComponentType() { return ");
	result.append("\""+componentType+"\"; }\n\n");

	result.append("    public void overrideProperties(UIComponent component) {\n");
	result.append("        super.overrideProperties(component);\n");
	String component = "UI"+componentType;
	result.append("        "+component+" "+componentType.toLowerCase()+
	    " = (UI"+componentType+ ")component;\n\n");
	if (valueHolderComponents.contains(component)) {
	    result.append("        if (component instanceof ValueHolder) {\n");
	    result.append("            if (value != null) {\n");
	    result.append("                if (isValueReference(value)) {\n");
	    result.append("                    ValueBinding vb = \n");
	    result.append("                        FacesContext.getCurrentInstance().getApplication().\n");
	    result.append("                        getValueBinding(value);\n");
	    result.append("                    "+componentType.toLowerCase()+".setValueBinding(");
	    result.append("\""+"value\", vb);\n"); 
	    result.append("                } else {\n"); 
	    result.append("                    ((ValueHolder)component).setValue(value);\n");
	    result.append("                }\n");
	    result.append("            }\n");
	    result.append("        }\n\n");
	}
	if (convertibleValueHolderComponents.contains(component)) {
	    result.append("        if (component instanceof ConvertibleValueHolder) {\n");
	    result.append("            if (converter != null) {\n");
	    result.append("                if (isValueReference(converter)) {\n");
	    result.append("                    ValueBinding vb = \n");
	    result.append("                        FacesContext.getCurrentInstance().getApplication().\n");
	    result.append("                        getValueBinding(converter);\n");
	    result.append("                    "+componentType.toLowerCase()+".setValueBinding(");
	    result.append("\""+"converter\", vb);\n"); 
	    result.append("                } else {\n"); 
	    result.append("                    Converter _converter = (Converter)Util.createInstance("+
	        "converter);\n");
	    result.append("                    ((ConvertibleValueHolder)component).setConverter(_converter);\n");
	    result.append("                }\n");
	    result.append("            }\n");
	    result.append("        }\n\n");
	}

	info = generateOverrideMethodFromAttributes(componentAttributeNames, componentType);
	if (info != null) {
	    result.append(info);
	}

	result.append("    }\n\n");
	
	// generate TagSupport Methods
	//
	info = generateTagSupportMethods();
	if (info != null) {
	    result.append(info);
	}

	return result.toString();
    }

    public void setParser(ConfigParser parser) {
        this.parser = parser;
    }

    public ConfigParser getParser() {
        return parser;
    }

    //
    // Helper methods
    //

    /**
     * Generate instance variables from a List of attribute names, the type (component.renderer).
     * The "generated" Map contains attribute name|attribute type mappings, and is used to
     * ensure we don't use the same attribute for generation more than once.
     */
    private String generateIvarsFromAttributes(List attributeNames, String type, Map generated) {
        String attributeName = null;
	String attributeClass = null;
        StringBuffer result = new StringBuffer(40000);
	result.append("\n\n    //\n    // Instance Variables\n    //\n");
	for (int i=0; i<attributeNames.size(); i++) {
	    attributeName = (String)attributeNames.get(i);
	    
	    // these are already handled in javax.faces.webapp.UIComponentTag
	    //
	    if (attributeName.equals("componentRef") ||
		attributeName.equals("rendered")) {
		continue;
	    }
	    boolean isRendererAttribute = false; 
	    attributeClass = getParser().getRendererAttributeClass(type, attributeName);
	    if (attributeClass != null) {
	        isRendererAttribute = true;
	    } else {
	        attributeClass = getParser().getComponentPropertyClass(type, attributeName);
	        if (attributeClass == null) {
	            throw new IllegalStateException("Can't find attribute class for type:"+
		        type+" and attribute name:"+attributeName);
		}
	        isRendererAttribute = false;
	    }
	    // check if we've already generated the same attribute name
	    //
	    String attrType = (String)generated.get(attributeName);
	    if (attrType != null) {
	        if (attrType.equals(attributeClass)) {
		    continue;
		} else {
		    throw new IllegalStateException("Already generated attribute name:"+
		        attributeName+" but with the type:"+attrType+
			" for type:"+type);
		}
	    }

	    String ivar = generateIvar(attributeName);

	    // don't generate an Ivar for those things that are already
	    // ivars in UIComponentTag.
	    if (!(attributeName.equals("componentRef") ||
		  attributeName.equals("id") || 
		  attributeName.equals("override") ||
		  attributeName.equals("rendered"))) {
		
		// if the attribute is a renderer attribute, or if it's a component
		// property which is value binding enabled - then we just generate
		// "String" types.
		//
		if (isRendererAttribute) {
		    result.append("    private java.lang.String "+ivar);
		} else if (valueBindingEnabledProperties.contains(attributeName)) {
		    result.append("    private java.lang.String "+ivar);
		} else {
		    result.append("    private "+attributeClass+" "+ivar);
		    // if it's a primitive
		    if (isPrimitive(attributeClass)) {
		        // assign the default value
		        result.append(" = "+(String)defaultPrimitiveValues.get(attributeClass));
	            }
		}
		result.append(";\n");
	    }
	    
	    generated.put(attributeName, attributeClass);
		    
        }	

	return result.toString();
    }

    /**
     * Generate accessor methods from a List of attribute names, the type (component.renderer).
     * The "generated" Map contains attribute name|attribute type mappings, and is used to
     * ensure we don't use the same attribute for generation more than once.
     */
    private String generateAccessorMethodsFromAttributes(List attributeNames, String type,
	Map generated) {
        String attributeName = null;
	String attributeClass = null;
        StringBuffer result = new StringBuffer(40000);
	result.append("\n\n    //\n    // Setter Methods\n    //\n");
	for (int i=0; i<attributeNames.size(); i++) {
	    attributeName = (String)attributeNames.get(i);
	    if (attributeName.equals("componentRef") ||
		attributeName.equals("rendered")) {
		continue;
	    }
	    boolean isRendererAttribute = false;
	    attributeClass = getParser().getRendererAttributeClass(type, attributeName);
	    if (attributeClass != null) {
	        isRendererAttribute = true;
	    } else {
	        attributeClass = getParser().getComponentPropertyClass(type, attributeName);
	        if (attributeClass == null) {
	            throw new IllegalStateException("Can't find attribute class for type:"+
		        type+":attribute name:"+attributeName);
	        }
	        isRendererAttribute = false;
	    } 
	    // check if we have already generated the same attribute name
	    //
	    String attrType = (String)generated.get(attributeName);
	    if (attrType != null) {
	        if (attrType.equals(attributeClass)) {
		    continue;
		} else {
		    throw new IllegalStateException("Already generated attribute name:"+
		        attributeName+" but with the type:"+attrType+
			" for type:"+type);
		}
	    }
	    String ivar = generateIvar(attributeName);
	    
	    // setter
	    result.append("    public void set" + 
			  Character.toUpperCase(attributeName.charAt(0)) +
			  attributeName.substring(1) + "(");
	    if (isRendererAttribute) {
	        result.append("java.lang.String " + ivar + ") {\n");
	    } else if (valueBindingEnabledProperties.contains(attributeName)) {
	        result.append("java.lang.String " + ivar + ") {\n");
	    } else {
		result.append(attributeClass + " " + ivar + ") {\n");
	    }
	    result.append("        this." + ivar + " = " + ivar + ";\n");
	    result.append("    }\n\n");

	    generated.put(attributeName, attributeClass);
        }	
	return result.toString();
    }

    /**
     * Generate override property method from a List of attribute names, 
     * the type (component.renderer).
     * The "generated" Map contains attribute name|attribute type mappings, and is used to
     * ensure we don't use the same attribute for generation more than once.
     */
    private String generateOverrideMethodFromAttributes(List attributeNames, String rendererType,
	String componentRendererType, String metaType, Map generated) {
        String attributeName = null;
	String attributeClass = null;
        StringBuffer result = new StringBuffer(40000);


	for (int i=0; i<attributeNames.size(); i++) {
	    attributeName = (String)attributeNames.get(i);
	    if (metaType.equals("renderer")) {
	        attributeClass = getParser().getRendererAttributeClass(rendererType, attributeName);
	    } else {
	        attributeClass = getParser().getComponentPropertyClass(componentRendererType, 
		    attributeName);
	    }
	    // check if we've already generated the same attribute name
	    //
	    String attrType = (String)generated.get(attributeName);
	    if (attrType != null) {
	        if (attrType.equals(attributeClass)) {
		    continue;
		} else {
		    throw new IllegalStateException("Already generated attribute name:"+
		        attributeName+" but with the type:"+attrType+
			" for:"+rendererType+":"+componentRendererType);
		}
	    }
	    String ivar = generateIvar(attributeName);
	    String componentType = determineComponentType(componentRendererType, rendererType);
            String vbKey = ivar;
	    if (ivar.equals("url")) {
	        vbKey = "value";
	    }
	    if (metaType.equals("renderer")) {
	        result.append("        if ("+ivar+" != null) {\n");
		result.append("            if (isValueReference("+ivar+")) {\n");
		result.append("                ValueBinding vb = FacesContext.getCurrentInstance().");
		result.append("getApplication().getValueBinding("+ivar+");\n");
                result.append("                "+componentType.toLowerCase());
		if (ivar.equals("_for")) {
	            result.append(".setValueBinding(\""+"_"+vbKey+"\", vb);\n");
		} else {
		    result.append(".setValueBinding(\""+vbKey+"\", vb);\n");
		}
		result.append("            } else {\n");
		if (isPrimitive(attributeClass)) {
		    result.append("                "+attributeClass+" _"+ivar+" ");
		    result.append("= new "+wrappersForNumbers.get(attributeClass));
		    result.append("("+ivar+")."+attributeClass+"Value();\n");
		    if (attributeClass.equals("boolean")) {
			result.append("                "+componentType.toLowerCase()+
				      ".getAttributes().put(\""+ivar+"\", ");
			result.append("_"+ivar+" ? Boolean.TRUE : Boolean.FALSE);\n");
		    } else {
		        result.append("                if (_"+ivar+" != ");
		        result.append(defaultPrimitiveValues.get(attributeClass)+") {\n");
		        result.append("                    "+componentType.toLowerCase()+
		            ".getAttributes().put(\""+ivar+"\", new ");
		        result.append(wrappersForNumbers.get(attributeClass)+"(_"+ivar+"));\n");
		        result.append("                }\n");
		    }
		} else {
		    if (ivar.equals("bundle")) {
			result.append("                "+componentType.toLowerCase()+
				      ".getAttributes().put(com.sun.faces.RIConstants.BUNDLE_ATTR, ");
		    }
		    else if (ivar.equals("_for")) {
			result.append("                "+componentType.toLowerCase()+
				      ".getAttributes().put(\"for\", ");
		    }
		    else {
			result.append("                "+componentType.toLowerCase()+
				      ".getAttributes().put(\""+ivar+"\", ");
		    }
		    result.append(ivar+");\n");
		}
		result.append("            }\n");
		result.append("        }\n");
	    } else {
	        if (attributeName.equals("id") || 
		    attributeName.equals("componentRef") ||
		    attributeName.equals("rendered") || attributeName.equals("converter") ||
		    attributeName.equals("value") || attributeName.equals("valueRef")) {
		    continue;
		}
	        if (valueBindingEnabledProperties.contains(attributeName)) {
	            result.append("        if ("+ivar+" != null) {\n");
		    result.append("            if (isValueReference("+ivar+")) {\n");
		    result.append("                ValueBinding vb = FacesContext.getCurrentInstance().");
		    result.append("getApplication().getValueBinding("+ivar+");\n");
                    result.append("                "+componentType.toLowerCase());
		    result.append(".setValueBinding(\""+vbKey+"\", vb);\n");
		    result.append("            } else {\n");
		    if (isPrimitive(attributeClass)) {
		        result.append("                "+attributeClass+" _"+ivar+" ");
		        result.append("= new "+wrappersForNumbers.get(attributeClass));
		        result.append("("+ivar+")."+attributeClass+"Value();\n");
			result.append("                "+componentType.toLowerCase()+
			    ".set"+Character.toUpperCase(attributeName.charAt(0)) +
			    attributeName.substring(1) + "(_" + ivar + ");\n");
		    } else {
	                result.append("                "+componentType.toLowerCase()+".set" + 
		            Character.toUpperCase(attributeName.charAt(0)) +
		            attributeName.substring(1) + "(" + ivar + ");\n");
		    }
		    result.append("            }\n");
		    result.append("        }\n");
	        } else {
	            result.append("        "+componentType.toLowerCase()+".set" + 
		        Character.toUpperCase(attributeName.charAt(0)) +
		        attributeName.substring(1) + "(" + ivar + ");\n");
		}
	    }
	    generated.put(attributeName, attributeClass);
        }	
	return result.toString();
    }

    /**
     * Overloaded method to deal with component types only.
     */
    private String generateOverrideMethodFromAttributes(List attributeNames, String componentType) {
        String attributeName = null;
	String attributeClass = null;
        StringBuffer result = new StringBuffer(40000);


	for (int i=0; i<attributeNames.size(); i++) {
	    attributeName = (String)attributeNames.get(i);
	    attributeClass = getParser().getComponentPropertyClass(componentType, attributeName);
	    String ivar = generateIvar(attributeName);
	    String vbKey = ivar;
	    if (ivar.equals("url")) {
	        vbKey = "value";
	    }

	    if (attributeName.equals("componentRef") ||
	        attributeName.equals("rendered") || attributeName.equals("converter") ||
		attributeName.equals("value") || attributeName.equals("valueRef")) {
		continue;
	    }
	    if (valueBindingEnabledProperties.contains(attributeName)) {
	        result.append("        if ("+ivar+" != null) {\n");
	        result.append("            if (isValueReference("+ivar+")) {\n");
		result.append("                ValueBinding vb = FacesContext.getCurrentInstance().");
		result.append("getApplication().getValueBinding("+ivar+");\n");
                result.append("                "+componentType.toLowerCase());
		result.append(".setValueBinding(\""+vbKey+"\", vb);\n");
		result.append("            } else {\n");
		if (isPrimitive(attributeClass)) {
		    result.append("                "+attributeClass+" _"+ivar+" ");
		    result.append("= new "+wrappersForNumbers.get(attributeClass));
		    result.append("("+ivar+")."+attributeClass+"Value();\n");
		    result.append("                "+componentType.toLowerCase()+
		        ".set"+Character.toUpperCase(attributeName.charAt(0)) +
		        attributeName.substring(1) + "(_" + ivar + ");\n");
		} else {
	            result.append("                "+componentType.toLowerCase()+".set" + 
		        Character.toUpperCase(attributeName.charAt(0)) +
		        attributeName.substring(1) + "(" + ivar + ");\n");
		}
		result.append("            }\n");
		result.append("        }\n");
	    } else {
	        result.append("        "+componentType.toLowerCase()+".set" + 
		    Character.toUpperCase(attributeName.charAt(0)) +
		    attributeName.substring(1) + "(" + ivar + ");\n");
	    }
        }	
	return result.toString();
    }

    private String generateTagSupportMethods() {
        StringBuffer result = new StringBuffer(20000);
        result.append("    //\n    // Methods From TagSupport\n    //\n\n");
	result.append("    public int doStartTag() throws JspException {\n");
	result.append("        int rc = 0;\n");
	result.append("        try {\n");
	result.append("            rc = super.doStartTag();\n");
	result.append("        } catch (JspException e) {\n");
	result.append("            if (log.isDebugEnabled()) {\n");
	result.append("                log.debug(getDebugString(), e);\n");
	result.append("            }\n");
	result.append("            throw e;\n");
	result.append("        } catch (Throwable t) {\n");
	result.append("            if (log.isDebugEnabled()) {\n");
	result.append("                log.debug(getDebugString(), t);\n");
	result.append("            }\n");
	result.append("            throw new JspException(t);\n");
	result.append("        }\n");
	result.append("        return rc;\n");
	result.append("    }\n\n");
	result.append("    public int doEndTag() throws JspException {\n");
	result.append("        int rc = 0;\n");
	result.append("        try {\n");
	result.append("            rc = super.doEndTag();\n");
	result.append("        } catch (JspException e) {\n");
	result.append("            if (log.isDebugEnabled()) {\n");
	result.append("                log.debug(getDebugString(), e);\n");
	result.append("            }\n");
	result.append("            throw e;\n");
	result.append("        } catch (Throwable t) {\n");
	result.append("            if (log.isDebugEnabled()) {\n");
	result.append("                log.debug(getDebugString(), t);\n");
	result.append("            }\n");
	result.append("            throw new JspException(t);\n");
	result.append("        }\n");
	result.append("        return rc;\n");
	result.append("    }\n\n");

	return result.toString();
    }

    protected Log getLog() {
	return log;
    }
}
