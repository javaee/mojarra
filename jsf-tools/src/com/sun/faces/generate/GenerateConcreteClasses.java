/*
 * $Id: GenerateConcreteClasses.java,v 1.7 2003/11/18 14:36:02 rlubke Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.generate;

import java.io.File;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class GenerateConcreteClasses extends GenerateBase {
    //
    // Protected Constants
    //
    
    //
    // Class Variables
    //
    
    // Log instance for this class
    protected static Log log = LogFactory.getLog(GenerateConcreteClasses.class);
    //
    // Instance Variables
    //
    
    // Attribute Instance Variables
    
    // Relationship Instance Variables

    //
    // Constructors and Initializers    
    //
    
    public GenerateConcreteClasses() {
	super();
    }

    //
    // Class methods
    //
    
    //
    // General Methods
    //

    /**
     *
     * <p>Generate the java classes for each of the renderers in the
     * config file using the following algorithm.</p> 
     *
     * <p>According to the DTD, each <code>renderer</code> may have one
     * or more <code>supported-component-class</code> elements.  Each 
     * <code>supported-component-class</code> element contains a 
     * <code>component-class</code> element.  For each
     * <code>renderer</code> in the config file:</p>
     *
     * <ul>
     *
     * <p>for each <code>component-class</code> element.</p>
     *
     * <ul>
     *
     * <p>use the value of the component class as the name of the class
     * to generate.</p>
     *
     * <p>call {@link #generateClass}, passing the class name, and the
     * renderer type</p>
     *
     * </ul>

     * </ul>
     */

    public void generateClasses() {
	String 
	    rendererType = null,
	    destClass = null,
	    generatedClass = null,
	    fileName = null;
	int 
	    lastDot = 0;
	Iterator 
	    classIter = null,
	    rendererIter = getParser().getRendererTypes();

	// for each renderer in the renderkit
	while (rendererIter.hasNext()) {
	    rendererType = (String) rendererIter.next();
	    classIter = getParser().getClassesForRendererType(rendererType).iterator();

	    // for each component-class element within the current
	    // renderer
	    while (classIter.hasNext()) {
		destClass = (String) classIter.next();

		// generate a class.
		if (null != destClass && 
		    null != (generatedClass = 
			     generateClass(destClass, rendererType))) {
		    // 
		    // Write the generated class to the outputDir
		    // 
		    fileName = getParser().replaceOccurrences(destClass, ".", 
							      File.separator) + 
			".java";
		    writeContentsToFile(generatedClass, fileName);
		}
	    }
	}
    }

    //
    // helpers
    // 

    /**
     * <p>Generate a concrete class named by the argument
     * <code>fullyQualifiedDestClass</code>.  Derive the base class for
     * this generated class from the argument.</p>
     *
     * <p>This method makes the following assumptions about the
     * <code>fullyQualifiedDestClass</code> argument.</p>
     *
     * <ul>
     *
     * <p>it is a fully qualified class name</p>
     *
     * <p>the first four characters after the last occurrence of '.' in
     * the string are "Html".  If not, this method returns null.</p>
     *
     * <p>it ends with <code>rendererType</code>.  If not, this method
     * returns null.</p>
     *
     * <p>The characters between "Html" and <code>rendererType</code>
     * are the component type for this class.</p>
     *
     * <p>The result of prepending <code>javax.faces.component.UI</code>
     * to the component type is the super-class for this generated
     * class.</p>
     *
     * </ul>
     *
     * @return the java source code of the generated class, as a String.
     */

    protected String generateClass(String fullyQualifiedDestClass, 
				   String rendererType) {
	StringBuffer result = new StringBuffer(40000);
	Map attrs = null;
	Iterator iter = null;
	String 
	    fullyQualifiedSrcClass = null,
	    componentType = null,
	    topMatter = null,
	    destClass = null,
	    attrName = null,
	    attrClass = null;
	int 
	    rendererIndex,
	    lastDot = fullyQualifiedDestClass.lastIndexOf(".");
	
	destClass = fullyQualifiedDestClass.substring(lastDot + 1);

	// verify the Html condition
	if (!("Html".equals(fullyQualifiedDestClass.substring(lastDot + 1, 
							      lastDot + 5)))){
	    return null;
	}
	
	if (!(fullyQualifiedDestClass.endsWith(rendererType))) {
	    return null;
	}
	rendererIndex = fullyQualifiedDestClass.indexOf(rendererType);

	// pull out the component type from the name.
	componentType = 
	    fullyQualifiedDestClass.substring(lastDot + 5, rendererIndex);
	// special case, deal with the case where the componentType and
	// the rendererType are the same.
	if (rendererIndex == (lastDot + 5)) {
	    componentType = rendererType;
	}
	
	fullyQualifiedSrcClass = "javax.faces.component.UI" + componentType;
	
	if (getLog().isInfoEnabled()) {
	    getLog().info("Generating " + fullyQualifiedDestClass + 
			  " from meta-data.");
	}
	if (null != (topMatter = getTopMatter())) {
	    result.append(topMatter);
	    result.append("\n\n");
	}
	
	// package declaration
	result.append("package " + 
		      fullyQualifiedDestClass.substring(0, lastDot) + ";\n\n");

	// imports
	result.append("import javax.faces.context.FacesContext;\n");
	result.append("import javax.faces.el.ValueBinding;\n");
	result.append("import java.io.IOException;\n\n");
	
	// class declaration
    result.append("/**\n");
    result.append(" * <p>The default <code>rendererType</code> for this class\n");
    result.append(" * is set to <code>").append(rendererType).append("</code>.");
    result.append("  This value can be changed by\n * calling the {@link");
    result.append(" #setRendererType} method.</p>\n");
    result.append(" */\n");
	result.append("public class " + destClass + 
		      " extends " + fullyQualifiedSrcClass + 
		      " {\n\n");
	
	// constructor
	result.append("  public " + destClass + "() {\n");
	result.append("    super();\n");
	result.append("    setRendererType(\"" + rendererType + "\");\n");
	result.append("  }\n\n");
	
	attrs = getParser().getAttributesForRenderer(rendererType);
	iter = attrs.keySet().iterator();
	String 
	    ivar = null,
	    getOrIs = null,
	    is = "is",
	    get = "get";
	boolean isPrimitive = false;

	// attributes
	while (iter.hasNext()) {
	    attrName = ((String) iter.next()).trim();
	    ivar = generateIvar(attrName);
	    attrClass = getParser().getRendererAttributeClass(rendererType,attrName).trim();
	    // ivar declaration
	    result.append("  private " + attrClass + " " + ivar);
	    // if it's a primitive
	    if (isPrimitive = isPrimitive(attrClass)) {
		// assign the default value
		result.append(" = " + 
			      (String) defaultPrimitiveValues.get(attrClass) +
			      ";\n");
		result.append("  private boolean " + ivar + 
			      "Set = false");
	    }
	    result.append(";\n");

	    // setter
        result.append("\n  /**\n");
        result.append("   * <p>Sets the value of the <code>");
        result.append(attrName).append("</code> property.</p>\n");
        result.append("   *\n");
        result.append("   * @param ").append(ivar);
        result.append(" the new value for the <code>").append(attrName);
        result.append("</code> property\n");
        result.append("   */\n");
	    result.append("  public void set" + 
			  Character.toUpperCase(attrName.charAt(0)) +
			  attrName.substring(1) + "(" + 
			  attrClass + " " + ivar + ") {\n");
	    result.append("      this." + ivar + " = " + 
			  ivar + ";\n");
	    if (isPrimitive) {
		result.append("      this." + ivar + "Set = true;\n");
	    }
	    result.append("  }\n");
	    if (attrClass.equals("boolean")) {
		getOrIs = is;
	    }
	    else {
		getOrIs = get;
	    }
	    // getter
        result.append("\n  /**\n");
        result.append("   * <p>Returns the value of the <code>");
        result.append(attrName).append("</code> property.</p>\n");
        result.append("   */\n");
	    result.append("  public " + attrClass + " " + getOrIs + 
			  Character.toUpperCase(attrName.charAt(0)) +
			  attrName.substring(1) + "() {\n");
	    if (isPrimitive) {
		result.append("      if (this." + ivar + "Set) {\n");
		result.append("          return (this." + ivar + ");\n");
		result.append("      }\n");
		result.append("      ValueBinding vb = null;\n");
		result.append("      " + attrClass + " result = " + 
			      (String) defaultPrimitiveValues.get(attrClass) +
			      ";\n");
		result.append("      if (null != (vb = getValueBinding(\"" +
			      ivar + "\"))) {\n");
		result.append("          result = ((" +
			      (String) wrappersForNumbers.get(attrClass) +
			      ") vb.getValue(getFacesContext()))." + 
			      attrClass + "Value();\n");
		result.append("      }\n");
 		result.append("      return result;\n");
	    }
	    else {
		result.append("      if (null != this." + ivar + ") {\n");
		result.append("          return (this." + ivar + ");\n");
		result.append("      }\n");
		result.append("      ValueBinding vb = null;\n");
		result.append("      if (null != (vb = getValueBinding(\"" +
			      ivar + "\"))) {\n");
		result.append("          return ((String) vb.getValue(getFacesContext()));\n");
		result.append("      }\n");
 		result.append("      return null;\n");
	    }
	    result.append("  }\n\n");
	}

	//
	// StateHolder
	//
	if (0 < attrs.keySet().size()) {
	    generateStateHolder(result, rendererType, attrs);
	}

	result.append("}\n");
	return result.toString();
    }

    protected void generateStateHolder(StringBuffer result, String rendererType,
				       Map attrs) {
	int 
	    curAttr = 1,
	    numAttrs = 0;
	Iterator iter = null;
	String
	    attrName = null,
	    ivar = null,
	    attrClass = null;

	// 
	// saveState
	// 
	curAttr = 1;
	numAttrs= attrs.keySet().size() + 1; // + 1 for super.state,
					     // saved in values[0]
	StringBuffer fillOutTheArray = new StringBuffer();
	fillOutTheArray.append("    values[0] = super.saveState(context);\n");
	// generate the state saving for each attribute
	iter = attrs.keySet().iterator();
	while (iter.hasNext()) {
	    attrName = ((String) iter.next()).trim();
	    ivar = generateIvar(attrName);
	    attrClass = getParser().getRendererAttributeClass(rendererType,attrName).trim();
	    // if it's a primitive
	    if (isPrimitive(attrClass)) {
		// wrap it
		if (attrClass.equals("boolean")) {
		    fillOutTheArray.append("    values[" + curAttr + "] = " + ivar+ 
				  " ? Boolean.TRUE : Boolean.FALSE;\n");
		}
		else {
		    fillOutTheArray.append("    values[" + curAttr + "] = new " +
				  (String) wrappersForNumbers.get(attrClass) +
				  "(" + ivar + ");\n");
		}
		// save the "ivarSet" ivar.
		curAttr++;
		fillOutTheArray.append("    values[" + curAttr + "] = " + ivar + 
			      "Set ? Boolean.TRUE : Boolean.FALSE;\n");
	    }
	    else if (attrClass.equals("String") || 
		     attrClass.equals("java.lang.String")) {
		// if it's a string
		fillOutTheArray.append("    values[" + curAttr + "] = " + 
			      ivar + ";\n");
	    }
	    else {
		throw new IllegalStateException("Can't generate state saving for attribute named " + ivar + " of type " + attrClass + ".");
	    }
	    curAttr++;
	}
	result.append("  public Object saveState(FacesContext context) {\n");
	result.append("    Object [] values = new Object[" + curAttr + "];\n");
	result.append(fillOutTheArray);
	result.append("    return (values);\n");
	result.append("  }\n");

	result.append("\n\n");
	
	// 
	// restoreState
	//
	curAttr = 1;
	numAttrs= attrs.keySet().size() + 1; // + 1 for super.state,
					     // saved in values[0]
	result.append("  public void restoreState(FacesContext context, Object state) {\n");
	result.append("    Object [] values = (Object []) state;\n");
	result.append("    super.restoreState(context, values[0]);\n");
	// generate the state restoring for each attribute
	iter = attrs.keySet().iterator();
	while (iter.hasNext()) {
	    attrName = ((String) iter.next()).trim();
	    ivar = generateIvar(attrName);
	    attrClass = getParser().getRendererAttributeClass(rendererType,attrName).trim();
	    // if it's a primitive
	    if (isPrimitive(attrClass)) {
		// un-wrap it
		if (attrClass.equals("boolean")) {
		    result.append("    " + ivar + " = ((Boolean) values[" + 
				  curAttr + "]).booleanValue();\n");
		}
		else {
		    result.append("    " + ivar + " = ((" + 
				  (String) wrappersForNumbers.get(attrClass) +
				  ") values[" + curAttr + 
				  "])." + attrClass + "Value();\n");
		}
		curAttr++;
		result.append("    " + ivar + "Set = ((Boolean) values[" + 
			      curAttr + "]).booleanValue();\n");
	    }
	    else if (attrClass.equals("String") || 
		     attrClass.equals("java.lang.String")) {
		// if it's a string
		result.append("    " + ivar + " = (String) values[" + 
			      curAttr + "];\n");
	    }
	    else {
		throw new IllegalStateException("Can't generate state saving for attribute named " + ivar + " of type " + attrClass + ".");
	    }
	    curAttr++;
	}
	result.append("  }\n");

    }

    protected Log getLog() {
	return log;
    }

    // 
    // Main
    //


    public static void main(String [] args) {
	String 
	    absolutePathToEntityDeclarationsFile = null,
	    absolutePathToTopMatterFile = null,
	    absolutePathToConfigFile = null,
	    absolutePathToOutputDir = null;

	// parse the arguments
	try {
	    for (int i = 0, len = args.length; i < len; i++) {
		if (args[i].equals("-d")) {
		    absolutePathToOutputDir = args[++i];
		}
		else if (args[i].equals("-f")) {
		    absolutePathToConfigFile = args[++i];
		}
		else if (args[i].equals("-e")) {
		    absolutePathToEntityDeclarationsFile = args[++i];
		}
		else if (args[i].equals("-c")) {
		    absolutePathToTopMatterFile = args[++i];
		}
	    }
	}
	catch (ArrayIndexOutOfBoundsException e) {
	    System.out.println("Error parsing arguments: " + e.getMessage());
	    return;
	}

	// generate the classes
	GenerateConcreteClasses me = new GenerateConcreteClasses();
	me.init(absolutePathToConfigFile, absolutePathToEntityDeclarationsFile,
		absolutePathToTopMatterFile, absolutePathToOutputDir);
	me.generateClasses();
    }


} // end of class GenerateConcreteClasses
