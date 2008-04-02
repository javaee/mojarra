/*
 * $Id: GraphMenuTreeTag.java,v 1.1 2003/02/14 01:08:33 jvisvanathan Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package components.taglib;

import components.model.Graph;
import javax.faces.component.UIComponent;
import javax.faces.webapp.FacesTag;
import javax.servlet.jsp.JspException;
import components.components.GraphComponent;
import javax.faces.context.FacesContext;

/**
 * This class creates a <code>Graph</code> instance if there is no modelReference
 * attribute specified on the component, represented by this tag and
 * stores it against the attribute name "graph_tree" in session scope.
 */
public class GraphMenuTreeTag extends FacesTag {

    protected String graphClass = null;
    protected String selectedClass = null;
    protected String unselectedClass = null;
    
    public UIComponent createComponent() {
        return (new GraphComponent());
    }


    public String getRendererType() {
        return ("MenuTree");
    }
    
    /**
     * The CSS style <code>class</code> to be applied to the text
     * of selected nodes.
     */
    public String getSelectedClass() {
        return (this.selectedClass);
    }

    public void setSelectedClass(String styleSelected) {
        this.selectedClass = styleSelected;
    }


    /**
     * The CSS style <code>class</code> to be applied to the text
     * of unselected nodes.
     */
    public String getUnselectedClass() {
        return (this.unselectedClass);
    }

    public void setUnselectedClass(String styleUnselected) {
        this.unselectedClass = styleUnselected;
    }

    /**
     * The CSS style <code>class</code> to be applied to the entire tree.
     */
    public String getGraphClass() {
        return (this.graphClass);
    }

    public void setGraphClass(String style) {
        this.graphClass = style;
    }
    
    protected void overrideProperties(UIComponent component) {
        super.overrideProperties(component);
       
        if ((graphClass != null) &&
            (component.getAttribute("graphClass") == null)) {
            component.setAttribute("graphClass", graphClass);
        }
        if ((selectedClass != null) &&
            (component.getAttribute("selectedClass") == null)) {
            component.setAttribute("selectedClass", selectedClass);
        }
        if ((unselectedClass != null) &&
            (component.getAttribute("unselectedClass") == null)) {
            component.setAttribute("unselectedClass", unselectedClass);
        }
        
        // if there is no modelReference attribute set on this tag, then
        // we need to build the graph.
        if ( getModelReference() == null ) {
            component.setModelReference("sessionScope.graph_tree");
            Graph graph = (Graph) context.getModelValue(component.getModelReference());
            // In the postback case, graph exists already. So make sure
            // it doesn't created again.
            if ( graph == null ) {
                graph = new Graph();
                context.setModelValue(component.getModelReference(), graph);
            }
        }
    }


}
