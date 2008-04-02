/*
 * $Id: ColumnTag.java,v 1.11 2005/06/09 22:37:49 jayashri Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */


package com.sun.faces.taglib.html_basic;

import javax.faces.component.UIColumn;
import javax.faces.component.UIComponent;
import javax.faces.el.ValueBinding;
import javax.faces.webapp.UIComponentTag;
import javax.servlet.jsp.JspException;

import com.sun.faces.util.Util;

import java.util.logging.Logger;
import java.util.logging.Level;

public class ColumnTag extends UIComponentTag {

    // Log instance for this class
    protected static Logger logger = 
            Util.getLogger(Util.FACES_LOGGER + Util.TAGLIB_LOGGER);

    //
    // Instance Variables
    //


    //
    // Setter Methods
    //
    // PROPERTY: footerClass
    private java.lang.String footerClass;
    public void setFooterClass(java.lang.String footerClass) {
        this.footerClass = footerClass;
    }

    // PROPERTY: headerClass
    private java.lang.String headerClass;
    public void setHeaderClass(java.lang.String headerClass) {
        this.headerClass = headerClass;
    }

    //
    // General Methods
    //
    public String getRendererType() {
        return null;
    }


    public String getComponentType() {
        return "javax.faces.Column";
    }


    protected void setProperties(UIComponent component) {
        super.setProperties(component);
        UIColumn column = null;

        try {
            column = (UIColumn) component;
        } catch (ClassCastException cce) {
            throw new IllegalStateException("Component " + component.toString() + " not expected type.  Expected: UIColumn.  Perhaps you're missing a tag?");
        }

        if (footerClass != null) {
            if (isValueReference(footerClass)) {
                ValueBinding vb = Util.getValueBinding(footerClass);
                column.setValueBinding("footerClass", vb);
            } else {
                column.getAttributes().put("footerClass", footerClass);
            }
        }
        if (headerClass != null) {
            if (isValueReference(headerClass)) {
                ValueBinding vb = Util.getValueBinding(headerClass);
                column.setValueBinding("headerClass", vb);
            } else {
                column.getAttributes().put("headerClass", headerClass);
            }
        }
    }

    //
    // Methods From TagSupport
    //

    public int doStartTag() throws JspException {
        int rc = 0;
        try {
            rc = super.doStartTag();
        } catch (JspException e) {
            if (logger.isLoggable(Level.WARNING)) {
                logger.log(Level.WARNING, getDebugString(), e);
            }
            throw e;
        } catch (Throwable t) {
            if (logger.isLoggable(Level.WARNING)) {
                logger.log(Level.WARNING, getDebugString(), t);
            }
            throw new JspException(t);
        }
        return rc;
    }


    public int doEndTag() throws JspException {
        int rc = 0;
        try {
            rc = super.doEndTag();
        } catch (JspException e) {
            if (logger.isLoggable(Level.WARNING)) {
                logger.log(Level.WARNING, getDebugString(), e);
            }
            throw e;
        } catch (Throwable t) {
            if (logger.isLoggable(Level.WARNING)) {
                logger.log(Level.WARNING, getDebugString(), t);
            }
            throw new JspException(t);
        }
        return rc;
    }


    public String getDebugString() {
        String result = "id: " + this.getId() + " class: " +
            this.getClass().getName();
        return result;
    }

}

