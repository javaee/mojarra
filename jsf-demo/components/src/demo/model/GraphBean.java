/*
 * $Id: GraphBean.java,v 1.3 2004/02/04 01:41:36 eburns Exp $
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

package demo.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.faces.event.ActionEvent;
import javax.faces.context.FacesContext;

import components.components.GraphComponent;
import components.model.Graph;
import components.model.Node;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <p>Backing file bean for TreeControl demo.</p>
 */

public class GraphBean {
    
   private static Log log = LogFactory.getLog(GraphBean.class);
   Graph menuGraph = null;
   Graph treeGraph = null;

   public GraphBean() {
   }

   public Graph getMenuGraph() { 
      // Construct a preconfigured customer list lazily.
      if ( menuGraph == null ) {
          Node root = new Node("Menu 2", "Menu", null, null, false, true);
          menuGraph = new Graph(root);

          Node file = new Node("File", "File 2", "/demo-test.faces", null, true, true);
          root.addChild(file);
          file.addChild(new Node("File-New", "New 2", "/demo-test.faces", null, true, false));
          file.addChild(new Node("File-Open", "Open 2", "/demo-test.faces", null, true, false));
          Node close = new Node("File-Close", "Close 2", "/demo-test.faces", null, false, false);
          file.addChild(close);
          file.addChild(new Node("File-Exit", "Exit 2", "/demo-test.faces", null, true, false));

          Node edit = new Node("Edit", "Edit 2", "/demo-test.faces", null, true, false);
          root.addChild(edit);
          edit.addChild(new Node("Edit-Cut", "Cut 2", "/demo-test.faces", null, true, false));
          edit.addChild(new Node("Edit-Copy", "Copy 2","/demo-test.faces", null, true, false));
          edit.addChild(new Node("Edit-Paste", "Paste 2", "/demo-test.faces", null, false, false));

          menuGraph.setSelected(close);
      }
      return menuGraph;
   }
    
   public void setMenuGraph(Graph newMenuGraph) { 
        this.menuGraph = newMenuGraph; 
   }
   
   public Graph getTreeGraph() { 
      // Construct a preconfigured Graph lazily.
      if ( treeGraph == null ) {
          Node root = new Node("Menu 4", "Menu 4", null, null, false, true);
          treeGraph = new Graph(root);

          Node file = new Node("File", "File 4", "/demo-test.faces", null, true, true);
          root.addChild(file);
          file.addChild(new Node("File-New", "New 4", "/demo-test.faces", null, true, false));
          file.addChild(new Node("File-Open", "Open 4", "/demo-test.faces", null, true, false));
          Node close = new Node("File-Close", "Close 4", "/demo-test.faces", null, false, false);
          file.addChild(close);
          file.addChild(new Node("File-Exit", "Exit 4", "/demo-test.faces", null, true, false));

          Node edit = new Node("Edit", "Edit 4", "/demo-test.faces", null, true, false);
          root.addChild(edit);
          edit.addChild(new Node("Edit-Cut", "Cut 4", "/demo-test.faces", null, true, false));
          edit.addChild(new Node("Edit-Copy", "Copy 4","/demo-test.faces", null, true, false));
          edit.addChild(new Node("Edit-Paste", "Paste 4", "/demo-test.faces", null, false, false));

          treeGraph.setSelected(close);
      }
      return treeGraph;
   }
    
   public void setTreeGraph(Graph newTreeGraph) { 
        this.treeGraph = newTreeGraph; 
   }
   
   /*
    * Processes the event queued on the graph component when a particular
    * node in the tree control is to be expanded or collapsed.
    */
   public void processGraphEvent(ActionEvent event) {
        if (log.isTraceEnabled()) {
            log.trace("TRACE: GraphBean.processGraphEvent ");
        }
        Graph graph = null;
        GraphComponent component = (GraphComponent)event.getSource();
        String path= (String) component.getAttributes().get("path");

        // Acquire the root node of the graph representing the menu
        graph = (Graph) component.getValue();
        if (graph == null) {
            if (log.isErrorEnabled()) {
                log.error("ERROR: Graph could not located in scope ");
            }
        }
        // Toggle the expanded state of this node
        Node node =  graph.findNode(path);
        if ( node == null ) {
            if (log.isErrorEnabled()) {
                log.error("ERROR: Node " + path + "could not be located. ");
            }
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

}
