/*
 * $Id: Data_TableTag.java,v 1.1 2003/11/05 05:46:00 eburns Exp $
 */

/*
 * Copyright 2002, 2003 Sun Microsystems, Inc. All Rights Reserved.
 * 
 * Redistribution and use in source and binary forms, with or
 * without modification, are permitted provided that the following
 * conditions are met:
 * 
 * - Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 * 
 * - Redistribution in binary form must reproduce the above
 *   copyright notice, this list of conditions and the following
 *   disclaimer in the documentation and/or other materials
 *   provided with the distribution.
 *    
 * Neither the name of Sun Microsystems, Inc. or the names of
 * contributors may be used to endorse or promote products derived
 * from this software without specific prior written permission.
 *  
 * This software is provided "AS IS," without a warranty of any
 * kind. ALL EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND
 * WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE OR NON-INFRINGEMENT, ARE HEREBY
 * EXCLUDED. SUN AND ITS LICENSORS SHALL NOT BE LIABLE FOR ANY
 * DAMAGES OR LIABILITIES SUFFERED BY LICENSEE AS A RESULT OF OR
 * RELATING TO USE, MODIFICATION OR DISTRIBUTION OF THIS SOFTWARE OR
 * ITS DERIVATIVES. IN NO EVENT WILL SUN OR ITS LICENSORS BE LIABLE
 * FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT,
 * SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER
 * CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF
 * THE USE OF OR INABILITY TO USE THIS SOFTWARE, EVEN IF SUN HAS
 * BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 *  
 * You acknowledge that this software is not designed, licensed or
 * intended for use in the design, construction, operation or
 * maintenance of any nuclear facility.
 */

package com.sun.faces.taglib.html_basic;


import com.sun.faces.taglib.BaseComponentTag;
import com.sun.faces.util.Util;
import javax.faces.component.UIComponent;
import javax.faces.component.UIData;
import javax.servlet.jsp.JspException;



/**
 * This class is the tag handler that evaluates the <code>data_table</code>
 * custom tag.
 */

public class Data_TableTag extends BaseComponentTag {


    // -------------------------------------------------------------- Properties


    public String getComponentType() { return ("DataTable"); }

    public String getRendererType() { return ("Table"); }


    // -------------------------------------------------------------- Attributes


    private String columnClasses = null;
    private String columnClasses_ = null;
    public void setColumnClasses(String columnClasses) {
        this.columnClasses_ = columnClasses;
    }


    private int first = -1;
    public void setFirst(int first) {
	this.first = first;
    }


    private String footerClass = null;
    private String footerClass_ = null;
    public void setFooterClass(String footerClass) {
        this.footerClass_ = footerClass;
    }


    private String headerClass = null;
    private String headerClass_ = null;
    public void setHeaderClass(String headerClass) {
        this.headerClass_ = headerClass;
    }


    private String rowClasses = null;
    private String rowClasses_ = null;
    public void setRowClasses(String rowClasses) {
        this.rowClasses_ = rowClasses;
    }


    private int rows = -1;
    public void setRows(int rows) {
	this.rows = rows;
    }


    private String styleClass = null;
    private String styleClass_ = null;
    public void setDataClass(String styleClass) {
        this.styleClass_ = styleClass;
    }


    private String value = null;
    private String value_ = null;
    public void setValue(String value) {
	this.value_ = value;
    }


    private String valueRef = null;
    private String valueRef_ = null;
    public void setValueRef(String valueRef) {
	this.valueRef_ = valueRef;
    }


    // var is not expression enabled
    private String var = null;
    public void setVar(String var) {
	this.var = var;
    }


    // ---------------------------------------------------------- Public Methods


    public void release() {

	super.release();
	columnClasses = null;
	first = -1;
	footerClass = null;
	headerClass = null;
	rowClasses = null;
	rows = -1;
	styleClass = null;
	value = null;
	valueRef = null;
	var = null;

    }


    // ------------------------------------------------------- Protected Methods


    protected void overrideProperties(UIComponent component) {

        super.overrideProperties(component);
        UIData data = (UIData) component;
        if (columnClasses != null) {
            component.getAttributes().put("columnClasses", columnClasses);
        }
        if (first >= 0) {
            data.setFirst(first);
        }
        if (footerClass != null) {
            component.getAttributes().put("footerClass", footerClass);
        }
        if (headerClass != null) {
            component.getAttributes().put("headerClass", headerClass);
        }
        if (rowClasses != null) {
            component.getAttributes().put("rowClasses", rowClasses);
        }
        if (rows >= 0) {
            data.setRows(rows);
        }
        if (styleClass != null) {
            component.getAttributes().put("styleClass", styleClass);
        }
	if (value != null) {
	    data.setValue(value);
	}
	if (valueRef != null) {
	    data.setValueRef(valueRef);
	}
	if (var != null) {
	    data.setVar(var);
	}


    }

    /* Evaluates expressions as necessary */
    protected void evaluateExpressions() throws JspException {
	super.evaluateExpressions();
        if (columnClasses_ != null) {
            columnClasses = Util.evaluateElExpression(columnClasses_, pageContext);
        }
        if (footerClass_ != null) {
            footerClass = Util.evaluateElExpression(footerClass_, pageContext);
        }
        if (headerClass_ != null) {
            headerClass = Util.evaluateElExpression(headerClass_, pageContext);
        }
        if (rowClasses_ != null) {
            rowClasses = Util.evaluateElExpression(rowClasses_, pageContext);
        }
        if (styleClass_ != null) {
            styleClass = Util.evaluateElExpression(styleClass_, pageContext);
        }
        if (value_ != null) {
            value = Util.evaluateElExpression(value_, pageContext);
        }
        if (valueRef_ != null) {
            valueRef = Util.evaluateElExpression(valueRef_, pageContext);
        }
    }


    //
    // Methods from TagSupport
    //

    public int doStartTag() throws JspException {
        // evaluate any expressions that we were passed
        evaluateExpressions();

        // chain to the parent implementation
        return super.doStartTag();
    }
}
