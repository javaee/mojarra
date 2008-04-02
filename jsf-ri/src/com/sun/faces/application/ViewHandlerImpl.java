/* 
 * $Id: ViewHandlerImpl.java,v 1.14 2003/10/07 19:53:08 rlubke Exp $ 
 */ 


/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */


// ViewHandlerImpl.java 

package com.sun.faces.application; 

import com.sun.faces.RIConstants;
import com.sun.faces.util.Util;

import javax.faces.FacesException;
import javax.faces.application.Application;
import javax.faces.application.StateManager;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/** 
 * <B>ViewHandlerImpl</B> is the default implementation class for ViewHandler. 
 * @version $Id: ViewHandlerImpl.java,v 1.14 2003/10/07 19:53:08 rlubke Exp $ 
 * 
 * @see javax.faces.application.ViewHandler 
 * 
 */ 
public class ViewHandlerImpl extends Object
        implements ViewHandler { 
    
    // 
    // Private/Protected Constants
    //
    private static final Log log = LogFactory.getLog(ViewHandlerImpl.class);

    //
    // Relationship Instance Variables
    // 

    protected StateManager stateManagerImpl = null;
    
    /**
     * <p>List of url-patterns defined for the FacesServlet.</p>
     */ 
    protected List facesServletMappings;
    
    /**
     * <p>Map to cache the processed results of getViewIdPath.</p>
     */ 
    private Map viewIdPathMap;
            

    public ViewHandlerImpl() {
	    stateManagerImpl = new StateManagerImpl();
        viewIdPathMap = new HashMap();
    }

    public void renderView(FacesContext context, 
			   UIViewRoot viewToRender) throws IOException, 
             FacesException { 

        if (null == context || null == viewToRender) { 
            throw new NullPointerException(Util.getExceptionMessage(
                Util.NULL_CONTEXT_ERROR_MESSAGE_ID));
        } 
        Application application = context.getApplication();
        if (application instanceof ApplicationImpl) {
            ((ApplicationImpl) application).responseRendered();
        }
        String requestURI = viewToRender.getViewId();
        
        if (!isPrefixMapped(getFacesMapping(context))) {            
            String suffixToUse = context.getExternalContext().
                getInitParameter(ViewHandler.DEFAULT_SUFFIX_PARAM_NAME);
            
            if (suffixToUse == null) {
                suffixToUse = ViewHandler.DEFAULT_SUFFIX;
            }
            
            // if the viewId doesn't already use the above suffix,
            // replace or append.
            if (!requestURI.endsWith(suffixToUse)) {
                StringBuffer buffer = new StringBuffer(requestURI);
                int extIdx = requestURI.lastIndexOf('.');
                if (extIdx != -1) {
                    buffer.replace(extIdx, requestURI.length(), suffixToUse);
                } else {
                    // no extension in the provided viewId, append the suffix
                    buffer.append(suffixToUse);
                }
                requestURI = buffer.toString();
            }
        }
        
        context.getExternalContext().dispatchMessage(requestURI);

    }

    public StateManager getStateManager() {
	return stateManagerImpl;
    }
    
    public UIViewRoot restoreView(FacesContext context, String viewId) {
        if (context == null) {
            throw new NullPointerException(Util.getExceptionMessage(
                Util.NULL_CONTEXT_ERROR_MESSAGE_ID));
        }

        UIViewRoot viewRoot = getStateManager().restoreView(context, viewId);

        if ( viewRoot == null) {
            viewRoot = new UIViewRoot();
            context.renderResponse();
        }
        viewRoot.setViewId(viewId);
        return viewRoot;
    }

    public UIViewRoot createView(FacesContext context, String viewId) {
        if (context == null) {
            throw new NullPointerException(Util.getExceptionMessage(
                Util.NULL_CONTEXT_ERROR_MESSAGE_ID));
        }

	UIViewRoot result = new UIViewRoot();
	result.setViewId(viewId);
	// PENDING(): not sure if we should set the RenderKitId here.
	// The UIViewRootBase ctor sets the renderKitId to the default
	// one.
	return result;
    }

    public void writeState(FacesContext context) throws IOException {
        if (context == null) {
            throw new NullPointerException(Util.getExceptionMessage(
								    Util.NULL_CONTEXT_ERROR_MESSAGE_ID));
        }
	if (getStateManager().isSavingStateInClient(context)) {
	    context.getResponseWriter().writeText(RIConstants.SAVESTATE_FIELD_MARKER,null);
	}
    }
    
    
    public String getViewIdPath(FacesContext context, String viewId) {             
        
        if (context == null || viewId == null) {
            throw new NullPointerException(Util.getExceptionMessage(
                Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }               
        
        if (viewId.charAt(0) != '/') {            
            throw new IllegalArgumentException(Util.getExceptionMessage(
                Util.ILLEGAL_VIEW_ID_ID,
                new Object[] { viewId }));
        }
        
        // Check our cache for a processed result        
        String mapping = getFacesMapping(context);
        Object cachedMapping = viewIdPathMap.get(mapping + viewId);
        if (cachedMapping != null) {
            return (String) cachedMapping;
        }
        
        // No previous processed value
        StringBuffer buffer = new StringBuffer(viewId);        
        
        
        if (isPrefixMapped(mapping)) {
            // prefix path mapping
            
            // if the mapping returned is only "/*", then the 
            // return the viewId with no modifications
            if (mapping.equals("/*")) {                
                return viewId;                
            } else {                                                          
                buffer.insert(0, mapping);                
            }
        } else {
            // extension mapping            
            
            // only replace or append if the viewId doesn't already
            // end with the mapping.
            if (!viewId.endsWith(mapping)) {
                int extIdx = viewId.lastIndexOf('.');
                if (extIdx != -1) {
                    buffer.replace(extIdx, viewId.length(), mapping);
                } else {
                    // no extension in the provided viewId, append
                    // the current value
                    buffer.append(mapping);
                }
            }
        }
        
        String result = buffer.toString();
        viewIdPathMap.put(mapping + viewId, result);
        return result;
    }

    /**
     * <p>Specifies a <code>List</code> of one or more URL patterns 
     * mapped to one or more {@link javax.faces.webapp.FacesServlet} 
     * instances.</p>
     * 
     * @param mappings the URL patterns of the 
     *  defined {@link javax.faces.webapp.FacesServlet}   
     * 
     * @exception NullPointerException if <code>mappings</code> is null 
     */ 
    public void setFacesMapping(List mappings) {
        
        if (mappings == null) {
            throw new NullPointerException(Util.getExceptionMessage(
                Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }    
            
        facesServletMappings = mappings;
    }

    /**
     * <p>Returns the URL pattern of the 
     * {@link javax.faces.webapp.FacesServlet} that
     * is executing the current request.  If there are multiple
     * URL patterns, the value returned by 
     * <code>HttpServletRequest.getServletPath()</code> is 
     * used to determine which mapping to return.</p>
     * 
     * @param context the {@link FacesContext} of the current request
     *  
     * @return the URL pattern of the {@link javax.faces.webapp.FacesServlet}
     * 
     * @exception NullPointerException if <code>context</code> is null
     * 
     * @exception FacesException if no mapping can be determined 
     *  based on the current request
     */ 
    private String getFacesMapping(FacesContext context) {
        // PENDING (rlubke) Need to handle the Portlet case
        
        if (context == null) {
            throw new NullPointerException(Util.getExceptionMessage(
                Util.NULL_CONTEXT_ERROR_MESSAGE_ID));
        }
        
        // If we only have one entry in the List,
        // return it.
        if (facesServletMappings.size() == 1) {
            return (String) facesServletMappings.get(0);
        }
        
        // pull the servlet path out of the HttpServletRequest and
        // use it to get the appropriate mapping
        Object request = context.getExternalContext().getRequest();
        String servletPath = null;
        if (request instanceof HttpServletRequest) {
            servletPath = ((HttpServletRequest) request).getServletPath();
        }
        
        String mapping = getMappingForRequest(servletPath);
        if (mapping == null) {
            String message = Util.getExceptionMessage(
                Util.FACES_SERVLET_MAPPING_CANNOT_BE_DETERMINED_ID,
                new Object[] { servletPath });
            if (log.isErrorEnabled()) {
                log.error(message);
            }            
            throw new FacesException(message);
        }
        
        return mapping;
        
    }
    
    /**
     * <p>Return the appropriate {@link javax.faces.webapp.FacesServlet} mapping
     * based on the servlet path of the current request.</p>
     * 
     * @param servletPath the <code>Servlet</code> that was invoked
     * 
     * @see HttpServletRequest#getServletPath()
     */ 
    private String getMappingForRequest(String servletPath) {
        
        if (servletPath == null) {
            return null;
        }                
                             
        String mapping = null;
        
        // If the path returned by HttpServletRequest.getServletPath()
        // returns a zero-length String, then the FacesServlet has
        // been mapped to '/*'.
        if (servletPath.length() == 0) {
            int idx = facesServletMappings.indexOf("/*");
            if (idx != 1) {
                return "/*";
            } else {
                // Shouldn't happen...
                return null;
            }
        }
        
        int extIdx = servletPath.lastIndexOf('.');
        if (extIdx != -1) {
            
            // Servlet could have been invoked using extension mapping. 
            // Check the mappings in our List for a matching
            // extension mapping.
            
            String extension = servletPath.substring(extIdx);            
            
            for (int i = 0, size = facesServletMappings.size();
                 i < size; i++) {               
                String temp = (String) facesServletMappings.get(i);               
                if (extension.equals(temp)) {                    
                    mapping = temp;
                    break;
                }
            }
            
        } 
        
        if (mapping == null) {
            
            // Servlet invoked using a prefix path as no extension
            // was matched.  Check the mappings in our List for a matching path.
            for (int i = 0, size = facesServletMappings.size();
                 i < size; i++) {                

                String temp = (String) facesServletMappings.get(i);
  
                if (servletPath.equals(temp)) {
                    mapping = temp;
                    break;
                }
            }
        }
        
        return mapping;
    }
    
    /**
     * </p>Returns true if the provided <code>url-mapping</code> is
     * a prefix path mapping (starts with <code>/</code>).</p>
     * @param mapping a <code>url-pattern</code>
     * @return true if the mapping starts with <code>/</code>
     */    
    private static boolean isPrefixMapped(String mapping) {
        return (mapping.charAt(0) == '/');
    }
        
} 
