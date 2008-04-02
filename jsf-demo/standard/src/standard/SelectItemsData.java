/*
 * $Id: SelectItemsData.java,v 1.5 2005/08/22 22:09:43 ofung Exp $
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
import javax.faces.model.SelectItemGroup;

import java.util.ArrayList;
import java.util.List;


/**
 * <p>This class provides collections of <code>SelectItem</code> instances
 * that are useful in use cases for the <code>UISelectMany</code> and
 * <code>UISelectOne</code> components.</p>
 */

public class SelectItemsData {


    // --------------------------------------------------------- Data Properties


    private List disableds;


    // Return a set of items where all even-numbered items are disabled
    public List getDisableds() {

        if (disableds == null) {
            disableds = new ArrayList();
            disableds.add(new SelectItem("A0", "Item A0", "", true));
            disableds.add(new SelectItem("A1", "Item A1", "", false));
            disableds.add(new SelectItem("A2", "Item A2", "", true));
            disableds.add(new SelectItem("A3", "Item A3", "", false));
            disableds.add(new SelectItem("A4", "Item A4", "", true));
            disableds.add(new SelectItem("A5", "Item A5", "", false));
        }
        return (disableds);

    }


    private List nesteds;


    // Return a set of nested items were even-numbered items in the primary
    // group are disabled, and odd-numbered items in the secondary group
    public List getNesteds() {

        if (nesteds == null) {
            System.out.println("Creating primary");
            SelectItem primary[] = new SelectItem[3];
            primary[0] = new SelectItem("P0", "Item P0", "", true);
            primary[1] = new SelectItem("P1", "Item P1", "", false);
            primary[2] = new SelectItem("P2", "Item P2", "", true);
            System.out.println("Creating secondary");
            SelectItem secondary[] = new SelectItem[3];
            secondary[0] = new SelectItem("S0", "Item S0", "", false);
            secondary[1] = new SelectItem("S1", "Item S1", "", true);
            secondary[2] = new SelectItem("S2", "Item S2", "", false);
            nesteds = new ArrayList();
            System.out.println("Creating nesteds");
            nesteds.add(new SelectItemGroup
                ("Primary", "", false, primary));
            nesteds.add(new SelectItemGroup
                ("Secondary", "", true, secondary));
            System.out.println("Returning nesteds");
        }
        return (nesteds);

    }


    // -------------------------------------------------------- Value Properties


    private String disabled;


    public String getDisabled() {
        return disabled;
    }


    public void setDisabled(String disabled) {
        this.disabled = disabled;
    }


    private String nested;


    public String getNested() {
        return nested;
    }


    public void setNested(String nested) {
        this.nested = nested;
    }


    private String[] disabledSelected;


    public String[] getDisabledSelected() {
        return disabledSelected;
    }


    public void setDisabledSelected(String[] disabledSelected) {
        this.disabledSelected = disabledSelected;
    }


    private String[] nestedSelected;


    public String[] getNestedSelected() {
        return nestedSelected;
    }


    public void setNestedSelected(String[] nested) {
        this.nestedSelected = nestedSelected;
    }

}
