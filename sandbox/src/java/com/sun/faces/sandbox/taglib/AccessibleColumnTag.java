/*
 * $Id: AccessibleColumnTag.java,v 1.3 2006/12/09 20:41:24 rlubke Exp $
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

import com.sun.faces.sandbox.util.Util;

import javax.faces.component.UIColumn;
import javax.faces.component.UIComponent;
import javax.faces.el.ValueBinding;
import javax.faces.webapp.UIComponentTag;
import javax.servlet.jsp.JspException;

public class AccessibleColumnTag extends UIComponentTag {

    // PROPERTY: footerClass
    private String footerClass;
    public void setFooterClass(String footerClass) {
        this.footerClass = footerClass;
    }

    // PROPERTY: headerClass
    private String headerClass;
    public void setHeaderClass(String headerClass) {
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
            if (isValueReference(footerClass)) {
                ValueBinding vb = Util.getValueBinding(footerClass);
                column.setValueBinding("width", vb);
            } else {
                column.getAttributes().put("footerClass", footerClass);
            }
        }
        if (headerClass != null) {
            if (isValueReference(headerClass)) {
                ValueBinding vb = Util.getValueBinding(headerClass);
                column.setValueBinding("width", vb);
            } else {
                column.getAttributes().put("headerClass", headerClass);
            }
        }
        if (rowHeader != null) {
            if (isValueReference(rowHeader)) {
                ValueBinding vb = Util.getValueBinding(rowHeader);
                column.setValueBinding("width", vb);
            } else {
                column.getAttributes().put("rowHeader",
                                           Boolean.valueOf(rowHeader));
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
            throw e;
        } catch (Throwable t) {
            throw new JspException(t);
        }

    }


    public int doEndTag() throws JspException {

        try {
            return super.doEndTag();
        } catch (JspException e) {
            throw e;
        } catch (Throwable t) {           
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
    private String rowHeader;

    /**
     * Setter for property rowHeader.
     * @param rowHeader New value of property rowHeader.
     */
    public void setRowHeader(String rowHeader) {
        this.rowHeader = rowHeader;
    }

}

