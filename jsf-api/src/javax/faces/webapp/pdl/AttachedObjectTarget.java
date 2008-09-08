/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 1997-2008 Sun Microsystems, Inc. All rights reserved.
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

import java.util.List;
import javax.faces.component.UIComponent;

/**
 * <p class="changed_modified_2_0">Within the declaration of a
 * <em>composite component</em>, an <code>AttachedObjectTarget</code>
 * allows the <em>composite component author</em> to expose the
 * semantics of an inner component to the <em>page author</em> without
 * exposing the rendering or implementation details of the <em>inner
 * component</em>.  The PDL implementation must populate the
 * <em>composite component metadata</em> with a
 * <code>List&lt;AttachedObjectTarget&gt;</code> that includes all of
 * the inner components exposed by the composite component author for
 * use by the page author.  This <code>List</code> must be exposed in
 * the value set of the <em>composite component
 * <code>BeanDescriptor</code></em> under the key {@link
 * #ATTACHED_OBJECT_TARGETS_KEY}.</p>

 * <div class="changed_added_2_0">
 *
 * <p>The {@link PDLUtils#retargetAttachedObjects} method is passed a
 * <code>List&lt;{@link AttachedObjectHandler&gt;</code> representing
 * the attached objects the page author wishes to attach to the
 * composite component.  <code>retargetAttachedObjects</code> gets the
 * <code>List&lt;AttachedObjectTarget&gt;</code> representing the inner
 * components exposed by the composite component author from the
 * <em>composite component metadata</em> and matches up the attached
 * objects provided by the page author with the attached object targets
 * provided by the composite component author.</p>

 * <p>Subinterfaces are provided for the common behavioral interfaces:
 * {@link javax.faces.component.ValueHolder}, {@link
 * javax.faces.component.EditableValueHolder} and {@link
 * javax.faces.component.ActionSource2}.  The default PDL implementation
 * must provide a corresponding Facelets tag handler for each of the
 * subinterfaces of this interface.  </p>

 * </div>
 *
 * @since 2.0
 */
public interface AttachedObjectTarget {

    /**
     * RELEASE_PENDING (edburns,rogerk) docs
     */
    public static final String ATTACHED_OBJECT_TARGETS_KEY =
            "javax.faces.webapp.pdl.AttachedObjectTargets";


    /**
     * RELEASE_PENDING (edburns,rogerk) docs
     * @return
     */
    public List<UIComponent> getTargets();


    /**
     * RELEASE_PENDING (edburns,rogerk) docs
     * @return
     */
    public String getName();
           

}
