/*
 * $Id: CustomerBean.java,v 1.3 2004/02/05 16:21:09 rlubke Exp $
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
        if (null == titleOptions) {
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

        }

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
