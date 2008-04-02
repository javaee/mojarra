/*
 * $Id: ResultSetTag.java,v 1.6 2003/09/25 17:48:12 horwat Exp $
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

package components.taglib;


import javax.faces.component.UIComponent;
import javax.faces.webapp.UIComponentTag;
import javax.servlet.jsp.JspException;


// PENDING(craigmcc) -- Why a body tag?
public class ResultSetTag extends UIComponentTag {


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


    private String navFacetOrientation = null;
    public void setNavFacetOrientation(String navFacetOrientation) {
	this.navFacetOrientation = navFacetOrientation;
    }


    private String panelClass = null;
    public void setPanelClass(String panelClass) {
        this.panelClass = panelClass;
    }


    private String rowClasses = null;
    public void setRowClasses(String rowClasses) {
        this.rowClasses = rowClasses;
    }


    private int rowsPerPage = 0;
    private boolean rowsPerPageSet = false;
    public void setRowsPerPage(int rowsPerPage) {
	this.rowsPerPage = rowsPerPage;
        this.rowsPerPageSet = true;
    }


    private String scrollerControlsLocation = null;
    public void setScrollerControlsLocation(String scrollerControlsLocation) {
	this.scrollerControlsLocation = scrollerControlsLocation;
    }


    public String getComponentType() {
        return ("Panel");
    }


    public String getRendererType() {
        return ("ResultSet");
    }


    public void release() {
        super.release();
        this.columnClasses = null;
        this.footerClass = null;
        this.headerClass = null;
        this.navFacetOrientation = null;
        this.panelClass = null;
        this.rowClasses = null;
        this.rowsPerPage = 0;
        this.rowsPerPageSet = false;
        this.scrollerControlsLocation = null;
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
        if (navFacetOrientation != null) {
            component.getAttributes().put("navFacetOrientation", navFacetOrientation);
        }
        if (panelClass != null) {
            component.getAttributes().put("panelClass", panelClass);
        }
        if (rowClasses != null) {
            component.getAttributes().put("rowClasses", rowClasses);
        }
        if (rowsPerPageSet) {
            component.getAttributes().put("rowsPerPage", new Integer(rowsPerPage));
        }
        if (scrollerControlsLocation != null) {
            component.getAttributes().put("scrollerControlsLocation", 
				   scrollerControlsLocation);
        }


    }


}
