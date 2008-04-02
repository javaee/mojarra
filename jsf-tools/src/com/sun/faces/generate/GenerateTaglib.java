/*
 * $Id: GenerateTaglib.java,v 1.5 2003/11/11 00:02:33 eburns Exp $
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


/**
 * This class is used to generate a tag library descriptor (tld) file and corresponding
 * tag class handlers from renderer/component definitions defined in a faces configuration
 * file.
 */
public class GenerateTaglib extends GenerateTagBase {
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

    private TaglibGenerator tagLibGenerator = null;

    // Seperate buffer for building "overrideProperties" method
    //
    private StringBuffer overridePropertiesBuf = null;

    //
    // Constructors and Initializers    
    //
    
    public GenerateTaglib(TaglibGenerator tagLibGenerator) {
	super();
	this.tagLibGenerator = tagLibGenerator;
    }

    //
    // Class methods
    //
    
    //
    // General Methods
    //

    public void init(String absolutePathToConfigFile,
		     String absolutePathToEntityDeclarationsFile,
		     String absolutePathToTldTopMatterFile,
		     String absolutePathToTopMatterFile,
		     String absolutePathToTldOutputDir,
		     String absolutePathToOutputDir) throws IllegalStateException {
        super.init(absolutePathToConfigFile, absolutePathToEntityDeclarationsFile,
	    absolutePathToTldTopMatterFile, absolutePathToTopMatterFile,
	    absolutePathToTldOutputDir, absolutePathToOutputDir);
	tagLibGenerator.setParser(getParser());
    }

    /**
     *
     * <p>Generate the tag handler definition (tld) file.</p> 
     *
     * <p>According to the DTD, each <code>renderer</code> may have one
     * or more <code>component-type</code> elements.  For each
     * <code>renderer</code> in the config file:</p>
     *
     * <ul>
     *
     * <p>for each <code>component-type</code> element.</p>
     *
     * <ul>
     *
     * <p>use the value of the component type in conjunction with the renderer
     * type as the name of tag entry.</p> 
     *
     * </ul>
     */
    public void generateTld() {
	String generatedTld = null;
	String fileName = null;

	//
	// Write the generated tld to the output dir.
	//
	generatedTld = generateTldContents();
        if (null != generatedTld) {
            fileName = getParser().replaceOccurrences("html_basic", ".", 
                File.separator) + ".tld";
            writeTldContentsToFile(generatedTld, fileName);
        }
    }

    /**
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
            componentType = null,
            rendererType = null,
            tagName = null,
            tagClass = null,
            tagClassPath = null,
            generatedClass = null,
            fileName = null;
        int
            lastDot = 0;
        Iterator
            rendererIter = getParser().getRendererTypes();
        // for each renderer in the renderkit
        while (rendererIter.hasNext()) {
            rendererType = (String) rendererIter.next();
            // For each component type...
            //
            Iterator componentTypes = getParser().getComponentTypes(rendererType);

            // for each component-type element within the current
            // renderer
            while (componentTypes.hasNext()) {
                componentType = (String) componentTypes.next();
                tagName = determineTagName(componentType, rendererType)+"Tag";
		tagClassPath = tagLibGenerator.getTagClassPath(tagName); 
                tagClass = tagClassPath+"."+tagName;

                // generate a class.
                if (null != componentType &&
                    null != (generatedClass =
                             generateClass(componentType, rendererType, tagName,
                             tagClassPath))) {

                    //
                    // Write the generated class to the outputDir
                    //
                    fileName = getParser().replaceOccurrences(tagClass, ".",
                                                              File.separator) +
                        ".java";
                    writeContentsToFile(generatedClass, fileName);
                }
            }
        }
	generateRemainingComponentClasses();
    }

    /** 
     * Generate the tld contents.
     */
    private String generateTldContents() {
	String tagName = null;
	String tagClass = null;
	String rendererType = null;
	String componentType = null;

	StringBuffer result = new StringBuffer(80000);

        result = generateTldTopPart(result);
	
	// For each renderer...
	//
	Iterator rendererTypes = getParser().getRendererTypes();
        while (rendererTypes.hasNext()) {
	    rendererType = (String)rendererTypes.next();
	    
	    // For each component type...
	    //
	    Iterator componentTypes = getParser().getComponentTypes(rendererType);
	    while (componentTypes.hasNext()) {
		componentType = (String)componentTypes.next();
	        tagName = determineTagName(componentType, rendererType);
	        if (getLog().isInfoEnabled()) {
	            getLog().info("Generating " + tagName + 
		        " from meta-data.");
	        }
		
		tagClass = tagLibGenerator.getTagClassPath(tagName)+"."+tagName+"Tag";

		result = generateTldTagInfo(result, tagName, tagClass, 
		    tagLibGenerator.getTeiClass(tagName), tagLibGenerator.getBodyContent(tagName));

                // get supported attribute names list (if any) for the renderer
	        //    If there are any, then use them to get the renderer atribute 
		//    information
	        //
		// Used to keep track so we don't generate the same attribute
		// more than once.  Key: attr name  Value: attr type
		//
		Map alreadyGenerated = new HashMap();
	        List attributeNames = getParser().getSupportedAttributeNames(
	            componentType, rendererType);
		if (attributeNames.isEmpty()) {
		    Iterator iter = getParser().getAttributesForRenderer(rendererType).
			keySet().iterator();
		    attributeNames = new ArrayList();
		    while (iter.hasNext()) {
		        attributeNames.add(iter.next());
		    }
		}
		//
                // Generate TLD attributes from renderer attributes
		//
	        if (!attributeNames.isEmpty()) {
		    result = generateTldRendererAttributes(result, rendererType,
		        tagName, attributeNames, alreadyGenerated);
		}
		//
                // Generate TLD attributes from component attributes...
                //
		Iterator iter = getParser().getAttributesForComponent(
		    componentType).keySet().iterator();
		attributeNames = new ArrayList();
		while (iter.hasNext()) {
		    attributeNames.add((String)iter.next());
		}

		if (!attributeNames.isEmpty()) {
		    result = generateTldComponentAttributes(result, componentType,
		    tagName, attributeNames, alreadyGenerated);
		}
		result.append("  </tag>\n\n");
            }
	}

	// Generate any remaining components that are not associated with a renderer
	//
	result = generateRemainingComponentTags(result);

	result.append("</taglib>");
	return result.toString();
    }

    /**
     * Generate a tag handler class based on the component type/renderer type combinaton.
     */
    private String generateClass( String componentType, String rendererType,
        String tagName, String tagClassPath) {
        String topMatter = null;

        StringBuffer result = new StringBuffer(40000);

        String tagClass = tagClassPath+"."+tagName;

        if (getLog().isInfoEnabled()) {
            getLog().info("Generating " + tagClass+ " from meta-data.");
        }
        if (null != (topMatter = getTopMatter())) {
            result.append(topMatter);
            result.append("\n\n");
        }

        // package declaration
        result.append("package "+tagClassPath+";\n\n");

        //imports
	result.append(tagLibGenerator.getImportInfo()+"\n\n");

        // class declaration
	result.append(tagLibGenerator.getClassDeclaration(tagName)+"\n\n");

	// log declaration
	result.append("public static Log log = LogFactory.getLog("+tagName+".class);\n\n");

	// get renderer attribute names.  First, see if there are any renderer
	// attribute names under the "supported" section.
        // get supported attribute names list (if any) for the renderer
	// If there are not any unser the "suppported" area, then just 
	// get the regular list of renderer names for the renderer type.
	//
	//
	List rendererAttributeNames = getParser().getSupportedAttributeNames(
	    componentType, rendererType);
	if (rendererAttributeNames.isEmpty()) {
	    Iterator iter = getParser().getAttributesForRenderer(rendererType).keySet().
	        iterator();
	    rendererAttributeNames = new ArrayList();
	    while (iter.hasNext()) {
	        rendererAttributeNames.add((String)iter.next());
	    }
	}

	// get component attribute names.
	//
	Iterator iter = getParser().getAttributesForComponent(
	    componentType).keySet().iterator();
	List componentAttributeNames = new ArrayList();
	while (iter.hasNext()) {
	    componentAttributeNames.add((String)iter.next());
	}
	
	// generate ivars
	//
	Map alreadyGenerated = new HashMap();
	// from renderer attributes
	String info = tagLibGenerator.generateIvars(rendererAttributeNames, rendererType,
	    alreadyGenerated);
	if (info != null) {
	    result.append(info);
	}
	// from component attributes
	info = tagLibGenerator.generateIvars(componentAttributeNames, componentType,
	    alreadyGenerated);
	if (info != null) {
	    result.append(info);
	}

	// generate accessor methods
	//
	alreadyGenerated = new HashMap();
	// From renderer attributes...
	info = tagLibGenerator.generateAccessorMethods(rendererAttributeNames, rendererType,
	    alreadyGenerated);
	if (info != null) {
	    result.append(info);
	}
	// From component attributes...
	info = tagLibGenerator.generateAccessorMethods(componentAttributeNames, componentType,
	    alreadyGenerated);
	if (info != null) {
	    result.append(info);
	}

	// generate general methods (override properties, etc...)
	//
	info = tagLibGenerator.generateGeneralMethods(rendererAttributeNames,
	    componentAttributeNames, rendererType, componentType);
	if (info != null) {
	    result.append(info);
	}
	
	// generate general purpose method used in logging.
	//
	result.append("    public String getDebugString() {\n");
	String res = "\"id: \"+this.getId()+\" class: \"+this.getClass().getName()";
	result.append("        String result = "+res+";\n");
	result.append("        return result;\n");
	result.append("    }\n\n");

	result.append("    public String getId() {\n");
	result.append("        return id;\n");
	result.append("    }\n"); 

	result.append("}\n");
	    
	return result.toString();
    }

    //
    // helpers
    // 
    
    /**
     * Generate tag handler classes from components not associated with any renderer.
     */
    private StringBuffer generateRemainingComponentTags(StringBuffer result) {
        // Iterate over all component types and only process the ones
        // which have not been processed before.
	//
	Iterator componentTypes = getParser().getComponentTypes();
	while (componentTypes.hasNext()) {
	    Map alreadyGenerated = new HashMap();
	    String componentType = (String)componentTypes.next();
	    if (getParser().componentHasRenderer(componentType)) {
	        continue;
	    }
	    String tagName = determineTagName(componentType, componentType);
	    if (getLog().isInfoEnabled()) {
	        getLog().info("Generating " + tagName + " from meta-data.");
	    }
		
	    String tagClass = tagLibGenerator.getTagClassPath(tagName)+"."+tagName+"Tag";

	    result = generateTldTagInfo(result, tagName, tagClass,
	        tagLibGenerator.getTeiClass(tagName), tagLibGenerator.getBodyContent(tagName));

	    Iterator iter = getParser().getAttributesForComponent(
	        componentType).keySet().iterator();
	    List attributeNames = new ArrayList();
	    while (iter.hasNext()) {
	        attributeNames.add((String)iter.next());
	    }
	    if (!attributeNames.isEmpty()) {
	        result = generateTldComponentAttributes(result, componentType,
	        tagName, attributeNames, alreadyGenerated);
	    }
	    result.append("  </tag>\n\n");
	}
	return result;
    }

    /**
     * Generates the top portion (as defined in an external file) of the tld.
     */
    private StringBuffer generateTldTopPart(StringBuffer result) {
	String topMatter = null;
	if (null != (topMatter = getTldTopMatter())) {
	    result.append(topMatter);
	    result.append("\n\n");
	}
	return result;
    }

    /**
     * Generates tag "header" information.
     */
    private StringBuffer generateTldTagInfo(StringBuffer result, String tagName,
        String tagClass, String teiClass, String bodyContent) {
        result.append("  <tag>\n");
	result.append("    <name>");
	result.append(tagName.toLowerCase());
	result.append("</name>\n");
	result.append("    <tag-class>");
	result.append(tagClass);
	result.append("</tag-class>\n");
	result.append("    <tei-class>");
	result.append(teiClass);
	result.append("</tei-class>\n");
	result.append("    <body-content>");
	result.append(bodyContent);
	result.append("</body-content>\n\n");

        return result;
    }

    /**
     * Generates tag attribute elements from component properties.
     */
    private StringBuffer generateTldComponentAttributes(StringBuffer result, 
        String componentType, String tagName, List propertyNames, Map alreadyGenerated) {

	String propertyName = null;
	String propertyClass = null;
	String propertyDescription = null;

	for (int i=0; i<propertyNames.size(); i++) {
	    propertyName = (String)propertyNames.get(i);
	    if (isAllUpperCase(propertyName)) {
		propertyName = propertyName.toLowerCase();
	    }
	    propertyClass = getParser().getComponentPropertyClass(
	        componentType, propertyName);
	    //
	    // check if we've already generated the same property name 
	    //
	    String attrType = (String)alreadyGenerated.get(propertyName);
	    if (attrType != null) {
	        if (attrType.equals(propertyClass)) {
		    continue;
		} else {
		    throw new IllegalStateException("Component Type:"+componentType+
			":Already generated attribute name:"+
		        propertyName+" but with the type:"+attrType);
		}
	    }
	    propertyDescription = getParser().getComponentPropertyDescription (
	        componentType, propertyName);
	    result.append("    <attribute>\n");
	    result.append("      <name>");
	    result.append(propertyName);
	    result.append("</name>\n");
	    result.append("      <required>");
	    result.append(tagLibGenerator.getRequired(tagName, propertyName));
	    result.append("</required>\n");
	    result.append("      <rtexprvalue>");
	    result.append(tagLibGenerator.getRtexprvalue(tagName, propertyName));
	    result.append("</rtexprvalue>\n");
	    result.append("      <type>");
	    result.append(propertyClass);
	    result.append("</type>\n");
	    if (propertyDescription != null) {
	        result.append("      <description>\n");
	        result.append("        "+propertyDescription+"\n");
	        result.append("      </description>\n");
	    }
	    result.append("    </attribute>\n");

	    if (alreadyGenerated != null) {
	        alreadyGenerated.put(propertyName, propertyClass);
	    }
	}
	return result;
    }
    
    /**
     * Generates tag attribute elements from renderer attributes.
     */
    private StringBuffer generateTldRendererAttributes(StringBuffer result, 
        String rendererType, String tagName, List attributeNames, Map alreadyGenerated) {

	String attributeName = null;
	String attributeClass = null;
	String attributeDescription = null;

	for (int i=0; i<attributeNames.size(); i++) {
	    attributeName = (String)attributeNames.get(i);
	    attributeClass = getParser().getRendererAttributeClass(
	        rendererType, attributeName);

	    // check if we've already generated the same attribute name from
	    // component properties
	    //
	    String attrType = (String)alreadyGenerated.get(attributeName);
	    if (attrType != null) {
	        if (attrType.equals(attributeClass)) {
		    continue;
		} else {
		    throw new IllegalStateException("Already generated attribute name:"+
		        attributeName+" but with the type:"+attrType+
			" for renderer type:"+rendererType);
		}
	    }

	    attributeDescription = getParser().getRendererAttributeDescription (
	        rendererType, attributeName);
	    result.append("    <attribute>\n");
	    result.append("      <name>");
	    result.append(attributeName);
	    result.append("</name>\n");
	    result.append("      <required>");
	    result.append(tagLibGenerator.getRequired(tagName, attributeName));
	    result.append("</required>\n");
	    result.append("      <rtexprvalue>");
	    result.append(tagLibGenerator.getRtexprvalue(tagName, attributeName));
	    result.append("</rtexprvalue>\n");
	    result.append("      <type>");
	    result.append(attributeClass);
	    result.append("</type>\n");
	    if (attributeDescription != null) {
	        result.append("      <description>\n");
	        result.append("        "+attributeDescription+"\n");
	        result.append("      </description>\n");
	    }
	    result.append("    </attribute>\n");

	    if (alreadyGenerated != null) {
	        alreadyGenerated.put(attributeName, attributeClass);
	    }
	}
	return result;
    }

    // Generate any remaining component classes from components not associated
    // with a renderer
    //
    private void generateRemainingComponentClasses() {
	String tagName = null;
	String tagClassPath = null;
	String tagClass = null;
	String generatedClass = null;
	String fileName = null;
	String topMatter = null;

        // Iterate over all component types and only process the ones
        // which have not been processed before.
	//
	Iterator componentTypes = getParser().getComponentTypes();
	while (componentTypes.hasNext()) {
            StringBuffer result = new StringBuffer(40000);
	    String componentType = (String)componentTypes.next();
	    if (getParser().componentHasRenderer(componentType)) {
	        continue;
	    }
	    tagName = determineComponentType(componentType, componentType)+"Tag";
	    tagClassPath = tagLibGenerator.getTagClassPath(tagName); 
            tagClass = tagClassPath+"."+tagName;
	    
            if (getLog().isInfoEnabled()) {
                getLog().info("Generating " + tagClass+ " from meta-data.");
            }
            if (null != (topMatter = getTopMatter())) {
                result.append(topMatter);
                result.append("\n\n");
            }

            // package declaration
            result.append("package "+tagClassPath+";\n\n");

            //imports
	    result.append(tagLibGenerator.getImportInfo()+"\n\n");

            // class declaration
	    result.append(tagLibGenerator.getClassDeclaration(tagName)+"\n\n");
	
	    // log declaration
	    result.append("    public static Log log = LogFactory.getLog("+tagName+".class);\n\n");

	    // get component attribute names.
	    //
	    Iterator iter = getParser().getAttributesForComponent(
	        componentType).keySet().iterator();
	    List componentAttributeNames = new ArrayList();
	    while (iter.hasNext()) {
	        componentAttributeNames.add((String)iter.next());
	    }
	
	    // generate ivars
	    //
	    Map generated = new HashMap();
	    String info = tagLibGenerator.generateIvars(componentAttributeNames, componentType, generated);
	    if (info != null) {
	        result.append(info);
	    }

	    // generate accessor methods
	    //
	    generated = new HashMap();
	    info = tagLibGenerator.generateAccessorMethods(componentAttributeNames, componentType,
	        generated);
	    if (info != null) {
	        result.append(info);
	    }

	    // generate general methods (override properties, etc...)
	    //
	    info = tagLibGenerator.generateGeneralMethods(componentAttributeNames, componentType);
	    if (info != null) {
	        result.append(info);
	    }        
	    
	    // generate general purpose method used in logging.
	    //
	    result.append("    public String getDebugString() {\n");
	    String res = "\"id: \"+this.getId()+\" class: \"+this.getClass().getName()";
	    result.append("        String result = "+res+";\n");
	    result.append("        return result;\n");
	    result.append("    }\n\n");

	    result.append("    public String getId() {\n");
	    result.append("        return id;\n");
	    result.append("    }\n"); 

	    result.append("}\n\n");

            //
            // Write the generated class to the outputDir
            //
            fileName = getParser().replaceOccurrences(tagClass, ".", File.separator) +
                ".java";
            writeContentsToFile(result.toString(), fileName);
	}
    }

    /**
     * Returns the tag name given a component type/renderer type combination.
     */
    private String determineTagName(String componentType, String rendererType) {
	String result = determineComponentType(componentType, rendererType);
	if (result.equals(rendererType)) {
	    return result;
	}
	result = result+"_"+rendererType;
	return result;
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
	    absolutePathToTldTopMatterFile = null,
	    absolutePathToTopMatterFile = null,
	    absolutePathToConfigFile = null,
	    absolutePathToTldOutputDir = null,
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
		else if (args[i].equals("-b")) {
		    absolutePathToTldTopMatterFile = args[++i];
		}
		else if (args[i].equals("-a")) {
		    absolutePathToTldOutputDir = args[++i];
		}
	    }
	}
	catch (ArrayIndexOutOfBoundsException e) {
	    System.out.println("Error parsing arguments: " + e.getMessage());
	    return;
	}

	// generate the classes
	// Here's where you could evaluate a command line arg which says which
	// tag library generator implementation to use.
	//
	HtmlTaglibGenerator tagLibGenerator = new HtmlTaglibGenerator();
	GenerateTaglib me = new GenerateTaglib(tagLibGenerator);
	me.init(absolutePathToConfigFile, absolutePathToEntityDeclarationsFile,
		absolutePathToTldTopMatterFile, absolutePathToTopMatterFile,
		absolutePathToTldOutputDir, absolutePathToOutputDir);
	me.generateTld();
	me.generateClasses();
    }


} // end of class GenerateTaglib
