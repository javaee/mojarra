/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package javax.faces.application;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p class="changed_added_2_0">Instances of {@link javax.faces.component.UIComponent}
 * or {@link javax.faces.render.Renderer} that have this annotation attached at
 * the class level will automatically have a resource dependency added so that
 * the named resource will be present in user agent's view of the
 * <code>UIViewRoot</code> in which this component or renderer is used.</p>
 * <p/>
 * <div class="changed_added_2_0">
 * <p/>
 * <p>For all callsites that must process this annotation (listed at the point
 * in the specification where the annotation processing must occur), the
 * following action must be taken.  </p>
 *
 * <ol>
 * <li><p> If this annotation is not present on the class in question, no action
 * must be taken.  </p></li>
 *
 * <li><p>Create a {@link javax.faces.component.UIOutput} instance by passing
 * <code>javax.faces.Output</code>. to {@link Application#createComponent(java.lang.String)}.</p></li>
 *
 * <li><p>Get the annotation instance from the class and obtain the values of
 * the <em>name</em>, <em>library</em>, and <em>target</em>
 * attributes.</p></li>
 *
 * <li><p>If <em>name</em> is the empty string throw an
 * <code>IllegalArgumentException</code> </p></li>
 *
 * <li><p>If <em>library</em> is the empty string, let <em>library</em>
 * be <code>null</code>.  Otherwise, if the literal string
 * <code>&quot;this&quot;</code> is the value of <em>library</em>
 * attribute, take the following action to replace the
 * <code>&quot;this&quot;</code> value for <em>library</em>.</p>

 * <p>If the annotated artifact is an instance of {@link
 * javax.faces.component.UIComponent}, or is an instance of {@link
 * javax.faces.render.Renderer} and an instance of the
 * <code>UIComponent</code> for that kind of <code>Renderer</code> is
 * available, ask the <code>UIComponent</code> instance for the
 * component attribute under the key {@link
 * Resource#COMPONENT_RESOURCE_KEY}.  If a value exists under that key,
 * assume the value is a {@link Resource} and replace the
 * <code>&quot;this&quot;</code> value for <em>library</em> with the
 * return from {@link Resource#getLibraryName}.</p>

 * </li>
 *
 * <li><p>If <em>target</em> is the empty string, let <em>target</em> be
 * <code>null</code>.</p></li>
 *
 * <li><p>Obtain the <em>renderer-type</em> for the resource <em>name</em> by
 * passing <em>name</em> to {@link ResourceHandler#getRendererTypeForResourceName}.</p></li>
 *
 * <li><p>Call <code>setRendererType</code> on the <code>UIOutput</code>
 * instance, passing the <em>renderer-type</em>.</p></li>
 *
 * <li><p>Obtain the <code>Map</code> of attributes from the
 * <code>UIOutput</code> component by calling {@link javax.faces.component.UIComponent#getAttributes}.</p></li>
 *
 * <li><p>Store the <em>name</em> into the attributes <code>Map</code> under the
 * key "name".</p></li>
 *
 * <li><p>If <em>library</em> is non-<code>null</code>, store it under the key
 * "library".</p></li>
 *
 * <li><p>If <em>target</em> is non-<code>null</code>, store it under the key
 * "target". </p></li>
 *
 * <li><p>If <em>target</em> is non-<code>null</code>, call {@link
 * javax.faces.component.UIViewRoot#addComponentResource(javax.faces.context.FacesContext,
 * javax.faces.component.UIComponent, java.lang.String)}, passing the
 * <code>UIOutput</code> instance and the <em>target</em> String as the second
 * and third parameters, respectively.</p></li>
 *
 * <li><p>Otherwise, if <em>target</em> is <code>null</code>, call {@link
 * javax.faces.component.UIViewRoot#addComponentResource(javax.faces.context.FacesContext,
 * javax.faces.component.UIComponent)}, passing the <code>UIOutput</code>
 * instance as the second argument.</p></li>
 * </ol>
 * </div>
 *
 * @since 2.0
 */
@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = ElementType.TYPE)
public @interface ResourceDependency {

    /**
     * <p class="changed_added_2_0">The <em>resourceName</em> of the
     * resource pointed to by this <code>ResourceDependency</code>.</p>
     */
    public String name();

    /**
     * <p class="changed_added_2_0">The <em>libraryName</em> in which
     * the resource pointed to by this <code>ResourceDependency</code>
     * resides.  If not specified, defaults to the empty string.</p>
     */
    public String library() default "";

    /**
     * <p class="changed_added_2_0">The value given for this attribute
     * will be passed as the "target" argument to {@link
     * javax.faces.component.UIViewRoot#addComponentResource(javax.faces.context.FacesContext, javax.faces.component.UIComponent, java.lang.String)}.  If this attribute is specified, {@link
 * javax.faces.component.UIViewRoot#addComponentResource(javax.faces.context.FacesContext,
 * javax.faces.component.UIComponent)} must be called instead, as described above.</p>
     */
    public String target() default "";

}
