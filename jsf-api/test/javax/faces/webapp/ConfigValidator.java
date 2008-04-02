/*
 * $Id: ConfigValidator.java,v 1.1 2003/04/07 23:37:50 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.webapp;


/**
 * <p>Config Bean for a Validator.</p>
 */
public class ConfigValidator extends ConfigFeature {

    private String validatorId;
    public String getValidatorId() {
        return (this.validatorId);
    }
    public void setValidatorId(String validatorId) {
        this.validatorId = validatorId;
    }

    private String validatorClass;
    public String getValidatorClass() {
        return (this.validatorClass);
    }
    public void setValidatorClass(String validatorClass) {
        this.validatorClass = validatorClass;
    }

}
