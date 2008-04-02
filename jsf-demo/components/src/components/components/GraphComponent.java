/*
 * $Id: GraphComponent.java,v 1.1 2003/02/14 01:08:25 jvisvanathan Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package components.components;


import java.io.IOException;
import java.util.Iterator;
import javax.faces.FacesException;
import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;
import components.model.Graph;
import components.model.Node;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import javax.faces.component.UICommand;
import java.util.Iterator;
import javax.faces.event.PhaseId;
import javax.faces.context.FacesContext;
import javax.faces.FacesException;


/**
 * Component wrapping a {@link Graph} object that is pointed at by the
 * local value or model reference expression.  This component supports
 * the processing of a {@link ActionEvent} that will toggle the expanded
 * state of the specified {@link Node} in the {@link Graph}.
 */

public class GraphComponent extends UICommand {


    private static Log log = LogFactory.getLog(GraphComponent.class);

    // Component type for this component
    public static final String TYPE = "GraphComponent";

    public GraphComponent() {
        addActionListener(new ActionListener() {
            // Processes the event queued on the graph component.
            public void processAction(ActionEvent event) {

                Graph graph = null;
                GraphComponent component = (GraphComponent)event.getSource();
                String path= (String) event.getActionCommand();

                // Acquire the root node of the graph representing the menu
                FacesContext context = FacesContext.getCurrentInstance();
                graph = (Graph) component.currentValue(context);
                if (graph == null) {
                    throw new FacesException("Graph could not be located");
                }
                // Toggle the expanded state of this node
                Node node =  graph.findNode(path);
                if ( node == null ) {
                    // PENDING (visvan) log error.
                    return;
                }    
                boolean current = node.isExpanded();
                node.setExpanded(!current);
                if (!current) {
                    Node parent = node.getParent();
                    if (parent != null) {
                        Iterator kids = parent.getChildren();
                        while (kids.hasNext()) {
                            Node kid = (Node) kids.next();
                            if (kid != node) {
                                kid.setExpanded(false);
                            }
                        }
                    }
                }
            }
        
            // This listener will handle events after the phase specified
            // as the return value;
            public PhaseId getPhaseId() {
                return PhaseId.ANY_PHASE;
            }
        });    
    }    
    
    // Return our component type
    public String getComponentType() {
        return (TYPE);
    }

    // Ignore update model requests
    public void updateModel(FacesContext context) {
    }


}
