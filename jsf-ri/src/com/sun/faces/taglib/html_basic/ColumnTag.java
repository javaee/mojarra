/*
 * $Id: ColumnTag.java,v 1.3 2003/09/25 16:36:28 rlubke Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.taglib.html_basic;


import com.sun.faces.taglib.BaseComponentTag;


/**
 * This class is the tag handler that evaluates the <code>column</code>
 * custom tag.
 */

public class ColumnTag extends BaseComponentTag {


    // -------------------------------------------------------------- Properties


    public String getComponentType() { return ("Column"); }


    public String getRendererType() { return (null); }


}
