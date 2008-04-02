/*
 * $Id: GenerateBase.java,v 1.1 2003/09/26 14:54:42 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.generate;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.xml.sax.SAXException;

import org.apache.commons.logging.Log;

public abstract class GenerateBase extends Object {
    //
    // Protected Constants
    //
    
    //
    // Class Variables
    //
    
    //
    // Instance Variables
    //
    
    // Attribute Instance Variables
    
    // Relationship Instance Variables

    ConfigParser parser = null;

    String topMatter = null;

    File outputDir = null;

    //
    // Constructors and Initializers    
    //
    
    public GenerateBase() {
	super();
	parser = new ConfigParser();
    }

    //
    // Methods to be used by subclass
    // 

    protected ConfigParser getParser() {
	return parser;
    }

    protected String getTopMatter() {
	return topMatter;
    }

    /**
     * <p>Validate and convert the arguments into our internal
     * representation.  Set the <code>outputDir</code> ivar as a
     * side-effect.  Parse the config file as a side-effect.</p>
     */
			
    protected void init(String absolutePathToConfigFile,
			String absolutePathToTopMatterFile,
			String absolutePathToOutputDir) throws IllegalStateException {
	// parse the config file
	FileReader configReader = null;
	BufferedReader topMatterReader = null;
	
	try {
	    configReader = new FileReader(absolutePathToConfigFile);
	    if (null != absolutePathToTopMatterFile) {
		topMatterReader = 
		    new BufferedReader(new FileReader(absolutePathToTopMatterFile));
	    }
	    outputDir = new File(absolutePathToOutputDir);
	}
	catch (FileNotFoundException e) {
	    if (getLog().isErrorEnabled()) {
		getLog().error("File not found.  Config file is " + 
			       absolutePathToConfigFile + 
			       "Output dir is " + absolutePathToOutputDir + ".");
	    }
	    throw new IllegalStateException(e.getMessage());
	}
	
	if (!outputDir.isDirectory() || !outputDir.canWrite()) {
	    if (getLog().isErrorEnabled()) {
		getLog().error("Can't write to output directory. " + 
			       "Output dir is " + absolutePathToOutputDir + ".");
	    }
	    throw new IllegalStateException("Can't write to " + 
					    absolutePathToOutputDir);
	}

	// read in the topMatter
	if (null != topMatterReader) {
	    StringBuffer topMatterBuf = new StringBuffer();
	    String curLine = null;
	    try {
		while (null != (curLine = topMatterReader.readLine())) {
		    topMatterBuf.append(curLine + "\n");
		}
		topMatterReader.close();
	    }
	    catch (IOException ioe) {
		if (getLog().isErrorEnabled()) {
		    getLog().error("Can't parse config file " + 
				   absolutePathToConfigFile + " exception: " + 
				   ioe.getClass().getName() + " " + 
				   ioe.getMessage());
		}
		throw new IllegalStateException(ioe.getMessage());
	    }
	    topMatter = topMatterBuf.toString();
	}	
	
	// read in and parse the config file
	try {
	    parser.parseConfig(configReader);
	    configReader.close();
	}
	catch (IOException ioe) {
	    if (getLog().isErrorEnabled()) {
		getLog().error("Can't parse config file " + 
			  absolutePathToConfigFile + " exception: " + 
			  ioe.getClass().getName() + " " + 
			  ioe.getMessage());
	    }
	    throw new IllegalStateException(ioe.getMessage());
	}
	catch (SAXException se) {
	    if (getLog().isErrorEnabled()) {
		getLog().error("Can't parse config file " + 
			  absolutePathToConfigFile + " exception: " + 
			  se.getClass().getName() + " " + 
			  se.getMessage());
	    }
	    throw new IllegalStateException(se.getMessage());
	}

    }

    /**
     * <p>Using {@link #outputDir} as the base, create the <code>File</code>
     * named by <code>relativeFileName</code>, filling it with content
     * from <code>fileContent</code>. </p>
     */

    protected void writeContentsToFile(String fileContent, String relativeFileName) {
	if (null == relativeFileName || null == fileContent || 
	    null == outputDir) {
	    throw new IllegalStateException();
	}
	try {
	    Writer writer = getWriterForFile(relativeFileName);
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

    protected Writer getWriterForFile(String relativeFileName) throws IllegalStateException, IOException {
	if (null == relativeFileName || null == outputDir) {
	    throw new IllegalStateException();
	}
	Writer result = null;
	File absFile = new File(outputDir, relativeFileName);

	// make any directories we need
	makeDirectories(absFile.getParentFile());
	
	if (getLog().isInfoEnabled()) {
	    getLog().info("Got FileWriter for " + absFile.toString() + ".");
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

    protected abstract Log getLog();

} // end of class GenerateBase
