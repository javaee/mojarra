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
package com.sun.faces.test.agnostic.ajax;

import java.io.Serializable;
import java.util.Date;
import java.util.Iterator;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.event.ActionEvent;

@ManagedBean
@SessionScoped
public class Issue2578Bean implements Serializable {

    private Date date;
    private Date date2;
    private String text;
    private String text2;

    public String getText2() {
        return text2;
    }

    public void setText2(String text2) {
        this.text2 = text2;
    }

    public Date getDate2() {
        return date2;
    }

    public void setDate2(Date date2) {
        this.date2 = date2;
    }
    
    

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
    
    

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
    
    public void altClear(ActionEvent event){
        this.date = null;
        this.date2 = null;
        this.text = null;
        this.text2 = null;
    }

    public void clear(ActionEvent event) {
        altClear(event);
    }

    private UIComponent getContainingForm(UIComponent component) {
        UIComponent previous = component;
        UIComponent parent = component.getParent();

        while (parent != null) {
            if (parent instanceof UIForm) {
                return parent;
            }
            previous = parent;
            parent = parent.getParent();
        }
        return previous;
    }

    protected void resetChildren(UIComponent component) {
        Iterator<UIComponent> kids = component.getFacetsAndChildren();
        while (kids.hasNext()) {
            UIComponent kid = kids.next();
            if (kid instanceof EditableValueHolder) {
                EditableValueHolder editable = (EditableValueHolder) kid;
                editable.resetValue();
            }
            resetChildren(kid);
        }
    }
}
