/*
 * $Id: LifecycleDriver.java,v 1.1 2002/03/13 17:59:32 eburns Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces;

import java.io.IOException;

/**
 * The interface to be implemented by a servlet which services
 * a JavaServer Faces page.  This interface separates a request
 * cycle into two distinct phases:
 * <ol>
 * <li>wire-up: constructs the UI component tree associated with the page
 * <li>execute-lifecycle: executes the lifecycle on the UI component tree
 *     which ultimately results in sending the response
 * </ol>
 * For a description of lifecycle processing:
 * @see javax.faces.LifecycleStage
 */
public interface LifecycleDriver {

    /**
     * Invoked from the <code>service()</code> method to construct
     * the UI component tree associated with the request.  When
     * this method completes, the UI component tree must be fully
     * instantiated.  
     * @param ctx the FacesContext object used to process this request
     * @throws IOException
     * @return TreeNavigator corresponding to the root of the UI
     *         component tree associated with the request
     */
    public TreeNavigator wireUp(FacesContext ctx) throws IOException;

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
