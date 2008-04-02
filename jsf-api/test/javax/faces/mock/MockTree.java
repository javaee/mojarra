/*
 * $Id: MockTree.java,v 1.2 2003/07/26 17:55:36 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.mock;


import javax.faces.component.UIComponent;
import javax.faces.render.RenderKitFactory;
import javax.faces.tree.Tree;


public class MockTree extends Tree {



    // ----------------------------------------------------------- Constructors


    public MockTree() {
    }


    public MockTree(UIComponent root) {
        setRoot(root);
    }


    // ----------------------------------------------------- Instance Variables


    private String renderKitId = RenderKitFactory.DEFAULT_RENDER_KIT;
    private UIComponent root = null;
    private String treeId = "/tree";


    // --------------------------------------------------------- Public Methods


    public void setRoot(UIComponent root) {
        this.root = root;
    }


    // ----------------------------------------------------------- Tree Methods


    public String getRenderKitId() {
        return (this.renderKitId);
    }


    public void setRenderKitId(String renderKitId) {
        this.renderKitId = renderKitId;
    }


    public UIComponent getRoot() {
        return (this.root);
    }


    public String getTreeId() {
        return (this.treeId);
    }


    public void setTreeId(String treeId) {
        this.treeId = treeId;
    }




}
