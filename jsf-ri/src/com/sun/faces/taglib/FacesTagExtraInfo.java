/*
 * $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.taglib;

import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;
import javax.servlet.jsp.tagext.TagData;

/**
 * <p>Necessary to work around an issue with the
 * JSP <code>id</code> attribute in JWSDP 1.2.
 */ 
public class FacesTagExtraInfo extends TagExtraInfo {

    //
    // General Methods
    //

    public VariableInfo[] getVariableInfo(TagData tagData) {
        return null;
    }

} // end of class FacesTagExtraInfo
