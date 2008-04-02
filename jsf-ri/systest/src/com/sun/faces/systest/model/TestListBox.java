/*
 * The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the License). You may not use this file except in
 * compliance with the License.
 *
 * You can obtain a copy of the License at
 * https://javaserverfaces.dev.java.net/CDDL.html or
 * legal/CDDLv1.0.txt.
 * See the License for the specific language governing
 * permission and limitations under the License.
 *
 * When distributing Covered Code, include this CDDL
 * Header Notice in each file and include the License file
 * at legal/CDDLv1.0.txt.
 * If applicable, add the following below the CDDL Header,
 * with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 *
 * [Name of File] [ver.__] [Date]
 *
 * Copyright 2006 Sun Microsystems Inc. All Rights Reserved
 */

package com.sun.faces.systest.model;

import javax.faces.model.SelectItem;

import java.util.ArrayList;
import java.util.List;

public class TestListBox {
        private List<TestBean2> listResults;
        private TestBean2[] arrayResults;

        public List<TestBean2> getListResults() {
                return listResults;
        }
        public void setListResults(List<TestBean2> listResults) {
                this.listResults = listResults;
        }
        public TestBean2[] getArrayResults() {
                return arrayResults;
        }
        public void setArrayResults(TestBean2[] arrayResults) {
                this.arrayResults = arrayResults;
        }


        public List getSelectItems() {
                List selectItems = new ArrayList();
                selectItems.add(new SelectItem(new TestBean2(10, "joe"), "joe"));
                selectItems.add(new SelectItem(new TestBean2(20, "bob"), "bob"));
                selectItems.add(new SelectItem(new TestBean2(30, "fred"), "fred"));

                return selectItems;
        }
}
