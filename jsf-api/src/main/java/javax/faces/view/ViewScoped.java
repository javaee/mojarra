/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2012 Oracle and/or its affiliates. All rights reserved.
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

package javax.faces.view;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import javax.enterprise.context.NormalScope;

/**
 * <p class="changed_added_2_2">When this annotation, along with {@code
 * javax.inject.Named} is found on a class, the runtime must place the
 * bean in a CDI scope such that it remains active as long as {@link
 * javax.faces.application.NavigationHandler#handleNavigation} does not
 * cause a navigation to a view with a viewId that is different than the
 * viewId of the current view. Any injections and notifications required
 * by CDI and the Java EE platform must occur as usual at the expected
 * time.</p>
 * 
 * <div class="changed_added_2_2">

 * <p>If <code>ProjectStage</code> is not
 * <code>ProjectStage.Production</code>, verify that the current {@link
 * javax.faces.component.UIViewRoot} does not have its {@code transient}
 * property set to {@code true}.  If so, add a <code>FacesMessage</code>
 * for the current {@code viewId} to the <code>FacesContext</code>
 * stating {@code @ViewScoped} beans cannot work if the view is marked
 * as transient.    Also log a <code>Level.WARNING</code> message to the 
 * log.  If <code>ProjectStage</code> <strong>is</strong>
 * <code>ProjectStage.Production</code>, do not do this
 * verification.</p>

 * <p>The bean must be stored in the map returned from 
 * {@link javax.faces.component.UIViewRoot#getViewMap(boolean)}.</p>

 * <p>Use of this annotation requires that any beans stored in view scope
 * must be serializable and proxyable as defined in the CDI specification.
 * </p>

 * <p>The runtime must ensure that any methods on the bean annotated
 * with {@code PostConstruct} or {@code PreDestroy} are called when the
 * scope begins and ends, respectively.  Two circumstances can cause the
 * scope to end.</p>

 * <ul>

 * <li><p>{@link javax.faces.context.FacesContext#setViewRoot} is called
 * with the new {@code UIViewRoot} being different than the current
 * one.</p></li>

 * <li><p>The session, that happened to be active when the bean was
 * created, expires.  If no session existed when the bean was created,
 * then this circumstance does not apply.</p></li>

 * </ul>

 * <p>In the session expiration case, the runtime must ensure that
 * {@link javax.faces.context.FacesContext#getCurrentInstance} returns a
 * valid instance if it is called during the processing of the
 * {@code @PreDestroy} annotated method.  The set of methods on {@code
 * FacesContext} that are valid to call in this circumstance is
 * identical to those documented as "valid to call this method during
 * application startup or shutdown". On the {@link
 * javax.faces.context.ExternalContext} returned from that {@code
 * FacesContext}, all of the methods documented as "valid to call this
 * method during application startup or shutdown" are valid to call.  In
 * addition, the method {@link
 * javax.faces.context.ExternalContext#getSessionMap} is also valid to
 * call.</p>

 * 
 * 
 * </div>

 * @since 2.2
 */
@NormalScope
@Inherited
@Documented
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD})
@Retention(value = RetentionPolicy.RUNTIME)
public @interface ViewScoped {
}
