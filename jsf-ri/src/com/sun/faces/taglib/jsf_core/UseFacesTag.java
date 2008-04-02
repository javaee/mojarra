/*
 * $Id: UseFacesTag.java,v 1.13 2003/05/03 04:08:45 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// UseFacesTag.java

package com.sun.faces.taglib.jsf_core;

import org.mozilla.util.Assert;
import org.mozilla.util.ParameterCheck;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.PageContext;
import javax.faces.context.FacesContext;
import javax.faces.tree.Tree;
import javax.servlet.http.HttpSession;
import com.sun.faces.RIConstants;

import com.sun.faces.util.Base64;
import com.sun.faces.util.Util;
import java.io.IOException;
import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectOutput;
import java.util.Map;
import javax.servlet.ServletConfig;
import javax.faces.webapp.JspResponseWriter;
import javax.servlet.jsp.JspWriter;
import javax.faces.webapp.UIComponentBodyTag;
import javax.faces.component.UIComponent;
import javax.faces.context.ResponseWriter;
import javax.faces.FacesException;
import javax.servlet.jsp.tagext.BodyTag;

/**
 *
 *  All JSF component tags must be nested within UseFacesTag.  This tag
 *  corresponds to the root of the UIComponent tree.  It does not have
 *  any renderers or attributes. It exists mainly to save the state of
 *  the response tree once all tags have been rendered.
 *
 * @version $Id: UseFacesTag.java,v 1.13 2003/05/03 04:08:45 eburns Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class UseFacesTag extends UIComponentBodyTag
{
    //
    // Protected Constants
    //

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

    // PENDING (visvan) extend from BodyTag instead of FacesBodyTag since
    // this tag handler doesn't correspond any component.
    public UseFacesTag()
    {
        super();
    }

    //
    // Class methods
    //

    // 
    // Accessors
    //

    //
    // General Methods
    //
    
    //
    // Methods from FacesBodyTag
    //

    protected int getDoStartValue() throws JspException {
        return BodyTag.EVAL_BODY_BUFFERED;
    }
    
    public int doAfterBody() throws JspException {
       
        // Look up the FacesContext instance for this request
        FacesContext facesContext = FacesContext.getCurrentInstance();
        if (facesContext == null) { 
            throw new JspException(Util.getExceptionMessage(Util.NULL_CONTEXT_ERROR_MESSAGE_ID));
        }
        
        // look up saveStateInClient parameter to check whether to save
        // state of tree in client or server. Default is server.
        String saveState = facesContext.getExternalContext().getInitParameter
                (RIConstants.SAVESTATE_INITPARAM);
        if ( saveState != null ) {
            Assert.assert_it (saveState.equalsIgnoreCase("true") || 
                saveState.equalsIgnoreCase("false"));
        }     
        if (saveState == null || saveState.equalsIgnoreCase("false")) {
            saveStateInSession(facesContext);
        } else {
            saveStateInPage(facesContext);           
        }
        return EVAL_PAGE;
    }    
    
    protected void saveStateInSession(FacesContext facesContext) 
            throws JspException {
        Map sessionMap = facesContext.getExternalContext().getSessionMap();
        sessionMap.put(RIConstants.REQUEST_LOCALE, facesContext.getLocale());
        sessionMap.put(RIConstants.FACES_TREE, facesContext.getTree() ); 
        // write buffered response to output. Since we are saving tree in session
        // no manipulation is necessary.
        try {
            getPreviousOut().write(getBodyContent().getString());
        } catch (Exception ioe) {
            ioe.printStackTrace();
            throw new JspException(ioe.getMessage());
        } 
        // com.sun.faces.util.DebugUtil.printTree(treeRoot, System.out); 
    }
    
    protected void saveStateInPage(FacesContext facesContext ) throws JspException {
        try {
            if ( getBodyContent() == null ) {
		Object params [] = { this.getClass().getName() };
                throw new JspException(Util.getExceptionMessage(Util.NULL_BODY_CONTENT_ERROR_MESSAGE_ID, params));
            }    
            // long beginTime = System.currentTimeMillis();
            // replace the marker in the buffered response, with response tree's
            // state info. To do this we we first serialize the tree and encode
            // it using Apache's utility and write it to the page using an
            // hidden field.
            String content = getBodyContent().getString();
            int markeridx = content.indexOf(RIConstants.SAVESTATE_MARKER);
            if ( markeridx == -1 ) {
                 getPreviousOut().write(content);
                 return;
            }
          
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutput out = new ObjectOutputStream(bos);
            out.writeObject(facesContext.getTree());
            //write out the locale.
            out.writeObject(facesContext.getLocale());
            out.close();
            
            String facesTree = " <input type=\"hidden\" name=\"" 
                   + RIConstants.FACES_TREE +  "\"" + " value=\"" +
            new String(Base64.encode(bos.toByteArray()), "ISO-8859-1")  + "\" />";
            
            StringBuffer sb = new StringBuffer(content);
            int markeridxend = markeridx + ((RIConstants.SAVESTATE_MARKER).length());
            sb.replace( markeridx, markeridxend, facesTree);   
            content = sb.toString(); 
            // write the buffered response along with the state information
            // to output.
            getPreviousOut().write(content);
            // PENDING(visvan): If we wanted to track time, here is where we'd do it
            // long endTime = System.currentTimeMillis();
            // System.out.println("Time to render output " + (endTime-beginTime));
        } catch (IOException iox) {
            iox.printStackTrace();
	    Object [] params = { "session", iox.getMessage() };
            throw new JspException(Util.getExceptionMessage(Util.SAVING_STATE_ERROR_MESSAGE_ID, params));
        }  
    }    

    /**

    * This should never get called for UseFacesTag.

    */ 

    public String getComponentType() {
	Assert.assert_it(false);
	throw new IllegalStateException();
    }    

    protected boolean isSuppressed() {
	return true;
    }

    public String getRendererType() {
        return null;
    }

    protected int getDoEndValue() throws JspException {
        return (EVAL_PAGE);
    }
    
} // end of class UseFacesTag
