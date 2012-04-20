/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright (c) 1997-2012 Oracle and/or its affiliates. All rights reserved.
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
package javax.faces.flow;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.faces.application.NavigationCase;
import javax.faces.context.FacesContext;
import javax.faces.lifecycle.ClientWindow;

public class Flow implements Serializable {
    
    private static final long serialVersionUID = -7506626306507232154L;
    
    public Flow() {
    }
    
    private String id;
    private String defaultNodeId;
    private List<ViewNode> views;
    private Map<String,NavigationCase> returns = new ConcurrentHashMap<String, NavigationCase>();

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Flow other = (Flow) obj;
        if ((this.id == null) ? (other.id != null) : !this.id.equals(other.id)) {
            return false;
        }
        if ((this.defaultNodeId == null) ? (other.defaultNodeId != null) : !this.defaultNodeId.equals(other.defaultNodeId)) {
            return false;
        }
        if (this.views != other.views && (this.views == null || !this.views.equals(other.views))) {
            return false;
        }
        if (this.returns != other.returns && (this.returns == null || !this.returns.equals(other.returns))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + (this.id != null ? this.id.hashCode() : 0);
        hash = 37 * hash + (this.defaultNodeId != null ? this.defaultNodeId.hashCode() : 0);
        hash = 37 * hash + (this.views != null ? this.views.hashCode() : 0);
        hash = 37 * hash + (this.returns != null ? this.returns.hashCode() : 0);
        return hash;
    }
    
    
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    public String getDefaultNodeId() {
        return defaultNodeId;
    }

    public void setDefaultNodeId(String defaultNodeId) {
        this.defaultNodeId = defaultNodeId;
    }
    
    public String getDefaultNodeIdPath() {
        return getId() + "/" + getDefaultNodeId();
    }
    

    public List<ViewNode> getViews() {
        return views;
    }

    public void setViews(List<ViewNode> views) {
        this.views = views;
    }
    
    public ViewNode getView(String viewNodeId) {
        List<ViewNode> myViews = getViews();
        ViewNode result = null;
        
        if (null != myViews) {
            for (ViewNode cur : myViews) {
                if (viewNodeId.equals(cur.getId())) {
                    result = cur;
                    break;
                }
            }
        }
        
        return result;
        
    }
    
    public Map<String,NavigationCase> getReturns(FacesContext context) {
        return returns;
    }
    
    public String getIdForCurrentWindow(FacesContext context) {
        String result = null;
        ClientWindow curWindow = context.getExternalContext().getClientWindow();
        result = curWindow.getId() + "_" + getId();
        
        return result;
    }
    
    public static String createIdForCurrentWindow(FacesContext context, String flowId) {
        ClientWindow curWindow = context.getExternalContext().getClientWindow();
        String result = curWindow.getId() + "_" + flowId;
        return result;
    }
    
}
