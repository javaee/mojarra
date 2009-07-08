
/*
 * $Id: ELFlash.java,v 1.6 2005/12/16 21:32:36 edburns Exp $
 */

/*
 * The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the License). You may not use this file except in
 * compliance with the License.
 *
 * You can obtain a copy of the License at
 * https://javaserverfaces.dev.java.net/CDDL.html or
 * legal/CDDLv1.0.txt.
 * See the License for the specific language governing
 * permission and limitations under the License.
 *
 * When distributing Covered Code, include this CDDL
 * Header Notice in each file and include the License file
 * at legal/CDDLv1.0.txt.
 * If applicable, add the following below the CDDL Header,
 * with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 *
 * [Name of File] [ver.__] [Date]
 *
 * Copyright 2005 Sun Microsystems Inc. All Rights Reserved
 */


package com.sun.faces.context.flash;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.faces.event.PhaseId;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * <p>A Map implementation that provides semantics identical to the <a
 * target="_"
 * href="http://api.rubyonrails.com/classes/ActionController/Flash.html">
 * "flash" concept in Ruby on Rails</a>.</p>
 * 
 * <p>Usage</p>
 *
 * <p>See {@link FlashELResolver} for usage instructions.</p>
 * 
 * <p>Methods on this Map have different effects depending on the
 * current lifecycle phase when the method is invoked.  During all
 * lifecycle phases earlier than invoke-application, methods on this Map
 * are directed to an internal Map that is cleared at the end of the
 * current lifecycle run.  For invoke-application and beyond, all
 * operations are directed to an internal Map that will be accessible
 * for all lifecycle phases on the <b>next</b> request on this session,
 * up to, but not including, invoke-application.</p>
 *
 * @author edburns
 */
public class ELFlash extends Flash {
    
    private Map<String,Map<String, Object>> innerMap = null;
    
    /** Creates a new instance of ELFlash */
    private ELFlash() {
        // We only need exactly two entries.
        innerMap = new ConcurrentHashMap<String,Map<String, Object>>(2);
    }

    /**
     * <p>Returns the flash <code>Map</code> for this session.  This is
     * a convenience method that calls
     * <code>FacesContext.getCurrentInstance()</code> and then calls the
     * overloaded <code>getFlash()</code> that takes a
     * <code>FacesContext</code> with it.</p>
     * 
     * @return The flash <code>Map</code> for this session.
     */
    
    public static Map<String,Object> getFlash() {
        FacesContext context = FacesContext.getCurrentInstance();
        return getFlash(context.getExternalContext(), true);
    }

    /**
     *
     * @param extContext the <code>ExternalContext</code> for this request.
     *
     * @param create <code>true</code> to create a new instance for this request if 
     * necessary; <code>false</code> to return <code>null</code> if there's no 
     * instance in the current <code>session</code>.
     * 
     * @return The flash <code>Map</code> for this session.
     */
    
    public static Flash getFlash(ExternalContext extContext, boolean create) {
        Map<String, Object> appMap = extContext.getApplicationMap();
        ELFlash flash = (ELFlash) 
            appMap.get(Constants.FLASH_ATTRIBUTE_NAME);
        if (null == flash && create) {
            synchronized (extContext.getContext()) {
                if (null == (flash = (ELFlash)
                    appMap.get(Constants.FLASH_ATTRIBUTE_NAME))) {
                    flash = new ELFlash();
                    appMap.put(Constants.FLASH_ATTRIBUTE_NAME, flash);
                }
            }
        }
        return flash;
    }
    
    public static ELFlash getELFlash() {
        return (ELFlash) getFlash();
    }

    public boolean isKeepMessages() {
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext extContext = context.getExternalContext();
        Map<String, Object> requestMap = extContext.getRequestMap();
        Map<String, Object> cookieMap = extContext.getRequestCookieMap();
        Object response = extContext.getResponse();
        Boolean value = (Boolean) requestMap.get(Constants.FLASH_KEEP_ALL_REQUEST_SCOPED_DATA_ATTRIBUTE);
        if (null == value) {
            if (response instanceof HttpServletResponse) {
                Cookie redirectCookie = (Cookie) cookieMap.get(Constants.FLASH_KEEP_ALL_REQUEST_SCOPED_DATA_ATTRIBUTE);
                if (null != redirectCookie) {
                    value = Boolean.TRUE;
                    HttpServletResponse servletResponse = (HttpServletResponse) response;
                    redirectCookie.setMaxAge(0);
                    servletResponse.addCookie(redirectCookie);
                } else {
                    value = Boolean.FALSE;
                }
                
                requestMap.put(Constants.FLASH_KEEP_ALL_REQUEST_SCOPED_DATA_ATTRIBUTE, value);
                
            }
        }
        
        return (value != null  && value);
    }
    
    public void setKeepMessages(boolean newValue) {
        FacesContext context = FacesContext.getCurrentInstance();
        Map<String, Object> requestMap = context.getExternalContext().getRequestMap();
        Boolean newBoolean = newValue ? Boolean.TRUE : Boolean.FALSE;
        Boolean oldBoolean = (!requestMap.containsKey(Constants.FLASH_KEEP_ALL_REQUEST_SCOPED_DATA_ATTRIBUTE)) ?
            null : (Boolean) requestMap.get(Constants.FLASH_KEEP_ALL_REQUEST_SCOPED_DATA_ATTRIBUTE);

        // If the value changed from null to true or from false to true,
        // set a cookie
        if ((null == oldBoolean && newValue) ||
            (Boolean.FALSE == oldBoolean && newValue)) {
            ExternalContext extContext = context.getExternalContext();
            HttpServletResponse servletResponse;
            //PortletRequest portletRequest = null;
            Object response = extContext.getResponse();
            if (response instanceof HttpServletResponse) {
                servletResponse = (HttpServletResponse) response;
                Cookie cookie = new Cookie(Constants.FLASH_KEEP_ALL_REQUEST_SCOPED_DATA_ATTRIBUTE, "t");
                cookie.setMaxAge(-1);
                servletResponse.addCookie(cookie);
            } else {
                /*****
                 * portletRequest = (PortletRequest) request;
                 * // You can't add a cookie in portlet.
                 * // http://wiki.java.net/bin/view/Portlet/JSR168FAQ#How_can_I_set_retrieve_a_cookie
                 * portletRequest.getPortletSession().setAttribute(Constants.FLASH_POSTBACK_REQUEST_ATTRIBUTE_NAME,
                 * thisRequestSequenceString, PortletSession.PORTLET_SCOPE);
                 *********/
            }
            
        }

        requestMap.put(Constants.FLASH_KEEP_ALL_REQUEST_SCOPED_DATA_ATTRIBUTE,
                newBoolean);
    }
    
    public boolean isRedirect() {
        boolean result;
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext extContext = context.getExternalContext();
        Map<String, Object> requestMap = extContext.getRequestMap();
        Map<String, Object> cookieMap = extContext.getRequestCookieMap();
        Object response = extContext.getResponse();
        Boolean value = (Boolean) requestMap.get(Constants.REDIRECT_AFTER_POST_ATTRIBUTE_NAME);
        if (value == null) {
            if (response instanceof HttpServletResponse) {
                Cookie redirectCookie = (Cookie) cookieMap.get(Constants.REDIRECT_AFTER_POST_ATTRIBUTE_NAME);
                if (null != redirectCookie) {
                    value = Boolean.TRUE;
                    HttpServletResponse servletResponse = (HttpServletResponse) response;
                    redirectCookie.setMaxAge(0);
                    servletResponse.addCookie(redirectCookie);
                } else {
                    value = Boolean.FALSE;
                }
                
                requestMap.put(Constants.REDIRECT_AFTER_POST_ATTRIBUTE_NAME, value);
                
            }
        }
        result = (value != null && value);
        return result;
    }
    
    public void setRedirect(boolean newValue) {
        FacesContext context = FacesContext.getCurrentInstance();
        Map<String, Object> requestMap = context.getExternalContext().getRequestMap();
        Boolean newBoolean = newValue ? Boolean.TRUE : Boolean.FALSE;
        Boolean oldBoolean = (!requestMap.containsKey(Constants.REDIRECT_AFTER_POST_ATTRIBUTE_NAME)) ?
            null : (Boolean) requestMap.get(Constants.REDIRECT_AFTER_POST_ATTRIBUTE_NAME);
        
        // If the value changed from null to true or from false to true,
        // set a cookie
        if ((null == oldBoolean && newValue) ||
            (Boolean.FALSE == oldBoolean && newValue)) {
            ExternalContext extContext = context.getExternalContext();
            HttpServletResponse servletResponse;
            //PortletRequest portletRequest = null;
            Object response = extContext.getResponse();
            if (response instanceof HttpServletResponse) {
                servletResponse = (HttpServletResponse) response;
                Cookie cookie = new Cookie(Constants.REDIRECT_AFTER_POST_ATTRIBUTE_NAME, "t");
                cookie.setMaxAge(-1);
                servletResponse.addCookie(cookie);
                // Need special handling of the keepMessages functionality
                // in the case of a redirect.  Normally the keepMessages cookie
                // is set to expire on the next request, but for the redirect
                // we need to keep the cookie for the GET that will be issued
                // by the user-agent.  After the user-agent has sent the
                // keepMessages cookie, it will be expired properly on the next
                // request.
                // Note that this has the side effect of this branch is that
                // the keepMessages cookie is added again to the response
                // (too bad there is no API to access cookies that have already
                // been added).
                if (isKeepMessages()) {
                    Cookie keepMessages = new Cookie(Constants.FLASH_KEEP_ALL_REQUEST_SCOPED_DATA_ATTRIBUTE, "t");
                    keepMessages.setMaxAge(-1);
                    servletResponse.addCookie(keepMessages);
                }
            } else {
                /*****
                 * portletRequest = (PortletRequest) request;
                 * // You can't add a cookie in portlet.
                 * // http://wiki.java.net/bin/view/Portlet/JSR168FAQ#How_can_I_set_retrieve_a_cookie
                 * portletRequest.getPortletSession().setAttribute(Constants.FLASH_POSTBACK_REQUEST_ATTRIBUTE_NAME,
                 * thisRequestSequenceString, PortletSession.PORTLET_SCOPE);
                 *********/
            }
            
        }
        requestMap.put(Constants.REDIRECT_AFTER_POST_ATTRIBUTE_NAME, newBoolean);
    }
    
    
    /**
     *
     * <p>The following datum are moved from request scope into the flash
     * to be made available on a subsequent call to 
     * {@link #restoreAllMessages}.</p>
     *
     * <ul>
     *
     *  <li><p>All request scoped attributes</p></li>
     *
     *  <li><p>All <code>FacesMessage</code> instances queued on the
     *   <code>FacesContext</code></p></li>
     *
     * </ul>
     * @param context Context for request
     */
    
    void saveAllMessages(FacesContext context) {
        ExternalContext extContext = context.getExternalContext();
        Map<String, Object> requestMap = extContext.getRequestMap();
        Boolean thisRequestIsGetAfterRedirectAfterPost;

        if (null != (thisRequestIsGetAfterRedirectAfterPost = (Boolean)
                     requestMap.get(Constants.THIS_REQUEST_IS_GET_AFTER_REDIRECT_AFTER_POST_ATTRIBUTE_NAME))
            && thisRequestIsGetAfterRedirectAfterPost) {
            return;
        }
                
        Iterator<String> messageClientIds = context.getClientIdsWithMessages();
        List<FacesMessage> facesMessages;
        Map<String, List<FacesMessage>> allFacesMessages = null;
        Iterator<FacesMessage> messageIter;
        String curMessageId;
        
        // Save all the FacesMessages into a Map, which we store in the flash.
        
        // Step 1, go through the FacesMessage instances for each clientId
        // in the messageClientIds list.
        while (messageClientIds.hasNext()) {
            curMessageId = messageClientIds.next();
            // Get the messages for this clientId
            messageIter = context.getMessages(curMessageId);
            facesMessages = new ArrayList<FacesMessage>();
            while (messageIter.hasNext()) {
                facesMessages.add(messageIter.next());
            }
            // Add the list to the map
            if (null == allFacesMessages) {
                allFacesMessages = new HashMap<String, List<FacesMessage>>();
            }
            allFacesMessages.put(curMessageId, facesMessages);
        }
        facesMessages = null;
        
        // Step 2, go through the FacesMessages that do not have a client
        // id associated with them.
        messageIter = context.getMessages(null);
        while (messageIter.hasNext()) {
            // Make sure to overwrite the previous facesMessages list
            facesMessages = new ArrayList<FacesMessage>();
            facesMessages.add(messageIter.next());
        }
        if (null != facesMessages) {
            // Add the list to the map
            if (null == allFacesMessages) {
                allFacesMessages = new HashMap<String, List<FacesMessage>>();
            }
            allFacesMessages.put(null, facesMessages);
        }
        boolean isRedirect = this.isRedirect();
        if (null != allFacesMessages) {
            if (isRedirect) {
                this.getMapForCookie(context, true).put(Constants.FACES_MESSAGES_ATTRIBUTE_NAME,
                        allFacesMessages);
                
            } else {
                this.putNext(Constants.FACES_MESSAGES_ATTRIBUTE_NAME,
                        allFacesMessages);
            }
        }
    }
    
    void restoreAllMessages(FacesContext context) {
        Map<String, Object> requestMap = context.getExternalContext().getRequestMap();
        
        Map<String, List<FacesMessage>> allFacesMessages;
        List<FacesMessage> facesMessages;
        Map<String,Object> map;

        if (null != (map = getMapForCookie(context, false))) {
            //noinspection unchecked
            if (null != (allFacesMessages = (Map<String, List<FacesMessage>>)
                         map.get(Constants.FACES_MESSAGES_ATTRIBUTE_NAME))) {
                for (Map.Entry<String, List<FacesMessage>> cur : allFacesMessages.entrySet()) {
                    if (null != (facesMessages = allFacesMessages.get(cur.getKey()))) {
                        for (FacesMessage curMessage : facesMessages) {
                            context.addMessage(cur.getKey(), curMessage);
                        }
                    }
                }
                boolean isGET = false;

                Object request = context.getExternalContext().getRequest();
                // to a servlet JSF app...
                if (request instanceof HttpServletRequest) {
                    isGET = ((HttpServletRequest)request).getMethod().equalsIgnoreCase("GET");
                } else {
                    isGET = true;
                    /******
                     * PortletRequest portletRequest = null;
                     * portletRequest = (PortletRequest) request;
                     *******/
                }

                if (isGET) {
                    requestMap.put(Constants.THIS_REQUEST_IS_GET_AFTER_REDIRECT_AFTER_POST_ATTRIBUTE_NAME,
                            Boolean.TRUE);
                }
            }
            map.remove(Constants.FACES_MESSAGES_ATTRIBUTE_NAME);
        }
    }
    
    /**
     * <p>Returns the correct Map considering the current lifecycle phase.</p>
     *
     * <p>If the current lifecycle phase is
     * render-response, call {@link #getNextRequestMap} and return the boolResult.
     * Otherwise, call {@link #getThisRequestMap} and return the boolResult.</p>
     *
     * @return the "correct" map for the current lifecycle phase.
     */
    
    protected Map<String,Object> getPhaseMap() {
        Map<String,Object> result;
        FacesContext context = FacesContext.getCurrentInstance();
        PhaseId currentPhase = context.getCurrentPhaseId();
        // If we're in render-response phase..., 
        // or this is an initial request (not a postback),
        // or this is the get from the redirect after post...
        if (currentPhase == PhaseId.RENDER_RESPONSE || 
            (!context.isPostback()) ||
            this.isRedirect()) {
            // make operations go to the next request Map.
            result = getNextRequestMap(context);
        }
        else {
            // Otherwise, make operations go this request Map.
            result = getThisRequestMap(context);
        }
        return result;
    }
    
    /**
     * @param context for the request
     * @return the Map flash for the next postback.  This Map is used by the
     * flash for all operations during the render-response lifecycle phase.  
     * During all other lifecycle phases, operations go instead to the Map
     * returned by {@link #getThisRequestMap}.
     *
     */
    private Map<String,Object> getNextRequestMap(FacesContext context) {
        return getMapForSequenceId(context, 
                Constants.FLASH_THIS_REQUEST_ATTRIBUTE_NAME);
    }
    
    /**
     * @param context for the request
     * @return the Map flash for this postback.  This Map is used by the flash
     * for all operations during all lifecycle phases except render-response.
     * During render-response, any flash operations go instead to the Map returned
     * by {@link #getNextRequestMap}.
     */
    private Map<String,Object> getThisRequestMap(FacesContext context) {
        return getMapForSequenceId(context,
                Constants.FLASH_POSTBACK_REQUEST_ATTRIBUTE_NAME);
    }    
    
    /**
     * <p>This is a private helper method for {@link #getNextRequestMap} and 
     * {@link #getThisRequestMap}.
     *
     * @param context Context of the request
     * @param attrName Name of the attribute to get the sequence for.
     *
     * @return the sequence Map given the sequence identifier.
     */
    
    private Map<String,Object> getMapForSequenceId(FacesContext context, String attrName) {
        Object sequenceId = context.getExternalContext().
                getRequestMap().get(attrName);
        if (null == sequenceId) {
            return null;
        }
        
        Map<String,Object> result = innerMap.get(sequenceId.toString());
        if (null == result) {
            result = new HashMap<String,Object>(4);
            innerMap.put(sequenceId.toString(), result);
        }
        
        return result;
    }
    
    private Map<String,Object> getMapForCookie(FacesContext context, boolean create) {
        String cookieName = 
                getCookieValue(context.getExternalContext());
        Map<String,Object> result = null;
        
        if (null != cookieName) {
            result = innerMap.get(cookieName);
            if (result == null && create) {
                result = new HashMap<String,Object>(4);
                innerMap.put(cookieName, result);
            }
        }
        
        return result;
    }
    
    
    /**
     * <p>Called by the {@link #doPostPhaseActions(javax.faces.context.FacesContext)} for the 
     * render-response phase to clear out the flash for the appropriate
     * sequence.
     * @param sequence Sequence to clear
     */
        
    void expireEntriesForSequence(String sequence) {
        Map<String, Object> toExpire = innerMap.get(sequence);
        if (null != toExpire) {
            toExpire.clear();
            innerMap.remove(sequence);
        }
    }
    
    /**
     * <p>Get the correct map as descibed above and perform this operation on
     * it.</p>
     */

    public Object remove(Object key) {
        return getPhaseMap().remove(key);
    }

    /**
     * <p>Get the correct map as descibed above and perform this operation on
     * it.</p>
     */

    public boolean containsKey(Object key) {
        return getPhaseMap().containsKey(key);
    }

    /**
     * <p>Get the correct map as descibed above and perform this operation on
     * it.</p>
     */

    public boolean containsValue(Object value) {
        return getPhaseMap().containsValue(value);
    }

    /**
     * <p>Get the correct map as descibed above and perform this operation on
     * it.</p>
     */

    public boolean equals(Object obj) {
        return obj instanceof Map && getPhaseMap().equals(obj);
    }
    
    /**
     * <p>This operation takes special care to handle the "keep" operation on 
     * the flash.  Consider the expression #{flash.keep.foo}.  A set operation
     * on this expression will simply end up calling the {@link #put} method.
     * During a get operation on this expression, we first look in the Map for  
     * the currently posting back request.  If no value is found, we look in
     * the request scope for the current request.  If a value is found there, we
     * place it in the map for the next request.  We return the value.</p>
     *
     * <p>In the case of a non "keep" operation, this method simply returns
     * the value from the Map for the currently posting back request.</p>
     */

    public Object get(Object key) {
        FacesContext context = FacesContext.getCurrentInstance();        
        Map<String,Object> map = this.isRedirect() ? getMapForCookie(context, false) : getThisRequestMap(context);
        Object requestValue;
        Object result = null;

        if (null != key) {
            if (key.equals("keepMessages")) {
                result = this.isKeepMessages();
            }
            if (key.equals("redirect")) {
                result = this.isRedirect();
            }
        }
        
        if (null == result) {
            // For gets, look in the postbackSequenceMap.
            if (null != map) {
                result = map.get(key);
            }
            // If not found in the postbackSequenceMap and we're doing a "keep" promotion...
            if (null == result && FlashELResolver.isDoKeep()) {
                // See if we have a value in the request scope.
                if (null != (requestValue =
                        FacesContext.getCurrentInstance().getExternalContext().
                        getRequestMap().get(key))) {
                    // get the value from the request scope
                    result = requestValue;
                }
            }
            // If this resolution is for a keep promotion...
            if (FlashELResolver.isDoKeep()) {
                FlashELResolver.setDoKeep(false);
                // Do the promotion.
                getNextRequestMap(context).put((String) key, result);
            }
        }
        return result;
    }

    @Override
    public void keep(String key) {
        FlashELResolver.setDoKeep(true);
        this.get(key);
        
    }

    @Override
    public void putNow(String key, Object value) {
        FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put(key, value);
    }
    
    /**
     * <p>Get the correct map as descibed above and perform this operation on
     * it.</p>
     */

    public void putAll(Map<? extends String, ?> t) {
        getPhaseMap().putAll(t);
    }

    /**
     * <p>Get the correct map as descibed above and perform this operation on
     * it.</p>
     */

    public Object put(String key, Object value) {
        Boolean boolResult = null;
        Object result;
        if (null != key) {
            if (key.equals("keepMessages")) {
                if (null != value && value.toString().equals("true")) {
                    this.setKeepMessages(boolResult = true);
                } else {
                    this.setKeepMessages(boolResult = false);
                }
                
            }
            if (key.equals("redirect")) {
                if (null != value && value.toString().equals("true")) {
                    this.setRedirect(boolResult = true);
                } else {
                    this.setRedirect(boolResult = false);
                }
            }
        }
        if (null == boolResult) {
            result = getPhaseMap().put(key, value);
        } else {
            result = boolResult;
        }
        return result; 
    }
    
    /**
     * <p>Get the correct map as descibed above and perform this operation on
     * it.</p>
     * @param key key to put
     * @param value value to put
     * @return object put
     */

    public Object putNext(String key, Object value) {
        FacesContext context = FacesContext.getCurrentInstance();
        return getNextRequestMap(context).put(key, value);
    }
    
    /**
     * <p>Get the correct map as descibed above and perform this operation on
     * it.</p>
     */

    public java.util.Collection<Object> values() {
        return getPhaseMap().values();
    }

    /**
     * <p>Get the correct map as descibed above and perform this operation on
     * it.</p>
     */

    public String toString() {
        return getPhaseMap().toString();
    }

    /**
     * <p>Get the correct map as descibed above and perform this operation on
     * it.</p>
     */

    public int size() {
        return getPhaseMap().size();
    }

    /**
     * <p>Get the correct map as descibed above and perform this operation on
     * it.</p>
     */

    public void clear() {
        getPhaseMap().clear();
    }

    /**
     * @throws CloneNotSupportedException
     */

    @SuppressWarnings({"CloneDoesntCallSuperClone"})
    protected Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    /**
     * <p>Get the correct map as descibed above and perform this operation on
     * it.</p>
     */

    public java.util.Set<java.util.Map.Entry<String, Object>> entrySet() {
        return getPhaseMap().entrySet();
    }

    /**
     * <p>Get the correct map as descibed above and perform this operation on
     * it.</p>
     */

    public int hashCode() {
        return getPhaseMap().hashCode();
    }

    /**
     * <p>Get the correct map as descibed above and perform this operation on
     * it.</p>
     */

    public boolean isEmpty() {
        return getPhaseMap().isEmpty();
    }

    /**
     * <p>Get the correct map as descibed above and perform this operation on
     * it.</p>
     */

    public java.util.Set<String> keySet() {
        return getPhaseMap().keySet();
    }


    /**
     * <p>Perform actions that need to happen on the
     * <code>afterPhase</code> event.</p>
     *
     * <p>For after restore-view, if this is a postback, we extract the
     * sequenceId from the request and store it in the request
     * scope.</p>
     *
     * <p>For after render-response, we clear out the flash for the
     * postback, while leaving the current one intact. </p>
     */
    @Override
    public void doPostPhaseActions(FacesContext context) {

        ExternalContext extContext = context.getExternalContext();
        ELFlash elFlash = ELFlash.getELFlash();

        if (PhaseId.RENDER_RESPONSE.equals(context.getCurrentPhaseId())) {
            expireEntries(context);
        }

        // If this requset is ending normally...
        if (PhaseId.RENDER_RESPONSE.equals(context.getCurrentPhaseId())) {
            // and the user requested we save all request scoped data...
            if (elFlash != null && elFlash.isKeepMessages()) {
                // save it all.
                elFlash.saveAllMessages(context);
            }
        }
        // Otherwise, if this request is ending early...
        else if ((context.getResponseComplete() ||
                  context.getRenderResponse()) &&
                  elFlash.isRedirect()) {
            // and the user requested we save all request scoped data...
            if (elFlash.isKeepMessages()) {
                // save it all.
                addCookie(extContext, elFlash);
                elFlash.saveAllMessages(context);
            }
        }
    }

    /**
     * <p>Perform actions that need to happen on the
     * <code>beforePhase</code> event.</p>
     *
     * <p>For all phases, store the current phaseId in request scope.</p>
     *
     * <p>For before restore-view, create a sequenceId for this request
     * and store it in request scope.</p>
     *
     * <p>For before render-response, store the sequenceId for this
     * request in the response.</p>
     *
     */

    public void doPrePhaseActions(FacesContext context) {
        ExternalContext extContext = context.getExternalContext();
        Object response = extContext.getResponse();
        Map<String, Object> requestMap = extContext.getRequestMap();
        String thisRequestSequenceString;
        String postbackSequenceString = null;
        ELFlash elFlash = (ELFlash) ELFlash.getFlash(extContext, true);

        // If we're on before-restore-view...
        if (PhaseId.RESTORE_VIEW.equals(context.getCurrentPhaseId())) {
            thisRequestSequenceString = Long.toString(sequenceNumber.incrementAndGet());
            // Put the sequence number for the request/response pair
            // that is starting with *this particular request* in the request scope
            // so the ELFlash can access it.
            requestMap.put(Constants.FLASH_THIS_REQUEST_ATTRIBUTE_NAME,
                    thisRequestSequenceString);

            // Make sure to restore all request scoped data
            if (null != elFlash && elFlash.isKeepMessages()) {
                elFlash.restoreAllMessages(context);
            }

            if (context.isPostback()) {
                // to a servlet JSF app...
                if (response instanceof HttpServletResponse) {
                    // extract the sequence number from the cookie or portletSession
                    // for the request/response pair for which this request is a postback.
                    postbackSequenceString = getCookieValue(extContext);
                } else {
                    /******
                     * PortletRequest portletRequest = null;
                     * portletRequest = (PortletRequest) request;
                     * // You can't retrieve a cookie in portlet.
                     * // http://wiki.java.net/bin/view/Portlet/JSR168FAQ#How_can_I_set_retrieve_a_cookie
                     * postbackSequenceString = (String)
                     * portletRequest.getPortletSession().
                     * getAttribute(Constants.FLASH_POSTBACK_REQUEST_ATTRIBUTE_NAME,
                     * PortletSession.PORTLET_SCOPE);
                     *******/
                }
                if (null != postbackSequenceString) {
                    // Store the sequenceNumber in the request so the
                    // after-render-response event can flush the flash
                    // of entries from that sequence
                    requestMap.put(Constants.FLASH_POSTBACK_REQUEST_ATTRIBUTE_NAME,
                            postbackSequenceString);
                }
            }

        }
        if (PhaseId.RENDER_RESPONSE.equals(context.getCurrentPhaseId())) {
            // Set the REQUEST_ID cookie to be the sequence number
            addCookie(extContext, elFlash);
        }

    }


    // ------------------------------------- Methods from old PhaseListener Impl


    private static final AtomicInteger sequenceNumber = new AtomicInteger(0);

    static String getCookieValue(ExternalContext extContext) {
        String result = null;
        Cookie cookie = (Cookie)
        extContext.getRequestCookieMap().
                get(Constants.FLASH_POSTBACK_REQUEST_ATTRIBUTE_NAME);
        if (null != cookie) {
            result = cookie.getValue();
        }

        return result;
    }

    private void addCookie(ExternalContext extContext, Flash flash) {
        // Do not update the cookie if redirect after post
        if (flash.isRedirect()) {
            return;
        }

        String thisRequestSequenceString;
        HttpServletResponse servletResponse;
        //PortletRequest portletRequest;
        Object thisRequestSequenceStringObj, response = extContext.getResponse();

        thisRequestSequenceStringObj = extContext.getRequestMap().
                get(Constants.FLASH_THIS_REQUEST_ATTRIBUTE_NAME);
        if (null == thisRequestSequenceStringObj) {
            return;
        }
        thisRequestSequenceString = thisRequestSequenceStringObj.toString();

        if (response instanceof HttpServletResponse) {
            servletResponse = (HttpServletResponse) response;
            Cookie cookie = new Cookie(Constants.FLASH_POSTBACK_REQUEST_ATTRIBUTE_NAME,
                    thisRequestSequenceString);
            cookie.setMaxAge(-1);
            servletResponse.addCookie(cookie);
        } else {
            /*****
             * portletRequest = (PortletRequest) request;
             * // You can't add a cookie in portlet.
             * // http://wiki.java.net/bin/view/Portlet/JSR168FAQ#How_can_I_set_retrieve_a_cookie
             * portletRequest.getPortletSession().setAttribute(Constants.FLASH_POSTBACK_REQUEST_ATTRIBUTE_NAME,
             * thisRequestSequenceString, PortletSession.PORTLET_SCOPE);
             *********/
        }
    }

    private void expireEntries(FacesContext context) {
        ExternalContext extContext = context.getExternalContext();
        String postbackSequenceString;
        // Clear out the flash for the postback.
        if (null != (postbackSequenceString = (String)
        extContext.getRequestMap().
                get(Constants.FLASH_POSTBACK_REQUEST_ATTRIBUTE_NAME))) {
            ELFlash flash = (ELFlash)ELFlash.getFlash(extContext, false);
            if (null != flash) {
                flash.expireEntriesForSequence(postbackSequenceString);
            }
        }

    }


}
