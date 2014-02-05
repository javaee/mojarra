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
