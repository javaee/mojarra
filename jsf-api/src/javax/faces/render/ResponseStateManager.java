/*
 * $Id: ResponseStateManager.java,v 1.7 2003/09/13 12:57:46 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.render;

import javax.faces.FacesException;
import javax.faces.context.FacesContext;
import javax.faces.application.StateManager.SerializedView;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;



/**
 * <p><strong>ResponseStateManager</strong> is the helper class to
 * {@link javax.faces.application.StateManager} that knows the specific
 * rendering technology being used to generate the response.  It is a
 * singleton abstract class, vended by the {@link RenderKit}.  This
 * class knows the mechanics of saving state, whether it be in hidden
 * fields, session, or some combination of the two.</p>
 */

public abstract class ResponseStateManager {

    /**
     *       
     * <p>Take the argument content buffer and replace the state markers
     * that we've written using writeStateMarker() with the appropriate
     * representation of the structure and state, writing the output to
     * the output writer.</p>
     *
     * <p>If the structure and state are to be written out to hidden
     * fields, the implementation must take care to make all necessary
     * character replacements to make the Strings suitable for inclusion
     * as an HTTP request paramater.</p>
     *
     * @return the written state.  In the case of JSP, this is a
     * <code>String</code>.
     *
     */
    public abstract void writeState(FacesContext context,
				    SerializedView state) throws IOException;
    
    /**

    * <p>The implementation must inspect the current request and return
    * the tree structure Object passed to it on a previous invocation of
    * <code>writeState()</code>.</p>

    * @return the tree structure Object passed in to
    * <code>writeState</code>.  If this is the initial request, this
    * method returns null.

    */

    public abstract Object getTreeStructureToRestore(FacesContext context, 
						     String viewId);

    /**

    * <p>The implementation must inspect the current requst and return
    * the component state Object passed to it on a previous invocation
    * of <code>writeState()</code>.</p>

    * @return the component state Object passed in to
    * <code>writeState</code>.

    */

    public abstract Object getComponentStateToRestore(FacesContext context);
}
