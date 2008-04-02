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


package com.sun.faces.config;

import java.util.HashMap;
import java.util.List;

public class NewCustomerFormHandler {


    public NewCustomerFormHandler() {
    }


    public String loginRequired() {
        return "loginRequired";
    }


    private String minimumAge;


    public String getMinimumAge() {
        return minimumAge;
    }


    public void setMinimumAge(String minimumAge) {
        this.minimumAge = minimumAge;
    }


    private String maximumAge;


    public String getMaximumAge() {
        return maximumAge;
    }


    public void setMaximumAge(String maximumAge) {
        this.maximumAge = maximumAge;
    }


    private String nationality;


    public String getNationality() {
        return nationality;
    }


    public void setNationality(String nationality) {
        this.nationality = nationality;
    }


    private List allowableValues;


    public List getAllowableValues() {
        return allowableValues;
    }


    public void setAllowableValues(List allowableValues) {
        this.allowableValues = allowableValues;
    }


    private String[] firstNames = {
        "bob",
        "jerry"
    };


    public String[] getFirstNames() {
        return firstNames;
    }


    public void setFirstNames(String[] newNames) {
        firstNames = newNames;
    }


    private HashMap claimAmounts;


    public HashMap getClaimAmounts() {
        return claimAmounts;
    }


    public void setClaimAmounts(HashMap claimAmounts) {
        this.claimAmounts = claimAmounts;
    }


}
