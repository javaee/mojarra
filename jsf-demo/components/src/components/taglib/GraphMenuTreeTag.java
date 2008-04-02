/*
 * $Id: GraphMenuTreeTag.java,v 1.7 2003/11/09 22:45:56 jvisvanathan Exp $
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

import components.model.Graph;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.webapp.UIComponentBodyTag;
import javax.servlet.jsp.JspException;
import components.components.GraphComponent;
import components.renderkit.Util;

/**
 * This class creates a <code>Graph</code> instance if there is no modelReference
 * attribute specified on the component, represented by this tag and
 * stores it against the attribute name "graph_tree" in session scope.
 */
public class GraphMenuTreeTag extends UIComponentBodyTag {

    protected String action_listener = null;
    protected String graphClass = null;
    protected String selectedClass = null;
    protected String unselectedClass = null;
    protected String valueRef = null;
    
    public String getComponentType() {
        return ("Graph");
    }


    public String getRendererType() {
        return ("MenuTree");
    }
    
    public String getValueRef()
    {
	return valueRef;
    }
    
    public void setValueRef(String newValueRef)
    {
	valueRef = newValueRef;
    }
    
    /**
     * Optional listener to handle tree expansion and contraction events
     */
    public String getAction_listener() {
	return (this.action_listener);
    }

    public void setAction_listener(String action_listener) {
	this.action_listener = action_listener;
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
        FacesContext context = FacesContext.getCurrentInstance();
        ValueBinding vb = null;
        
        GraphComponent graphComponent = (GraphComponent)component;
        if ((action_listener != null) &&
            (component.getAttributes().get("action_listener") == null)) {
            component.getAttributes().put("action_listener", action_listener);
        }

        if ((graphClass != null) &&
            (component.getAttributes().get("graphClass") == null)) {
            component.getAttributes().put("graphClass", graphClass);
        }
        if ((selectedClass != null) &&
            (component.getAttributes().get("selectedClass") == null)) {
            component.getAttributes().put("selectedClass", selectedClass);
        }
        if ((unselectedClass != null) &&
            (component.getAttributes().get("unselectedClass") == null)) {
            component.getAttributes().put("unselectedClass", unselectedClass);
        }
        if ( valueRef != null) {
            vb = context.getApplication().getValueBinding(valueRef);
            component.setValueBinding("value", vb); 
        }
        
        // if there is no valueRef attribute set on this tag, then
        // we need to build the graph.
        if ( valueRef == null ) {
            vb = context.getApplication().getValueBinding("sessionScope.graph_menu");
            component.setValueBinding("value", vb); 
           
            // In the postback case, graph exists already. So make sure
            // it doesn't created again.
            Graph graph = (Graph) ((GraphComponent)component).getValue();
            if ( graph == null ) {
                graph = new Graph();
                vb.setValue(context, graph);
            }    
        } 
    }


}
