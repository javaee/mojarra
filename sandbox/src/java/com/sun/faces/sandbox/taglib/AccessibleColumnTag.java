/*
 * $Id: AccessibleColumnTag.java,v 1.2 2006/12/07 05:03:56 rlubke Exp $
 */

/*
 * The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the License). You may not use this file except in
 * compliance with the License.
 *
 * You can obtain a copy of the License at
 * https://javaserverfaces.dev.java.net/CDDL.html or
 * legal/CDDLv1.0.txt.
 * See the License for the specific language governing
 * permission and limitations under the License.
 *
 * When distributing Covered Code, include this CDDL
 * Header Notice in each file and include the License file
 * at legal/CDDLv1.0.txt.
 * If applicable, add the following below the CDDL Header,
 * with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 *
 * [Name of File] [ver.__] [Date]
 *
 * Copyright 2005 Sun Microsystems Inc. All Rights Reserved
 */


package com.sun.faces.sandbox.taglib;

import javax.faces.component.UIColumn;
import javax.faces.component.UIComponent;
import javax.faces.webapp.UIComponentELTag;
import javax.servlet.jsp.JspException;

import com.sun.faces.util.Util;

import java.util.logging.Logger;
import java.util.logging.Level;
import javax.el.ValueExpression;

public class AccessibleColumnTag extends UIComponentELTag {

    // Log instance for this class
    private static final Logger logger =
            Util.getLogger(Util.FACES_LOGGER + Util.TAGLIB_LOGGER);

    //
    // Instance Variables
    //


    //
    // Setter Methods
    //
    // PROPERTY: footerClass
    private ValueExpression footerClass;
    public void setFooterClass(ValueExpression footerClass) {
        this.footerClass = footerClass;
    }

    // PROPERTY: headerClass
    private ValueExpression headerClass;
    public void setHeaderClass(ValueExpression headerClass) {
        this.headerClass = headerClass;
    }    

    //
    // General Methods
    //
    public String getRendererType() {
        return null;
    }


    public String getComponentType() {
        return "com.sun.faces.sandbox.AccessibleHtmlColumn";
    }


    protected void setProperties(UIComponent component) {
        super.setProperties(component);
        UIColumn column;

        try {
            column = (UIColumn) component;
        } catch (ClassCastException cce) {
            throw new IllegalStateException("Component " + component.toString() + " not expected type.  Expected: UIColumn.  Perhaps you're missing a tag?");
        }
        if (footerClass != null) {
            if (!footerClass.isLiteralText()) {
                column.setValueExpression("footerClass", footerClass);
            } else {
                column.getAttributes().put("footerClass", footerClass.getExpressionString());
            }
        }
        if (headerClass != null) {
            if (!headerClass.isLiteralText()) {
                column.setValueExpression("headerClass", headerClass);
            } else {
                column.getAttributes().put("headerClass",
                        Boolean.valueOf(headerClass.getExpressionString()));
            }
        }
        if (rowHeader != null) {
            if (!rowHeader.isLiteralText()) {
                column.setValueExpression("rowHeader", rowHeader);
            } else {
                column.getAttributes().put("rowHeader",
                        Boolean.valueOf(rowHeader.getExpressionString()));
            }
        }
    }

    //
    // Methods From TagSupport
    //

    public int doStartTag() throws JspException {

        try {
            return super.doStartTag();
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

    }


    public int doEndTag() throws JspException {

        try {
            return super.doEndTag();
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

    }

    // RELEASE
    public void release() {
        super.release();
        this.headerClass = null;
        this.footerClass = null;
        this.rowHeader = null;
    }

    public String getDebugString() {
        return ("id: " + this.getId() + " class: " +
            this.getClass().getName());
    }

    /**
     * Holds value of property rowHeader.
     */
    private ValueExpression rowHeader;

    /**
     * Setter for property rowHeader.
     * @param rowHeader New value of property rowHeader.
     */
    public void setRowHeader(ValueExpression rowHeader) {
        this.rowHeader = rowHeader;
    }

}

