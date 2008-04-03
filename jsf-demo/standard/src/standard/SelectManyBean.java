/*
 * $Id: SelectManyBean.java,v 1.6 2007/04/27 22:00:43 ofung Exp $
 */

/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
 * 
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License. You can obtain
 * a copy of the License at https://glassfish.dev.java.net/public/CDDL+GPL.html
 * or glassfish/bootstrap/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 * 
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at glassfish/bootstrap/legal/LICENSE.txt.
 * Sun designates this particular file as subject to the "Classpath" exception
 * as provided by Sun in the GPL Version 2 section of the License file that
 * accompanied this code.  If applicable, add the following below the License
 * Header, with the fields enclosed by brackets [] replaced by your own
 * identifying information: "Portions Copyrighted [year]
 * [name of copyright owner]"
 * 
 * Contributor(s):
 * 
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

package standard;


import javax.faces.model.SelectItem;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>This class provides options lists and values for <code>UISelectMany</code>
 * tests.</p>
 */

public class SelectManyBean implements Serializable {

    // ------------------------------------------------------------- Constructor


    public SelectManyBean() {
        System.out.println("SelectManyBean()");
    }

    // ---------------------------------------------------------- Current Values


    private int intValuesArray[] = new int[]{2, 4, 6};

    public int[] getIntValuesArray() {
        System.out.print("getIntValuesArray(");
        if (intValuesArray != null) {
            for (int i = 0; i < intValuesArray.length; i++) {
                System.out.print("" + intValuesArray[i] + ",");
            }
        }
        System.out.println(")");
        return (this.intValuesArray);
    }

    public void setIntValuesArray(int intValuesArray[]) {
        System.out.print("setIntValuesArray(");
        if (intValuesArray != null) {
            for (int i = 0; i < intValuesArray.length; i++) {
                System.out.print("" + intValuesArray[i] + ",");
            }
        }
        System.out.println(")");
        this.intValuesArray = intValuesArray;
    }


    private Integer integerValuesArray[] = new Integer[]
          {
                new Integer(3),
                new Integer(5),
                new Integer(7),
          };

    public Integer[] getIntegerValuesArray() {
        System.out.print("getIntegerValuesArray(");
        if (integerValuesArray != null) {
            for (int i = 0; i < intValuesArray.length; i++) {
                System.out.print("" + integerValuesArray[i] + ",");
            }
        }
        System.out.println(")");
        return (this.integerValuesArray);
    }

    public void setIntegerValuesArray(Integer integerValuesArray[]) {
        System.out.print("setIntegerValuesArray(");
        if (integerValuesArray != null) {
            for (int i = 0; i < intValuesArray.length; i++) {
                System.out.print("" + integerValuesArray[i] + ",");
            }
        }
        System.out.println(")");
        this.integerValuesArray = integerValuesArray;
    }


    private List integerValuesList = new ArrayList();

    {
        integerValuesList.add(new Integer(1));
        integerValuesList.add(new Integer(3));
        integerValuesList.add(new Integer(5));
    }

    public List getIntegerValuesList() {
        System.out.print("getIntegerValuesList(");
        if (integerValuesList != null) {
            for (int i = 0; i < integerValuesList.size(); i++) {
                System.out.print(integerValuesList.get(i) + ",");
            }
        }
        System.out.println(")");
        return (this.integerValuesList);
    }

    public void setIntegerValuesList(List integerValuesList) {
        System.out.print("setIntegerValuesList(");
        if (integerValuesList != null) {
            for (int i = 0; i < integerValuesList.size(); i++) {
                System.out.print(integerValuesList.get(i) + ",");
            }
        }
        System.out.println(")");
        this.integerValuesList = integerValuesList;
    }


    private String stringValuesArray[] = new String[]
          {
                "String 4",
                "String 6",
                "String 8",
          };

    public String[] getStringValuesArray() {
        System.out.print("getStringValuesArray(");
        if (stringValuesArray != null) {
            for (int i = 0; i < stringValuesArray.length; i++) {
                System.out.print(stringValuesArray[i] + ",");
            }
        }
        System.out.println(")");
        return (this.stringValuesArray);
    }

    public void setStringValuesArray(String stringValuesArray[]) {
        System.out.print("setStringValuesArray(");
        if (stringValuesArray != null) {
            for (int i = 0; i < stringValuesArray.length; i++) {
                System.out.print(stringValuesArray[i] + ",");
            }
        }
        System.out.println(")");
        this.stringValuesArray = stringValuesArray;
    }


    private List stringValuesList = new ArrayList();

    {
        stringValuesList.add("String 3");
        stringValuesList.add("String 6");
        stringValuesList.add("String 9");
    }

    public List getStringValuesList() {
        System.out.print("getStringValuesList(");
        if (stringValuesList != null) {
            for (int i = 0; i < stringValuesList.size(); i++) {
                System.out.print(stringValuesList.get(i) + ",");
            }
        }
        System.out.println(")");
        return (this.stringValuesList);
    }

    public void setStringValuesList(List stringValuesList) {
        System.out.print("setStringValuesList(");
        if (stringValuesList != null) {
            for (int i = 0; i < stringValuesList.size(); i++) {
                System.out.print(stringValuesList.get(i) + ",");
            }
        }
        System.out.println(")");
        this.stringValuesList = stringValuesList;
    }


    private SelectManyRegistered registeredArray[] =
          new SelectManyRegistered[]
                {new SelectManyRegistered("foo"),
                 new SelectManyRegistered("baz"),
                };

    public SelectManyRegistered[] getRegisteredArray() {
        return (this.registeredArray);
    }

    public void setRegisteredArray(SelectManyRegistered registeredArray[]) {
        this.registeredArray = registeredArray;
    }


    private SelectManyUnregistered unregisteredArray[] =
          new SelectManyUnregistered[]
                {new SelectManyUnregistered("foo"),
                 new SelectManyUnregistered("baz"),
                };

    public SelectManyUnregistered[] getUnregisteredArray() {
        return (this.unregisteredArray);
    }

    public void setUnregisteredArray(
          SelectManyUnregistered unregisteredArray[]) {
        this.unregisteredArray = unregisteredArray;
    }

    // ----------------------------------------------------------- Options Lists


    public SelectItem[] getIntOptions() {
        SelectItem items[] = new SelectItem[10];
        for (int i = 0; i < items.length; i++) {
            items[i] = new SelectItem(new Integer(i), "Option " + i);
        }
        return (items);
    }


    public SelectItem[] getIntegerOptions() {
        SelectItem items[] = new SelectItem[10];
        for (int i = 0; i < items.length; i++) {
            items[i] = new SelectItem(new Integer(i), "Option " + i);
        }
        return (items);
    }


    public SelectItem[] getStringOptions() {
        SelectItem items[] = new SelectItem[10];
        for (int i = 0; i < items.length; i++) {
            items[i] = new SelectItem("String " + i, "Option " + i);
        }
        return (items);
    }


    public SelectItem[] getRegisteredOptions() {
        SelectItem items[] = new SelectItem[4];
        items[0] = new SelectItem(new SelectManyRegistered("foo"));
        items[1] = new SelectItem(new SelectManyRegistered("bar"));
        items[2] = new SelectItem(new SelectManyRegistered("baz"));
        items[3] = new SelectItem(new SelectManyRegistered("bop"));
        return (items);
    }


    public SelectItem[] getUnregisteredOptions() {
        SelectItem items[] = new SelectItem[4];
        items[0] = new SelectItem(new SelectManyUnregistered("foo"));
        items[1] = new SelectItem(new SelectManyUnregistered("bar"));
        items[2] = new SelectItem(new SelectManyUnregistered("baz"));
        items[3] = new SelectItem(new SelectManyUnregistered("bop"));
        return (items);
    }


}
