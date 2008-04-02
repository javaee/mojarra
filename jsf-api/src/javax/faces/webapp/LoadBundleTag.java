/*
 * $Id: LoadBundleTag.java,v 1.2 2003/11/04 16:57:46 rlubke Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.webapp;


import javax.faces.context.FacesContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Enumeration;

/**
 * <p>Tag action that loads the specified ResourceBundle as a Map into
 * the request scope of the current {@link
 * javax.faces.context.FacesContext}.</p>
 * 
 */

public class LoadBundleTag extends TagSupport {


    // ------------------------------------------------------------- Attributes

    private String basename;

    /**
     * <p>Set the base name of the <code>ResourceBundle</code> to be
     * loaded.</p>
     */
    public void setBasename(String newBasename) {
	basename = newBasename;
    }

    private String var;

    /**
     * <p>Set the name of the attribute in the request scope under which
     * to store the <code>ResourceBundle</code> <code>Map</code>.</p>
     */

    public void setVar(String newVar) {
	var = newVar;
    }



    // --------------------------------------------------------- Public Methods


    /**
     * <p>Load the <code>ResourceBundle</code> named by our
     * <code>basename</code> property.</p>  Wrap it in an immutable
     * <code>Map</code> implementation and store the <code>Map</code> in
     * the request attr set of under the key given by our
     * <code>var</code> property.
     *
     * @exception JspException if a JSP error occurs
     */
    public int doStartTag() throws JspException {

        if (null == basename || null == var) { // PENDING - i18n
            throw new JspException("null basename or var");
        }
	ResourceBundle bundle = null;
	HashMap toStore = null;
	FacesContext context = FacesContext.getCurrentInstance();
	
	bundle = ResourceBundle.getBundle(basename, 
					  context.getViewRoot().getLocale(),
					  getCurrentLoader(this));
	if (null == bundle) {
	    throw new JspException("null ResourceBundle for " + basename);
	}
	
	toStore = new HashMap();

	Enumeration keys = bundle.getKeys();
	String key = null;
	while (keys.hasMoreElements()) {
	    key = (String) keys.nextElement();
	    toStore.put(key, bundle.getString(key));
	}
	
	context.getExternalContext().getRequestMap().put(var, toStore);

        return (EVAL_BODY_INCLUDE);

    }


    /**
     * <p>Release references to any acquired resources.
     */
    public void release() {

        this.basename = null;
        this.var = null;

    }

    protected static ClassLoader getCurrentLoader(Object fallbackClass) {
        ClassLoader loader =
	    Thread.currentThread().getContextClassLoader();
	if (loader == null) {
	    loader = fallbackClass.getClass().getClassLoader();
	}
	return loader;
    }



}
