/*
 * $Id: ResponseStateManager.java,v 1.3 2003/07/31 12:22:23 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.render;

import javax.faces.FacesException;
import javax.faces.context.FacesContext;

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

    * <p>This method causes a special marker, the contents of which are
    * only interpreted by this <code>ResponseStateManager</code>
    * implementation, to be immediately written to the
    * <code>ResponseWriter</code> for this request.  This marker
    * encapsulates the state of the tree.  This method must be called
    * once per <code>UIForm</code> instance in the tree.</p>

    */

    public abstract void writeStateMarker(FacesContext context) throws IOException;

    /**

    * <p>Take the argument content buffer and replace the state markers
    * that we've written using writeStateMarker() with the appropriate
    * representation of the structure and state, writing the output to
    * the output writer.</p>

    * <p>If the structure and state are to be written out to hidden
    * fields, the implementation must take care to make all necessary
    * character replacements to make the Strings suitable for inclusion
    * as an HTTP request paramater.</p>

    */

    public abstract void writeState(Reader content, Writer out,
				    Object structure, Object state);

    /**

    * <p>The implementation must inspect the current request and return
    * the tree structure Object passed to it on a previous invocation of
    * <code>writeState()</code>.</p>

    * @return the tree structure Object passed in to
    * <code>writeState</code>.  If this is the initial request, this
    * method returns null.

    */

    public abstract Object getTreeStructureToRestore(FacesContext context, 
						     String treeId);

    /**

    * <p>The implementation must inspect the current requst and return
    * the tree state Object passed to it on a previous invocation of
    * <code>writeState()</code>.</p>

    * @return the tree Object string passed in to
    * <code>writeState</ncode>.

    */

    public abstract Object getTreeStateToRestore(FacesContext context);
}
