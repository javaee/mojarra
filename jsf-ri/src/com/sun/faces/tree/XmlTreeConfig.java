/*
 * $Id: XmlTreeConfig.java,v 1.1 2002/06/18 04:56:32 rkitain Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// XmlTreeConfig.java

package com.sun.faces.tree;

import org.mozilla.util.Assert;
import org.mozilla.util.ParameterCheck;

import javax.faces.tree.TreeFactory;
import javax.faces.tree.Tree;
import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.component.UIComponentBase;

import javax.servlet.ServletContext;

import java.util.Iterator;
import java.util.Set;
import java.util.ArrayList;

import java.io.InputStream;
import java.net.URL;

/**
 *
 *  <B>XmlTreeConfig</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: XmlTreeConfig.java,v 1.1 2002/06/18 04:56:32 rkitain Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class XmlTreeConfig {
//
// Protected Constants
//

//
// Class Variables
//

//
// Instance Variables
//

    private String pageUrl = null;
    private UIComponentBase root = null;

// Attribute Instance Variables


// Relationship Instance Variables

//
// Constructors and Initializers    
//
    public XmlTreeConfig(UIComponentBase root) {
        this.root = root;
    }

//
// Class methods
//

//
// General Methods
//

    public void setPageUrl(String pageUrl) {
        this.pageUrl = pageUrl;
    }

    public String getPageUrl() {
        return pageUrl;
    }

    public UIComponentBase getRoot() {
        return root;
    }

    public void addChild(UIComponent component) {
        root.addChild(component);
    }
}
