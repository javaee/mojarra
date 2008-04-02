/*
 * $Id: ModelBean.java,v 1.1 2003/07/30 00:24:57 rlubke Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package standard;


/**
 * Simple bean for Model value demonstration. 
 *
 * @version $Id: ModelBean.java,v 1.1 2003/07/30 00:24:57 rlubke Exp $
 */

public class ModelBean {


    private String label = "Label from Model";

    public String getLabel() {
       return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
