/*
 * $Id: ConfigConverter.java,v 1.3 2004/02/04 23:39:34 ofung Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.webapp;


/**
 * <p>Config Bean for a Converter.</p>
 */
public class ConfigConverter extends ConfigFeature {

    private String converterId;
    public String getConverterId() {
        return (this.converterId);
    }
    public void setConverterId(String converterId) {
        this.converterId = converterId;
    }

    private String converterClass;
    public String getConverterClass() {
        return (this.converterClass);
    }
    public void setConverterClass(String converterClass) {
        this.converterClass = converterClass;
    }

}
