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
 * Copyright 2006 Sun Microsystems Inc. All Rights Reserved
 */

package com.sun.faces.application;

import javax.faces.FacesException;
import javax.faces.application.StateManager;
import javax.faces.component.NamingContainer;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.render.ResponseStateManager;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.sun.faces.RIConstants;
import com.sun.faces.config.WebConfiguration;
import com.sun.faces.config.WebConfiguration.WebContextInitParameter;
import com.sun.faces.io.FastStringWriter;
import com.sun.faces.renderkit.RenderKitUtils;
import com.sun.faces.util.DebugUtil;
import com.sun.faces.util.LRUMap;
import com.sun.faces.util.MessageUtils;
import com.sun.faces.util.ReflectionUtils;
import com.sun.faces.util.TypedCollections;
import com.sun.faces.util.Util;

public class StateManagerImpl extends StateManager {

    private static final Logger LOGGER =
              Util.getLogger(Util.FACES_LOGGER + Util.APPLICATION_LOGGER);
    private static final Object[] STATE_PLACEHOLDER = new Object[0];   
    
    private char requestIdSerial;

    /** Number of views in logical view to be saved in session. */
    private int noOfViews;
    private int noOfViewsInLogicalView;
    private Map<String,Class<?>> classMap = 
          new ConcurrentHashMap<String,Class<?>>(32);



    // ---------------------------------------------------------- Public Methods

    @SuppressWarnings("deprecation")
    public UIViewRoot restoreView(FacesContext context, String viewId,
                                  String renderKitId) {

        UIViewRoot viewRoot = null;
        if (isSavingStateInClient(context)) {
            viewRoot = restoreTree(context, viewId, renderKitId);           
        } else {
            // restore tree from session.
            // The ResponseStateManager implementation may be using the new 
            // methods or deprecated methods.  We need to know which one 
            // to call.
            Object id;
            ResponseStateManager rsm =
                  RenderKitUtils.getResponseStateManager(context, renderKitId);
            if (hasGetStateMethod(rsm)) {
                Object[] stateArray = (Object[]) rsm.getState(context, viewId);
                id = stateArray[0];
            } else {
                id = rsm.getTreeStructureToRestore(context, viewId);
            }

            if (null != id) {
                if (LOGGER.isLoggable(Level.FINE)) {
                    LOGGER.fine("Begin restoring view in session for viewId "
                                + viewId);
                }
                String idString = (String) id;
                String idInLogicalMap;
                String idInActualMap;

                int sep = idString.indexOf(NamingContainer.SEPARATOR_CHAR);
                assert(-1 != sep);
                assert(sep < idString.length());

                idInLogicalMap = idString.substring(0, sep);
                idInActualMap = idString.substring(sep + 1);

                ExternalContext externalCtx = context.getExternalContext();
                Object sessionObj = externalCtx.getSession(false);

                // stop evaluating if the session is not available
                if (sessionObj == null) {
                    if (LOGGER.isLoggable(Level.FINE)) {
                        LOGGER.fine(
                              "Can't Restore Server View State, session expired for viewId: "
                              + viewId);
                    }
                    return null;
                }

                Object [] stateArray = null;
                synchronized (sessionObj) {
                    Map logicalMap = (Map) externalCtx.getSessionMap()
                          .get(RIConstants.LOGICAL_VIEW_MAP);
                    if (logicalMap != null) {
                        Map actualMap = (Map) logicalMap.get(idInLogicalMap);
                        if (actualMap != null) {
                            Map<String,Object> requestMap = 
                                  context.getExternalContext().getRequestMap();
                            requestMap.put(RIConstants.LOGICAL_VIEW_MAP,
                                           idInLogicalMap);
                            if (rsm.isPostback(context)) {
                                requestMap.put(RIConstants.ACTUAL_VIEW_MAP,
                                               idInActualMap);
                            }
                            stateArray =
                                  (Object[]) actualMap.get(idInActualMap);
                        }
                    }
                }
                if (stateArray == null) {
                    if (LOGGER.isLoggable(Level.FINE)) {
                        LOGGER.fine(
                              "Session Available, but View State does not exist for viewId: "
                              + viewId);
                    }
                    return null;
                }

                // We need to clone the tree, otherwise we run the risk
                // of being left in a state where the restored
                // UIComponent instances are in the session instead
                // of the TreeNode instances.  This is a problem 
                // for servers that persist session data since 
                // UIComponent instances are not serializable.
                viewRoot = restoreTree(((Object[]) stateArray[0]).clone(), 
                                       context);                

                if (LOGGER.isLoggable(Level.FINE)) {
                    LOGGER.fine("End restoring view in session for viewId "
                                + viewId);
                }
            }
        }

        return viewRoot;
    }


    @SuppressWarnings("deprecation")
    @Override
    public SerializedView saveSerializedView(FacesContext context) {


        SerializedView result = null;
       
        // irrespective of method to save the tree, if the root is transient
        // no state information needs to  be persisted.
        UIViewRoot viewRoot = context.getViewRoot();
        if (viewRoot.isTransient()) {
            return result;
        }

        // honor the requirement to check for id uniqueness
        checkIdUniqueness(context, viewRoot, new HashSet<String>());


        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("Begin creating serialized view for "
                        + viewRoot.getViewId());
        }
        List<TreeNode> treeList = new ArrayList<TreeNode>(32);
        captureChild(treeList, 0, viewRoot, context);        
        Object[] tree = treeList.toArray();      
        
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("End creating serialized view " + viewRoot.getViewId());
        }
        if (!isSavingStateInClient(context)) {
            //
            // Server Side state saving is handled stored in two nested LRU maps
            // in the session.
            //
            // The first map is called the LOGICAL_VIEW_MAP.  A logical view
            // is a top level view that may have one or more actual views inside
            // of it.  This will be the case when you have a frameset, or an
            // application that has multiple windows operating at the same time.
            // The LOGICAL_VIEW_MAP map contains 
            // an entry for each logical view, up to the limit specified by the
            // numberOfViewsParameter.  Each entry in the LOGICAL_VIEW_MAP
            // is an LRU Map, configured with the numberOfViewsInLogicalView
            // parameter.  
            //
            // The motivation for this is to allow better memory tuning for 
            // apps that need this multi-window behavior.                                                     
            int logicalMapSize = getNumberOfViewsParameter(context);
            int actualMapSize = getNumberOfViewsInLogicalViewParameter(context);
        
            ExternalContext externalContext = context.getExternalContext();
            Object sessionObj = externalContext.getSession(true);
            Map<String, Object> sessionMap = Util.getSessionMap(context);


            synchronized (sessionObj) {
                Map<String, Map> logicalMap = TypedCollections.dynamicallyCastMap(
                      (Map) sessionMap.get(RIConstants.LOGICAL_VIEW_MAP), String.class, Map.class);
                if (logicalMap == null) {
                    logicalMap = new LRUMap<String, Map>(logicalMapSize);
                    sessionMap.put(RIConstants.LOGICAL_VIEW_MAP, logicalMap);
                }

                Map<String, Object> requestMap =
                      externalContext.getRequestMap();
                String idInLogicalMap = (String)
                      requestMap.get(RIConstants.LOGICAL_VIEW_MAP);
                if (idInLogicalMap == null) {
                    idInLogicalMap = createUniqueRequestId();
                }
                assert(null != idInLogicalMap);
                
                String idInActualMap = createUniqueRequestId();
               
                Map<String, Object[]> actualMap = TypedCollections.dynamicallyCastMap(
                      logicalMap.get(idInLogicalMap), String.class, Object[].class);
                if (actualMap == null) {
                    actualMap = new LRUMap<String, Object[]>(actualMapSize);
                    logicalMap.put(idInLogicalMap, actualMap);
                }

                String id = idInLogicalMap + NamingContainer.SEPARATOR_CHAR +
                            idInActualMap;
                result = new SerializedView(id, null);                
                Object[] stateArray = actualMap.get(idInActualMap);
                // reuse the array if possible
                if (stateArray != null) {                    
                    stateArray[0] = tree;
                    stateArray[1] = STATE_PLACEHOLDER;
                } else {
                    actualMap.put(idInActualMap, new Object[] { tree, STATE_PLACEHOLDER });
                }                
            }
        } else {
            result = new SerializedView(tree, STATE_PLACEHOLDER);
        }

        return result;           

    }
    

    @SuppressWarnings("deprecation")
    @Override
    public void writeState(FacesContext context, SerializedView state)
          throws IOException {

        String renderKitId = context.getViewRoot().getRenderKitId();
        ResponseStateManager rsm =
              RenderKitUtils.getResponseStateManager(context, renderKitId);
        if (hasGetStateMethod(rsm)) {
            Object[] stateArray = new Object[2];
            stateArray[0] = state.getStructure();
            stateArray[1] = state.getState();
            rsm.writeState(context, stateArray);
        } else {
            rsm.writeState(context, state);
        }

    }    


    // ------------------------------------------------------- Protected Methods


    protected void checkIdUniqueness(FacesContext context,
                                     UIComponent component,
                                     Set<String> componentIds)
          throws IllegalStateException {

        // deal with children/facets that are marked transient.        
        for (Iterator<UIComponent> kids = component.getFacetsAndChildren();
             kids.hasNext();) {

            UIComponent kid = kids.next();
            // check for id uniqueness
            String id = kid.getClientId(context);
            if (componentIds.add(id)) {
                checkIdUniqueness(context, kid, componentIds);
            } else {
                if (LOGGER.isLoggable(Level.SEVERE)) {
                    LOGGER.log(Level.SEVERE,
                               "jsf.duplicate_component_id_error",
                               id);
                }
                FastStringWriter writer = new FastStringWriter(128);
                DebugUtil.simplePrintTree(context.getViewRoot(), id, writer);
                String message = MessageUtils.getExceptionMessageString(
                            MessageUtils.DUPLICATE_COMPONENT_ID_ERROR_ID, id) 
                      + '\n'
                      + writer.toString();
                throw new IllegalStateException(message);
            }
        }

    }


    // --------------------------------------------------------- Private Methods

    /**
         * Returns the value of ServletContextInitParameter that specifies the
         * maximum number of views to be saved in this logical view. If none is specified
         * returns <code>DEFAULT_NUMBER_OF_VIEWS_IN_LOGICAL_VIEW_IN_SESSION</code>.
         * @param context the FacesContext
         * @return number of logical views
         */
        protected int getNumberOfViewsInLogicalViewParameter(FacesContext context) {

            if (noOfViewsInLogicalView != 0) {
                return noOfViewsInLogicalView;
            }
            WebConfiguration webConfig = 
                  WebConfiguration.getInstance(context.getExternalContext());
            String noOfViewsStr = webConfig
                  .getContextInitParameter(WebContextInitParameter.NumberOfLogicalViews);
            String defaultValue =
                  WebContextInitParameter.NumberOfLogicalViews.getDefaultValue();
            try {
                noOfViewsInLogicalView = Integer.valueOf(noOfViewsStr);
            } catch (NumberFormatException nfe) {
                if (LOGGER.isLoggable(Level.FINE)) {
                        LOGGER.fine("Error parsing the servetInitParameter "
                                    +
                                    WebContextInitParameter.NumberOfLogicalViews.getQualifiedName() 
                                    + ". Using default "
                                    +
                                    noOfViewsInLogicalView);
                }
                try {
                    noOfViewsInLogicalView = Integer.valueOf(defaultValue);
                } catch (NumberFormatException ne) {
                    // won't occur
                }
            }        
       
            return noOfViewsInLogicalView;

        }


        /**
         * Returns the value of ServletContextInitParameter that specifies the
         * maximum number of logical views to be saved in session. If none is specified
         * returns <code>DEFAULT_NUMBER_OF_VIEWS_IN_SESSION</code>.
         * @param context the FacesContext
         * @return number of logical views
         */
        protected int getNumberOfViewsParameter(FacesContext context) {
        
            if (noOfViews != 0) {
                return noOfViews;
            }
            WebConfiguration webConfig = 
                  WebConfiguration.getInstance(context.getExternalContext());
            String noOfViewsStr = webConfig
                  .getContextInitParameter(WebContextInitParameter.NumberOfViews);
            String defaultValue =
                  WebContextInitParameter.NumberOfViews.getDefaultValue();
            try {
                noOfViews = Integer.valueOf(noOfViewsStr);
            } catch (NumberFormatException nfe) {
                if (LOGGER.isLoggable(Level.FINE)) {
                        LOGGER.fine("Error parsing the servetInitParameter "
                                    +
                                    WebContextInitParameter.NumberOfViews.getQualifiedName() 
                                    + ". Using default "
                                    +
                                    noOfViews);
                }
                try {
                    noOfViews = Integer.valueOf(defaultValue);
                } catch (NumberFormatException ne) {
                    // won't occur
                }
            }        
       
            return noOfViews;        

        }
    
    
    
    private static void captureChild(List<TreeNode> tree, 
                                     int parent,
                                     UIComponent c,
                                     FacesContext ctx) {

        if (!c.isTransient()) {
            TreeNode n = new TreeNode(parent, c, ctx);
            int pos = tree.size();
            tree.add(n);
            captureRest(tree, pos, c, ctx);
        }

    }


    private static void captureFacet(List<TreeNode> tree, 
                                     int parent, 
                                     String name,
                                     UIComponent c,
                                     FacesContext ctx) {

        if (!c.isTransient()) {
            FacetNode n = new FacetNode(parent, name, c, ctx);
            int pos = tree.size();
            tree.add(n);
            captureRest(tree, pos, c, ctx);
        }

    }


    private static void captureRest(List<TreeNode> tree, 
                                    int pos, 
                                    UIComponent c,
                                    FacesContext ctx) {

        // store children
        int sz = c.getChildCount();
        if (sz > 0) {
            List<UIComponent> child = c.getChildren();
            for (int i = 0; i < sz; i++) {
                captureChild(tree, pos, child.get(i), ctx);
            }
        }

        // store facets
        sz = c.getFacetCount();
        if (sz > 0) {
            for (Entry<String, UIComponent> entry : c.getFacets().entrySet()) {
                captureFacet(tree,
                             pos,
                             entry.getKey(),
                             entry.getValue(),
                             ctx);
            }
        }

    }


    /**
     * Looks for the presence of a declared method (by name) in the specified 
     * class and returns a <code>boolean</code> outcome (true, if the method 
     * exists).
     *      
     * @param instance The instance of the class that will be used as 
     *  the search domain.     
     * 
     * @return <code>true</code> if the method exists, otherwise 
     *  <code>false</code>
     */
    private boolean hasGetStateMethod(ResponseStateManager instance) {

        return (ReflectionUtils.lookupMethod(
              instance.getClass(),
                                             "getState",
                                              FacesContext.class, 
                                              String.class) != null);

    }


    private UIComponent newInstance(TreeNode n, FacesContext ctx) 
    throws FacesException {

        try {
            Class<?> t = classMap.get(n.componentType);
            if (t == null) {
                t = Util.loadClass(n.componentType, n);
                if (t != null) {
                    classMap.put(n.componentType, t);
                } else {
                    throw new NullPointerException();
                }
            }
            
            UIComponent c = (UIComponent) t.newInstance();
            c.setId(n.id);
            if (!n.trans) {
                c.restoreState(ctx, n.state);
            }
            return c;
        } catch (Exception e) {
            throw new FacesException(e);
        }

    }   


    @SuppressWarnings("deprecation")
    private UIViewRoot restoreTree(FacesContext context,
                                   String viewId,
                                   String renderKitId) {

        ResponseStateManager rsm =
              RenderKitUtils.getResponseStateManager(context, renderKitId);
        Object[] treeStructure;
         if (hasGetStateMethod(rsm)){

        Object[] stateArray = (Object[]) rsm.getState(context, viewId);
            treeStructure = (Object[]) stateArray[0];
        } else {
           treeStructure = (Object[]) rsm
                  .getTreeStructureToRestore(context, viewId);
        }

        if (treeStructure == null) {
            return null;
        }

        return restoreTree(treeStructure, context);
    }
    
     private String createUniqueRequestId() {

        if (requestIdSerial++ == Character.MAX_VALUE) {
            requestIdSerial = 0;
        }
        return UIViewRoot.UNIQUE_ID_PREFIX + ((int) requestIdSerial);

    }


    private UIViewRoot restoreTree(Object[] tree, FacesContext ctx) 
    throws FacesException {

        UIComponent c;
        FacetNode fn;
        TreeNode tn;
        for (int i = 0; i < tree.length; i++) {
            if (tree[i]instanceof FacetNode) {
                fn = (FacetNode) tree[i];
                c = newInstance(fn, ctx);
                tree[i] = c;               
                if (i != fn.parent) {
                    ((UIComponent) tree[fn.parent]).getFacets()
                          .put(fn.facetName, c);
                }

            } else {
                tn = (TreeNode) tree[i];
                c = newInstance(tn, ctx);
                tree[i] = c;
                if (i != tn.parent) {
                    ((UIComponent) tree[tn.parent]).getChildren().add(c);
                }
            }
        }
        return (UIViewRoot) tree[0];

    }


    private static class TreeNode implements Externalizable {

        private static final String NULL_ID = "";

        public String componentType;
        public String id;
        public Serializable state;
        public boolean trans;

        public int parent;

        private static final long serialVersionUID = -835775352718473281L;


    // ------------------------------------------------------------ Constructors


        public TreeNode() { }


        public TreeNode(int parent, UIComponent c, FacesContext ctx) {

            this.parent = parent;
            this.id = c.getId();
            this.componentType = c.getClass().getName(); 
            this.trans = c.isTransient();
            if (!trans) {
                this.state = (Serializable) c.saveState(ctx);
            } 

        }


    // --------------------------------------------- Methods From Externalizable

        public void writeExternal(ObjectOutput out) throws IOException {

            out.writeInt(this.parent);
            out.writeUTF(this.componentType);
            out.writeBoolean(this.trans);
            if (this.id != null) {
                out.writeUTF(this.id);
            } else {
                out.writeUTF(NULL_ID);
            }
            if (!trans) {
                out.writeObject(state);
            }

        }


        public void readExternal(ObjectInput in)
              throws IOException, ClassNotFoundException {

            this.parent = in.readInt();
            this.componentType = in.readUTF();
            this.trans = in.readBoolean();
            this.id = in.readUTF();
            if (id.length() == 0) {
                id = null;
            }            
            if (!trans) {
                state = (Serializable) in.readObject();
            }

        }

    }

    private static final class FacetNode extends TreeNode {


        public String facetName;

        private static final long serialVersionUID = -3777170310958005106L;


    // ------------------------------------------------------------ Constructors
        
        public FacetNode() { }

        public FacetNode(int parent, 
                         String name, 
                         UIComponent c, 
                         FacesContext ctx) {

            super(parent, c, ctx);
            this.facetName = name;

        }


    // ---------------------------------------------------------- Public Methods

        @Override
        public void readExternal(ObjectInput in)
              throws IOException, ClassNotFoundException {

            super.readExternal(in);
            this.facetName = in.readUTF();

        }

        @Override
        public void writeExternal(ObjectOutput out) throws IOException {

            super.writeExternal(out);
            out.writeUTF(this.facetName);

        }

    }

} // END StateManagerImpl