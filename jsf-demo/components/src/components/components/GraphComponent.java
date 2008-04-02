/*
 * $Id: GraphComponent.java,v 1.5 2003/08/27 23:15:39 eburns Exp $
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


import components.model.Graph;
import components.model.Node;
import components.renderkit.Util;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import javax.faces.FacesException;
import javax.faces.component.base.UIOutputBase;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.FacesEvent;
import javax.faces.event.FacesListener;
import javax.faces.event.PhaseId;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * Component wrapping a {@link Graph} object that is pointed at by the
 * local value or value reference expression.  This component supports
 * the processing of a {@link ActionEvent} that will toggle the expanded
 * state of the specified {@link Node} in the {@link Graph}.
 */

public class GraphComponent extends UIOutputBase {


    private static Log log = LogFactory.getLog(GraphComponent.class);


    public GraphComponent() {
        GraphListener listener = new GraphListener();
        addFacesListener(listener);    
    }   
    
    // PENDING(craigmcc) - reflect the "untangling broadcast" changes
    // invokes listener method passing event as argument 
    public boolean broadcast(FacesEvent event, PhaseId phaseId)
        throws AbortProcessingException {

        if ((event == null) || (phaseId == null)) {
            throw new NullPointerException();
        }
        if (phaseId.equals(PhaseId.ANY_PHASE)) {
            throw new IllegalStateException();
        }
        if (event instanceof GraphEvent) {
            if (listeners == null) {
                return (false);
            }
            GraphEvent aevent = (GraphEvent) event;
            int ordinal = phaseId.getOrdinal();
            broadcast(aevent, listeners[PhaseId.ANY_PHASE.getOrdinal()]);
            broadcast(aevent, listeners[ordinal]);
            for (int i = ordinal + 1; i < listeners.length; i++) {
                if ((listeners[i] != null) && (listeners[i].size() > 0)) {
                    return (true);
                }
            }
            return (false);
        } else {
            throw new IllegalArgumentException();
        }

    }


    // PENDING(craigmcc) - reflect the "untangling broadcast" changes
    protected void broadcast(GraphEvent event, List list) {

        if (list == null) {
            return;
        }
        Iterator listeners = list.iterator();
        while (listeners.hasNext()) {
            GraphListener listener = 
                (GraphListener) listeners.next();
            listener.processGraphEvent(event);
        }
    }


    /**
     * <p>Faces Listener implementation which toggles the selected Node
     * in the GraphComponent;</p>
     */
    public class GraphListener implements FacesListener {

        public GraphListener() {
        }
        
        // Processes the event queued on the graph component.
        public void processGraphEvent(GraphEvent event) {
            Graph graph = null;
            GraphComponent component = (GraphComponent)event.getSource();
            String path= (String) event.getPath();

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
    }    
}
