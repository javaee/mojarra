/*
 * $Id: Generator.java,v 1.1 2004/12/13 19:07:48 rlubke Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.generate;

import com.sun.faces.config.beans.FacesConfigBean;

/**
 * <p>Base interface for all <code>jsf-tools</code> generators.</p>
 */
public interface Generator {

    /**
     * <p>Perform whatever generation tasks are necessary using
     * the provided <code>FacesConfigBean</code> as the model.
     *
     * @param configBean model data
     */
    public void generate(FacesConfigBean configBean);

}
