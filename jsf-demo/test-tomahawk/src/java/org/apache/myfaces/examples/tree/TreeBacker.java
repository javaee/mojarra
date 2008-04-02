/*
 * Copyright 2005 The Apache Software Foundation.
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
package org.apache.myfaces.examples.tree;

import org.apache.myfaces.custom.tree2.HtmlTree;
import org.apache.myfaces.custom.tree2.TreeNode;
import org.apache.myfaces.custom.tree2.TreeNodeBase;
import org.apache.myfaces.custom.tree2.TreeModel;
import org.apache.myfaces.custom.tree2.TreeModelBase;

import javax.faces.context.FacesContext;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.validator.ValidatorException;
import javax.faces.event.ActionEvent;
import java.io.Serializable;

/**
 * Backer bean for use in example.  Basically makes a TreeNode available.
 *
 * @author Sean Schofield
 * @version $Revision: 1.1 $ $Date: 2005/11/08 06:08:43 $
 */
public class TreeBacker implements Serializable
{
    private TreeModelBase _treeModel;
    private HtmlTree _tree;

    public TreeNode getTreeData()
    {
        TreeNode treeData = new TreeNodeBase("foo-folder", "Inbox", false);

        // construct a set of fake data (normally your data would come from a database)

        // populate Frank's portion of the tree
        TreeNodeBase personNode = new TreeNodeBase("person", "Frank Foo", false);
        personNode.getChildren().add(new TreeNodeBase("foo-folder", "Requires Foo", false));
        TreeNodeBase folderNode = new TreeNodeBase("foo-folder", "Requires Foo Reviewer", false);
        folderNode.getChildren().add(new TreeNodeBase("document", "X050001", true));
        folderNode.getChildren().add(new TreeNodeBase("document", "X050002", true));
        folderNode.getChildren().add(new TreeNodeBase("document", "X050003", true));
        personNode.getChildren().add(folderNode);
        personNode.getChildren().add(new TreeNodeBase("foo-folder", "Requires Foo Recommendation", false));
        folderNode = new TreeNodeBase("foo-folder", "Requires Foo Approval", false);
        folderNode.getChildren().add(new TreeNodeBase("document", "J050001", true));
        folderNode.getChildren().add(new TreeNodeBase("document", "J050002", true));
        folderNode.getChildren().add(new TreeNodeBase("document", "J050003", true));
        folderNode.getChildren().add(new TreeNodeBase("document", "E050011", true));
        folderNode.getChildren().add(new TreeNodeBase("document", "R050002", true));
        folderNode.getChildren().add(new TreeNodeBase("document", "C050003", true));
        personNode.getChildren().add(folderNode);
        folderNode = new TreeNodeBase("bar-folder", "Requires Bar Processing", false);
        folderNode.getChildren().add(new TreeNodeBase("document", "X050003", true));
        folderNode.getChildren().add(new TreeNodeBase("document", "X050011", true));
        folderNode.getChildren().add(new TreeNodeBase("document", "F050002", true));
        folderNode.getChildren().add(new TreeNodeBase("document", "G050003", true));
        personNode.getChildren().add(folderNode);
        folderNode = new TreeNodeBase("bar-folder", "Requires Bar Approval", false);
        folderNode.getChildren().add(new TreeNodeBase("document", "J050006", true));
        folderNode.getChildren().add(new TreeNodeBase("document", "J050007", true));
        personNode.getChildren().add(folderNode);

        treeData.getChildren().add(personNode);

        // populate Betty's portion of the tree
        personNode = new TreeNodeBase("person", "Betty Bar", false);
        personNode.getChildren().add(new TreeNodeBase("foo-folder", "Requires Foo", false));
        folderNode = new TreeNodeBase("foo-folder", "Requires Foo Reviewer", false);
        folderNode.getChildren().add(new TreeNodeBase("document", "X012000", true));
        folderNode.getChildren().add(new TreeNodeBase("document", "X013000", true));
        folderNode.getChildren().add(new TreeNodeBase("document", "X014000", true));
        personNode.getChildren().add(folderNode);
        folderNode = new TreeNodeBase("foo-folder", "Requires Foo Recommendation", false);
        folderNode.getChildren().add(new TreeNodeBase("document", "J010026", true));
        folderNode.getChildren().add(new TreeNodeBase("document", "J020002", true));
        folderNode.getChildren().add(new TreeNodeBase("document", "J030103", true));
        folderNode.getChildren().add(new TreeNodeBase("document", "E030214", true));
        folderNode.getChildren().add(new TreeNodeBase("document", "R020444", true));
        folderNode.getChildren().add(new TreeNodeBase("document", "C010000", true));
        personNode.getChildren().add(folderNode);
        personNode.getChildren().add(new TreeNodeBase("foo-folder", "Requires Foo Approval", false));
        folderNode = new TreeNodeBase("bar-folder", "Requires Bar Processing", false);
        folderNode.getChildren().add(new TreeNodeBase("document", "T052003", true));
        folderNode.getChildren().add(new TreeNodeBase("document", "T020011", true));
        personNode.getChildren().add(folderNode);
        folderNode = new TreeNodeBase("bar-folder", "Requires Bar Approval", false);
        folderNode.getChildren().add(new TreeNodeBase("document", "J010002", true));
        folderNode.getChildren().add(new TreeNodeBase("document", "J030047", true));
        folderNode.getChildren().add(new TreeNodeBase("document", "F030112", true));
        personNode.getChildren().add(folderNode);

        treeData.getChildren().add(personNode);

        return treeData;
    }

    /**
     * NOTE: This is just to show an alternative way of supplying tree data.  You can supply either a
     * TreeModel or TreeNode.
     *
     * @return TreeModel
     */
    public TreeModel getExpandedTreeData()
    {
        return new TreeModelBase(getTreeData());
    }

    public void setTree(HtmlTree tree)
    {
        _tree = tree;
    }

    public HtmlTree getTree()
    {
        return _tree;
    }

    public String expandAll()
    {
        _tree.expandAll();
        return null;
    }

    private String _nodePath;

    public void setNodePath(String nodePath)
    {
        _nodePath = nodePath;
    }

    public String getNodePath()
    {
        return _nodePath;
    }

    public void checkPath(FacesContext context, UIComponent component, java.lang.Object value)
    {
        // make sure path is valid (leaves cannot be expanded or renderer will complain)
        FacesMessage message = null;

        String[] path = _tree.getPathInformation(value.toString());

        for (int i = 0; i < path.length; i++)
        {
            String nodeId = path[i];
            try
            {
                _tree.setNodeId(nodeId);
            }
            catch (Exception e)
            {
                throw new ValidatorException(message, e);
            }

            if (_tree.getNode().isLeaf())
            {
                message = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                           "Invalid node path (cannot expand a leaf): " + nodeId,
                                           "Invalid node path (cannot expand a leaf): " + nodeId);
                throw new ValidatorException(message);
            }
        }
    }

    public void expandPath(ActionEvent event)
    {
        _tree.expandPath(_tree.getPathInformation(_nodePath));
    }
}
