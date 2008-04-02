/*
 * $Id: ScrollerComponent.java,v 1.1 2003/10/28 20:11:00 jvisvanathan Exp $
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

package components.components;

import java.io.IOException;

import java.util.List;
import java.util.Map;

import javax.faces.component.UIData;
import javax.faces.component.UIForm;
import javax.faces.component.UICommand;
import javax.faces.event.ActionEvent;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import java.util.MissingResourceException;

/**
 * This component produces a search engine style scroller that facilitates
 * easy navigation over results that span across several pages.
 */
public class ScrollerComponent extends UICommand {
   
    public static final String NORTH = "NORTH";
    public static final String SOUTH = "SOUTH";
    public static final String EAST = "EAST";
    public static final String WEST = "WEST";
    public static final String BOTH = "BOTH";
    
    public static final int ACTION_NEXT = -1;
    public static final int ACTION_PREVIOUS = -2;
    public static final int ACTION_NUMBER = -3;
 
    public static final String FORM_NUMBER_ATTR = "com.sun.faces.FormNumber";
    
    /**
     * The component attribute that tells where to put the user supplied
     * markup in relation to the "jump to the Nth page of results"
     * widget.
     */
    public static final String FACET_MARKUP_ORIENTATION_ATTR = 
	"navFacetOrientation";

    public ScrollerComponent() {
        super();
        this.setRendererType(null);
    }
    
    public void decode(FacesContext context) {
        String curPage = null;
	String action = null;
	int actionInt = 0;
	int currentPage = 1;
        int currentRow = 1;
        String clientId = getClientId(context);
        Map requestParameterMap = (Map) context.getExternalContext().
                getRequestParameterMap();
        action = (String) requestParameterMap.get(clientId + "_action");
        if ( action == null || action.length() == 0 ) {
            // nothing to decode
            return;
        }
	
        this.getAttributes().put("action", action);
        curPage = (String) requestParameterMap.get(clientId + "_curPage");
        currentPage = Integer.valueOf(curPage).intValue();

        // Assert that action's length is 1.
        switch (actionInt = Integer.valueOf(action).intValue()) {
        case ACTION_NEXT:
            currentPage++;
            break;
        case ACTION_PREVIOUS:
            currentPage--;
            // Assert 1 < currentPage
            break;
        default:
            currentPage = actionInt;
            break;
        } 
        // from the currentPage, calculate the current row to scroll to.
        currentRow = (currentPage - 1) * getRowsPerPage(context);
        this.getAttributes().put("currentPage", new Integer(currentPage));
        this.getAttributes().put("currentRow", new Integer(currentRow));
        this.queueEvent(new ActionEvent(this));
    }

    public void encodeBegin(FacesContext context) throws IOException {
	return;
    }

    public void encodeEnd(FacesContext context) throws IOException {
        int currentPage = 1;
        
        ResponseWriter writer = context.getResponseWriter();

        String clientId = getClientId(context);
	Integer curPage = (Integer)getAttributes().get("currentPage");
        if ( curPage != null) {
            currentPage = curPage.intValue();
        }
	int totalPages = getTotalPages(context);
        
	writer.write("<table border=\"0\" cellpadding=\"0\" align=\"center\">");
	writer.write("<tr align=\"center\" valign=\"top\">");
	writer.write("<td><font size=\"-1\">Result&nbsp;Page:&nbsp;</font></td>");

	// write the Previous link if necessary
	writer.write("<td>");
	writeNavWidgetMarkup(context, clientId, ACTION_PREVIOUS,
			     (1 < currentPage));
	// last arg is true iff we're not the first page
	writer.write("</td>");

	// render the page navigation links
	int i = 0;
	int first = 1;
	int last = totalPages;
	
	if (10 < currentPage) {
	    first = currentPage - 10;
	}
	if ((currentPage + 9) < totalPages) {
	    last = currentPage + 9;
	}
	for (i = first; i <= last; i++) {
	    writer.write("<td>");
	    writeNavWidgetMarkup(context, clientId, i, (i != currentPage));
	    writer.write("</td>");
	}
	    
	// write the Next link if necessary
	writer.write("<td>");
	writeNavWidgetMarkup(context, clientId, ACTION_NEXT,
			     (currentPage < totalPages));
	writer.write("</td>");
	writer.write("</tr>");
	writer.write(getHiddenFields(clientId));
	writer.write("</table>");
    }

    public boolean getRendersChildren() { 
        return true; 
    }
     
    //
    // Helper methods
    // 

    /**
     * Write the markup to render a navigation widget.  Override this to
     * replace the default navigation widget of link with something
     * else.
     */
     protected void writeNavWidgetMarkup(FacesContext context,
					String clientId,
					int navActionType,
					boolean enabled) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
	String facetOrientation = NORTH;
	String facetName = null;
	String linkText = null;
	String localLinkText = null;
	UIComponent facet = null;
	boolean isCurrentPage = false;
	boolean isPageNumber = false;

	// Assign values for local variables based on the navActionType
	switch (navActionType) {
	case ACTION_NEXT:
	    facetName = "next";
	    linkText = "Next";
	    break;
	case ACTION_PREVIOUS:
	    facetName = "previous";
	    linkText = "Previous";
	    break;
	default:
	    facetName = "number";
	    linkText = "" + navActionType;
	    isPageNumber = true;
	    // heuristic: if navActionType is number, and we are not
	    // enabled, this must be the current page.
	    if (!enabled) {
		facetName = "current";
		isCurrentPage = true;
	    }
	    break;
	}

	// leverage any navigation facets we have 
	writer.write("\n&nbsp;");
        if (enabled) {
	    writer.write("<a " + getAnchorAttrs(context, clientId, 
						navActionType) + ">");
        }
        
        facet = getFacet(facetName);
	// render the facet pertaining to this widget type in the NORTH
	// and WEST cases.
        if (facet != null) {
	    // If we're rendering a "go to the Nth page" link
	    if (isPageNumber) {
		// See if the user specified an orientation
		String facetO = (String) getAttributes().get(FACET_MARKUP_ORIENTATION_ATTR);
		if (facet != null) {
		    facetOrientation = facetO;
		    // verify that the orientation is valid
		    if (!(facetOrientation.equalsIgnoreCase(NORTH) || 
                          facetOrientation.equalsIgnoreCase(SOUTH) || 
                          facetOrientation.equalsIgnoreCase(EAST) || 
                          facetOrientation.equalsIgnoreCase(WEST))) {
			  facetOrientation = NORTH;
		    }
		}
	    }

	    // output the facet as specified in facetOrientation
	    if (facetOrientation.equalsIgnoreCase(NORTH) || 
		facetOrientation.equalsIgnoreCase(EAST)) {
		facet.encodeBegin(context);
		if (facet.getRendersChildren()) {
		    facet.encodeChildren(context);
		}
		facet.encodeEnd(context);
	    }
	    // The difference between NORTH and EAST is that NORTH
	    // requires a <br>.
	    if (facetOrientation.equalsIgnoreCase(NORTH)) {
		writer.startElement("br", null); // PENDING(craigmcc)
		writer.endElement("br");
	    }
	}
	
	// if we have a facet, only output the link text if
	// navActionType is number
	if (null != facet) {
	    if (navActionType != ACTION_NEXT && 
		navActionType != ACTION_PREVIOUS) {
		writer.write(linkText);
	    }
	}
	else {
	    writer.write(linkText);
	}

	// output the facet in the EAST and SOUTH cases
	if (null != facet) {
	    if (facetOrientation.equalsIgnoreCase(SOUTH)) {
		writer.startElement("br", null); // PENDING(craigmcc)
		writer.endElement("br");
	    }
	    // The difference between SOUTH and WEST is that SOUTH
	    // requires a <br>.
	    if (facetOrientation.equalsIgnoreCase(SOUTH) || 
		facetOrientation.equalsIgnoreCase(WEST)) {	    
		facet.encodeBegin(context);
		if (facet.getRendersChildren()) {
		    facet.encodeChildren(context);
		}
		facet.encodeEnd(context);
	    }
	}

	if (enabled) {
	    writer.write("</a>");
	}

    }

    /**
     * <p>Build and return the string consisting of the attibutes for a
     * result set navigation link anchor.</p>
     * @param context the FacesContext
     * @param clientId the clientId of the enclosing UIComponent
     * @param action the value for the rhs of the =
     * @return a String suitable for setting as the value of a navigation
     * href.
     */
     private String getAnchorAttrs(FacesContext context, String clientId,  
             int action) {
        int currentPage = 1;                           
	int formNumber = getFormNumber(context, getForm(context));
        Integer curPage = (Integer)getAttributes().get("currentPage");
        if ( curPage != null) {
            currentPage = curPage.intValue();
        }
	String result = 
	    "href=\"#\" " + 
	    "onmousedown=\"" + 
	    "document.forms[" + formNumber + "]['" + clientId + "_action'].value='" + action + "'; " + 
	    "document.forms[" + formNumber + "]['" + clientId + "_curPage'].value='" + currentPage + "'; " +
	    "document.forms[" + formNumber + "].submit()\"";

	return result;
    }

    private String getHiddenFields(String clientId) {
	String result = 
	    "<input type=\"hidden\" name=\"" + clientId + "_action\"/>\n" +
	    "<input type=\"hidden\" name=\"" + clientId + "_curPage\"/>";

	return result;
    }

    // PENDING(edburns): avoid doing this each time called.  Perhaps
    // store in our own attr?
    protected UIForm getForm(FacesContext context) {
        UIComponent parent = this.getParent();
        while (parent != null) {
            if (parent instanceof UIForm) {
                break;
            }
            parent = parent.getParent();
        }
	return (UIForm) parent;
    }

    protected int getFormNumber(FacesContext context, UIForm form) {
	// If we don't have a form, return 0
	if (null == form) {
	    return 0;
	}
	Integer formsInt = (Integer) 
	    form.getAttributes().get(FORM_NUMBER_ATTR);

	return formsInt.intValue();
    }

    /**
     * Returns the total number of pages in the result set based on 
     * <code>rows</code> and <code>rowCount</code> of <code>UIData</code> 
     * component that this scroller is associated with.
     * For the purposes of this demo, we are assuming the <code>UIData</code> to 
     * be child of <code>UIForm</code> component and not nested inside a custom
     * NamingContainer.
     */
    protected int getTotalPages(FacesContext context) {
        String forValue = (String) getAttributes().get("for");
	UIData uiData = (UIData) getForm(context).findComponent(forValue);
        if ( uiData == null) {
            return 0;
        }
	int rowsPerPage = uiData.getRows();
	int totalRows = 0;
	int result = 0;
        totalRows = uiData.getRowCount();
	result = totalRows / rowsPerPage;
	if (0 != (totalRows % rowsPerPage)) {
	    result++;
	}
	return result;
    }
    
    /**
     * Returns the number of rows to display by looking up the 
     * <code>UIData</code> component that this scroller is associated with.
     * For the purposes of this demo, we are assuming the <code>UIData</code> to 
     * be child of <code>UIForm</code> component and not nested inside a custom
     * NamingContainer.
     */
    protected int getRowsPerPage(FacesContext context) {
       String forValue = (String) getAttributes().get("for");
       UIData uiData = (UIData) getForm(context).findComponent(forValue);
        if (uiData == null ) {
            return 0;
        }
	return uiData.getRows();
    }
} 
