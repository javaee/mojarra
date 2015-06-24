/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2010 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */

package javax.faces;


/**
 * <p><strong class="changed_modified_2_0 changed_modified_2_1 changed_modified_2_2">FactoryFinder</strong>
 * implements the standard discovery algorithm for all factory objects
 * specified in the JavaServer Faces APIs.  For a given factory class
 * name, a corresponding implementation class is searched for based on
 * the following algorithm.  Items are listed in order of decreasing
 * search precedence:</p> 

 * <ul> 

 * <li><p>If the JavaServer Faces configuration file bundled into the
 * <code>WEB-INF</code> directory of the webapp contains a
 * <code>factory</code> entry of the given factory class name, that
 * factory is used.<p></li>

 * <li><p>If the JavaServer Faces configuration files named by the
 * <code>javax.faces.CONFIG_FILES</code> <code>ServletContext</code> init
 * parameter contain any <code>factory</code> entries of the given
 * factory class name, those injectionProvider are used, with the last one taking
 * precedence.</p></li> 

 * <li><p>If there are any JavaServer Faces configuration files bundled
 * into the <code>META-INF</code> directory of any jars on the
 * <code>ServletContext</code>'s resource paths, the
 * <code>factory</code> entries of the given factory class name in those
 * files are used, with the last one taking precedence.</p></li>

 * <li><p>If a <code>META-INF/services/{factory-class-name}</code>
 * resource is visible to the web application class loader for the
 * calling application (typically as a injectionProvider of being present in the
 * manifest of a JAR file), its first line is read and assumed to be the
 * name of the factory implementation class to use.</p></li>

 * <li><p>If none of the above steps yield a match, the JavaServer Faces
 * implementation specific class is used.</p></li>

 * </ul>

 * <p>If any of the injectionProvider found on any of the steps above happen to
 * have a one-argument constructor, with argument the type being the
 * abstract factory class, that constructor is invoked, and the previous
 * match is passed to the constructor.  For example, say the container
 * vendor provided an implementation of {@link
 * javax.faces.context.FacesContextFactory}, and identified it in
 * <code>META-INF/services/javax.faces.context.FacesContextFactory</code>
 * in a jar on the webapp ClassLoader.  Also say this implementation
 * provided by the container vendor had a one argument constructor that
 * took a <code>FacesContextFactory</code> instance.  The
 * <code>FactoryFinder</code> system would call that one-argument
 * constructor, passing the implementation of
 * <code>FacesContextFactory</code> provided by the JavaServer Faces
 * implementation.</p>

 * <p>If a Factory implementation does not provide a proper one-argument
 * constructor, it must provide a zero-arguments constructor in order to
 * be successfully instantiated.</p>

 * <p>Once the name of the factory implementation class is located, the
 * web application class loader for the calling application is requested
 * to load this class, and a corresponding instance of the class will be
 * created.  A side effect of this rule is that each web application
 * will receive its own instance of each factory class, whether the
 * JavaServer Faces implementation is included within the web
 * application or is made visible through the container's facilities for
 * shared libraries.</p>
 */

public final class FactoryFinder {

    // ----------------------------------------------------------- Constructors


    /**
     * Package-private constructor to disable instantiation of this class.
     */
    FactoryFinder() {
    }

    // ----------------------------------------------------- Manifest Constants


    /**
     * <p>The property name for the
     * {@link javax.faces.application.ApplicationFactory} class name.</p>
     */
    public final static String APPLICATION_FACTORY =
         "javax.faces.application.ApplicationFactory";

    /**
     * <p>The property name for the
     * {@link javax.faces.lifecycle.ClientWindowFactory} class name.</p>
     * @since 2.2
     */
    public final static String CLIENT_WINDOW_FACTORY =
         "javax.faces.lifecycle.ClientWindowFactory";

    /**
     * <p class="changed_added_2_0">The property name for the {@link
     * javax.faces.context.ExceptionHandlerFactory} class name.</p>
     */
    public final static String EXCEPTION_HANDLER_FACTORY =
         "javax.faces.context.ExceptionHandlerFactory";

    /**
     * <p class="changed_added_2_0">The property name for the {@link
     * javax.faces.context.ExternalContextFactory} class name.</p>
     */
    public final static String EXTERNAL_CONTEXT_FACTORY =
         "javax.faces.context.ExternalContextFactory";

    /**
     * <p>The property name for the
     * {@link javax.faces.context.FacesContextFactory} class name.</p>
     */
    public final static String FACES_CONTEXT_FACTORY =
         "javax.faces.context.FacesContextFactory";

    /**
     * <p class="changed_added_2_1">The property name for the
     * {@link javax.faces.view.facelets.FaceletCacheFactory} class name.</p>
     *
     * @since 2.1
     */
    public final static String FACELET_CACHE_FACTORY =
         "javax.faces.view.facelets.FaceletCacheFactory";

    /**
     * <p class="changed_added_2_2">The property name for the
     * {@link javax.faces.context.FlashFactory} class name.</p>
     * 
     * @since 2.2
     */
    public final static String FLASH_FACTORY =
         "javax.faces.context.FlashFactory";

    /**
     * <p class="changed_added_2_2">The property name for the
     * {@link javax.faces.flow.FlowHandlerFactory} class name.</p>
     * 
     * @since 2.2
     */
    public final static String FLOW_HANDLER_FACTORY =
         "javax.faces.flow.FlowHandlerFactory";

    /**
     * <p class="changed_added_2_0">The property name for the {@link
     * javax.faces.context.PartialViewContextFactory} class name.</p>
     */
    public final static String PARTIAL_VIEW_CONTEXT_FACTORY =
          "javax.faces.context.PartialViewContextFactory";

    /**
     * <p class="changed_added_2_0">The property name for the {@link
     * javax.faces.component.visit.VisitContextFactory} class name.</p>
     */
    public final static String VISIT_CONTEXT_FACTORY =
         "javax.faces.component.visit.VisitContextFactory";

    /**
     * <p>The property name for the
     * {@link javax.faces.lifecycle.LifecycleFactory} class name.</p>
     */
    public final static String LIFECYCLE_FACTORY =
         "javax.faces.lifecycle.LifecycleFactory";

    /**
     * <p>The property name for the
     * {@link javax.faces.render.RenderKitFactory} class name.</p>
     */
    public final static String RENDER_KIT_FACTORY =
         "javax.faces.render.RenderKitFactory";

    /**
     * <p class="changed_added_2_0">The property name for the {@link
     * javax.faces.view.ViewDeclarationLanguage} class name.</p>
     */
    public final static String VIEW_DECLARATION_LANGUAGE_FACTORY =
         "javax.faces.view.ViewDeclarationLanguageFactory";

    /**
     * <p class="changed_added_2_0">The property name for the {@link
     * javax.faces.view.facelets.TagHandlerDelegate} class name.</p>
     */
    public final static String TAG_HANDLER_DELEGATE_FACTORY =
         "javax.faces.view.facelets.TagHandlerDelegateFactory";

    // ------------------------------------------------------- Static Variables

    static final CurrentThreadToServletContext FACTORIES_CACHE;

    static {
        FACTORIES_CACHE = new CurrentThreadToServletContext();
    }

    // --------------------------------------------------------- Public Methods


    /**
     * <p><span class="changed_modified_2_0">Create</span> (if
     * necessary) and return a per-web-application instance of the
     * appropriate implementation class for the specified JavaServer
     * Faces factory class, based on the discovery algorithm described
     * in the class description.</p>
     *
     * <p class="changed_added_2_0">The standard injectionProvider and wrappers
     * in JSF all implement the interface {@link FacesWrapper}.  If the
     * returned <code>Object</code> is an implementation of one of the
     * standard injectionProvider, it must be legal to cast it to an instance of
     * <code>FacesWrapper</code> and call {@link
     * FacesWrapper#getWrapped} on the instance.</p>
     *
     * @param factoryName Fully qualified name of the JavaServer Faces factory
     *                    for which an implementation instance is requested
     * @throws FacesException           if the web application class loader
     *                                  cannot be identified
     * @throws FacesException           if an instance of the configured factory
     *                                  implementation class cannot be loaded
     * @throws FacesException           if an instance of the configured factory
     *                                  implementation class cannot be instantiated
     * @throws IllegalArgumentException if <code>factoryName</code> does not
     *                                  identify a standard JavaServer Faces factory name
     * @throws IllegalStateException    if there is no configured factory
     *                                  implementation class for the specified factory name
     * @throws NullPointerException     if <code>factoryname</code>
     *                                  is null
     */
    public static Object getFactory(String factoryName)
         throws FacesException {

        FactoryFinderInstance manager;
        // Bug 20458755: If the factory being requested is the special 
        // SERVLET_CONTEXT_FINDER, do not lazily create the FactoryFinderInstance.
        if (null != factoryName && factoryName.equals(ServletContextFacesContextFactory.SERVLET_CONTEXT_FINDER_NAME)) {
            manager = FACTORIES_CACHE.getApplicationFactoryManager(false);
        } else {
            manager = FACTORIES_CACHE.getApplicationFactoryManager();
        }
        Object result = null;
        if (null != manager) {
         result = manager.getFactory(factoryName);
        }
        return result;

    }

    /**
     * <p>This method will store the argument
     * <code>factoryName/implName</code> mapping in such a way that
     * {@link #getFactory} will find this mapping when searching for a
     * match.</p>
     * <p/>
     * <p>This method has no effect if <code>getFactory()</code> has
     * already been called looking for a factory for this
     * <code>factoryName</code>.</p>
     * <p/>
     * <p>This method can be used by implementations to store a factory
     * mapping while parsing the Faces configuration file</p>
     *
     * @throws IllegalArgumentException if <code>factoryName</code> does not
     *                                  identify a standard JavaServer Faces factory name
     * @throws NullPointerException     if <code>factoryname</code>
     *                                  is null
     */
    public static void setFactory(String factoryName,
                                  String implName) {

        FactoryFinderInstance manager =
              FACTORIES_CACHE.getApplicationFactoryManager();
        manager.addFactory(factoryName, implName);

    }


    /**
     * <p><span class="changed_modified_2_0">Release</span> any
     * references to factory instances associated with the class loader
     * for the calling web application.  <span
     * class="changed_modified_2_0">This method must be called during of
     * web application shutdown.</span></p>
     *
     * @throws FacesException if the web application class loader
     *                        cannot be identified
     */
    public static void releaseFactories() throws FacesException {
        synchronized(FACTORIES_CACHE) {

            if (!FACTORIES_CACHE.applicationMap.isEmpty()) {

                FactoryFinderInstance fm = FACTORIES_CACHE.getApplicationFactoryManager();
                fm.releaseFactories();
            }

            FACTORIES_CACHE.removeApplicationFactoryManager();
        }
    }


    // -------------------------------------------------------- Private Methods

    // Called via reflection from automated tests.
    private static void reInitializeFactoryManager() {
        FACTORIES_CACHE.resetSpecialInitializationCaseFlags();
    }

}
