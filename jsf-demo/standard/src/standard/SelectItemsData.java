/*
 * $Id: SelectItemsData.java,v 1.1 2004/01/30 07:43:14 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package standard;


import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;
import javax.faces.model.SelectItemGroup;


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
    public String getDisabled() { return disabled; }
    public void setDisabled(String disabled) { this.disabled = disabled; }


    private String nested;
    public String getNested() { return nested; }
    public void setNested(String nested) { this.nested = nested; }


}
