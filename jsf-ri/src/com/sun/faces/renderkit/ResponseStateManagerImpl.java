/*
 * $Id: ResponseStateManagerImpl.java,v 1.1 2003/08/23 00:39:07 jvisvanathan Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */


package com.sun.faces.renderkit;

import javax.faces.render.ResponseStateManager;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import java.io.IOException;
import java.io.Writer;
import java.io.Reader;

import com.sun.faces.RIConstants;
import com.sun.faces.util.Util;

import org.mozilla.util.Assert;

/**
 *
 *  <B>RenderKitImpl</B> is a class ...
 *
 */

public class ResponseStateManagerImpl extends ResponseStateManager {

    //
    // Protected Constants
    //

    //
    // Class Variables
    //

    //
    // Instance Variables
    //

    //
    // Ivars used during actual client lifetime
    //

    // Relationship Instance Variables

    
    //
    // Constructors and Initializers    
    //

    public ResponseStateManagerImpl() {
        super();
    }


    //
    // Class methods
    //

    //
    // General Methods
    //

    //
    // Methods From ResponseStateManager
    //

    public Object getComponentStateToRestore(FacesContext context) {
        // PENDING (visvan) implement saveStateInPage in the next phase
        // of state saving.
        return null;
    }
    
    public Object getTreeStructureToRestore(FacesContext context, 
        String treeId) {
        // PENDING (visvan) implement saveStateInPage in the next phase
        // of state saving.
        return null;
    }
    
    public void writeState(Reader content, Writer out, Object structure, 
        Object state) {
        // PENDING (visvan) implement saveStateInPage in the next phase
        // of state saving.
    }
    
    public void writeStateMarker(FacesContext context) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        Assert.assert_it(writer != null);
        // if we are saving state in page, insert a marker into buffer so that 
        // UseFaces tag can replace it state information.
        boolean isSaveStateInPage = Util.isSaveStateInPage(context);
        if ( isSaveStateInPage){
	    writer.writeText(RIConstants.SAVESTATE_FIELD_MARKER,null);
        }
    }
    

} // end of class ResponseStateManagerImpl

