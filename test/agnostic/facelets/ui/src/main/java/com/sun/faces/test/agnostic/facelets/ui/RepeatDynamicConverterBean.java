/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2013 Oracle and/or its affiliates. All rights reserved.
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
package com.sun.faces.test.agnostic.facelets.ui;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.ValueHolder;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.NumberConverter;

@ManagedBean(name = "repeatDynamicConverterBean")
@SessionScoped
public class RepeatDynamicConverterBean implements Serializable {

    private int counter = 0;
    private List<RepeatDynamicConverterItem> items = new LinkedList<RepeatDynamicConverterItem>();

    public List<RepeatDynamicConverterItem> getItems() {
        return items;
    }

    public void add() {
        items.add(new RepeatDynamicConverterItem(++counter));
    }

    public String getString() {
        StringBuilder sb = new StringBuilder();

        for (RepeatDynamicConverterItem item : items) {
            sb.append("[");

            String value = "null";
            if (item.getValue() != null) {
                value = item.getValue().toString();
            }

            sb.append(value);
            sb.append("] ");
        }

        return sb.toString();
    }

    UIComponent findRepeatDynamicConverterItemValue(UIComponent root) {
        if ("itemValue".equals(root.getId())) {
            return root;
        }

        for (UIComponent child : root.getChildren()) {
            UIComponent ret = findRepeatDynamicConverterItemValue(child);
            if (ret != null) {
                return ret;
            }
        }

        return null;
    }

    public void addConverters() {
        FacesContext ctx = FacesContext.getCurrentInstance();

        if (!ctx.isPostback()) {
            UIComponent component = findRepeatDynamicConverterItemValue(ctx.getViewRoot());
            if (component instanceof ValueHolder) {
                ValueHolder parentValueHolder = (ValueHolder) component;
                Converter parentConverter = parentValueHolder.getConverter();
                if (parentConverter == null) {
                    NumberConverter numberConverter = new NumberConverter();
                    numberConverter.setMaxFractionDigits(2);
                    numberConverter.setPattern("##.00");
                    parentValueHolder.setConverter(numberConverter);
                }
            }
        }
    }
}
