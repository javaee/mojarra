/*
 * $Id: ColumnTag.java,v 1.22 2007/07/19 15:50:56 rlubke Exp $
 */

/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
 * 
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License. You can obtain
 * a copy of the License at https://glassfish.dev.java.net/public/CDDL+GPL.html
 * or glassfish/bootstrap/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 * 
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at glassfish/bootstrap/legal/LICENSE.txt.
 * Sun designates this particular file as subject to the "Classpath" exception
 * as provided by Sun in the GPL Version 2 section of the License file that
 * accompanied this code.  If applicable, add the following below the License
 * Header, with the fields enclosed by brackets [] replaced by your own
 * identifying information: "Portions Copyrighted [year]
 * [name of copyright owner]"
 * 
 * Contributor(s):
 * 
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
    private javax.el.ValueExpression footerClass;
    public void setFooterClass(javax.el.ValueExpression footerClass) {
        this.footerClass = footerClass;
    }

    // PROPERTY: headerClass
    private javax.el.ValueExpression headerClass;
    public void setHeaderClass(javax.el.ValueExpression headerClass) {
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
                column.getAttributes().put("headerClass", headerClass.getExpressionString());
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
    }

    public String getDebugString() {
        return "id: " + this.getId() + " class: " +
            this.getClass().getName();
    }

}

