/*
 * $Id: GenerateBase.java,v 1.5 2003/09/30 13:52:30 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.generate;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.io.InputStream;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.net.MalformedURLException;
import org.xml.sax.SAXException;
import org.xml.sax.InputSource;
import org.xml.sax.EntityResolver;

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
			String absolutePathToEntityDeclarationsFile,
			String absolutePathToTopMatterFile,
			String absolutePathToOutputDir) throws IllegalStateException {
	// parse the config file
	File 
	    entityFile = null,
	    configFile = null;
	URL 
	    entityUrl = null,
	    configUrl = null;

	BufferedReader topMatterReader = null;
	
	try {
	    configFile = new File(absolutePathToConfigFile);
	    configUrl = configFile.toURL();
	    entityFile = new File(absolutePathToEntityDeclarationsFile);
	    entityUrl = entityFile.toURL();

	    if (null != absolutePathToTopMatterFile) {
		topMatterReader = 
		    new BufferedReader(new FileReader(absolutePathToTopMatterFile));
	    }
	    outputDir = new File(absolutePathToOutputDir);
	}
	catch (IOException e) {
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

	final URL finalEntityUrl = entityUrl;
	parser = new ConfigParser();
	parser.setEntityResolver(new EntityResolver() {
		// Get a hook into the entity resolution system, so we
		// can resolve the standard-html-renderkit-impl entity.
		public InputSource resolveEntity(String publicId,
						 String systemId) throws SAXException, IOException {
		    InputSource result = null;
		    EntityResolver defaultResolver = 
			GenerateBase.this.parser.getDefaultEntityResolver();
		    int lastSlash = -1;
		    String entity = null;
		    boolean tryDefaultResolver = true;
		    
		    // see if we need to resolve this entity on our own.
		    if (null != systemId &&
			(-1 != (lastSlash = systemId.lastIndexOf("/")))) {
			entity = systemId.substring(lastSlash + 1);
			// if the requested systemId is contained in our
			// finalEntityUrl
			if (-1 != 
			    finalEntityUrl.toExternalForm().indexOf(entity)) {
			    // build an input source around it.
			    InputStream localStream = null;
			    try {
				localStream = finalEntityUrl.openStream();
				result = new InputSource(finalEntityUrl.toExternalForm());
				result.setByteStream(localStream);
				tryDefaultResolver = false;
			    }
			    catch (IOException lioe) {
				// fall through to defaultResolver
			    }
			}
		    }

		    if (tryDefaultResolver) {
			result = defaultResolver.resolveEntity(publicId, 
							       systemId);
		    }
		    return result;
		}
	    });
	
	// read in and parse the config file
        InputStream stream = null;
        InputSource source = null;
	try {
	    stream = configUrl.openStream();
	    source = new InputSource(configUrl.toExternalForm());
	    source.setByteStream(stream);
	    parser.parseConfig(source);
	    
	    try {
		if (stream != null) {
		    stream.close();
		}
	    } catch (Exception e) {
		if (getLog().isWarnEnabled()) {
		    getLog().warn(e.getMessage(), e);
		}
	    }    
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

    protected static String [] keywords = {
	"for"
    };

    /**
     * <p>generate a Java Language Identifier given the argument
     * <code>ivar</code>. </p>
     */

    protected String generateIvar(String ivar) {
	String result = ivar;

	for (int i = 0, len = keywords.length; i < len; i++) {
	    if (keywords[i].equals(ivar)) {
		result = "_" + ivar;
	    }
	}

	return result;
    }

    protected abstract Log getLog();

} // end of class GenerateBase

class LocalEntityResolver extends Object implements EntityResolver {
    GenerateBase base = null;

    LocalEntityResolver(GenerateBase yourBase) {
	base = yourBase; // are belong to us.
    }

    public InputSource resolveEntity(String publicId,
				     String systemId) throws SAXException, IOException {
	System.out.println("publicId: " + publicId + " systemId: " + systemId);
	return base.parser.getDefaultEntityResolver().resolveEntity(publicId,
								    systemId);
    }

}
