/*
 * $Id: GenerateConcreteClasses.java,v 1.1 2003/09/25 22:22:05 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.generate;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class GenerateConcreteClasses extends Object {
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

    ConfigParser parser = null;

    File outputDir = null;
    
    //
    // Constructors and Initializers    
    //
    
    public GenerateConcreteClasses() {
	super();
	parser = new ConfigParser();
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

    public void generateClasses(String absolutePathToConfigFile,
				String absolutePathToOutputDir,
				List absoluteClassNames) {
	String 
	    componentType = null,
	    rendererType = null,
	    destRoot = null,
	    sourceClass = null,
	    destClass = null,
	    generatedClass = null;
	int 
	    lastDot = 0;
	List rendererTypes = null;

	try {
	    validateArgumentsAndParseConfig(absolutePathToConfigFile, 
					    absolutePathToOutputDir,
					    absoluteClassNames);
	}
	catch (Exception e) {
	    if (log.isErrorEnabled()) {
		log.error("Can't validate arguments.");
	    }
	    return;
	}
	
	for (int i = 0, len = absoluteClassNames.size(); i < len; i++) {
	    sourceClass = (String) absoluteClassNames.get(i);
	    rendererTypes = 
		parser.getRendererTypesForClass(sourceClass);
	    lastDot = sourceClass.lastIndexOf(".");
	    componentType = sourceClass.substring(lastDot + 3);
	    destRoot = sourceClass.substring(0, lastDot + 1) + "html.Html" + 
		componentType;
	    for (int j = 0, jLen = rendererTypes.size(); j < jLen; j++){
		rendererType = (String) rendererTypes.get(j);
		destClass = destRoot + rendererType;
		generatedClass = generateClass(destClass, rendererType,
					       sourceClass);
		try {
		    writeClass(destClass, generatedClass);
		}
		catch (IllegalStateException ise) {
		    if (log.isErrorEnabled()) {
			log.error("Can't write class " + destClass + ". " + 
				  ise.getMessage());
		    }
		    return;
		}
		catch (IOException ioe) {
		    if (log.isErrorEnabled()) {
			log.error("Can't write class " + destClass + ". " + 
				  ioe.getMessage());
		    }
		    return;
		}
	    }
	}
    }

    //
    // helpers
    // 

    /**
     * <p>Generate a concrete class named by the argument
     * <code>destClass</code>, that is a subclass of
     * <code>srcClass</code>.</p>
     *
     * @return the java source code of the generated class, as a String.
     */

    protected String generateClass(String destClass, String rendererType,
				   String srcClass) {
	StringBuffer result = new StringBuffer(40000);
	if (log.isInfoEnabled()) {
	    log.info("Generating " + destClass + " from\n" + srcClass + 
		     " + meta-data.");
	}
	int lastDot = destClass.lastIndexOf(".");
	result.append("package " + destClass.substring(0, lastDot) + ";\n\n");
	result.append("public class " + destClass.substring(lastDot + 1) + 
		      " extends " + srcClass + 
		      " {\n");
	Map attrs = parser.getAttributesForRenderer(rendererType);
	Iterator iter = attrs.keySet().iterator();
	String 
	    attrName = null,
	    attrClass = null;
	
	while (iter.hasNext()) {
	    attrName = (String) iter.next();
	    attrClass = (String) attrs.get(attrName);
	    result.append("  protected " + attrClass + " " + attrName + ";\n");
	    result.append("  public void set" + 
			  Character.toUpperCase(attrName.charAt(0)) +
			  attrName.substring(1) + "(" + 
			  attrClass + " " + attrName + ") {\n");
	    result.append("      this." + attrName + " = " + attrName + ";\n");
	    result.append("  }\n");
	    result.append("  public " + attrClass + " get" + 
			  Character.toUpperCase(attrName.charAt(0)) +
			  attrName.substring(1) + "() {\n");
	    result.append("      return " + attrName + ";\n");
	    result.append("  }\n\n");
	    
	}
	result.append("}\n");
	return result.toString();
    }

    /**
     * <p>Validate and convert the arguments into our internal
     * representation.  Set the <code>outputDir</code> ivar as a
     * side-effect.  Parse the config file as a side-effect.</p>
     */

    protected void validateArgumentsAndParseConfig(String absolutePathToConfigFile,
						   String absolutePathToOutputDir,
						   List absoluteClassNames) throws Exception {
	// parse the config file
	FileInputStream configStream = null;

	try {
	    configStream = new FileInputStream(absolutePathToConfigFile);
	    outputDir = new File(absolutePathToOutputDir);
	}
	catch (FileNotFoundException e) {
	    if (log.isErrorEnabled()) {
		log.error("Can't open file.  Config file is " + 
			  absolutePathToConfigFile + 
			  "Output dir is " + absolutePathToOutputDir + ".");
	    }
	    if (null != configStream) {
		configStream.close();
	    }
	    throw e;
	}
	
	if (!outputDir.isDirectory() || !outputDir.canWrite()) {
	    if (log.isErrorEnabled()) {
		log.error("Can't write to output directory. " + 
			  "Output dir is " + absolutePathToOutputDir + ".");
	    }
	    configStream.close();
	    throw new FileNotFoundException();
	}

	if (absoluteClassNames.size() <= 0) {
	    if (log.isErrorEnabled()) {
		log.error("No Classes to generate.");
	    }

	    throw new IllegalStateException();
	}
	try {
	    parser.parseConfig(configStream);
	}
	catch (Exception parseException) {
	    if (log.isErrorEnabled()) {
		log.error("Can't parse config file " + 
			  absolutePathToConfigFile + " exception: " + 
			  parseException.getClass().getName() + " " + 
			  parseException.getMessage());
	    }
	    configStream.close();
	    throw parseException;
	}
    }

    /**
     * <p>Using <code>outputDir</code> as the base, create the class
     * named by <code>absoluteClassName</code>, filling it with content
     * from <code>classContent</code>. </p>
     */

    protected void writeClass(String absoluteClassName,
			      String classContent) throws IllegalStateException, IOException {
	if (null == absoluteClassName || null == classContent || 
	    null == outputDir) {
	    throw new IllegalStateException();
	}

	Writer writer = getWriterForClass(absoluteClassName);
	writer.write(classContent);
	writer.flush();
	writer.close();
    }

    protected Writer getWriterForClass(String absoluteClassName) throws IllegalStateException, IOException {
	if (null == absoluteClassName || null == outputDir) {
	    throw new IllegalStateException();
	}
	Writer result = null;
	String fileName = ConfigParser.replaceOccurrences(absoluteClassName,
							  ".", File.separator)+
	    ".java";
	File absFile = new File(outputDir, fileName);

	// make any directories we need
	makeDirectories(absFile.getParentFile());

	if (log.isInfoEnabled()) {
	    log.info("Writing file " + absFile.toString() + ".");
	}

	return (new FileWriter(absFile));
    }

    protected void makeDirectories(File dir) {
	if (null == dir) {
	    return;
	}
	File parentFile = null;
	if (null != (parentFile = dir.getParentFile())) {
	    if (!parentFile.exists()) {
		makeDirectories(parentFile);
		parentFile.mkdir();
	    }
	    else {
		dir.mkdir();
	    }
	}
	return;
    }

    // 
    // Main
    //


    public static void main(String [] args) {
	String 
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
		else {
		    sourceClasses.add(args[i]);
		}
	    }
	}
	catch (ArrayIndexOutOfBoundsException e) {
	    System.out.println("Error parsing arguments: " + e.getMessage());
	    return;
	}
	if (log.isInfoEnabled()) {
	    log.info("Config file is " + absolutePathToConfigFile + ".");
	    log.info("Output dir is " + absolutePathToOutputDir + ".");
	    for (int i = 0, len = sourceClasses.size(); i < len; i++) {
		log.info("Source class " + sourceClasses.get(i));
	    } 
	}

	// generate the classes
	GenerateConcreteClasses me = new GenerateConcreteClasses();
	me.generateClasses(absolutePathToConfigFile,
			   absolutePathToOutputDir, 
			   sourceClasses);
    }


} // end of class GenerateConcreteClasses
