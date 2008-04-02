/*
 * $Id: UseFacesTag.java,v 1.4 2003/01/17 18:07:23 rkitain Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
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
import java.io.IOException;
import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectOutput;
import javax.servlet.ServletConfig;
import javax.faces.webapp.JspResponseWriter;
import javax.servlet.jsp.JspWriter;
import javax.faces.webapp.FacesBodyTag;
import javax.faces.component.UIComponent;
import javax.faces.context.ResponseWriter;
import javax.faces.FacesException;
import javax.servlet.jsp.tagext.BodyTag;

/**
 *
 *  All JSF component tags must be nested within UseFacesTag. This tag
 * does not have any renderers or attributes. It exists mainly to
 * save the state of the response tree once all tags have been rendered.
 *
 * @version $Id: UseFacesTag.java,v 1.4 2003/01/17 18:07:23 rkitain Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class UseFacesTag extends FacesBodyTag
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
    public int doStartTag() throws JspException {
        return BodyTag.EVAL_BODY_BUFFERED;
    }
    
    public int doAfterBody() throws JspException {
       
        // Look up the FacesContext instance for this request
        FacesContext facesContext = (FacesContext)
            pageContext.getAttribute(FacesContext.FACES_CONTEXT_ATTR,
                                     PageContext.REQUEST_SCOPE);
        if (facesContext == null) { // FIXME - i18n
            throw new JspException("Cannot find FacesContext");
        }
        
        // look up saveStateInClient parameter to check whether to save
        // state of tree in client or server. Default is server.
        String saveState = facesContext.getServletContext().getInitParameter
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
        HttpSession session = facesContext.getHttpSession();
        session.setAttribute(RIConstants.REQUEST_LOCALE, facesContext.getLocale());
        session.setAttribute(RIConstants.FACES_TREE, facesContext.getTree() ); 
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
                throw new JspException("BodyContent is null ");
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
            throw new JspException("Error while saving state in session : " + 
                    iox.getMessage());
        }  
    }    
    
    public UIComponent createComponent() {
        return null;
    }    

    public String getRendererType() {
        return null;
    }
    
    public int doEndTag() throws JspException {
        this.numChildren = 0;
        this.childIndex = 0;
        return (EVAL_PAGE);
    }
    
} // end of class UseFacesTag
