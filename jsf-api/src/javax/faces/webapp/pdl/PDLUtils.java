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

import java.beans.BeanDescriptor;
import java.beans.BeanInfo;
import java.util.List;
import javax.faces.component.ActionSource2;
import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.component.ValueHolder;
import javax.faces.context.FacesContext;

/**
 * <p class="changed_added_2_0">It is very unlikely this class will be
 * in the final public API.  This class contains methods that handle
 * responsibilities that would be done in a specific PDL implementation,
 * but are common to all PDL possible PDL implementations and therefore
 * should be offered in a resuable manner.</p>
 * 
 * @since 2.0
 */
public class PDLUtils {

    /**
     * <p class="changed_added_2_0">Leverage the component metadata
     * specified in section 4.3.2 for the purpose of re-targeting
     * attached objects from the top level composite component to the
     * individual {@link AttachedObjectTarget} inside the composite
     * component.  This method must be called by the PDL implementation
     * when creating the <code>UIComponent</code> tree when a composite
     * component usage is encountered.</p>
     *
     * <div class="changed_added_2_0">

     * <p>An algorithm semantically equivalent to the following must be
     * implemented.</p>

	<ul>

	  <li><p>Obtain the metadata for the composite component.
	  Currently this entails getting the value of the {@link
	  UIComponent#BEANINFO_KEY} component attribute, which will be
	  an instance of <code>BeanInfo</code>.  If the metadata cannot
	  be found, log an error message and return.</p></li>

	  <li><p>Get the <code>BeanDescriptor</code> from the
	  <code>BeanInfo</code>.</p></li>

	  <li><p>Get the value of the {@link
	  AttachedObjectTarget#ATTACHED_OBJECT_TARGETS_KEY} from the
	  <code>BeanDescriptor</code>'s <code>getValue()</code> method.
	  This will be a <code>List&lt;{@link
	  AttachedObjectTarget}&gt;</code>.  Let this be
	  <em>targetList</em>.</p></li>

	  <li><p>For each <em>curHandler</em> entry in the argument
	  <code>handlers</code></p>

	<ul>

	  <li><p>Let <em>forAttributeValue</em> be the return from
	  {@link AttachedObjectHandler#getFor}.  </p></li>

	  <li><p>For each <em>curTarget</em> entry in
	  <em>targetList</em>, the first of the following items that
	  causes a match will take this action:</p>

<p style="margin-left: 3em;">For each <code>UIComponent</code> in the
list returned from <em>curTarget.getTargets()</em>, call
<em>curHandler.<a
href="AttachedObjectHandler.html#applyAttachedObject">applyAttachedObject()</></em>,
passing the <code>FacesContext</code> and the
<code>UIComponent</code>.</p>

          <p>and cause this inner loop to terminate.</p>

	<ul>

	  <li><p>If <em>curHandler</em> is an instance of {@link
	  ActionSource2AttachedObjectHandler} and <em>curTarget</em> is
	  an instance of {@link ActionSource2AttachedObjectTarget},
	  consider it a match.</p></li>

	  <li><p>If <em>curHandler</em> is an instance of {@link
	  EditableValueHolderAttachedObjectHandler} and <em>curTarget</em> is
	  an instance of {@link EditableValueHolderAttachedObjectTarget},
	  consider it a match.</p></li>

	  <li><p>If <em>curHandler</em> is an instance of {@link
	  ValueHolderAttachedObjectHandler} and <em>curTarget</em> is
	  an instance of {@link ValueHolderAttachedObjectTarget},
	  consider it a match.</p></li>

	</ul>



</li>


	</ul>




</li>




	</ul>
    

     *
     * </div>
     *
     * @param context

     * @param topLevelComponent The UIComponent in the view to which the
     * attached objects must be attached.  This UIComponent must have
     * its component metadata already associated and available from via
     * the JavaBeans API.

     * @param handlers specified by the page author in the consuming
     * page, provided to this method by the PDL implementation, this is
     * a list of implementations of {@link AttachedObjectHandler}, each
     * one of which represents a relationship between an attached object
     * and the UIComponent to which it is attached.
     */
    public static void retargetAttachedObjects(FacesContext context,
            UIComponent topLevelComponent,
            List<AttachedObjectHandler> handlers) {
        
        BeanInfo componentBeanInfo = (BeanInfo) 
                topLevelComponent.getAttributes().get(UIComponent.BEANINFO_KEY);
        // PENDING(edburns): log error message if componentBeanInfo is null;
        if (null == componentBeanInfo) {
            return;
        }
        BeanDescriptor componentDescriptor = componentBeanInfo.getBeanDescriptor();
        // There is an entry in targetList for each attached object in the 
        // <composite:interface> section of the composite component.
        List<AttachedObjectTarget> targetList = (List<AttachedObjectTarget>)
                componentDescriptor.getValue(AttachedObjectTarget.ATTACHED_OBJECT_TARGETS_KEY);
        // Each entry in targetList will vend one or more UIComponent instances
        // that is to serve as the target of an attached object in the consuming
        // page.
        List<UIComponent> targetComponents = null;
        String forAttributeValue, curTargetName, handlerTagId, componentTagId;
        boolean foundMatch = false;
        
        // For each of the attached object handlers...
        for (AttachedObjectHandler curHandler : handlers) {
            // Get the name given to this attached object by the page author
            // in the consuming page.
            forAttributeValue = curHandler.getFor();
            // For each of the attached objects in the <composite:interface> section
            // of this composite component...
            foundMatch = false;
            for (AttachedObjectTarget curTarget : targetList) {
                if (foundMatch) {
                    break;
                }
                // Get the name given to this attached object target by the
                // composite component author
                curTargetName = curTarget.getName();
                targetComponents = curTarget.getTargets();

                if (curHandler instanceof ActionSource2AttachedObjectHandler &&
                    curTarget instanceof ActionSource2AttachedObjectTarget) {
                    if (forAttributeValue.equals(curTargetName)) {
                        for (UIComponent curTargetComponent : targetComponents) {
                            curHandler.applyAttachedObject(context, curTargetComponent);
                            foundMatch = true;
                        }
                    }
                }
                else if (curHandler instanceof EditableValueHolderAttachedObjectHandler &&
                         curTarget instanceof EditableValueHolderAttachedObjectTarget) {
                    if (forAttributeValue.equals(curTargetName)) {
                        for (UIComponent curTargetComponent : targetComponents) {
                            curHandler.applyAttachedObject(context, curTargetComponent);
                            foundMatch = true;
                        }
                    }
                }
                else if (curHandler instanceof ValueHolderAttachedObjectHandler &&
                         curTarget instanceof ValueHolderAttachedObjectTarget) {
                    if (forAttributeValue.equals(curTargetName)) {
                        for (UIComponent curTargetComponent : targetComponents) {
                            curHandler.applyAttachedObject(context, curTargetComponent);
                            foundMatch = true;
                        }
                    }
                }
            }
        }
    }

}
