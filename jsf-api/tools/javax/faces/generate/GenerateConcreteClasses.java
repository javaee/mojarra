/*
 * $Id: GenerateConcreteClasses.java,v 1.4 2003/09/29 14:57:45 eburns Exp $
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
     * <p>This method applys the following algorithm to derive the
     * <code>destClass</code> from each of the elements in
     * <code>absoluteClassNames</code>.</p>
     *
     * <ul>
     *
     * <p>Consider the sample className
     * <code>javax.faces.component.UICommand</code></p>
     *
     * <p>Find the last dot in className.</p>
     * 
     * <p>Assume the next two chars after the last dot can be thrown
     * out.</p>
     *
     * <p>Save the rest of the string to a var, componentType.  In this
     * case, it's <code>command</code>.</p>
     *
     * <p>Generated class name is <code>className.substring(0, lastDot)
     * + "." + "html.Html" + componentType + &lt;rendererType&gt;</code>,
     * where &lt;rendererType&gt; is each of the renderers that supports
     * that component, as specified in the config file.</p>
     *
     * <p>So for the <code>Button</code> renderer type, you'd get
     * <code>javax.faces.component.html.HtmlCommandButton</code>.</p>
     *
     * </ul>
     */

    public void generateClasses(List absoluteClassNames) {
	String 
	    componentType = null,
	    rendererType = null,
	    destRoot = null,
	    sourceClass = null,
	    destClass = null,
	    generatedClass = null,
	    fileName = null;
	int 
	    lastDot = 0;
	List rendererTypes = null;

	for (int i = 0, len = absoluteClassNames.size(); i < len; i++) {
	    //
	    // Build up the "destRoot" local varible
	    // 
	    sourceClass = (String) absoluteClassNames.get(i);
	    rendererTypes = 
		getParser().getRendererTypesForClass(sourceClass);
	    lastDot = sourceClass.lastIndexOf(".");
	    componentType = sourceClass.substring(lastDot + 3);
	    destRoot = sourceClass.substring(0, lastDot + 1) + "html.Html" + 
		componentType;

	    //
	    // For each rendererType, generate a class
	    // 
	    for (int j = 0, jLen = rendererTypes.size(); j < jLen; j++){
		rendererType = (String) rendererTypes.get(j);
		// special case, if the renderer type and the component
		// type are the same, 
		if (rendererType.equals(componentType)) {
		    // omit the renderer type from the class name.
		    destClass = destRoot;
		}
		else {
		    destClass = destRoot + rendererType;
		}
		generatedClass = generateClass(destClass, rendererType,
					       sourceClass);
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

    //
    // helpers
    // 

    /**
     * <p>Generate a concrete class named by the argument
     * <code>fullyQualifiedDestClass</code>, that is a subclass of
     * <code>fullyQualifiedSrcClass</code>.</p>
     *
     * @return the java source code of the generated class, as a String.
     */

    protected String generateClass(String fullyQualifiedDestClass, 
				   String rendererType,
				   String fullyQualifiedSrcClass) {
	StringBuffer result = new StringBuffer(40000);
	Map attrs = null;
	Iterator iter = null;
	String 
	    topMatter = null,
	    destClass = null,
	    attrName = null,
	    attrClass = null;
	int lastDot = fullyQualifiedDestClass.lastIndexOf(".");
	destClass = fullyQualifiedDestClass.substring(lastDot + 1);

	if (getLog().isInfoEnabled()) {
	    getLog().info("Generating " + 
			  fullyQualifiedDestClass + " from\n" + 
			  fullyQualifiedSrcClass + 
		     " + meta-data.");
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
	
	while (iter.hasNext()) {
	    attrName = ((String) iter.next()).trim();
	    ivar = generateIvar(attrName);
	    attrClass = ((String) attrs.get(attrName)).trim();
	    result.append("  protected " + attrClass + " " + ivar + ";\n");

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
	    absolutePathToTopMatterFile = null,
	    absolutePathToConfigFile = null,
	    absolutePathToOutputDir = null;
	List sourceClasses = new ArrayList();

	// parse the arguments
	try {
	    for (int i = 0, len = args.length; i < len; i++) {
		if (args[i].equals("-d")) {
		    absolutePathToOutputDir = args[++i];
		}
		else if (args[i].equals("-f")) {
		    absolutePathToConfigFile = args[++i];
		}
		else if (args[i].equals("-c")) {
		    absolutePathToTopMatterFile = args[++i];
		}
		else {
		    sourceClasses.add(args[i]);
		}
	    }
	}
	catch (ArrayIndexOutOfBoundsException e) {
	    System.out.println("Error parsing arguments: " + e.getMessage());
	    return;
	}

	// generate the classes
	GenerateConcreteClasses me = new GenerateConcreteClasses();
	me.init(absolutePathToConfigFile, absolutePathToTopMatterFile,
		absolutePathToOutputDir);
	me.generateClasses(sourceClasses);
    }


} // end of class GenerateConcreteClasses
