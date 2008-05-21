/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package javax.faces.component;

import javax.faces.application.Resource;

/**
 * <div class="changed_added_2_0">
 *
 * <p>A composite component is a {@link UIComponent} instance that
 * implements this interface and whose children are defined in a PDL
 * file located within a resource library.  See {@link
 * javax.faces.application.ResourceHandler} for details about resource
 * libraries.  This marker interface is necessary for two reasons:</p>
 *
 * 	<ol>

	  <li><p>To enable "re-targeting" attached objects from the page
	  containing a usage of the composite component to a specific
	  child component of the composite component.</p>

          <p>This marker interface enables the PDL tag handler
          implementations for tags that represent attached objects (for
          example, <code>&lt;f:actionListener /&gt;</code>,
          <code>&lt;f:valueChangeListener /&gt;</code>,
          <code>&lt;f:validator /&gt;</code> (and any custom tag
          representing a {@link javax.faces.validator.Validator}),
          <code>&lt;f:converter /&gt;</code> (and any custom tag
          representing a {@link javax.faces.convert.Converter}) to tell
          if it is valid to attach the object</p></li>

	  <li><p>To enable the top level PDL file of the composite
	  component to support the
	  <code>#{resource['this:gradlogtop.jpg']}</code>
	  syntax. </p></li>

	</ol>

 *
 * </div>
 */
public interface CompositeComponent {

    public Resource getResource();
    public void setResource(Resource toSet);

    
}
