/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2010 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.faces.systest.model;

import java.util.ArrayList;
import javax.faces.model.ListDataModel;

/**
 * @author edburns
 */
public class Bean62 {

    /**
     * Creates a new instance of Bean62
     */
    public Bean62() {
    }

    public Bean62(String label) {
        this.label = label;
    }

    /**
     * Holds value of property model.
     */
    private ListDataModel model;

    /**
     * Getter for property model.
     *
     * @return Value of property model.
     */
    public ListDataModel getModel() {
        if (null == this.model) {
            ArrayList list = new ArrayList();
            if (isRoot()) {
                list.add(new Bean62(label + ".one"));
                list.add(new Bean62(label + ".two"));
                list.add(new Bean62(label + ".three"));
            } else {
                list.add("leaf1");
                list.add("leaf2");
                list.add("leaf3");
            }
            model = new ListDataModel(list);
        }
        return this.model;
    }

    /**
     * Setter for property model.
     *
     * @param model New value of property model.
     */
    public void setModel(ListDataModel model) {

        this.model = model;
    }

    /**
     * Holds value of property root.
     */
    private boolean root = false;

    /**
     * Getter for property root.
     *
     * @return Value of property root.
     */
    public boolean isRoot() {

        return this.root;
    }

    /**
     * Setter for property root.
     *
     * @param root New value of property root.
     */
    public void setRoot(boolean root) {

        this.root = root;
    }

    /**
     * Holds value of property label.
     */
    private String label;

    /**
     * Getter for property label.
     *
     * @return Value of property label.
     */
    public String getLabel() {

        return this.label;
    }

    /**
     * Setter for property label.
     *
     * @param label New value of property label.
     */
    public void setLabel(String label) {

        this.label = label;
    }

    public String action() {
        Bean62 yyyInstance = (Bean62) this.model.getRowData();
        Object wwwInstance = yyyInstance.getModel().getRowData();

        setCurStatus(wwwInstance);

        return null;
    }

    /**
     * Holds value of property curStatus.
     */
    private Object curStatus;

    /**
     * Getter for property curStatus.
     *
     * @return Value of property curStatus.
     */
    public Object getCurStatus() {

        return this.curStatus;
    }

    /**
     * Setter for property curStatus.
     *
     * @param curStatus New value of property curStatus.
     */
    public void setCurStatus(Object curStatus) {

        this.curStatus = curStatus;
    }
}
