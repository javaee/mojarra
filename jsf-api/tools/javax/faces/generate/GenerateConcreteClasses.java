/*
 * $Id: GenerateConcreteClasses.java,v 1.7 2003/10/07 01:45:17 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.generate;

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

    protected Map defaultPrimitiveValues = null;
    
    //
    // Constructors and Initializers    
    //
    
    public GenerateConcreteClasses() {
	super();

	defaultPrimitiveValues = new HashMap(7);
	defaultPrimitiveValues.put("char", "Character.MIN_VALUE");
	defaultPrimitiveValues.put("double", "Double.MIN_VALUE");
	defaultPrimitiveValues.put("float", "Float.MIN_VALUE");
	defaultPrimitiveValues.put("short", "Short.MIN_VALUE");
	defaultPrimitiveValues.put("byte", "Byte.MIN_VALUE");
	defaultPrimitiveValues.put("long", "Long.MIN_VALUE");
	defaultPrimitiveValues.put("int", "Integer.MIN_VALUE");
	defaultPrimitiveValues.put("boolean", "false");
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
     * or more <code>component-class</code> elements.  For each
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
     * <p>call {@link generateClass}, passing the class name, and the
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
	
	// class declaration
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

	// attributes
	while (iter.hasNext()) {
	    attrName = ((String) iter.next()).trim();
	    ivar = generateIvar(attrName);
	    attrClass = ((String) attrs.get(attrName)).trim();
	    // ivar declaration
	    result.append("  protected " + attrClass + " " + ivar);
	    // if it's a primitive
	    if (defaultPrimitiveValues.containsKey(attrClass)) {
		// assign the default value
		result.append(" = " + 
			      (String) defaultPrimitiveValues.get(attrClass));
	    }
	    result.append(";\n");

	    // setter
	    result.append("  public void set" + 
			  Character.toUpperCase(attrName.charAt(0)) +
			  attrName.substring(1) + "(" + 
			  attrClass + " " + ivar + ") {\n");
	    result.append("      this." + ivar + " = " + 
			  ivar + ";\n");
	    result.append("  }\n");
	    if (attrClass.equals("boolean")) {
		getOrIs = is;
	    }
	    else {
		getOrIs = get;
	    }
	    // getter
	    result.append("  public " + attrClass + " " + getOrIs + 
			  Character.toUpperCase(attrName.charAt(0)) +
			  attrName.substring(1) + "() {\n");
	    result.append("      return " + ivar + ";\n");
	    result.append("  }\n\n");
	    
	}
	result.append("}\n");
	return result.toString();
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
