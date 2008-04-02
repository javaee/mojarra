/*
 * $Id: ButtonAction.java,v 1.3 2003/03/27 19:44:06 jvisvanathan Exp $
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

// ButtonAction.java

package nonjsp.lifecycle;

import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;
import javax.faces.tree.TreeFactory;
import javax.faces.FactoryFinder;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.mozilla.util.Assert;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 *
 * <B>ButtonAction</B> is a class that implements an ActionListener.
 * When a specific component fires an event the listener grabs the
 * event and handles it appropriately. In this case, a new xul page
 * is loaded when a button is pressed.
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: ButtonAction.java,v 1.3 2003/03/27 19:44:06 jvisvanathan Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class ButtonAction implements ActionListener {
    //
    // Protected Constants
    //

    // Log instance for this class
    protected static Log log = LogFactory.getLog(ButtonAction.class);

    //
    // Class Variables
    //

    //
    // Instance Variables
    //

    // Attribute Instance Variables

    // Relationship Instance Variables

    //
    // Constructors and Initializers    
    //

    public ButtonAction() {
    }

    //
    // Class methods
    //

    //
    // General Methods
    //

    
    // This listener will process events after the phase specified.
    
    public PhaseId getPhaseId() {
        return PhaseId.ANY_PHASE;
    }

    public void processAction(ActionEvent event) {
        trace("Action.processAction : actionCommand : "+
            event.getActionCommand());

        //set the component tree to next xul page
        String treeId = null;

	FacesContext context = FacesContext.getCurrentInstance();
	if (event.getActionCommand().equals("Hello")) {
		treeId = "/hi.xul";
            }
        trace("Action: TreeId: " + treeId);

        if (null != treeId) {
            TreeFactory treeFactory = (TreeFactory)
                FactoryFinder.getFactory(FactoryFinder.TREE_FACTORY);
            Assert.assert_it(null != treeFactory);
            context.setTree(treeFactory.getTree(context,treeId));

            Map requestMap = context.getExternalContext().getRequestMap();
            requestMap.put("javax.servlet.include.path_info", treeId);

	    context.renderResponse();
        }

    }

    //Logging
    private void trace(String msg) {
	if (log.isTraceEnabled()) {
            log.trace(msg);
	}
    }

} // end of class ButtonAction
