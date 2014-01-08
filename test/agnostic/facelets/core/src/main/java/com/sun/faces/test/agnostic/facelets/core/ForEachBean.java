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
package com.sun.faces.test.agnostic.facelets.core;

import javax.faces.bean.ManagedBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Collections;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.model.ListDataModel;

@ManagedBean
@SessionScoped
public class ForEachBean {

    private ArrayList<String> names;
    private ArrayList<Integer> numbers;
    private ArrayList<String> repeatValues;
    private Object [] pages;
    private ListDataModel dataModel;
    
    int count;
    
    final int max = 10;

    public ForEachBean() {
        init();
        append();
    }
    
    private void init() {
        count = 0;
        names = new ArrayList<String>();
        numbers = new ArrayList<Integer>();
        repeatValues = new ArrayList<String>();
        dataModel = new ListDataModel(names);

        Map<String,String> item1 = Collections.singletonMap("page",
                                                            "includedDynamically01.xhtml");
        
        Map<String,String> item2 = Collections.singletonMap("page",
                                                            "includedDynamically02.xhtml");

        Object [] myPages = { item1, item2 };

        pages = myPages;
    }

    public int getCount() {
        return count;
    }
    
    public boolean isEvenCount() {
        return count % 2 == 0;
    }
    
    private void append() {
        count++;
        
        if (names.size() > 10) {
            names.clear();
        }
        
        if (numbers.size() > 10) {
            numbers.clear();
        }
        
        if (repeatValues.size() > 10) {
            repeatValues.clear();
        }
        
        names.add("Bobby");
        names.add("Jerry");
        names.add("Phil");
        
        for (int i = 0; i < 3; i++) {
            numbers.add(new Integer(i));
        }

        repeatValues.add("Blue");
        repeatValues.add("Red");
        repeatValues.add("Green");
    }
    
    public void modify(PhaseEvent e) {
        if (!e.getPhaseId().equals(PhaseId.APPLY_REQUEST_VALUES)) {
            return;
        }
        append();
        
    }
    
    public String getReset() {
        names.clear();
        numbers.clear();
        repeatValues.clear();
        count = 0;
        
        append();

        return "true";
    }

    public ArrayList<String> getRepeatValues() {
        return repeatValues;
    }
    
    public ArrayList<Integer> getNumbers() {
        return numbers;
    }
    
    public List<String> getNames() {
        return names;
    }

    public Object [] getPages() {
        return pages;
    }

    public ListDataModel getDataModel() {
        return dataModel;
    }
    
    
    
}
