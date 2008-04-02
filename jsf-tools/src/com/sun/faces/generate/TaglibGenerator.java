/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.generate;

import java.util.List;
import java.util.Map;

/**
 * Interface for tag library generation implementations.
 */
public interface TaglibGenerator {

    /**
     * Implementations provide their own tag class path;
     */
    public String getTagClassPath(String tagName);
    
    /**
     * Implementations provide their own tei class path; 
     */
    public String getTeiClass(String tagName);

    /**
     * Implementations provide their own body content; 
     */
    public String getBodyContent(String tagName); 

    /**
     * Implementations provide their own required attribute value; 
     */
    public String getRequired(String tagName, String attributeName, 
			      String tagAttributeStatus);

    /**
     * Implementations provide their own rtexprvalue attribute value; 
     */
    public String getRtexprvalue(String tagName, String attributeName); 

    /**
     * Implementations provide their own class import information; 
     */
    public String getImportInfo(); 

    /**
     * Implementations provide their own class delcaration information; 
     */
    public String getClassDeclaration(String tagName);

    /**
     * Implementations provide ther own instance variable generation logic.
     */
    public String generateIvars(List attributeNames, String type, Map generated);

    /**
     * Implementations provide ther own accessor method generation logic.
     */
    public String generateAccessorMethods(List attributeNames, String type, Map generated);

    /**
     * Implementations provide ther own generation logic for general methods.
     */
    public String generateGeneralMethods(List rendererAttributeNames,
	List componentAttributeNames, String rendererType, String componentType);

    /**
     * Implementations provide ther own generation logic for general methods.
     */
    public String generateGeneralMethods(List componentAttributeNames, String componentType);

    /**
     * Implementations provide logic to return attributes that are
     * inserted into the TLD of every tag.
     */
    public String generateGlobalAttributes();

}
