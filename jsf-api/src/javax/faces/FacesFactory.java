/*
 * $Id: FacesFactory.java,v 1.1 2002/04/11 22:51:21 eburns Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces;

// FacesFactory.java

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.ServletContext;
import java.util.Map;

/**
 *

 *  In order to be available in the <B>AbstractFactory</B> system, a
 *  class must have a Factory that implements
 *  <B>FacesAbstractFactory</B>. <P>

 *
 * <B>Lifetime And Scope</B> <P>

 * Same as <B>AbstractFactory</B>.
 *
 * @version $Id: FacesFactory.java,v 1.1 2002/04/11 22:51:21 eburns Exp $
 * 
 * @see	javax.faces.AbstractFactory
 *
 */

public interface FacesFactory {

    public Object newInstance(String facesName, ServletRequest req, ServletResponse res) throws FacesException;
    public Object newInstance(String facesName, ServletContext ctx) throws FacesException;
    public Object newInstance(String facesName) throws FacesException;

    public Object newInstance(String facesName, Map args) throws FacesException;

} // end of interface FacesFactory
