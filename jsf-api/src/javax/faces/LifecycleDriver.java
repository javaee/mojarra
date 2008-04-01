/*
 * $Id: LifecycleDriver.java,v 1.2 2002/03/15 23:29:20 eburns Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces;

import java.io.IOException;

/**
 * The interface which encapsulates the processing of a JSF request.  
 * This interface separates a request cycle into two distinct tasks:
 * <ol>
 * <li>wireUp: constructs the UI component tree associated with the page
 * <li>executeLifecycle: executes the lifecycle on the UI component tree
 *     which ultimately results in generating a response
 * </ol>
 * For a description of lifecycle processing:
 * @see javax.faces.LifecycleStage
 */
public interface LifecycleDriver {

    /**
     * Invoked to construct the UI component tree associated with 
     * the request.  When this method completes, the UI component 
     * tree corresponding to the request must be fully instantiated.  
     * @param ctx the FacesContext object used to process this request
     * @param root the root of the component tree
     * @throws IOException
     * @return TreeNavigator corresponding to the root of the UI
     *         component tree associated with the request
     */
    public TreeNavigator wireUp(FacesContext ctx, UIPage root) throws IOException;

    /**
     * Invoked from the <code>service()</code> method to execute
     * the lifecycle of the specified UI component tree.  This
     * method must be invoked after <code>wireUp()</code>.
     * @param ctx the FacesContext object used to process this request
     * @throws IOException
     */
    public void executeLifecycle(FacesContext ctx, 
				 TreeNavigator treeNavigator) throws FacesException, IOException;

}
