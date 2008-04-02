package com.sun.faces.config;

import java.util.List;
import java.util.HashMap;

public class NewCustomerFormHandler {


    public NewCustomerFormHandler() {
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

    private String [] firstNames = { 
	"bob",
	"jerry"
    };
    public String [] getFirstNames() {
	return firstNames;
    }
    public void setFirstNames(String [] newNames) {
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
