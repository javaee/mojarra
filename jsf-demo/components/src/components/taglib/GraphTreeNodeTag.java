/*
 * $Id: GraphTreeNodeTag.java,v 1.1 2003/02/14 01:08:34 jvisvanathan Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// GraphTreeNodeTag.java

package components.taglib;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import components.model.Graph;
import components.model.Node;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.webapp.FacesBodyTag;
import javax.servlet.jsp.tagext.BodyTag;

/**
 *
 * <B>GraphMenuNodeTag</B> builds the graph as the nodes are processed.
 * This tag creates a node with specified properties. Locates the parent of 
 * this node by using the node name from its immediate parent tag of the 
 * type GraphTreeNodeTag. If the parent could not be located, then the created
 * node is assumed to be root.
 */

public class GraphTreeNodeTag extends FacesBodyTag {
    
    //
    // Protected Constants
    //

    //
    // Class Variables
    //

    //
    // Instance Variables
    //

    // Attribute Instance Variables

    private String name = null;
    private String icon = null;
    private String label =null;
    private String action = null;
    private boolean expanded;
    private boolean enabled = true;
   
    // Relationship Instance Variables

    //
    // Constructors and Initializers    
    //

    public GraphTreeNodeTag() {
        super();
    }

    //
    // Class methods
    //

    //
    // General Methods
    //
    public String getName() {
        return (this.name);
    }

    public void setName(String name) {
        this.name = name;
    }


    public boolean getExpanded() {
        return (this.expanded);
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }


    public String getIcon() {
        return (this.icon);
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getLabel() {
        return (this.label);
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean getEnabled() {
        return (this.enabled);
    }

    public String getAction() {
        return (this.action);
    }

    public void setAction(String action) {
        this.action = action;
    }

    public UIComponent createComponent() {
        return null;
    }
    
    public String getRendererType() {
        return null;
    }
    
    //
    // Methods from FacesBodyTag
    //
    public int doStartTag() throws JspException {
        
        FacesContext context = FacesContext.getCurrentInstance();
        Graph graph =(Graph) context.getModelValue("sessionScope.graph_tree");
        
        // In the postback case, graph and the node exist already.So make sure
        // it doesn't created again.
        if ( graph.findNodeByName(getName()) != null) {
            return BodyTag.EVAL_BODY_BUFFERED;
        }    
        Node node = new Node( getName(),getLabel(), getAction(), getIcon(), 
                 getEnabled(), getExpanded());
        
        // get the immediate ancestor/parent tag of this tag.
        GraphTreeNodeTag parentNode = null;
        try {
            parentNode = (GraphTreeNodeTag) findAncestorWithClass(this,
                GraphTreeNodeTag.class);
        } catch ( Exception e ) {
            System.out.println("Exception while locating GraphTreeNodeTag.class");
        }    
        // if this tag has no parent that is a node tag,
        if ( parentNode == null ) {
            // then this should be root
            graph.setRoot( node );
        } else {
            // add the node to its parent node.
            Node nodeToAdd = graph.findNodeByName( parentNode.getName());
            // this node should exist
            if ( nodeToAdd != null ) {
                nodeToAdd.addChild( node );
            }
        }
        
        return BodyTag.EVAL_BODY_BUFFERED;
    }
    
    public int doEndTag() throws JspException {
        return (EVAL_PAGE);
    }
  
}
    

