/*
 * $Id: ApplicationFactory.java,v 1.2 2003/03/13 01:11:54 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.application;


/**
 * <p><strong>ApplicationFactory</strong> is a factory object that creates
 * (if needed) and returns {@link Application} instances.  Implementations of
 * JavaServer Faces must provide at least a default implementation of
 * {@link Application}.</p>
 *
 * <p>There must be one {@link ApplicationFactory} instance per web
 * application that is utilizing JavaServer Faces.  This instance can be
 * acquired, in a portable manner, by calling:</p>
 * <pre>
 *   ApplicationFactory factory = (ApplicationFactory)
 *    FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY);
 * </pre>
 */

public abstract class ApplicationFactory {


    /**
     * <p>Create (if needed) and return an {@link Application} instance
     * for this web application.</p>
     */
    public abstract Application getApplication();


    /**
     * <p>Replace the {@link Application} instance that will be
     * returned for this web application.</p>
     *
     * @param application The replacement {@link Application} instance
     */
    public abstract void setApplication(Application application);


}
