/**
 * 
 */
package com.sun.faces.sandbox.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lee
 *
 */
public class TreeNode {
    protected List<TreeNode> children = new ArrayList();
    protected String label;
    protected String content;
    protected TreeNode() {
        
    }
    
    protected TreeNode (String label, String content) {
        this.label = label;
        this.content = content;
    }
    
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public String getLabel() {
        return label;
    }
    public void setLabel(String label) {
        this.label = label;
    }
    public void add(TreeNode child) {
        children.add(child);
    }
    public List<TreeNode> getChildren() {
        return children;
    }
    public boolean hasChildren() {
        return children.size() > 0;
    }
}
