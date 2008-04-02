/*
 * $Id: GenerateTagBase.java,v 1.1 2003/11/04 18:38:34 rkitain Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.generate;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This class contains common utility methods shared by tag generation
 * subclasses.
 */
public class GenerateTagBase extends GenerateBase {
    //
    // Protected Constants
    //
    
    //
    // Class Variables
    //
    
    // Log instance for this class
    protected static Log log = LogFactory.getLog(GenerateTagBase.class);
    //
    // Instance Variables
    //
    
    // Attribute Instance Variables
    
    // Relationship Instance Variables

    private String tldTopMatter = null;

    private File tldOutputDir = null;

    //
    // Constructors and Initializers    
    //
    
    public GenerateTagBase() {
	super();
    }

    //
    // Class methods
    //
    
    //
    // General Methods
    //

    /**
     * Returns the textual top portion of the tld file.
     */
    protected String getTldTopMatter() {
        return tldTopMatter;
    }


    /**
     * Initialization method - performs file management.
     */
    protected void init(String absolutePathToConfigFile,
	             String absolutePathToEntityDeclarationsFile,
		     String absolutePathToTldTopMatterFile,
		     String absolutePathToTopMatterFile,
		     String absolutePathToTldOutputDir,
		     String absolutePathToOutputDir) throws IllegalStateException {
        super.init(absolutePathToConfigFile, absolutePathToEntityDeclarationsFile,
	    absolutePathToTopMatterFile, absolutePathToOutputDir);

        BufferedReader tldTopMatterReader = null;
	StringBuffer topMatterBuf = new StringBuffer();
	if (null != absolutePathToTldTopMatterFile) {
	    try {
	        tldTopMatterReader = new BufferedReader(
	            new FileReader(absolutePathToTldTopMatterFile));
		if (null != tldTopMatterReader) {
	            String curLine = null;
		    while (null != (curLine = tldTopMatterReader.readLine())) {
		        topMatterBuf.append(curLine+"\n");
		    }
		    tldTopMatterReader.close();
		}
	    } catch (IOException ioe) {
                if (getLog().isErrorEnabled()) {
		    getLog().error("Can't parse config file " + 
				   absolutePathToTldTopMatterFile + " exception: " + 
				   ioe.getClass().getName() + " " + 
				   ioe.getMessage());
		}
		throw new IllegalStateException(ioe.getMessage());
	    }
	    tldTopMatter = topMatterBuf.toString();
	}
	tldOutputDir = new File(absolutePathToTldOutputDir);
	if (!tldOutputDir.isDirectory() || !tldOutputDir.canWrite()) {
	    if (getLog().isErrorEnabled()) {
		getLog().error("Can't write to tld output directory. " + 
			       "Output dir is " + absolutePathToTldOutputDir + ".");
	    }
	    throw new IllegalStateException("Can't write to " + 
					    absolutePathToTldOutputDir);
	}
    }

    /**
     * <p>Using {@link #outputDir} as the base, create the <code>TLD File</code>
     * named by <code>relativeFileName</code>, filling it with content
     * from <code>fileContent</code>. </p>
     */

    protected void writeTldContentsToFile(String fileContent, String relativeFileName) {
	if (null == relativeFileName || null == fileContent || 
	    null == tldOutputDir) {
	    throw new IllegalStateException();
	}
	try {
	    Writer writer = getWriterForTldFile(relativeFileName);
	    writer.write(fileContent);
	    writer.flush();
	    writer.close();
	}
	catch (IllegalStateException ise) {
	    if (getLog().isErrorEnabled()) {
		getLog().error("Can't write class " + 
			       relativeFileName + ". " + ise.getMessage());
	    }
	}
	catch (IOException ioe) {
	    if (getLog().isErrorEnabled()) {
		getLog().error("Can't write class " + 
			       relativeFileName + ". " + ioe.getMessage());
	    }
	}
    }    

    protected Writer getWriterForTldFile(String relativeFileName) throws IllegalStateException, 
        IOException {
	if (null == relativeFileName || null == tldOutputDir) {
	    throw new IllegalStateException();
	}
	Writer result = null;
	File absFile = new File(tldOutputDir, relativeFileName);

	// make any directories we need
	makeDirectories(absFile.getParentFile());
	
	return (new FileWriter(absFile));
    }

    protected Log getLog() {
	return log;
    }

    /**
     * Returns the component type portion of a type name string by using 
     * the renderer type.  For example:
     *     typeName = "CommandButton"; rendererType="Button";
     *     componentType = "Command";
     */
    protected String determineComponentType(String typeName, String rendererType) {
	String result = null;
	if (typeName.equals(rendererType)) {
	    return typeName;
	}
        int rendererTypePosition = typeName.indexOf(rendererType);
	// Just use typeName as the component type
	if (rendererTypePosition <= 0) {
	    return typeName;
	}
	result = typeName.substring(0,rendererTypePosition);
	return result;
    }
    
    /**
     * Returns the renderer type portion of a type name string by using 
     * the component type.  For example:
     *     typeName = "CommandButton"; componentType="Command";
     *     rendererType = "Button";
     */
    protected String determineRendererType(String typeName, String componentType) {
	String result = null;
	if (typeName.equals(componentType)) {
	    return typeName;
	}
        int componentTypePosition = typeName.indexOf(componentType);
	if (componentTypePosition < 0) {
	    throw new IllegalStateException("Can't determine renderer type from component type/"+
		"renderer type combination:"+typeName+" : "+componentType);
	}
	result = typeName.substring(componentTypePosition);
	return result;
    }

} // end of class GenerateTagBase
