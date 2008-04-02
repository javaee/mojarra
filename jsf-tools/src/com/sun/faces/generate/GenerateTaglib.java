/*
 * $Id: GenerateTaglib.java,v 1.1 2003/10/09 17:01:14 eburns Exp $
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

public class GenerateTaglib extends GenerateBase {
    //
    // Protected Constants
    //
    
    //
    // Class Variables
    //
    
    // Log instance for this class
    protected static Log log = LogFactory.getLog(GenerateTaglib.class);
    //
    // Instance Variables
    //
    
    // Attribute Instance Variables
    
    // Relationship Instance Variables

    //
    // Constructors and Initializers    
    //
    
    public GenerateTaglib() {
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
     * <p>Generate the tag handler java classes for each of the
     * renderers in the config file using the following algorithm.</p>
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

		    /************** PENDING(rogerk): implement this
		    // 
		    // Write the generated class to the outputDir
		    // 
		    fileName = getParser().replaceOccurrences(destClass, ".", 
							      File.separator) + 
			".java";
		    writeContentsToFile(generatedClass, fileName);
		    ************************/
		}
	    }
	}
    }

    public void generateTld() {
    }

    //
    // helpers
    // 

    /**
     *
     * @return the java source code of the generated class, as a String.
     */

    protected String generateClass(String fullyQualifiedDestClass, 
				   String rendererType) {
	StringBuffer result = new StringBuffer(40000);
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
	GenerateTaglib me = new GenerateTaglib();
	me.init(absolutePathToConfigFile, absolutePathToEntityDeclarationsFile,
		absolutePathToTopMatterFile, absolutePathToOutputDir);
	me.generateTld();
	me.generateClasses();
    }


} // end of class GenerateTaglib
