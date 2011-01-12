/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2011 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
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

import com.sun.faces.renderkit.RenderKitUtils;
import com.sun.faces.util.Util;

import javax.faces.FacesException;
import javax.faces.application.ProjectStage;
import javax.faces.application.StateManager;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.render.RenderKit;
import javax.faces.render.ResponseStateManager;
import javax.faces.view.StateManagementStrategy;
import javax.faces.view.ViewDeclarationLanguage;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>
 * A <code>StateManager</code> implementation to meet the requirements
 * of the specification.
 * </p>
 *
 * <p>
 * For those who had compile dependencies on this class, we're sorry for any
 * inconvenience, but this had to be re-worked as the version you depended on
 * was incorrectly implemented.  
 * </p>
 */
public class StateManagerImpl extends StateManager {

    private boolean isDevelopmentMode;
    private Map<String,Class<?>> classMap;


    // ------------------------------------------------------------ Constructors


    /**
     * Create a new <code>StateManagerImpl</code> instance.
     */
    public StateManagerImpl() {

        isDevelopmentMode = FacesContext.getCurrentInstance().isProjectStage(ProjectStage.Development);
        classMap = new ConcurrentHashMap<String,Class<?>>(32);

    }


    // ----------------------------------------------- Methods from StateManager


    /**
     * @see {@link javax.faces.application.StateManager#saveView(javax.faces.context.FacesContext))
     */
    @Override
    public Object saveView(FacesContext context) {

        if (context == null) {
            return null;
        }

        // irrespective of method to save the tree, if the root is transient
        // no state information needs to  be persisted.
        UIViewRoot viewRoot = context.getViewRoot();
        if (viewRoot.isTransient()) {
            return null;
        }
        
        Object result;
        String viewId = context.getViewRoot().getViewId();
        StateManagementStrategy strategy = null;
        ViewDeclarationLanguage vdl =
              context.getApplication().getViewHandler().
                    getViewDeclarationLanguage(context, viewId);
        if (vdl != null) {
            strategy = vdl.getStateManagementStrategy(context, viewId);
        }

        if (null != strategy) {
            result = strategy.saveView(context);
        } else {
            // honor the requirement to check for id uniqueness
            Util.checkIdUniqueness(context,
                                   viewRoot,
                                   new HashSet<String>(viewRoot.getChildCount() << 1));

            List<TreeNode> treeList = new ArrayList<TreeNode>(32);
            Object state = viewRoot.processSaveState(context);
            captureChild(treeList, 0, viewRoot);
            Object[] tree = treeList.toArray();

            result = new Object[]{tree, state};
        }
        
        return result;

    }


    /**
     * @see {@link StateManager#writeState(javax.faces.context.FacesContext, Object)}
     */
    @Override
    public void writeState(FacesContext context, Object state)
          throws IOException {

        RenderKit rk = context.getRenderKit();
        ResponseStateManager rsm = rk.getResponseStateManager();
        rsm.writeState(context, state);

    }


    /**
     * @see {@link StateManager#restoreView(javax.faces.context.FacesContext, String, String)}
     */
    public UIViewRoot restoreView(FacesContext context,
                                  String viewId,
                                  String renderKitId) {
        UIViewRoot result = null;
        StateManagementStrategy strategy = null;
        
        ViewDeclarationLanguage vdl =
              context.getApplication().getViewHandler().
                    getViewDeclarationLanguage(context, viewId);
        if (vdl != null) {
            strategy = vdl.getStateManagementStrategy(context, viewId);
        }

        if (null != strategy) {
            result = strategy.restoreView(context, viewId, renderKitId);
        } else {
            ResponseStateManager rsm =
                    RenderKitUtils.getResponseStateManager(context, renderKitId);
            Object[] state = (Object[]) rsm.getState(context, viewId);

            if (state != null && state.length >= 2) {
                // We need to clone the tree, otherwise we run the risk
                // of being left in a state where the restored
                // UIComponent instances are in the session instead
                // of the TreeNode instances.  This is a problem
                // for servers that persist session data since
                // UIComponent instances are not serializable.
                UIViewRoot viewRoot = null;
                if (state[0] != null) {
                    viewRoot = restoreTree(context,
                                           renderKitId,
                                           ((Object[]) state[0]).clone());
                }
                if (viewRoot != null && state[1] != null) {
                    viewRoot.processRestoreState(context, state[1]);
                }

                result = viewRoot;
            }
        }

        return result;

    }


    // --------------------------------------------------------- Private Methods


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

            assert (t != null);
            UIComponent c = (UIComponent) t.newInstance();
            c.setId(n.id);

            return c;
        } catch (Exception e) {
            throw new FacesException(e);
        }

    }   

    private UIViewRoot restoreTree(FacesContext ctx,
                                   String renderKitId,
                                   Object[] tree)
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
                    ctx.setViewRoot(viewRoot);
                    viewRoot.setRenderKitId(renderKitId);
                }
            }
        }
        return (UIViewRoot) tree[0];

    }


    // ----------------------------------------------------------- Inner Classes


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

    } // END TreeNode


    private static final class FacetNode extends TreeNode {


        public String facetName;

        private static final long serialVersionUID = -3777170310958005106L;


    // ------------------------------------------------------------ Constructors

        @SuppressWarnings({"UnusedDeclaration"})
        public FacetNode() { } // for serialization purposes

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

    } // END FacetNode


} // END StateManagerImpl
