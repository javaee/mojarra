/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
 * 
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License. You can obtain
 * a copy of the License at https://glassfish.dev.java.net/public/CDDL+GPL.html
 * or glassfish/bootstrap/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 * 
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at glassfish/bootstrap/legal/LICENSE.txt.
 * Sun designates this particular file as subject to the "Classpath" exception
 * as provided by Sun in the GPL Version 2 section of the License file that
 * accompanied this code.  If applicable, add the following below the License
 * Header, with the fields enclosed by brackets [] replaced by your own
 * identifying information: "Portions Copyrighted [year]
 * [name of copyright owner]"
 * 
 * Contributor(s):
 * 
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */

package com.sun.faces.application;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.FacesException;
import javax.faces.application.StateManager;
import javax.faces.application.ProjectStage;
import javax.faces.component.NamingContainer;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.render.ResponseStateManager;

import com.sun.faces.config.WebConfiguration;
import com.sun.faces.config.WebConfiguration.BooleanWebContextInitParameter;
import com.sun.faces.config.WebConfiguration.WebContextInitParameter;
import com.sun.faces.io.FastStringWriter;
import com.sun.faces.renderkit.RenderKitUtils;
import com.sun.faces.spi.SerializationProvider;
import com.sun.faces.spi.SerializationProviderFactory;
import com.sun.faces.util.DebugUtil;
import com.sun.faces.util.FacesLogger;
import com.sun.faces.util.LRUMap;
import com.sun.faces.util.MessageUtils;
import com.sun.faces.util.ReflectionUtils;
import com.sun.faces.util.TypedCollections;
import com.sun.faces.util.Util;
import com.sun.faces.util.RequestStateManager;
import com.sun.faces.RIConstants;

public class StateManagerImpl extends StateManager {

    private static final Logger LOGGER = FacesLogger.APPLICATION.getLogger();
    private static final String STATEMANAGED_SERIAL_ID_KEY =
          StateManagerImpl.class.getName() + ".SerialId";

    private static final String LOGICAL_VIEW_MAP =
          RIConstants.FACES_PREFIX + "logicalViewMap";
    
    private SerializationProvider serialProvider;
    private WebConfiguration webConfig;

    /** Number of views in logical view to be saved in session. */
    private int noOfViews;
    private int noOfViewsInLogicalView;
    private Map<String,Class<?>> classMap;
    private boolean isDevelopmentMode;


    // ------------------------------------------------------------ Constructors


    public StateManagerImpl() {
        FacesContext fContext = FacesContext.getCurrentInstance();
        serialProvider = SerializationProviderFactory
                             .createInstance(fContext.getExternalContext());
        webConfig = WebConfiguration.getInstance(fContext.getExternalContext());
        if (!(isDevelopmentMode = (fContext.getApplication().getProjectStage() == ProjectStage.Development))) {
            classMap = new ConcurrentHashMap<String,Class<?>>(32);
        }
    }


    // ---------------------------------------------------------- Public Methods

    @SuppressWarnings("deprecation")
    public UIViewRoot restoreView(FacesContext context, String viewId,
                                  String renderKitId) {

        UIViewRoot viewRoot = null;
        if (isSavingStateInClient(context)) {
            viewRoot = restoreTree(context, viewId, renderKitId);
            if (viewRoot != null) {
                restoreState(context, viewRoot, renderKitId);
            } 
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
                id = ((stateArray != null) ? stateArray[0] : null);
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
                          .get(LOGICAL_VIEW_MAP);
                    if (logicalMap != null) {
                        Map actualMap = (Map) logicalMap.get(idInLogicalMap);
                        if (actualMap != null) {
                            RequestStateManager.set(context,
                                                    RequestStateManager.LOGICAL_VIEW_MAP,
                                                    idInLogicalMap);
                            if (rsm.isPostback(context)) {
                                RequestStateManager.set(context,
                                                        RequestStateManager.ACTUAL_VIEW_MAP,
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
                viewRoot = restoreTree(renderKitId,
                        ((Object[]) stateArray[0]).clone());
                viewRoot.processRestoreState(context, handleRestoreState(stateArray[1])); 

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
        checkIdUniqueness(context, viewRoot, new HashSet<String>(viewRoot.getChildCount() << 1));


        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("Begin creating serialized view for "
                        + viewRoot.getViewId());
        }
        List<TreeNode> treeList = new ArrayList<TreeNode>(32);
        Object state = viewRoot.processSaveState(context);
        captureChild(treeList, 0, viewRoot);        
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
            int logicalMapSize = getNumberOfViewsParameter();
            int actualMapSize = getNumberOfViewsInLogicalViewParameter();
        
            ExternalContext externalContext = context.getExternalContext();
            Object sessionObj = externalContext.getSession(true);
            Map<String, Object> sessionMap = externalContext.getSessionMap();


            synchronized (sessionObj) {
                Map<String, Map> logicalMap = TypedCollections.dynamicallyCastMap(
                      (Map) sessionMap.get(LOGICAL_VIEW_MAP), String.class, Map.class);
                if (logicalMap == null) {
                    logicalMap = new LRUMap<String, Map>(logicalMapSize);
                    sessionMap.put(LOGICAL_VIEW_MAP, logicalMap);
                }
            
                String idInLogicalMap = (String)
                      RequestStateManager.get(context, RequestStateManager.LOGICAL_VIEW_MAP);
                if (idInLogicalMap == null) {
                    idInLogicalMap = createUniqueRequestId(context);
                }
                assert(null != idInLogicalMap);
                
                String idInActualMap = createUniqueRequestId(context);
               
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
                    stateArray[1] = handleSaveState(state);
                } else {
                    actualMap.put(idInActualMap, new Object[] { tree, handleSaveState(state) });
                }
                // always call put/setAttribute as we may be in a clustered environment.
                sessionMap.put(LOGICAL_VIEW_MAP, logicalMap);
            }
        } else {
            result = new SerializedView(tree, state);
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
     * maximum number of views to be saved in this logical view. If none is
     * specified returns <code>DEFAULT_NUMBER_OF_VIEWS_IN_LOGICAL_VIEW_IN_SESSION</code>.
     *
     * @return number of logical views
     */
    protected int getNumberOfViewsInLogicalViewParameter() {

        if (noOfViewsInLogicalView != 0) {
            return noOfViewsInLogicalView;
        }

        String noOfViewsStr = webConfig
              .getOptionValue(WebContextInitParameter.NumberOfLogicalViews);
        String defaultValue =
              WebContextInitParameter.NumberOfLogicalViews.getDefaultValue();
        try {
            noOfViewsInLogicalView = Integer.valueOf(noOfViewsStr);
        } catch (NumberFormatException nfe) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.fine("Error parsing the servetInitParameter "
                            +
                            WebContextInitParameter.NumberOfLogicalViews
                                  .getQualifiedName()
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
     * maximum number of logical views to be saved in session. If none is
     * specified returns <code>DEFAULT_NUMBER_OF_VIEWS_IN_SESSION</code>.
     *
     * @return number of logical views
     */
    protected int getNumberOfViewsParameter() {

        if (noOfViews != 0) {
            return noOfViews;
        }

        String noOfViewsStr = webConfig
              .getOptionValue(WebContextInitParameter.NumberOfViews);
        String defaultValue =
              WebContextInitParameter.NumberOfViews.getDefaultValue();
        try {
            noOfViews = Integer.valueOf(noOfViewsStr);
        } catch (NumberFormatException nfe) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.fine("Error parsing the servetInitParameter "
                            +
                            WebContextInitParameter.NumberOfViews
                                  .getQualifiedName()
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


    /**
     * @param state the object returned from <code>UIView.processSaveState</code>
     * @return If {@link BooleanWebContextInitParameter#SerializeServerState} is
     *  <code>true</code>, serialize and return the state, otherwise, return
     *  <code>state</code> unchanged.
     */
    private Object handleSaveState(Object state) {

        if (webConfig.isOptionEnabled(BooleanWebContextInitParameter.SerializeServerState)) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream(1024);
            ObjectOutputStream oas = null;
            try {
                oas = serialProvider.createObjectOutputStream(baos);
                oas.writeObject(state);
                oas.flush();
            } catch (Exception e) {
                throw new FacesException(e);
            } finally {
                if (oas != null) {
                    try {
                        oas.close();
                    } catch (IOException ignored) { }
                }
            }
            return baos.toByteArray();
        } else {
            return state;
        }

    }


    /**
     * @param state the state as it was stored in the session
     * @return an object that can be passed to <code>UIViewRoot.processRestoreState</code>.
     *  If {@link BooleanWebContextInitParameter#SerializeServerState} de-serialize the
     *  state prior to returning it, otherwise return <code>state</code> as is.
     */
    private Object handleRestoreState(Object state) {

        if (webConfig.isOptionEnabled(BooleanWebContextInitParameter.SerializeServerState)) {
            ByteArrayInputStream bais = new ByteArrayInputStream((byte[]) state);
            ObjectInputStream ois = null;
            try {
                ois = serialProvider.createObjectInputStream(bais);
                return ois.readObject();
            } catch (Exception e) {
                throw new FacesException(e);
            } finally {
                if (ois != null) {
                    try {
                        ois.close();
                    } catch (IOException ignored) { }
                }
            }
        } else {
            return state;
        }

    }


    private static void captureChild(List<TreeNode> tree, 
                                     int parent,
                                     UIComponent c) {

        if (!c.isTransient()) {
            TreeNode n = new TreeNode(parent, c);
            int pos = tree.size();
            tree.add(n);
            captureRest(tree, pos, c);
        }

    }


    private static void captureFacet(List<TreeNode> tree, 
                                     int parent, 
                                     String name,
                                     UIComponent c) {

        if (!c.isTransient()) {
            FacetNode n = new FacetNode(parent, name, c);
            int pos = tree.size();
            tree.add(n);
            captureRest(tree, pos, c);
        }

    }


    private static void captureRest(List<TreeNode> tree, 
                                    int pos, 
                                    UIComponent c) {

        // store children
        int sz = c.getChildCount();
        if (sz > 0) {
            List<UIComponent> child = c.getChildren();
            for (int i = 0; i < sz; i++) {
                captureChild(tree, pos, child.get(i));
            }
        }

        // store facets
        sz = c.getFacetCount();
        if (sz > 0) {
            for (Entry<String, UIComponent> entry : c.getFacets().entrySet()) {
                captureFacet(tree,
                             pos,
                             entry.getKey(),
                             entry.getValue());
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


    private UIComponent newInstance(TreeNode n)
    throws FacesException {

        try {
            Class<?> t = ((classMap != null) ? classMap.get(n.componentType) : null);
            if (t == null) {
                t = Util.loadClass(n.componentType, n);
                if (t != null && classMap != null) {
                    classMap.put(n.componentType, t);
                } else {
                    if (!isDevelopmentMode) {
                        throw new NullPointerException();
                    }
                }
            }
            
            UIComponent c = (UIComponent) t.newInstance();
            c.setId(n.id);

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
        if (hasGetStateMethod(rsm)) {
            Object[] stateArray = (Object[]) rsm.getState(context, viewId);
            if (stateArray == null) {
                // this is necessary as some frameworks may call
                // ViewHandler.restoreView() for non-postback requests.
                return null;
            }
            treeStructure = (Object[]) stateArray[0];
        } else {
            treeStructure = (Object[]) rsm
                  .getTreeStructureToRestore(context, viewId);
        }

        if (treeStructure == null) {
            return null;
        }
        UIViewRoot root = restoreTree(renderKitId, treeStructure);
        root.setViewId(viewId);
        return root;
    }

    private String createUniqueRequestId(FacesContext ctx) {

        Map<String, Object> sm = ctx.getExternalContext().getSessionMap();
        AtomicInteger idgen =
              (AtomicInteger) sm.get(STATEMANAGED_SERIAL_ID_KEY);
        if (idgen == null) {
            idgen = new AtomicInteger(1);
        }
        // always call put/setAttribute as we may be in a clustered environment.
        sm.put(STATEMANAGED_SERIAL_ID_KEY, idgen);
        return (UIViewRoot.UNIQUE_ID_PREFIX + idgen.getAndIncrement());

    }


    @SuppressWarnings("deprecation")
    private void restoreState(FacesContext context,
                              UIViewRoot root,
                              String renderKitId) {
        ResponseStateManager rsm =
              RenderKitUtils.getResponseStateManager(context, renderKitId);
        Object state;
        if (ReflectionUtils.lookupMethod(rsm.getClass(),
                                         "getState",
                                         FacesContext.class,
                                         String.class) != null) {        
            Object[] stateArray =
                  (Object[]) rsm.getState(context, root.getViewId());
            state = stateArray[1];
        } else {
            state = rsm.getComponentStateToRestore(context);
        }
        root.processRestoreState(context, state);
    }


    private UIViewRoot restoreTree(String renderKitId, Object[] tree)
    throws FacesException {

        UIComponent c;
        FacetNode fn;
        TreeNode tn;      
        for (int i = 0; i < tree.length; i++) {
            if (tree[i]instanceof FacetNode) {
                fn = (FacetNode) tree[i];
                c = newInstance(fn);
                tree[i] = c;               
                if (i != fn.parent) {
                    ((UIComponent) tree[fn.parent]).getFacets()
                          .put(fn.facetName, c);
                }

            } else {
                tn = (TreeNode) tree[i];
                c = newInstance(tn);
                tree[i] = c;
                if (i != tn.parent) {
                    ((UIComponent) tree[tn.parent]).getChildren().add(c);
                } else {
                    assert(c instanceof UIViewRoot);
                    UIViewRoot viewRoot = (UIViewRoot) c;
                    FacesContext.getCurrentInstance().setViewRoot(viewRoot);
                    viewRoot.setRenderKitId(renderKitId);
                }
            }
        }
        return (UIViewRoot) tree[0];

    }


    private static class TreeNode implements Externalizable {

        private static final String NULL_ID = "";

        public String componentType;
        public String id;       

        public int parent;

        private static final long serialVersionUID = -835775352718473281L;


    // ------------------------------------------------------------ Constructors


        public TreeNode() { }


        public TreeNode(int parent, UIComponent c) {

            this.parent = parent;
            this.id = c.getId();
            this.componentType = c.getClass().getName();

        }


    // --------------------------------------------- Methods From Externalizable

        public void writeExternal(ObjectOutput out) throws IOException {

            out.writeInt(this.parent);
            out.writeUTF(this.componentType);
            if (this.id != null) {
                out.writeUTF(this.id);
            } else {
                out.writeUTF(NULL_ID);
            }
        }


        public void readExternal(ObjectInput in)
              throws IOException, ClassNotFoundException {

            this.parent = in.readInt();
            this.componentType = in.readUTF();
            this.id = in.readUTF();
            if (id.length() == 0) {
                id = null;
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
                         UIComponent c) {

            super(parent, c);
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
