/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2011 Oracle and/or its affiliates. All rights reserved.
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

package switchlist;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ActionEvent;
import java.util.Map;
import java.util.LinkedHashMap;
import java.io.Serializable;


@ManagedBean(name="switchlist")
@SessionScoped
public class SwitchlistBean implements Serializable {

    private static final long serialVersionUID = -6301210065157592489L;

    private Map<String, String> items1 = new LinkedHashMap<String, String>();
    private Map<String, String> items2 = new LinkedHashMap<String, String>();
    private String[] list1 = null;
    private String[] list2 = null;

    {   items1.put("one", "one");
        items1.put("two", "two");
        items1.put("three", "three");
        items1.put("four", "four");     }

    {   items2.put("five", "five");
        items2.put("six", "six");
        items2.put("seven", "seven");
        items2.put("eight", "eight");   }

    public void move1to2(ActionEvent ae) {
        if (list1 != null && list1.length > 0) {
            for (String item : list1 ) {
                items2.put(item, items1.remove(item));
            }
        }
    }

    public void move2to1(ActionEvent ae) {
        if (list2 != null && list2.length > 0) {
            for (String item : list2 ) {
                items1.put(item, items2.remove(item));
            }
        }
    }

    public String[] getList1() {
        return list1;
    }

    public void setList1(String list[]) {
        this.list1 = list;
    }

    public String[] getList2() {
        return list2;
    }

    public void setList2(String list[]) {
        this.list2 = list;
    }

    public Map getItems1() {
        return items1;    
    }

    public Map getItems2() {
        return items2;
    }
}
