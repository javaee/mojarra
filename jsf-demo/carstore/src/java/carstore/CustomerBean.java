/*
 * $Id: CustomerBean.java,v 1.2 2005/08/22 22:08:35 ofung Exp $
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

package carstore;

import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import java.util.ArrayList;
import java.util.Collection;
import java.util.ResourceBundle;

public class CustomerBean extends Object {


    String firstName = null;
    String middleInitial = null;
    String lastName = null;
    String mailingAddress = null;
    String city = null;
    String state = null;
    String zip = null;
    String month = null;
    String year = null;


    public CustomerBean() {
        super();
    }


    protected Collection titleOptions = null;


    public Collection getTitleOptions() {
        titleOptions = new ArrayList();
        ResourceBundle rb = ResourceBundle.getBundle(
            "carstore.bundles.Resources",
            (FacesContext.getCurrentInstance().getViewRoot().getLocale()));
        String titleStr = (String) rb.getObject("mrLabel");
        titleOptions.add(new SelectItem(titleStr, titleStr,
                                        titleStr));
        titleStr = (String) rb.getObject("mrsLabel");
        titleOptions.add(new SelectItem(titleStr, titleStr,
                                        titleStr));
        titleStr = (String) rb.getObject("msLabel");
        titleOptions.add(new SelectItem(titleStr, titleStr,
                                        titleStr));

        return titleOptions;
    }


    public void setTitleOptions(Collection newOptions) {
        titleOptions = new ArrayList(newOptions);
    }


    String title = null;


    public void setCurrentTitle(String newTitle) {
        title = newTitle;
    }


    public String getCurrentTitle() {
        return title;
    }


    public void setFirstName(String first) {
        firstName = first;
    }


    public String getFirstName() {
        return firstName;
    }


    public void setMiddleInitial(String mI) {
        middleInitial = mI;
    }


    public String getMiddleInitial() {
        return middleInitial;
    }


    public void setLastName(String last) {
        lastName = last;
    }


    public String getLastName() {
        return lastName;
    }


    public void setMailingAddress(String mA) {
        mailingAddress = mA;
    }


    public String getMailingAddress() {
        return mailingAddress;
    }


    public void setCity(String cty) {
        city = cty;
    }


    public String getCity() {
        return city;
    }


    public void setState(String sT) {
        state = sT;
    }


    public String getState() {
        return state;
    }


    public void setZip(String zipCode) {
        zip = zipCode;
    }


    public String getZip() {
        return zip;
    }


    public void setMonth(String mth) {
        month = mth;
    }


    public String getMonth() {
        return month;
    }


    public void setYear(String yr) {
        year = yr;
    }


    public String getYear() {
        return year;
    }
}
