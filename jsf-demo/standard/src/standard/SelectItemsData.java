/*
 * $Id: SelectItemsData.java,v 1.4 2004/05/12 18:47:07 ofung Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All Rights Reserved.
 * 
 * Redistribution and use in source and binary forms, with or
 * without modification, are permitted provided that the following
 * conditions are met:
 * 
 * - Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 * 
 * - Redistribution in binary form must reproduce the above
 *   copyright notice, this list of conditions and the following
 *   disclaimer in the documentation and/or other materials
 *   provided with the distribution.
 *    
 * Neither the name of Sun Microsystems, Inc. or the names of
 * contributors may be used to endorse or promote products derived
 * from this software without specific prior written permission.
 *  
 * This software is provided "AS IS," without a warranty of any
 * kind. ALL EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND
 * WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE OR NON-INFRINGEMENT, ARE HEREBY
 * EXCLUDED. SUN AND ITS LICENSORS SHALL NOT BE LIABLE FOR ANY
 * DAMAGES OR LIABILITIES SUFFERED BY LICENSEE AS A RESULT OF OR
 * RELATING TO USE, MODIFICATION OR DISTRIBUTION OF THIS SOFTWARE OR
 * ITS DERIVATIVES. IN NO EVENT WILL SUN OR ITS LICENSORS BE LIABLE
 * FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT,
 * SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER
 * CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF
 * THE USE OF OR INABILITY TO USE THIS SOFTWARE, EVEN IF SUN HAS
 * BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 *  
 * You acknowledge that this software is not designed, licensed or
 * intended for use in the design, construction, operation or
 * maintenance of any nuclear facility.
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
