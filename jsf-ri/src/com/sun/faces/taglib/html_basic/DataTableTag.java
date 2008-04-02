/*
 * $Id: DataTableTag.java,v 1.2 2003/09/11 15:27:30 craigmcc Exp $
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


import com.sun.faces.taglib.FacesTag;
import javax.faces.component.UIComponent;
import javax.faces.component.UIData;


/**
 * This class is the tag handler that evaluates the <code>data_table</code>
 * custom tag.
 */

public class DataTableTag extends FacesTag {


    // -------------------------------------------------------------- Properties


    public String getComponentType() { return ("Data"); }

    public String getRendererType() { return ("Table"); }


    // -------------------------------------------------------------- Attributes


    private String columnClasses = null;
    public void setColumnClasses(String columnClasses) {
        this.columnClasses = columnClasses;
    }


    private int first = -1;
    public void setFirst(int first) {
	this.first = first;
    }


    private String footerClass = null;
    public void setFooterClass(String footerClass) {
        this.footerClass = footerClass;
    }


    private String headerClass = null;
    public void setHeaderClass(String headerClass) {
        this.headerClass = headerClass;
    }


    private String rowClasses = null;
    public void setRowClasses(String rowClasses) {
        this.rowClasses = rowClasses;
    }


    private int rows = -1;
    public void setRows(int rows) {
	this.rows = rows;
    }


    private String styleClass = null;
    public void setDataClass(String styleClass) {
        this.styleClass = styleClass;
    }


    private String value = null;
    public void setValue(String value) {
	this.value = value;
    }


    private String valueRef = null;
    public void setValueRef(String valueRef) {
	this.valueRef = valueRef;
    }


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
            component.setAttribute("columnClasses", columnClasses);
        }
        if (first >= 0) {
            data.setFirst(first);
        }
        if (footerClass != null) {
            component.setAttribute("footerClass", footerClass);
        }
        if (headerClass != null) {
            component.setAttribute("headerClass", headerClass);
        }
        if (rowClasses != null) {
            component.setAttribute("rowClasses", rowClasses);
        }
        if (rows >= 0) {
            data.setRows(rows);
        }
        if (styleClass != null) {
            component.setAttribute("styleClass", styleClass);
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


}
