/*
 * $Id: AbstractFactoryBase.java,v 1.1 2002/04/11 22:51:20 eburns Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces;

// AbstractFactoryBase.java

import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.ServletContext;

/**
 *

 *  A concrete subclass of <B>AbstractFactoryBase</B> allows runtime
 *  association of an API class with an implementation class.  <P>

* <B>Algorithm:</B> <P>

* When <CODE>newInstance(facesName,...)</CODE> is called,
* getFactoryForFacesName() is consulted, in concert with
* <CODE>FactoryFinder.find()</CODE>, to find an instance of
* <B>FacesFactory</B> for the given <B>facesName</B>.  This
* <B>FacesFactory</B> instance is then used to create the instance for
* <B>facesName</B>. <P>

 *
 * <B>Lifetime And Scope</B> <P> 

 * There should be one instance of an <B>AbstractFactoryBase</B>
 * subclass per ServletContext.  This class is <A
 * HREF="http://whatis.techtarget.com/definition/0,,sid9_gci331590,00.html">thread-safe</A>. 

 *
 * @version $Id: AbstractFactoryBase.java,v 1.1 2002/04/11 22:51:20 eburns Exp $
 * 
 * @see	javax.faces.FacesFactory
 * @see	javax.faces.FactoryFinder#find
 *
 */

public abstract class AbstractFactoryBase extends Object
{
//
// Protected Constants
//

//
// Class Variables
//

//
// Instance Variables
//

// Attribute Instance Variables

// Relationship Instance Variables

//
// Constructors and Initializers    
//

//
// Class methods
//

//
// General Methods
//

    protected abstract FacesFactory getFactoryForFacesName(String facesName) throws FactoryConfigurationError;

    // 
    // General methods
    //
    
    /**

    * This method allows the addition of a new facesName/FacesFactory
    * association at runtime.  If an association already exists for this
    * facesName, the old one is replaced.  <P>

    * @return previous FacesFactory associated with specified facesName,
    * or null if there was no associated FacesFactory for facesName.

    */

    public abstract Object addFactoryForFacesName(FacesFactory factory,
						  String facesName);

    public Object newInstance(String facesName) throws FactoryConfigurationError, FacesException {
	Object result = null;
	FacesFactory factory = getFactoryForFacesName(facesName);

	// Assert.assert_it(null != factory);

	result = factory.newInstance(facesName);
	return result;
    }

    public Object newInstance(String facesName, ServletRequest req,
			      ServletResponse resp) throws FactoryConfigurationError, FacesException {
	Object result = null;
	FacesFactory factory = getFactoryForFacesName(facesName);

	// Assert.assert_it(null != factory);
	
	result = factory.newInstance(facesName, req, resp);
	return result;
    }

    public Object newInstance(String facesName, ServletContext ctx) throws FactoryConfigurationError, FacesException {
	Object result = null;
	FacesFactory factory = getFactoryForFacesName(facesName);

	// Assert.assert_it(null != factory);

	result = factory.newInstance(facesName, ctx);
	return result;
    }

    public Object newInstance(String facesName, Map args) throws FactoryConfigurationError, FacesException {
	Object result = null;
	FacesFactory factory = getFactoryForFacesName(facesName);

	// Assert.assert_it(null != factory);

	result = factory.newInstance(facesName, args);
	return result;
    }


} // end of class AbstractFactoryBase


// The testcase for this class is com.sun.faces.TestAbstractFactory
