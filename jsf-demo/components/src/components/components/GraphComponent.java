/*
 * $Id: GraphComponent.java,v 1.2 2003/02/21 23:44:49 ofung Exp $
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
