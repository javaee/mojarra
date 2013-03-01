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

package javax.faces.bean;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;

/**
 * <p class="changed_added_2_0"><span
 * class="changed_modified_2_2">When</span> this annotation, along with
 * {@code ManagedBean} is found on a class, the runtime must act as if a
 * <code>&lt;managed-bean-scope&gt;view&lt;managed-bean-scope&gt;</code>
 * element was declared for the corresponding managed bean.</p>

 * <div class="changed_added_2_2">

 * <p>If <code>ProjectStage</code> is not
 * <code>ProjectStage.Production</code>, verify that the current {@code
 * UIViewRoot} does not have its {@code transient}
 * property set to {@code true}.  If so, add a <code>FacesMessage</code>
 * for the current {@code viewId} to the <code>FacesContext</code>
 * stating {@code @ViewScoped} beans cannot work if the view is marked
 * as transient.  Also log a <code>Level.WARNING</code> message to the 
 * log.  If <code>ProjectStage</code> <strong>is</strong>
 * <code>ProjectStage.Production</code>, do not do this
 * verification.</p>

 * <p>The bean must be stored in the map returned from 
 * {@code javax.faces.component.UIViewRoot.getViewMap(boolean)}.</p>

 * <p>The runtime must ensure that any methods on the bean annotated
 * with {@code PostConstruct} or {@code PreDestroy} are called when the
 * scope begins and ends, respectively.  Two circumstances can cause the
 * scope to end.</p>

 * <ul>

 * <li><p>{@code FacesContext.setViewRoot()} is called with the new
 * {@code UIViewRoot} being different than the current one.</p></li>

 * <li><p>The session, that happened to be active when the bean was
 * created, expires.  If no session existed when the bean was created,
 * then this circumstance does not apply.</p></li>

 * </ul>

 * <p>In the session expiration case, the runtime must ensure that
 * {@code FacesContext.getCurrentInstance()} returns a valid instance if
 * it is called during the processing of the {@code @PreDestroy}
 * annotated method.  The set of methods on {@code FacesContext} that
 * are valid to call in this circumstance is identical to those
 * documented as "valid to call this method during application startup
 * or shutdown". On the {@code ExternalContext} returned from that
 * {@code FacesContext}, all of the methods documented as "valid to call
 * this method during application startup or shutdown" are valid to
 * call.  In addition, the method {@code
 * ExternalContext.getSessionMap()} is also valid to call.</p>


 * <p>The annotations in this package may be
 * deprecated in a future version of this specification because they
 * duplicate functionality provided by other specifications included in
 * JavaEE.  When possible, the corresponding annotations from the
 * appropriate Java EE specification should be used in preference to
 * these annotations.  In this case, the corresponding annotation is
 * {@code javax.faces.view.ViewScoped}.  The functionality of this
 * corresponding annotation is identical to this one, but it is
 * implemented as a CDI custom scope.</p>

 * </div>

 *
 * @since 2.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
public @interface ViewScoped {
}
