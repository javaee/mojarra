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

package javax.faces.view;

import java.util.List;
import javax.faces.component.UIComponent;

/**
 * <p class="changed_added_2_0">Within the declaration of a
 * <em>composite component</em>, an <code>AttachedObjectTarget</code>
 * allows the <em>composite component author</em> to expose the
 * semantics of an inner component to the <em>page author</em> without
 * exposing the rendering or implementation details of the <em>inner
 * component</em>.  See {@link
 * ViewDeclarationLanguage#getComponentMetadata} for the context in
 * which implementations of this interface are used.</p>
 * 
 * <p class="changed_added_2_0">The implementation must ensure that 
 * this instance is thread safe and may be shared among different component 
 * trees.</p>

 * <div class="changed_added_2_0">

 * <p>Subinterfaces are provided for the common behavioral interfaces:
 * {@link javax.faces.component.behavior.Behavior}, {@link
 * javax.faces.component.ValueHolder}, {@link
 * javax.faces.component.EditableValueHolder} and {@link
 * javax.faces.component.ActionSource2}.  The default VDL implementation
 * must provide a corresponding Facelets tag handler for each of the
 * subinterfaces of this interface.  </p>

 * </div>
 *
 * @since 2.0
 */
public interface AttachedObjectTarget {

    /**
     * <p class="changed_added_2_0">The key in the value set of the
     * <em>composite component <code>BeanDescriptor</code></em>, the
     * value for which is a
     * <code>List&lt;AttachedObjectTarget&gt;</code>.</p>
     */
    public static final String ATTACHED_OBJECT_TARGETS_KEY =
            "javax.faces.view.AttachedObjectTargets";


    /**
     * <p class="changed_added_2_0">Returns the
     * <code>List&lt;UIComponent&gt;</code> that this
     * <code>AttachedObjectTarget</code> exposes.  Each <em>attached
     * object</em> exposed by the <em>composite component author</em>
     * may point at multiple <code>UIComponent</code> instances within
     * the composite component.  This method is used by the {@link
     * javax.faces.view.ViewDeclarationLanguage#retargetAttachedObjects}
     * method to take the appropriate action on the attached object.</p>
     *
     */
    public List<UIComponent> getTargets(UIComponent topLevelComponent);


    /**
     * <p class="changed_added_2_0">Returns the name by which this
     * attached object target is exposed to the <em>page
     * author</em>.</p>
     * 
     */
    public String getName();

}
