/*
 * $Id: CustomerBean.java,v 1.4 2007/04/27 22:00:19 ofung Exp $
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

package carstore;

import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import java.util.ArrayList;
import java.util.Collection;
import java.util.ResourceBundle;

public class CustomerBean {


    private String firstName = null;
    private String middleInitial = null;
    private String lastName = null;
    private String mailingAddress = null;
    private String city = null;
    private String state = null;
    private String zip = null;
    private String month = null;
    private String year = null;


    public CustomerBean() {
        super();
    }


    protected Collection<SelectItem> titleOptions = null;


    public Collection getTitleOptions() {
        titleOptions = new ArrayList<SelectItem>(3);
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


    public void setTitleOptions(Collection<SelectItem> newOptions) {
        titleOptions = new ArrayList<SelectItem>(newOptions);
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
