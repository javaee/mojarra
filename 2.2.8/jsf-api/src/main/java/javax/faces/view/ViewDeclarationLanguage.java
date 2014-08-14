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

import java.beans.BeanInfo;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import javax.faces.application.Resource;
import javax.faces.application.ResourceHandler;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;

/**
 * <p class="changed_added_2_0"><span class="changed_modified_2_0_rev_a
 * changed_modified_2_1 changed_modified_2_2">The</span> contract that a view declaration
 * language must implement to interact with the JSF runtime.  An
 * implementation of this class must be thread-safe.</p>
 *
 * <div class="changed_added_2_0">
 * 
 * <p>Instances of this class are application scoped and must be
 * obtained from the {@link ViewDeclarationLanguageFactory}.</p>

 * </div>
 * 
 * @since 2.0
 * 
 */
public abstract class ViewDeclarationLanguage {

    /**
     * <p class="changed_added_2_0">Identifier for the JSP view declaration 
     * language.</p>
     *
     * @since 2.1
     */
    public final static String JSP_VIEW_DECLARATION_LANGUAGE_ID =
        "java.faces.JSP";

    /**
     * <p class="changed_added_2_0">Identifier for the Facelets view 
     * declaration language.</p>
     *
     * @since 2.1
     */
    public final static String FACELETS_VIEW_DECLARATION_LANGUAGE_ID =
        "java.faces.Facelets";

    /**
     * <p class="changed_added_2_0">Return a reference to the component
     * metadata for the composite component represented by the argument
     * <code>componentResource</code>, or <code>null</code> if the
     * metadata cannot be found.  See section JSF.7.7.2 for the
     * specification of the default implementation. JSP implementations
     * must throw <code>UnsupportedOperationException</code>.</p>
     *
     * @param context The <code>FacesContext</code> for this request.
     * @param componentResource The <code>Resource</code> that represents the component.
     * @since 2.0
     *
     * @throws NullPointerException if any of the arguments are
     * <code>null</code>.
     *
     * @throws javax.faces.FacesException if there is an error in
     * obtaining the metadata
     *
     * @throws UnsupportedOperationException if this is a JSP VDL
     * implementation.
     */
    public abstract BeanInfo getComponentMetadata(FacesContext context, Resource componentResource);


    /**
     * <p class="changed_added_2_0">Return a reference to the view
     * metadata for the view represented by the argument
     * <code>viewId</code>, or <code>null</code> if the metadata cannot
     * be found.  See section JSF.7.7.2 for the specification of the
     * default implementation.  Facelets for JSF 2 implementation must
     * return non-<code>null</code>. JSP implementations must return
     * <code>null</code>.</p>
     *
     * @param context The <code>FacesContext</code> for this request.
     * @param viewId the view id from whith to extract the metadata
     * @since 2.0
     *
     * @throws NullPointerException if any of the arguments are
     * <code>null</code>.
     *
     * @throws javax.faces.FacesException if there is an error in
     * obtaining the metadata
     */
    public abstract ViewMetadata getViewMetadata(FacesContext context, String viewId);


    /**
     * <p class="changed_added_2_0">Take implementation specific action
     * to discover a <code>Resource</code> given the argument
     * <code>componentResource</code>.  See section JSF.7.7.2 for the
     * specification of the default implementation.  JSP implementations
     * must throw <code>UnsupportedOperationException</code>.</p>
     *
     * @param context The <code>FacesContext</code> for this request.
     * @param componentResource The <code>Resource</code> that represents the component.
     * @since 2.0
     *
     * @throws NullPointerException if any of the arguments are
     * <code>null</code>.
     *
     * @throws javax.faces.FacesException if there is an error in
     * obtaining the script component resource
     * @throws UnsupportedOperationException if this is a JSP VDL
     * implementation.
     */
    public abstract Resource getScriptComponentResource(FacesContext context,
                                                        Resource componentResource);
    
    
    /**
     * <p class="changed_added_2_0"><span class="changed_modified_2_2">Create</span>
     * a <code>UIViewRoot</code> from the VDL contained in the artifact referenced by the argument
     * <code>viewId</code>.  <span class="changed_modified_2_2">See section JSF.7.7.2 for the specification of
     * the default implementation.</span></p>
     *
     * @param context the <code>FacesContext</code> for this request.
     * @param viewId the identifier of an artifact that contains the VDL
     * syntax that describes this view.
     *
     * @throws NullPointerException if any of the arguments are
     * <code>null</code>

     * @since 2.0
     */

    public abstract UIViewRoot createView(FacesContext context,
                                 String viewId);
    
    /**
     * <p class="changed_added_2_2">Create a component given a 
     * {@link ViewDeclarationLanguage} specific
     * tag library URI and tag name.  The runtime must support this method operating
     * for the Facelets VDL.
     * Other kinds of {@code ViewDeclarationLanguage} may be supported but are not
     * required to be supported. For backward compatibility
     * with decorated {@code ViewDeclrationLanguage} implementations that do
     * not override this method, a default implementation is provided that returns
     * {@code null}.  However, any implementation that is compliant with the
     * version of the specification in which this method was introduced must
     * implement this method.
     * </p>
     * 
     * @param context the {@link FacesContext} for this request
     * @param taglibURI the fully qualified tag library URI that contains the component
     * @param tagName the name of the tag within that library that exposes the component
     * @param attributes any name=value pairs that would otherwise have been 
     * given on the markup that would cause the creation of this component or
     * {@code null} if no attributes need be given.  
     * 
     * @throws NullPointerException if {@code context}, {@code taglibURI}, or 
     * {@code tagName} are {@code null}
     * 
     * @since 2.2
     */
    
    public UIComponent createComponent(FacesContext context, 
            String taglibURI, String tagName, 
            Map<String, Object> attributes) {
        return null;
    }
    
    
    /**
     * <p class="changed_added_2_0">Restore a <code>UIViewRoot</code>
     * from a previously created view.  See section JSF.7.7.2 for the
     * specification of the default implementation.</p>
     *
     * @param context the <code>FacesContext</code> for this request.
     * @param viewId the identifier for a previously rendered view.
     *
     * @throws NullPointerException if any of the arguments are
     * <code>null</code>
     */
    public abstract UIViewRoot restoreView(FacesContext context, String viewId);


    /**
     * <p class="changed_added_2_0"><span
     * class="changed_modified_2_0_rev_a">Assuming</span> the component
     * metadata for argument <code>topLevelComponent</code> has been
     * made available by an earlier call to {@link
     * ViewDeclarationLanguage#getComponentMetadata}, leverage the
     * component metadata for the purpose of re-targeting attached
     * objects from the top level composite component to the individual
     * {@link AttachedObjectTarget} instances inside the composite
     * component.  This method must be called by the {@link
     * ViewDeclarationLanguage} implementation when creating the
     * <code>UIComponent</code> tree when a composite component usage is
     * encountered.</p>
     *
     * <div class="changed_added_2_0">
     *
     * <p>An algorithm semantically equivalent to the following must be
     * implemented.</p>
     *
     *<ul>
     *
     *<li><p>Obtain the metadata for the composite component.
     *Currently this entails getting the value of the {@link
     *UIComponent#BEANINFO_KEY} component attribute, which will be
     *an instance of <code>BeanInfo</code>.  If the metadata cannot
     *be found, log an error message and return.</p></li>
     *
     *<li><p>Get the <code>BeanDescriptor</code> from the
     *<code>BeanInfo</code>.</p></li>
     *
     *<li><p>Get the value of the {@link
     *AttachedObjectTarget#ATTACHED_OBJECT_TARGETS_KEY} from the
     *<code>BeanDescriptor</code>'s <code>getValue()</code> method.
     *This will be a <code>List&lt;{@link
     *AttachedObjectTarget}&gt;</code>.  Let this be
     *<em>targetList</em>.</p></li>
     *
     *<li><p>For each <em>curHandler</em> entry in the argument
     *<code>handlers</code></p>
     *
     *<ul>
     *
     *<li><p>Let <em>forAttributeValue</em> be the return from
     *{@link AttachedObjectHandler#getFor}.  </p></li>
     *
     *<li><p>For each <em>curTarget</em> entry in
     *<em>targetList</em>, the first of the following items that
     *causes a match will take this action:</p>
     *
     *<p style="margin-left: 3em;">For each <code>UIComponent</code> in the
     *list returned from <em>curTarget.getTargets()</em>, call
     *<em>curHandler.<a
     *href="AttachedObjectHandler.html#applyAttachedObject">applyAttachedObject()</a></em>,
     *passing the <code>FacesContext</code> and the
     *<code>UIComponent</code>.</p>
     *
     *<p>and cause this inner loop to terminate.</p>
     *
     *<ul>
     *
     *<li><p>If <em>curHandler</em> is an instance of {@link
     *ActionSource2AttachedObjectHandler} and <em>curTarget</em> is an
     *instance of {@link ActionSource2AttachedObjectTarget}, and
     *<em>curTarget.getName()</em> is equal to <em>curTargetName</em>,
     *consider it a match.</p></li>
     *
     *<li><p>If <em>curHandler</em> is an instance of {@link
     *EditableValueHolderAttachedObjectHandler} and <em>curTarget</em>
     *is an instance of {@link EditableValueHolderAttachedObjectTarget},
     *<span class="changed_modified_2_0_rev_a">and
     *<em>curTarget.getName()</em> is equal to <em>curTargetName</em>,
     *consider it a match.</span></p></li>
     *
     *<li class="changed_modified_2_0_rev_a"><p>If <em>curHandler</em>
     *is an instance of {@link ValueHolderAttachedObjectHandler} and
     *<em>curTarget</em> is an instance of {@link
     *ValueHolderAttachedObjectTarget}, and <em>curTarget.getName()</em>
     *is equal to <em>curTargetName</em>, consider it a match.</p></li>

     *<li><p>If <em>curHandler</em> is an instance of {@link
     *BehaviorHolderAttachedObjectHandler} and <em>curTarget</em> is an
     *instance of {@link BehaviorHolderAttachedObjectTarget}, and either
     *of the following conditions are true,</p>

     * <ul>
     *
     * <li><em>curHandler.getEventName()</em> is not <code>null</code>
     * and is equal to <em>curTargetName</em>.</li>
     *
     * <li><em>curHandler.getEventName()</em> is <code>null</code> and
     * <em>curTarget.isDefaultEvent()</em> is <code>true</code>.</li>
     *
     * </ul>

     *<p>consider it a match.</p></li>

     *</ul>
     *</li>
     *</ul>
     *</li>
     *</ul>

     * <p class="changed_modified_2_0_rev_a">The implementation must
     * support retargeting attached objects from the top level compsite
     * component to targets that are composite and non-composite
     * components.</p>
     *
     * <p>An implementation is provided that will throw
     * <code>UnsupportedOperationException</code>.  A Faces implementation
     * compliant with version 2.0 and beyond of the specification must 
     * override this method.</p>
     *
     * </div>
     *
     * @param context the FacesContext for this request.
     *
     * @param topLevelComponent The UIComponent in the view to which the
     * attached objects must be attached.  This UIComponent must have
     * its component metadata already associated and available from via
     * the JavaBeans API.
     *
     * @throws NullPointerException if any of the arguments are
     * <code>null</code>.

     * @since 2.0
     *
     */
    public void retargetAttachedObjects(FacesContext context,
                                        UIComponent topLevelComponent,
                                        List<AttachedObjectHandler> handlers)  {
        
        // no-op

    }


    /**
     * <p class="changed_added_2_0">Assuming the component metadata for
     * argument <code>topLevelComponent</code> has been made available
     * by an earlier call to {@link
     * ViewDeclarationLanguage#getComponentMetadata}, leverage the
     * component metadata for the purpose of re-targeting any method
     * expressions from the top level component to the appropriate inner
     * component.  For each attribute that is a
     * <code>MethodExpression</code> (as indicated by the presence of a
     * "<code>method-signature</code>" attribute and the absence of a
     * "<code>type</code>" attribute), the following action must be
     * taken:</p>

     * <div class="changed_added_2_0">
     *
     * <ul>
     *
     * <li><p>Get the value of the <em>targets</em> attribute.  If the
     * value is a <code>ValueExpression</code> evaluate it.  If there is
     * no <em>targets</em> attribute, let the name of the metadata
     * element be the evaluated value of the <em>targets
     * attribute.</em></p></li>
     * 
     * <li><p>Interpret <em>targets</em> as a space (not tab) separated
     * list of ids. For each entry in the list:</p>
     * 
     * <ul>
     *
     * <li><p>Find the inner component of the
     * <em>topLevelComponent</em> with the id equal to
     * the current list entry.  For discussion, this component is called
     * <em>target</em>.  If not found, log and error and continue to
     * the next attribute.</p></li>
     *
     * <li><p>For discussion the declared name of the attribute is
     * called <em>name</em>.</p></li>
     *
     * <li><p>In the attributes map of the
     * <em>topLevelComponent</em>, look up the entry under the key
     * <em>name</em>.  Assume the result is a
     * <code>ValueExpression</code>.  For discussion, this is
     * <em>attributeValueExpression</em>.  If not found, log an error
     * and continue to the next attribute.</p></li>
     *
     * <li><p>If <em>name</em> is equal to the string "action", or
     * "actionListener" without the quotes, assume <em>target</em> is
     * an {@link javax.faces.component.ActionSource2}.</p></li>
     *
     * <li><p>If <em>name</em> is equal to the string "validator", or
     * "valueChangeListener" without the quotes, assume
     * <em>target</em> is an {@link
     * javax.faces.component.EditableValueHolder}.</p></li>
     *
     * <li><p>Call <code>getExpressionString()</code> on the
     * <em>attributeValueExpression</em> and use that string to
     * create a <code>MethodExpression</code> of the appropriate
     * signature for <em>name</em>.</p></li>
     *
     * <li><p>If <em>name</em> is not equal to any of the previously
     * listed strings, call <code>getExpressionString()</code> on the
     * <em>attributeValueExpression</em> and use that string to
     * create a <code>MethodExpression</code> where the signature is
     * created based on the value of the
     * "<code>method-signature</code>" attribute of the
     * <code>&lt;composite:attribute /&gt;</code> tag.</p></li>
     *
     * <li><p>Let the resultant <code>MethodExpression</code> be
     * called <em>attributeMethodExpression</em> for discussion.
     * </p></li>
     *
     * <li><p>If <em>name</em> is equal to the string "action"
     * without the quotes, call {@link
     * javax.faces.component.ActionSource2#setActionExpression} on
     * <em>target</em>, passing <em>attributeMethodExpression</em>.</p></li>
     *
     * <li><p>If <em>name</em> is equal to the string
     * "actionListener" without the quotes, call {@link
     * javax.faces.component.ActionSource#addActionListener} on
     * <em>target</em>, passing <em>attributeMethodExpression</em>
     * wrapped in a {@link javax.faces.event.MethodExpressionActionListener}.</p></li>
     *
     * <li><p>If <em>name</em> is equal to the string
     * "validator" without the quotes, call {@link
     * javax.faces.component.EditableValueHolder#addValidator} on <em>target</em>,
     * passing <em>attributeMethodExpression</em> wrapped in a {@link
     * javax.faces.validator.MethodExpressionValidator}.</p></li>
     *
     * <li><p>If <em>name</em> is equal to the string
     * "valueChangeListener" without the quotes, call {@link
     * javax.faces.component.EditableValueHolder#addValueChangeListener} on
     * <em>target</em>, passing <em>attributeMethodExpression</em> wrapped in a
     * {@link javax.faces.event.MethodExpressionValueChangeListener}.</p></li>
     *
     * <li><p>Otherwise, assume that the <code>MethodExpression</code>
     * should be placed in the components attribute set.  The runtme
     * must create the <code>MethodExpression</code> instance based on
     * the value of the "<code>method-signature</code>"
     * attribute.</p></li>

     * </ul>
     * 
     * </li>
     * 
     * </ul>
     *
     * <p>An implementation is provided that will throw
     * <code>UnsupportedOperationException</code>.  A Faces implementation
     * compliant with version 2.0 and beyond of the specification must 
     * override this method.</p>
     *
     * </div>
     *
     * @param context the FacesContext for this request.
     *
     * @param topLevelComponent The UIComponent in the view to which the
     * attached objects must be attached.  This UIComponent must have
     * its component metadata already associated and available from via
     * the JavaBeans API.
     *
     * @throws NullPointerException if <code>context</code>
     * or <code>topLevelComponent</code> is <code>null</code>.
     *
     * @since 2.0
     */
    public void retargetMethodExpressions(FacesContext context,
                                          UIComponent topLevelComponent) {

        // no-op
        
    }
    
    /**
     * <p class="changed_added_2_2">Return the list of resource library
     * contracts that will be made available for use in the view
     * specified by the argument {@code viewId}.  If no match is found,
     * return an empty list.  See section JSF.7.7.2 for the
     * specification of the default implementation.  For backward
     * compatibility with prior implementations, an implementation is
     * provided that returns {@code null}, but any implementation
     * compliant with the version of the specification in which this
     * method was introduced must implement it as specified in
     * JSF.7.7.2. </p>
     * 
     * @param context the {@code FacesContext} for this request
     * @param viewId the view id for which the applicable resource library 
     * contracts should be calculated.
     * 
     * @since 2.2
     */
    
    public List<String> calculateResourceLibraryContracts(FacesContext context,
            String viewId) {
        return null;
    }
    

    /**
     * <p class="changed_added_2_0"><span
     * class="changed_modified_1">Take</span> any actions specific to
     * this VDL implementation to cause the argument
     * <code>UIViewRoot</code> which must have been created via a call
     * to {@link #createView}, to be populated with children.</p>

     * <div class="changed_added_2_0">

     * <p>The Facelets implementation must insure that markup comprising
     * the view must be executed, with the {@link
     * javax.faces.component.UIComponent} instances in the view being
     * encountered in the same depth-first order as in other lifecycle
     * methods defined on <code>UIComponent</code>, and added to the
     * view (but not rendered) during the traversal. The runtime must
     * guarantee that the view must be fully populated before any of the
     * following happen.</p>

     * <ul>
     *
     * <li><p>The {@link javax.faces.event.PhaseListener#afterPhase}
     * method of any <code>PhaseListener</code>s attached to the
     * application is called</p></li>

     * <li><p>The {@link javax.faces.component.UIViewRoot} phase
     * listener installed via {@link
     * javax.faces.component.UIViewRoot#setAfterPhaseListener} or {@link
     * javax.faces.component.UIViewRoot#addPhaseListener} are called.</p></li>
     *
     * </ul>

     * <p class="changed_modified_2_1">If the <code>root</code> is
     * already populated with children, the view must still be re-built,
     * but care must be taken to ensure that the existing components are
     * correctly paired up with their VDL counterparts in the VDL page.
     * Also, any system events that would normally be generated during
     * the adding or removing of components from the view must be
     * temporarily disabled during the creation of the view and then
     * re-enabled when the view has been built.</p>

     * </div>

     * @param context the <code>FacesContext</code> for this request

     * @param root the <code>UIViewRoot</code> to populate with children
     * using techniques specific to this VDL implementation.
     */
    public abstract void buildView(FacesContext context, UIViewRoot root)
    throws IOException;

    
    /**
     * <p class="changed_added_2_0">Render a view rooted at
     * argument<code>view</code>. See section JSF.7.7.2 for the
     * specification of the default implementation.</p>
     *
     * @param context the <code>FacesContext</code> for this request.
     * @param view the <code>UIViewRoot</code> from an early call to
     * {@link #createView} or {@link #restoreView}.
     *
     * @throws NullPointerException if any of the arguments are
     * <code>null</code>
     */
    public abstract void renderView(FacesContext context,
                                    UIViewRoot view)
    throws IOException;
    
    /**
     * <p class="changed_added_2_0">For implementations that want to
     * control the implementation of state saving and restoring, the
     * {@link StateManagementStrategy} allows them to do so.  Returning
     * <code>null</code> indicates that the implementation wishes the
     * runtime to handle the state saving and restoring.
     * Implementations that provide the VDL for Facelets for JSF 2.0 and
     * later must return non-<code>null</code> from this method.</p>
     *
     *
     * @since 2.0
     */ 

    public abstract StateManagementStrategy getStateManagementStrategy(FacesContext context,
            String viewId);


    /**
     * <p class="changed_added_2_1"><span class="changed_modified_2_2">Tests</span>
     * whether a physical resource
     * corresponding to the specified viewId exists.</p>
     *
     * <p class="changed_modified_2_2">The default implementation uses 
     * {@link javax.faces.application.ResourceHandler#createViewResource}
     * to locate the physical resource.</p>
     *
     * @param context The <code>FacesContext</code> for this request.
     * @param viewId the view id to test
     *
     * @since 2.1
     */    
    public boolean viewExists(FacesContext context, 
                              String viewId) {
        boolean result = false;
        ResourceHandler rh = context.getApplication().getResourceHandler();
        result = null != rh.createViewResource(context, viewId);

        return result;
    }

    /**
     * <p class="changed_added_2_1">Returns a non-null String that can be 
     * used to identify this view declaration language.</p>
     *
     * <p>The default implementation returns the fully qualified class name
     * of the view declaration language implementation.  Subclasses may
     * override to provide a more meaningful id.</p>
     *
     * @since 2.1
     */
    public String getId() {
        return getClass().getName();
    }

    private static final Logger LOGGER =
          Logger.getLogger("javax.faces.view", "javax.faces.LogStrings");
}
