/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.config;

public class ClaimConfiguration extends Object {

    public ClaimConfiguration() {}

    protected Double waterDamageAmount = new Double(100.0);
    public Double getWaterDamageAmount() {
	return waterDamageAmount;
    }

    public void setWaterDamageAmount(Double newWaterDamageAmount) {
	waterDamageAmount = newWaterDamageAmount;
    }


}
