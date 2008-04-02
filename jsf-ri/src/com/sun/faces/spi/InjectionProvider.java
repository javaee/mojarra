package com.sun.faces.spi;

/**
 * <p>This interface defines an integration point for Java EE vendors.
 * Each vendor will need to provide an implementation of this interface
 * which will provide the JSF implementation the necessary hooks to 
 * perform resource injection.</p>  
 * 
 * <p>The implementation of this interface *must* be thread-safe and must
 * have a no-arg constructor.</p>
 */
public interface InjectionProvider {

    /**
     * <p>The implementation of this method must perform the following
     * steps:
     *    <ul>
     *        <li>Inject the supported resources per the Servlet 2.5
     *           specification into the provided object</li>
     *        <li>Inoke any method marked with the <code>@PreDestroy</code>
     *          annotation (per the Common Annotations Specification)</li>
     *    </ul>
     * </p>
     * @param managedBean the target managed bean
     * @throws InjectionProviderException if an error occurs during 
     *  resource injection
     */
    public void inject(Object managedBean) throws InjectionProviderException;

    
    /**
     * <p>The implemenation of this method must invoke any
     * method marked with the <code>@PreDestroy</code> annotation
     * (per the Common Annotations Specification).
     * @param managedBean the target managed bean
     * @throws InjectionProviderException if an error occurs when invoking
     *  the method annotated by the <code>@PreDestroy</code> annotation
     */
    public void invokePreDestroy(Object managedBean) 
    throws InjectionProviderException;
}
