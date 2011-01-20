/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2010 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */

package com.sun.faces.taglib.html_basic;

import javax.faces.component.UIColumn;
import javax.faces.component.UIComponent;
import javax.faces.webapp.UIComponentELTag;
import javax.servlet.jsp.JspException;
import javax.el.ValueExpression;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.sun.faces.util.Util;
import com.sun.faces.util.FacesLogger;

public class ColumnTag extends UIComponentELTag {

    // Log instance for this class
    private static final Logger logger = FacesLogger.TAGLIB.getLogger();

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

    // PROPERTY: rowHeader
    private ValueExpression rowHeader;
    public void setRowHeader(ValueExpression rowHeader) {
        this.rowHeader = rowHeader;
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
        UIColumn column;

        try {
            column = (UIColumn) component;
        } catch (ClassCastException cce) {
            throw new IllegalStateException("Component " + component.toString() + " not expected type.  Expected: UIColumn.  Perhaps you're missing a tag?");
        }
        if (footerClass != null) {
            column.setValueExpression("footerClass", footerClass);
        }
        if (headerClass != null) {
            column.setValueExpression("headerClass", headerClass);
        }
        if (rowHeader != null) {
            column.setValueExpression("rowHeader", rowHeader);
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
    }

    public String getDebugString() {
        return "id: " + this.getId() + " class: " +
            this.getClass().getName();
    }

}

