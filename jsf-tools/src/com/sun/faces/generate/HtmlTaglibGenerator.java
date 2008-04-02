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

    // This List will hold attribute names to be used in "evaluateExpressions" method;
    //
    private List evaluateExpressionsAttributes = null;

    // This boolean indicates if we generated an "evaluateExpressions" method - so
    // we'll know that we need to generate a call to that method when generating
    // doStartTag method.
    //
    private boolean generatedEvaluateExpressions;

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
    public static final String RTEXPRVALUE = "true";

    public HtmlTaglibGenerator() {
        valueHolderComponents = new ArrayList();
	valueHolderComponents.add("UICommand");
	valueHolderComponents.add("UIData");
	valueHolderComponents.add("UIGraphic");
	valueHolderComponents.add("UIOutput");
	valueHolderComponents.add("UIPanel");
	valueHolderComponents.add("UIInput");
	convertibleValueHolderComponents = new ArrayList();
	convertibleValueHolderComponents.add("UIOutput");
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
	sb.append("import javax.faces.webapp.UIComponentTag;\n");
	sb.append("import javax.faces.component.*;\n");
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
	    result.append("            ValueHolder valueHolder = (ValueHolder)component;\n");
	    result.append("            if (null != valueRef) {\n");
	    result.append("                valueHolder.setValueRef(valueRef);\n");
	    result.append("            }\n");
	    result.append("            if (null != value) {\n");
	    result.append("                valueHolder.setValue(value);\n");
	    result.append("            }\n\n");
	    result.append("        }\n\n");
	}
	if (convertibleValueHolderComponents.contains(component)) {
	    result.append("        if (component instanceof ConvertibleValueHolder) {\n");
	    result.append("            if (null != converter) {\n");
	    result.append("                ((ConvertibleValueHolder)component).setConverter(converter);\n");
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

	// generate evaluateExpressions method;
	//
	generatedEvaluateExpressions = false; 
	generated = new HashMap();
	result.append("    public void evaluateExpressions() throws JspException {\n");
	info = generateEvaluateExpressions(rendererAttributeNames, rendererType, generated);
	if (info != null) {
	    result.append(info);
	    generatedEvaluateExpressions = true;
	}
	info = generateEvaluateExpressions(componentAttributeNames, componentRendererType,
	    generated);
	if (info != null) {
	    result.append(info);
	    generatedEvaluateExpressions = true;
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
	    result.append("            ValueHolder valueHolder = (ValueHolder)component;\n");
	    result.append("            if (null != valueRef) {\n");
	    result.append("                valueHolder.setValueRef(valueRef);\n");
	    result.append("            }\n");
	    result.append("            if (null != value) {\n");
	    result.append("                valueHolder.setValue(value);\n");
	    result.append("            }\n\n");
	    result.append("        }\n\n");
	}
	if (convertibleValueHolderComponents.contains(component)) {
	    result.append("        if (component instanceof ConvertibleValueHolder) {\n");
	    result.append("            if (null != converter) {\n");
	    result.append("                ((ConvertibleValueHolder)component).setConverter(converter);\n");
	    result.append("            }\n");
	    result.append("        }\n\n");
	}

	info = generateOverrideMethodFromAttributes(componentAttributeNames, componentType);
	if (info != null) {
	    result.append(info);
	}

	result.append("    }\n\n");
	
	// generate evaluateExpressions method;
	//
	Map generated = new HashMap();
	generatedEvaluateExpressions = false; 
	result.append("    public void evaluateExpressions() throws JspException {\n");
	info = generateEvaluateExpressions(componentAttributeNames, componentType, generated);
	if (info != null) {
	    result.append(info);
	    generatedEvaluateExpressions = true; 
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
     * NOTE: Most strings are used in expression language evaluation, so there will be
     * a set of two for each ivar (ivar_, ivar).  The exception to this rule is the
     * variable "var".
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
	    attributeClass = getParser().getRendererAttributeClass(type, attributeName);
	    if (attributeClass == null) {
	        attributeClass = getParser().getComponentPropertyClass(type, attributeName);
	    }
	    if (attributeClass == null) {
	        throw new IllegalStateException("Can't find attribute class for type:"+
		    type+" and attribute name:"+attributeName);
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
		
		result.append("    private "+attributeClass+" "+ivar);
		// if it's a primitive
		if (isPrimitive(attributeClass)) {
		    // assign the default value
		    result.append(" = "+(String)defaultPrimitiveValues.get(attributeClass));
		}
		result.append(";\n");
	    }
	    
	    // if it's a String, we need to generate the "<name>_" flavor 
	    // for expression evaluation (later);
	    // Special Condition: Don't generate it for "var" attribute;
	    //
	    if (attributeClass.equals("java.lang.String")) {
		if (!attributeName.equals("var")) {
	            result.append("    private "+attributeClass+" "+ivar+"_;\n");
		    if (evaluateExpressionsAttributes == null) {
		        evaluateExpressionsAttributes = new ArrayList();
		    }
		    evaluateExpressionsAttributes.add(ivar);
		}
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
	    attributeClass = getParser().getRendererAttributeClass(type, attributeName);
	    if (attributeClass == null) {
	        attributeClass = getParser().getComponentPropertyClass(type, attributeName);
	    }
	    if (attributeClass == null) {
	        throw new IllegalStateException("Can't find attribute class for type:"+
		    type+":attribute name:"+attributeName);
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
			  attributeName.substring(1) + "(" + 
			  attributeClass + " " + ivar + ") {\n");
	    
	    // Special Case: "var" is represented as "var" (not "var_");
	    //
	    if (attributeName.equals("var")) {
	        result.append("        this." + ivar + " = " + ivar + ";\n");
	    } else if (attributeClass.equals("java.lang.String")) {
	        result.append("        this." + ivar + "_" + " = " + ivar + ";\n");
	    } else {
	        result.append("        this." + ivar + " = " + ivar + ";\n");
	    }
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

	    if (metaType.equals("renderer")) {
		if (isPrimitive(attributeClass)) {
		    result.append("        if ("+ivar+" != ");
		    result.append(defaultPrimitiveValues.get(attributeClass)+") {\n");
		    result.append("            "+componentType.toLowerCase()+
		        ".getAttributes().put(\""+ivar+"\", ");
		} else {
	            result.append("        if (null != "+ivar+") {\n");
		    if (ivar.equals("bundle")) {
			result.append("            "+componentType.toLowerCase()+
				      ".getAttributes().put(com.sun.faces.RIConstants.BUNDLE_ATTR, ");
		    }
		    else if (ivar.equals("_for")) {
			result.append("            "+componentType.toLowerCase()+
				      ".getAttributes().put(\"for\", ");
		    }
		    else {
			result.append("            "+componentType.toLowerCase()+
				      ".getAttributes().put(\""+ivar+"\", ");
		    }
		}
		if (isPrimitive(attributeClass)) {
		    if (attributeClass.equals("boolean")) {
			result.append(ivar+" ? Boolean.TRUE : Boolean.FALSE);\n");
		    } else {
		        result.append("new "+(String)wrappersForNumbers.get(attributeClass)+
		        "("+ivar+"));\n");
		    }
		} else {
		    result.append(ivar+");\n");
		}
		result.append("        }\n");
	    } else {
	        if (attributeName.equals("id") || 
		    attributeName.equals("componentRef") ||
		    attributeName.equals("rendered") || attributeName.equals("converter") ||
		    attributeName.equals("value") || attributeName.equals("valueRef")) {
		    continue;
		}
	        result.append("        "+componentType.toLowerCase()+".set" + 
		    Character.toUpperCase(attributeName.charAt(0)) +
		    attributeName.substring(1) + "(" + ivar + ");\n");
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

	    if (attributeName.equals("componentRef") ||
	        attributeName.equals("rendered") || attributeName.equals("converter") ||
		attributeName.equals("value") || attributeName.equals("valueRef")) {
		continue;
	    }
	    result.append("        "+componentType.toLowerCase()+".set" + 
	        Character.toUpperCase(attributeName.charAt(0)) +
		attributeName.substring(1) + "(" + ivar + ");\n");
        }	
	return result.toString();
    }

    /**
     * Generates the special "evaluateExpressions" method;
     */
    private String generateEvaluateExpressions(List attributeNames, String type, Map generated) {
        StringBuffer result = new StringBuffer(40000);
	String ivar = null;
	String ivar_ = null;
	String attributeName = null;
	String attributeClass= null;
	for (int i=0; i<attributeNames.size(); i++) {
	    attributeName = (String)attributeNames.get(i);
	    // check if we've already generated the same attribute name
	    //
	    attributeClass = getParser().getComponentPropertyClass(type, attributeName);
	    if (attributeClass == null) {
	        attributeClass = getParser().getRendererAttributeClass(type, attributeName);
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
			" for:"+type);
		}
	    }
	    int idx = evaluateExpressionsAttributes.indexOf(attributeName);
	    if (idx < 0) {
	        continue;
	    }
	    ivar = (String)evaluateExpressionsAttributes.get(idx);
	    ivar_ = ivar+"_";
	    result.append("        if ("+ivar_+" != null) {\n");
	    result.append("            "+ivar+" = Util.evaluateElExpression("+ivar_+", pageContext);\n");
	    result.append("        }\n");
	    generated.put(attributeName, attributeClass);
	}
	return result.toString();
    }
	    
    private String generateTagSupportMethods() {
        StringBuffer result = new StringBuffer(20000);
        result.append("    //\n    // Methods From TagSupport\n    //\n\n");
	result.append("    public int doStartTag() throws JspException {\n");
	result.append("        int rc = 0;\n");
	result.append("        try {\n");
	if (generatedEvaluateExpressions) {
	    result.append("            evaluateExpressions();\n"); 
	    generatedEvaluateExpressions = false;
	}
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
