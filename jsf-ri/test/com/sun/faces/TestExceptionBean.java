/*
 * $Id: TestExceptionBean.java,v 1.1 2004/05/10 19:56:13 jvisvanathan Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces;
/**
 * <p>JavaBean represented the data for an individual customer.</p>
 */

public class TestExceptionBean implements java.io.Serializable {


    public TestExceptionBean() throws InstantiationException{
        throw new InstantiationException("TestConstructorException Passed");
    }


    private String name = null;

    public String getName() {
        return (this.name);
    }


    public void setName(String name) {
        this.name = name;
    }

}
