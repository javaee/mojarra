/*
 * $Id: ColumnTag.java,v 1.2 2003/09/11 15:27:29 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.taglib.html_basic;


import com.sun.faces.taglib.FacesTag;


/**
 * This class is the tag handler that evaluates the <code>column</code>
 * custom tag.
 */

public class ColumnTag extends FacesTag {


    // -------------------------------------------------------------- Properties


    public String getComponentType() { return ("Column"); }


    public String getRendererType() { return (null); }


}
