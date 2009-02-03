/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package javax.faces.application;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.Inherited;

/**
 * <p class="changed_added_2_0">Container annotation to specify multiple
 * {@link ResourceDependency} annotations on a single class.  Example:</p>

<pre><code>
&#0064;ResourceDependencies( {
  &#0064;ResourceDependency(library="corporate", name="css_master.css"),
  &#0064;ResourceDependency(library="client01", name="layout.css"),
  &#0064;ResourceDependency(library="corporate", name="typography.css"),
  &#0064;ResourceDependency(library="client01", name="colorAndMedia.css"),
  &#0064;ResourceDependency(library="corporate", name="table2.css"),
  &#0064;ResourceDependency(library="fancy", name="commontaskssection.css"),
  &#0064;ResourceDependency(library="fancy", name="progressBar.css"),
  &#0064;ResourceDependency(library="fancy", name="css_ns6up.css")
                       })
</code></pre>


 * <div class="changed_added_2_0">

 * <p>The action described in {@link ResourceDependency} must be taken for each
 * <code>&#0064;ResourceDependency</code> present in the container
 * annotation. </p>

 * </div>
 */
@Retention(value=RetentionPolicy.RUNTIME)
@Target(value=ElementType.TYPE)
@Inherited
public @interface ResourceDependencies {
    
    ResourceDependency[] value();
}
