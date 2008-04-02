/*
 * Copyright 2004 The Apache Software Foundation.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.myfaces.examples.listexample;

import java.io.Serializable;

import org.apache.myfaces.custom.tree.DefaultMutableTreeNode;
import org.apache.myfaces.custom.tree.model.DefaultTreeModel;

/**
 * <p>
 * Bean holding the tree hierarchy.
 * </p>
 * 
 * @author <a href="mailto:dlestrat@apache.org">David Le Strat</a>
 */
public class TreeTable implements Serializable
{
    private DefaultTreeModel treeModel;

    /**
     * @param treeModel The treeModel.
     */
    public TreeTable(DefaultTreeModel treeModel)
    {
        this.treeModel = treeModel;
    }

    /**
     * <p>
     * Default constructor.
     * </p>
     */
    public TreeTable()
    {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode(new TreeItem(1, "XY", "9001", "XY 9001"));
        DefaultMutableTreeNode a = new DefaultMutableTreeNode(new TreeItem(2, "A", "9001", "A 9001"));
        root.insert(a);
        DefaultMutableTreeNode b = new DefaultMutableTreeNode(new TreeItem(3, "B", "9001", "B 9001"));
        root.insert(b);
        DefaultMutableTreeNode c = new DefaultMutableTreeNode(new TreeItem(4, "C", "9001", "C 9001"));
        root.insert(c);

        DefaultMutableTreeNode node = new DefaultMutableTreeNode(new TreeItem(5, "a1", "9002", "a1 9002"));
        a.insert(node);
        node = new DefaultMutableTreeNode(new TreeItem(6, "a2", "9002", "a2 9002"));
        a.insert(node);
        node = new DefaultMutableTreeNode(new TreeItem(7, "a3", "9002", "a3 9002"));
        a.insert(node);
        node = new DefaultMutableTreeNode(new TreeItem(8, "b", "9002", "b 9002"));
        b.insert(node);

        a = node;
        node = new DefaultMutableTreeNode(new TreeItem(9, "x1", "9003", "x1 9003"));
        a.insert(node);
        node = new DefaultMutableTreeNode(new TreeItem(9, "x2", "9003", "x2 9003"));
        a.insert(node);
        
        this.treeModel = new DefaultTreeModel(root);
    }

    /**
     * @return Returns the treeModel.
     */
    public DefaultTreeModel getTreeModel()
    {
        return treeModel;
    }

    /**
     * @param treeModel The treeModel to set.
     */
    public void setTreeModel(DefaultTreeModel treeModel)
    {
        this.treeModel = treeModel;
    }
}
