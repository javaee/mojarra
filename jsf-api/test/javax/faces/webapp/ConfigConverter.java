/*
 * $Id: ConfigConverter.java,v 1.1 2003/04/07 21:45:34 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
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

    public String toString() {
        StringBuffer sb = new StringBuffer("ConfigConverter[converterId=");
        sb.append(converterId);
        sb.append(",converterClass=");
        sb.append(converterClass);
        sb.append("]");
        return (sb.toString());
    }

}
