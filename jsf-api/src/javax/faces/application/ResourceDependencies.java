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
 * <p class="changed_added_2_0">Instances of {@link
 * javax.faces.component.UIComponent} or {@link
 * javax.faces.render.Renderer} that have this annotation attached at
 * the class level will automatically have the resource dependencies added
 * so that the named resources will be present in user agent's view of
 * the <code>UIViewRoot</code> in which this component or renderer is
 * used.</p>
 *
 * <div class="changed_added_2_0">

 * <p>For all callsites that must process this annotation (listed at the
 * point in the specification where the annotation processing must
 * occur), the following action must be taken.  </p>
 *
 * 	<ol>

	  <li><p> If this annotation is not present on the class in
	  question, no action must be taken.  </p></li>

          <li><p>Get the annotation instance from the class and obtain
          the contained array of annotation instances.</p></li>

	  <li><p>For each of the contained annotation instances:</p>
            <ul>
              <li><p>obtain the values of the <em>name</em>, 
              <em>library</em>, and <em>target</em> attributes.</p></li>

	      <li><p>Create a {@link javax.faces.component.UIOutput}
	      instance by passing <code>javax.faces.Output</code>. to {@link
	      Application#createComponent(java.lang.String)}.</p></li>

	      <li><p>If <em>name</em> is the empty string throw an
	      <code>IllegalArgumentException</code> </p></li>

	      <li><p>If <em>library</em> is the empty string, let
	      <em>library</em> be <code>null</code>.</p></li>

	      <li><p>If <em>target</em> is the empty string, let
	      <em>target</em> be <code>null</code>.</p></li>

	      <li><p>Obtain the <em>renderer-type</em> for the resource
	      <em>name</em> by passing <em>name</em> to {@link
	      ResourceHandler#getRendererTypeForResourceName}.</p></li>

	      <li><p>Call <code>setRendererType</code> on the
	      <code>UIOutput</code> instance, passing the
	      <em>renderer-type</em>.</p></li>

	      <li><p>Obtain the <code>Map</code> of attributes from the
	      <code>UIOutput</code> component by calling {@link
	      javax.faces.component.UIComponent#getAttributes}.</p></li>

	      <li><p>Store the <em>name</em> into the attributes
	      <code>Map</code> under the key "name".</p></li>

	      <li><p>If <em>library</em> is non-<code>null</code>, store it
	      under the key "library".</p></li>

	      <li><p>If <em>target</em> is non-<code>null</code>, store it
	      under the key "target". </p></li>

	      <li><p>If <em>target</em> is non-<code>null</code>, call
	      {@link
	      javax.faces.component.UIViewRoot#addComponentResource(javax.faces.context.FacesContext,
	      javax.faces.component.UIComponent, java.lang.String)}, passing the
	      <code>UIOutput</code> instance and the <em>target</em> String
	      as the second and third parameters, respectively.</p></li>

	      <li><p>Otherwise, if <em>target</em> is <code>null</code>,
	      call {@link
	      javax.faces.component.UIViewRoot#addComponentResource(javax.faces.context.FacesContext,
	      javax.faces.component.UIComponent)}, passing the
	      <code>UIOutput</code> instance as the second
	      argument.</p></li>
            </ul>
          </li>

	</ol>


 * </div>
 */
@Retention(value=RetentionPolicy.RUNTIME)
@Target(value=ElementType.TYPE)
public @interface ResourceDependencies {
    
    ResourceDependency[] value();
}
