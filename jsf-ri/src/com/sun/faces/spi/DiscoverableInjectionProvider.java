package com.sun.faces.spi;

import com.sun.faces.util.Util;

/**
 * <p><code>InjectionProvider</code>s that implement this interface
 * can be configured via <code>META-INF/services/com.sun.faces.spi.InjectionProvider</code>.
 *
 * <p>The format of the configuration entries is:</p>
 * <ul>
 *   <li><code>&lt;InjectionProviderClassName&gt;:&lt;DelegateClassName&gt;</code></li>
 * <ul>
 *
 * <p>Example:</p}
 * <ul>
 *    <li><code>com.sun.faces.vendor.GlassFishInjectionProvider:com.sun.enterprise.InjectionManager</code></li>
 * </ul>
 *
 * <p>Multiple <code>DiscoverableInjectionProvider</code>s can be configured
 * within a single services entry.</p>
 */
public abstract class DiscoverableInjectionProvider implements InjectionProvider {


    /**
     * @param delegateClass the name of the delegate used by the
     *  <code>InjectionProvider</code> implementation.
     * @return returns <code>true</code> if the
     *  <code>InjectionProvider</code> instance
     *  is appropriate for the container its currently
     *  deployed within, otherwise return <code>false</code>
     */
    public static boolean isInjectionFeatureAvailable(String delegateClass) {

        try {
            Util.loadClass(delegateClass, null);
            return true;
        } catch (Exception e) {
            return false;
        }
        
    }

}
