/*
 * $Id: ManagedBeanFactory.java,v 1.6 2006/09/01 01:22:59 tony_robertson Exp $
 */

/*
 * The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the License). You may not use this file except in
 * compliance with the License.
 *
 * You can obtain a copy of the License at
 * https://javaserverfaces.dev.java.net/CDDL.html or
 * legal/CDDLv1.0.txt.
 * See the License for the specific language governing
 * permission and limitations under the License.
 *
 * When distributing Covered Code, include this CDDL
 * Header Notice in each file and include the License file
 * at legal/CDDLv1.0.txt.
 * If applicable, add the following below the CDDL Header,
 * with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 *
 * [Name of File] [ver.__] [Date]
 *
 * Copyright 2005 Sun Microsystems Inc. All Rights Reserved
 */

package com.sun.faces.spi;

import com.sun.faces.config.beans.ManagedBeanBean;
import java.util.Map;

import javax.faces.context.FacesContext;

/**
 * 
 * <p>Define the interface for instantiating a jsf
 * <code>managed-bean</code> instance and discovering its scope.  This
 * class primarily has value when used in concert with the {@link
 * ManagedBeanFactoryWrapper} class.  Clients may provide a
 * &lt;context-param&gt;, as in the following example:</p>
 *
 * <pre><code>
&lt;context-param&gt;
  &lt;param-name&gt;com.sun.faces.managedBeanFactoryDecoratorClass&lt;/param-name&gt;
  &lt;param-value&gt;com.sun.faces.systest.NewManagedBeanFactory&lt;/param-value&gt;
&lt;/context-param&gt;
 * </code></pre>
 *
 * <p>If the class identified by the &lt;param-value&gt; element above
 * has a public constructor that takes a reference to a
 * <code>ManagedBeanFactory</code> as its only argument, it will be
 * called for every &lt;managed-bean&gt; element in the application
 * configuration resources.  This argument
 * <code>ManagedBeanFactory</code> instance, called the "parent" will
 * have already been populated with a <code>ManagedBeanBean</code>
 * instance that is a JavaBean that represents the information from the
 * application configuration resources for that particular bean.  If
 * desired, the custom <code>ManagedBeanFactory</code> class can
 * leverage any public methods from the parent.</p>
 *
 * <p>Usage Example</p>
 *
 * <pre><code>
public class NewManagedBeanFactory extends ManagedBeanFactoryWrapper {
    
    private ManagedBeanFactory parent = null;
    
    public NewManagedBeanFactory(ManagedBeanFactory old) {
        this.parent = old;
    }

    public ManagedBeanFactory getWrapped() {
        return parent;
    }
    
    public Object newInstance(FacesContext context) {
      Object newBean = parent.newInstance(context);
      // Take some action after the instantiation of every managed 
      // bean, for example, notify a bean lifecycle listener.

      Scope scope = parent.getScope();
      if (scope == Scope.SESSION) {
        // Take some action involving an HttpSessionListener
        // to call a lifecycle method when the session is destroyed
      }
      else if (scope == Scope.REQUEST) {
        // Take some action involving an ServletRequestListener
        // to call a lifecycle method when the session is destroyed
      }
      else if (scope == Scope.APPLICATION) {
        // Take some action involving a ServletContextListener to call
        // a lifecycle method when the application is destroyed.
      }
    }
}

 * </code></pre>
 *
 * <p>This example shows how you can provide a simple class that hooks
 * into the managed bean creation lifecycle to provide a lifecycle
 * notification scheme.</p>
 *
 * @author edburns, rlubke
 */
public abstract class ManagedBeanFactory {
    
    public enum Scope {
        NONE, REQUEST, SESSION, APPLICATION
    }

    /**
     * <p>Return the {@link Scope} of the managed-bean created by this
     * factory.</p>
     */ 
    
    public abstract Scope getScope();

    /**
     * <p>Return a new instance of this managed-bean.  It is the
     * caller's responsibility to call {@link #getScope} and store the
     * returned managed-bean in the proper scope.</p>
     */
    
    public abstract Object newInstance(FacesContext context);   

    /**
     * <p>Set the JavaBean that encapsulates the configuration data for
     * the bean instance to be created by this factory.</p>
     */
    
    public abstract void setManagedBeanBean(ManagedBeanBean bean);
    
    /**
     * <p>Get the JavaBean that encapsulates the configuration data for
     * the bean instance to be created by this factory.</p>
     */
    public abstract ManagedBeanBean getManagedBeanBean();

    /**
     * <p>Set the <code>Map</code> of managed-bean-name to
     * <code>ManagedBeanFactory</code> instances into this factory
     * instance so that properties that are managed beans may be
     * instantiated if necessary.</p>
     */ 
    
    public abstract void setManagedBeanFactoryMap(Map<String, ManagedBeanFactory> others);

    /**
     * <p>Get the <code>Map</code> of managed-bean-name to
     * <code>ManagedBeanFactory</code> instances passed in a previous
     * call to {@link #setManagedBeanFactoryMap}.</p>
     *
     * <p>Note that this property enables the factory to know the
     * complete set of configured managed-beans in this application.</p>
     */
    
    public abstract Map<String, ManagedBeanFactory> getManagedBeanFactoryMap();


    /**
     * @return <code>true</code> if the managed bean instance created
     * by this factory is a candidate for resource injection otherwise,
     * returns <code>false</code>
     */
    public abstract boolean isInjectable();
    
}
