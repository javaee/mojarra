/*
 * $Id: SelectManyBean.java,v 1.4 2005/08/22 22:09:43 ofung Exp $
 */

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
 * Copyright 2005 Sun Microsystems Inc. All Rights Reserved
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


    private int intValuesArray[] = new int[] { 2, 4, 6 };
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
        { new SelectManyRegistered("foo"),
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
        { new SelectManyUnregistered("foo"),
          new SelectManyUnregistered("baz"),
        };
    public SelectManyUnregistered[] getUnregisteredArray() {
        return (this.unregisteredArray);
    }
    public void setUnregisteredArray(SelectManyUnregistered unregisteredArray[]) {
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
