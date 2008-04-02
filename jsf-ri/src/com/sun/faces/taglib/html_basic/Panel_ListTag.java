/*
 * $Id: Panel_ListTag.java,v 1.16 2003/10/07 20:15:53 horwat Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.taglib.html_basic;

import javax.faces.component.UIComponent;
import javax.servlet.jsp.JspException;

import com.sun.faces.taglib.BaseComponentTag;
import com.sun.faces.util.Util;


/**
 * This class is the tag handler that evaluates the 
 * <code>panel_list</code> custom tag.
 */

public class Panel_ListTag extends BaseComponentTag {

    //
    // Protected Constants
    //

    //
    // Class Variables
    //

    //
    // Instance Variables
    //
    private String columnClasses = null;
    private String columnClasses_ = null;
    private String footerClass = null;
    private String footerClass_ = null;
    private String headerClass = null;
    private String headerClass_ = null;
    private String panelClass = null;
    private String panelClass_ = null;
    private String rowClasses = null;
    private String rowClasses_ = null;
     
    // Attribute Instance Variables

    // Relationship Instance Variables

    //
    // Constructors and Initializers    
    //
    
    public Panel_ListTag()
    {
        super();
    }

    //
    // Class methods
    //

    // 
    // Accessors
    //
    
    public void setColumnClasses(String newColumnClasses) {
        this.columnClasses_ = newColumnClasses;
    }
    
    public void setFooterClass(String newFooterClass) {
        this.footerClass_ = newFooterClass;
    }
    
    public void setHeaderClass(String newHeaderClass) {
        this.headerClass_ = newHeaderClass;
    }
    
    public void setPanelClass(String newPanelClass) {
        this.panelClass_ = newPanelClass;
    }
    
    public void setRowClasses(String newRowClasses) {
        this.rowClasses_ = newRowClasses;
    }
    
    public void release() {
        super.release();
        this.columnClasses = null;
        this.footerClass = null;
        this.headerClass = null;
        this.panelClass = null;
        this.rowClasses = null;
    }


    protected void overrideProperties(UIComponent component) {
        super.overrideProperties(component);
        if (columnClasses != null) {
            component.getAttributes().put("columnClasses", columnClasses);
        }
        if (footerClass != null) {
            component.getAttributes().put("footerClass", footerClass);
        }
        if (headerClass != null) {
            component.getAttributes().put("headerClass", headerClass);
        }
        if (panelClass != null) {
            component.getAttributes().put("panelClass", panelClass);
        }
        if (rowClasses != null) {
            component.getAttributes().put("rowClasses", rowClasses);
        }
        
        // set HTML 4.0 attributes if any
        if (summary != null) {
            component.getAttributes().put("summary", summary);
        }
        if (width != Integer.MIN_VALUE) {
            component.getAttributes().put("width", new Integer(width));
        }
        if (bgcolor != null) {
            component.getAttributes().put("bgcolor", bgcolor);
        }
        if (frame != null) {
            component.getAttributes().put("frame", frame);
        }
        if (rules != null) {
            component.getAttributes().put("rules", rules);
        }
        if (border != Integer.MIN_VALUE) {
            component.getAttributes().put("border", new Integer(border));
        }
        if (cellspacing != Integer.MIN_VALUE) {
            component.getAttributes().put("cellspacing", new Integer(cellspacing));
        }
        if (cellpadding != Integer.MIN_VALUE) {
            component.getAttributes().put("cellpadding", new Integer(cellpadding));
        }
    }

    /**
     * This is implemented by faces subclasses to allow globally turing off
     * the render kit.
     */
    public String getRendererType() {
        return ("List"); 
    }

    public String getComponentType() { 
        return ("PanelList"); 
    }    

    /* Evaluates expressions as necessary */
    private void evaluateExpressions() throws JspException {
        if (columnClasses_ != null) {
            columnClasses = Util.evaluateElExpression(columnClasses_, pageContext);
   	}
        if (footerClass_ != null) {
            footerClass = Util.evaluateElExpression(footerClass_, pageContext);
        }
        if (headerClass_ != null) {
            headerClass = Util.evaluateElExpression(headerClass_, pageContext);
        }
     	if (panelClass_ != null) {
            panelClass = Util.evaluateElExpression(panelClass_, pageContext);
        }
        if (rowClasses_ != null) {
            rowClasses = Util.evaluateElExpression(rowClasses_, pageContext);
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
