/*
 * $Id: ResultSetTag.java,v 1.1 2003/02/14 20:36:19 eburns Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package components.taglib;


import javax.faces.component.UIComponent;
import javax.faces.component.UIPanel;
import javax.faces.webapp.FacesBodyTag;
import javax.servlet.jsp.JspException;

public class ResultSetTag extends FacesBodyTag {


    private String rowsPerPage = null;
    public String getRowsPerPage()
    {
	return rowsPerPage;
    }
    
    public void setRowsPerPage(String newRowsPerPage)
    {
	rowsPerPage = newRowsPerPage;
    }

    private String navFacetOrientation = null;
    public String getNavFacetOrientation()
    {
	return navFacetOrientation;
    }
    
    public void setNavFacetOrientation(String newNavFacetOrientation)
    {
	navFacetOrientation = newNavFacetOrientation;
    }

    private String scrollerControlsLocation = null;
    public String getScrollerControlsLocation()
    {
	return scrollerControlsLocation;
    }
    
    public void setScrollerControlsLocation(String newScrollerControlsLocation)
    {
	scrollerControlsLocation = newScrollerControlsLocation;
    }



    private String columnClasses = null;
    public void setColumnClasses(String columnClasses) {
        this.columnClasses = columnClasses;
    }


    private String footerClass = null;
    public void setFooterClass(String footerClass) {
        this.footerClass = footerClass;
    }


    private String headerClass = null;
    public void setHeaderClass(String headerClass) {
        this.headerClass = headerClass;
    }


    private String panelClass = null;
    public void setPanelClass(String panelClass) {
        this.panelClass = panelClass;
    }


    private String rowClasses = null;
    public void setRowClasses(String rowClasses) {
        this.rowClasses = rowClasses;
    }


    public UIComponent createComponent() {
        return (new UIPanel());
    }


    public String getRendererType() {
        return ("ResultSet");
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
        if ((columnClasses != null) &&
            (component.getAttribute("columnClasses") == null)) {
            component.setAttribute("columnClasses", columnClasses);
        }
        if ((footerClass != null) &&
            (component.getAttribute("footerClass") == null)) {
            component.setAttribute("footerClass", footerClass);
        }
        if ((headerClass != null) &&
            (component.getAttribute("headerClass") == null)) {
            component.setAttribute("headerClass", headerClass);
        }
        if ((panelClass != null) &&
            (component.getAttribute("panelClass") == null)) {
            component.setAttribute("panelClass", panelClass);
        }
        if ((rowClasses != null) &&
            (component.getAttribute("rowClasses") == null)) {
            component.setAttribute("rowClasses", rowClasses);
        }
        if ((rowsPerPage != null) &&
            (component.getAttribute("rowsPerPage") == null)) {
	    try {
		component.setAttribute("rowsPerPage", 
				       Integer.valueOf(rowsPerPage));
	    }
	    catch (NumberFormatException e) {
	    }
        }
        if ((navFacetOrientation != null) &&
            (component.getAttribute("navFacetOrientation") == null)) {
            component.setAttribute("navFacetOrientation", navFacetOrientation);
        }
        if ((scrollerControlsLocation != null) &&
            (component.getAttribute("scrollerControlsLocation") == null)) {
            component.setAttribute("scrollerControlsLocation", 
				   scrollerControlsLocation);
        }


    }


}
