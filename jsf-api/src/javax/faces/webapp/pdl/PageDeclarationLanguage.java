/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
 * 
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License. You can obtain
 * a copy of the License at https://glassfish.dev.java.net/public/CDDL+GPL.html
 * or glassfish/bootstrap/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 * 
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at glassfish/bootstrap/legal/LICENSE.txt.
 * Sun designates this particular file as subject to the "Classpath" exception
 * as provided by Sun in the GPL Version 2 section of the License file that
 * accompanied this code.  If applicable, add the following below the License
 * Header, with the fields enclosed by brackets [] replaced by your own
 * identifying information: "Portions Copyrighted [year]
 * [name of copyright owner]"
 * 
 * Contributor(s):
 * 
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

package javax.faces.webapp.pdl;

import java.beans.BeanInfo;
import javax.faces.application.Resource;
import javax.faces.context.FacesContext;

/**
 * <p class="changed_added_2_0">The contract that a page declaration
 * language must implement to interact with the JSF runtime.
 * PENDING(edburns): more work needs to be done on this, including:</p>
 *
 * 	<ul>

	  <li><p>Make it fit the needs so the runtime can interact with
	  the PDL only thru this contract.  We'll need to add methods to
	  make this happen.  We'll need to provide JSP and Facelets
	  implementations for this.</p></li>

	  <li><p>add a <code>&lt;page-declaration-language&gt;</code>
	  element to faces-config, and add 287-style annotation as well.
	  </p></li>

	</ul>

 * 
 * @since 2.0
 * 
 */
public abstract class PageDeclarationLanguage {

    /**
     * <p class="changed_added_2_0">Return a reference to the component
     * metadata for the composite component represented by the argument
     * <code>componentResource</code>.  The default implementation must
     * support <code>Resource</code> being a Facelet markup file that is
     * to be interpreted as a composite component as specified in
     * section 4.3 of the spec prose document.  The default
     * implementation must support authoring the component metadata
     * using tags placed inside of a <code>&lt;composite:interface
     * /&gt;</code> element, which is specified in the <a target="_"
     * href="../../../../../pdldocs/facelets/index.html">Facelets
     * taglibrary docs</a>.</p>
     *
     * <div class="changed_added_2_0">

     * <p>In the current version of the specification, the <em>composite
     * component metadata</em> is exposed using the JavaBeans API.</p>

     * <p>In the current version of the specification, only composite
     * components have component metadata.  <em>Composite component
     * metadata</em> currently consists of the following
     * information:</p>

     * 	<ul>

	  <li><p>The <em>composite component <em>BeanInfo</em></em>,
	  returned from this method.</p>

          <p>This <code>BeanInfo</code> instance must be exposed in the
          component attributes <code>Map</code> under the key {@link
          javax.faces.component.UIComponent#BEANINFO_KEY}.  The caller
          of this method is responsible for ensuring this entry is
          placed in the component attributes <code>Map</code> for any
          instance of any composite component in the view.</p>

          </li>

	  <li><p>The {@link Resource} from which the composite component
	  was created.</p>

          <p>This <code>Resource</code> instance must be exposed in the
          component attributes <code>Map</code> under the key {@link
          Resource#COMPONENT_RESOURCE_KEY}.  The caller of this method
          is responsible for ensuring this entry is placed in the
          component attributes <code>Map</code> for any instance of any
          composite component in the view.</p>

          </li>


	  <li><p>The <em>composite component
	  <code>BeanDescriptor</code></em></p>

          <p>This <code>BeanDescriptor</code> must be available by
          calling <code>getBeanDescriptor()</code> on the <em>composite
          component <code>BeanInfo</code></em>.</p>

          <p>The <em>composite component
          <code>BeanDescriptor</code></em> exposes the following
          information.</p>

<ul>

	  <li><p>The list of exposed {@link AttachedObjectTarget}s to
	  which the page author can attach things such as listeners,
	  converters, or validators.</p>

          <p>The PDL implementation must populate the <em>composite
          component metadata</em> with a
          <code>List&lt;AttachedObjectTarget&gt;</code> that includes
          all of the inner components exposed by the composite component
          author for use by the page author.</p>

          <p>This <code>List</code>
          must be exposed in the value set of the <em>composite
          component <code>BeanDescriptor</code></em> under the key
          {@link AttachedObjectTarget#ATTACHED_OBJECT_TARGETS_KEY}.</p>

         </li>

	  <li><p>A <code>ValueExpression</code> that evaluates to the
	  component type of the composite component.  By default this is
	  "javax.faces.NamingContainer" but the composite component page
	  author can change this, or provide a script-based
	  <code>UIComponent</code> implementation that is required to
	  implement {@link javax.faces.component.NamingContainer}.</p>

          <p>This <code>ValueExpression</code> must be exposed in the
          value set of the <em>composite component
          <code>BeanDescriptor</code></em> under the key {@link
          javax.faces.component.UIComponent#COMPOSITE_COMPONENT_TYPE_KEY}.</p>

          </li>

	  <li><p>A <code>Map&lt;String, PropertyDescriptor&gt;</code>
	  representing the facets declared by the <em>composite
	  component author</em> for use by the <em>page author</em>.  
	  </p>

          <p>This <code>Map</code> must be exposed in the
          value set of the <em>composite component
          <code>BeanDescriptor</code></em> under the key {@link
          javax.faces.component.UIComponent#FACETS_KEY}.</p>

          </li>

	  <li><p>The "name" "displayName" "preferred" and "expert"
	  attributes of the <code>&lt;composite:interface/ &gt;</code>
	  are exposed using the corresponding methods on the
	  <em>composite component <code>BeanDescriptor</code></em>. Any
	  additional attributes on <code>&lt;composite:interface/
	  &gt;</code> are exposed as attributes accessible from the
	  <code>getValue()</code> and <code>attributeNames()</code>
	  methods on <code>PropertyDescriptor</code>. The return type
	  from <code>getValue()</code> must be a <code>Expression</code>
	  for such attributes.</p></li>



</ul>
          </li>

	  <li><p>Any attributes declared by the <em>composite component
	  author</em> using <code>&lt;composite:attribute/ &gt;</code>
	  elements must be exposed in the array of
	  <code>PropertyDescriptor</code>s returned from
	  <code>getPropertyDescriptors()</code> on the <em>composite
	  component BeanInfo</em>.</p>


          <p>For each such attribute, for any <code>String</code> or
          <code>boolean</code> valued JavaBeans properties on the
          interface <code>PropertyDescriptor</code> (and its
          superinterfaces) that are also given as attributes on a
          <code>&lt;composite:attribute/ &gt;</code> element, those
          properties must be exposed as properties on the
          <code>PropertyDescriptor</code> for that markup element. Any
          additional attributes on <code>&lt;composite:attribute/
          &gt;</code> are exposed as attributes accessible from the
          <code>getValue()</code> and <code>attributeNames()</code>
          methods on <code>PropertyDescriptor</code>. The return type
          from <code>getValue()</code> must be a
          <code>Expression</code>.</p>

          </li>




	</ul>


     * <p>This method is called from {@link
     * javax.faces.application.Application#createComponent(FacesContext,
     * Resource)} as a result of the PDL implementation encountering a
     * composite component within a view.</p>
     *
     *
     * </div>
     * 
     * @param context The <code>FacesContext</code> for this request.
     * @param componentResource The <code>Resource</code> that represents the component.
     * @since 2.0
     */
    public abstract BeanInfo getComponentMetadata(FacesContext context, Resource componentResource);


    /**
     * <p class="changed_added_2_0">Take implementation specific action
     * to discover a <code>Resource</code> given the argument
     * <code>componentResource</code>.  The returned
     * <code>Resource</code> if non-<code>null</code>, must point to a
     * script file that can be turned into something that extends {@link
     * javax.faces.component.UIComponent} and implements {@link
     * javax.faces.component.NamingContainer}.</p>
     *
     * <p class="changed_added_2_0">This method is called from {@link javax.faces.application.Application#createComponent(FacesContext, Resource)}.</p>
     *
     * @param context The <code>FacesContext</code> for this request.
     * @param componentResource The <code>Resource</code> that represents the component.
     * @since 2.0
     */
    public abstract Resource getScriptComponentResource(FacesContext context,
            Resource componentResource);
}
