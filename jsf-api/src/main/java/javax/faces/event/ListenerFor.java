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

package javax.faces.event;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.Inherited;

/**
 * <p class="changed_added_2_0">Classes tagged with this annotation are
 * installed as listeners using the method {@link
 * javax.faces.application.Application#subscribeToEvent} or {@link
 * javax.faces.component.UIComponent#subscribeToEvent} (depending on the
 * circumstances, described below).</p>
 *
 * <div class="changed_added_2_0">
 *
 * <p>The default implementation must support attaching this annotation
 * to {@link javax.faces.component.UIComponent} or {@link
 * javax.faces.render.Renderer} classes.  In both cases, the annotation
 * processing described herein must commence during the implementation
 * of any variant of {@link
 * javax.faces.application.Application}<code>.createComponent()</code>
 * and must complete before the <code>UIComponent</code> instance is
 * returned from <code>createComponent()</code>. The annotation
 * processing must proceed according to an algorithm semantically
 * equivalent to the following.</p>

 * <ul>

          <li><p> If this annotation is not present on the class in
          question, no action must be taken.  </p></li>

          <p>Determine the "target" on which to call
          <code>subscribeToEvent</code>.</p>

          <p>If the class to which this annotation is attached
          implements {@link ComponentSystemEventListener} and is a
          <code>UIComponent</code> instance, "target" is the
          <code>UIComponent</code> instance.</p>

          <p>If the class to which this annotation is attached
          implements {@link ComponentSystemEventListener} and is a
          <code>Renderer</code> instance, "target" is the
          <code>UIComponent</code> instance that is to be rendered by
          this <code>Renderer</code> instance.</p>

          <p>If the class to which this annotation is attached
          implements {@link ComponentSystemEventListener} and is neither
          an instance of <code>Renderer</code> nor
          <code>UIComponent</code>, the action taken is
          unspecified. This case must not trigger any kind of error.</p>

	  <p>If the class to which this annotation is attached
	  implements <code>SystemEventListener</code> and does not
	  implement <code>ComponentSystemEventListener</code>, "target"
	  is the {@link javax.faces.application.Application}
	  instance.</p>

          </li>

	  <li>

          <p>Determine the variant of <code>subscribeToEvent()</code>to
          call and the parameters to pass to it.</p>

          <p>If "target" is a <code>UIComponent</code> call {@link
          javax.faces.component.UIComponent#subscribeToEvent(Class,
          ComponentSystemEventListener)}, passing the {@link
          #systemEventClass} of the annotation as the first argument and
          the instance of the class to which this annotation is attached
          (which must implement
          <code>ComponentSystemEventListener</code>) as the second
          argument.</p>

          <p>If "target" is the {@link
          javax.faces.application.Application} instance, inspect the
          value of the {@link #sourceClass} annotation attribute value.</p>

          <p>If the value is <code>Void.class</code>, call {@link
          javax.faces.application.Application#subscribeToEvent(Class,
          SystemEventListener)}, passing the value of {@link
          #systemEventClass} as the first argument and the instance of the
          class to which this annotation is attached (which must
          implement <code>SystemEventListener) as the second
          argument.</code></p>

          <p>Otherwise, call {@link
          javax.faces.application.Application#subscribeToEvent(Class,
          Class, SystemEventListener)}, passing the value of {@link
          #systemEventClass} as the first argument, the value of {@link
          #sourceClass} as the second argument, and the instance of the
          class to which this annotation is attached (which must
          implement <code>SystemEventListener) as the third
          argument.</code></p>

          </li>

 * </ul>

 * <p>Example: The standard renderer for
 * <code>javax.faces.resource.Stylesheet</code> must have the following
 * annotation declaration:</p>

 * <pre><code>@ListenerFor(systemEventClass=PostAddToViewEvent.class)</code></pre>

 * <p>This will cause the renderer to be added as a listener for the
 * {@link PostAddToViewEvent} to all components that list it as their
 * renderer.</p>

 * </div>
 *
 * @since 2.0
 */

@Retention(value=RetentionPolicy.RUNTIME)
@Target(value=ElementType.TYPE)
@Inherited
public @interface ListenerFor {

    /**
     * <p class="changed_added_2_0">The kind of system event for which
     * this class will be installed as a listener.  The implementation
     * only supports exact matches on the <code>Class</code> and must
     * not honor subclass relationships.  It is valid to have EL
     * Expressions in the value of this attribute, as long as the
     * expression resolves to an instance of the expected type.</p>
     */

    public Class<? extends SystemEvent> systemEventClass();


    /**
     * <p class="changed_added_2_0">The kind of object that emits events
     * of the type given by the value of the {@link #systemEventClass}
     * attribute.  It is valid to have EL Expressions in the value of
     * this attribute, as long as the expression resolves to an instance
     * of the expected type.</p>
     */ 
    public Class sourceClass() default Void.class;

}
