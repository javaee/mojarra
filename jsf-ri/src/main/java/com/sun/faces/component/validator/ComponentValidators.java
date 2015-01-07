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

package com.sun.faces.component.validator;


import com.sun.faces.facelets.tag.jsf.ComponentSupport;
import com.sun.faces.util.RequestStateManager;

import javax.faces.context.FacesContext;
import javax.faces.component.EditableValueHolder;
import javax.faces.validator.Validator;
import javax.faces.application.Application;
import javax.faces.view.facelets.ValidatorHandler;
import javax.faces.view.facelets.FaceletContext;

import java.util.*;

import javax.faces.event.PhaseId;

/**
 * <p>
 * This class is responsible for adding default validators and/or validators
 * that wrap multiple <code>EditableValueHolder</code> instances within the view.
 * </p>
 */
public class ComponentValidators {


    /**
     * Key within the <code>FacesContext</code>'s attribute map under which
     * a single <code>ComponentValidators</code> instance will be stored.
     */
    private static final String COMPONENT_VALIDATORS = "javax.faces.component.ComponentValidators";


    /**
     * Stack of <code>ValidatorInfo<code> instances.  Each instance represents
     * a particular nesting level within the view.  As a nesting level is encountered,
     * a <code>ValidatorInfo</code> will be pushed to the stack and all
     * <code>EditableValueHolder</code> instances will be configured based on
     * all <code>ValidatorInfo</code>s on the stack.  When the current nesting level
     * is closed, the <code>ValidatorInfo</code> instance will be popped and thus
     * have no impact on other <code>EditableValueHolder</code>s.
     */
    private LinkedList<ValidatorInfo> validatorStack = null;


    // ------------------------------------------------------------ Constructors

    
    public ComponentValidators() {

        validatorStack = new LinkedList<ValidatorInfo>();

    }


    // ---------------------------------------------------------- Public Methods


    /**
     * @param context the <code>FacesContext</code> for the current request
     * @param createIfNull flag indicating whether or not a
     *  <code>ComponentValidators</code> instance should be created or not
     * @return a <code>ComponentValidators</code> instance for processing
     *  a view request.  If <code>createIfNull</code> is <code>false</code>
     *  and no <code>ComponentValidators</code> has been created, this method
     *  will return <code>null</code>
     */
    public static ComponentValidators getValidators(FacesContext context,
                                                    boolean createIfNull) {

        Map<Object, Object> attrs = context.getAttributes();
        ComponentValidators componentValidators = (ComponentValidators) attrs
              .get(COMPONENT_VALIDATORS);

        if ((componentValidators == null) && createIfNull) {
            componentValidators = new ComponentValidators();
            attrs.put(COMPONENT_VALIDATORS, componentValidators);
        }

        return componentValidators;
    }


    /**
     * <p>
     * Creates and installs default validators, if any, into the argument
     * <code>EditableValueHolder</code>.  This method is merely a utility
     * method to be called when there is no <code>ComponentValidators</code>
     * available, or there are no <code>ValidatorInfo</code> instances on the
     * stack.
     * </p>
     *
     * @param ctx the <code>FacesContext</code> for the current request
     * @param editableValueHolder the component receiving the <code>Validator</code>s
     */
    @SuppressWarnings({"unchecked"})
    public static void addDefaultValidatorsToComponent(FacesContext ctx,
                                                       EditableValueHolder editableValueHolder) {

        if (ComponentSupport.isBuildingNewComponentTree(ctx)) {
            Set<String> keySet = ctx.getApplication().getDefaultValidatorInfo().keySet();
            List<String> validatorIds = new ArrayList<String>(keySet.size());
            Set<String> disabledValidatorIds = (Set<String>)
                  RequestStateManager.remove(ctx, RequestStateManager.DISABLED_VALIDATORS);
            for (String key : keySet) {
                if (disabledValidatorIds != null && disabledValidatorIds.contains(key)) {
                    continue;
                }
                validatorIds.add(key);
            }

            addValidatorsToComponent(ctx, validatorIds, editableValueHolder, null);
        }
    }


    /**
     * <p>
     * Based on the <code>ValidatorInfo</code> instances present on the stack,
     * configure the argument <code>EditableValueHolder</code> with <code>Validator</code>s
     * created from the available info.
     * </p>
     *
     * @param ctx the <code>FacesContext</code> for the current request
     * @param editableValueHolder the component receiving the <code>Validator</code>s
     */
    @SuppressWarnings({"unchecked"})
    public void addValidators(FacesContext ctx,
                              EditableValueHolder editableValueHolder) {

        if ((validatorStack == null) || validatorStack.isEmpty()) {
            addDefaultValidatorsToComponent(ctx, editableValueHolder);
            return;
        }

        Application application = ctx.getApplication();
        Map<String, String> defaultValidatorInfo =
              application.getDefaultValidatorInfo();
        Set<String> keySet = defaultValidatorInfo.keySet();

        List<String> validatorIds = new ArrayList<String>(keySet.size());
        for (String key : keySet) {
            validatorIds.add(key);
        }

        Set<String> disabledIds = (Set<String>)
              RequestStateManager.remove(ctx,
                                         RequestStateManager.DISABLED_VALIDATORS);
        int count = validatorStack.size();
        for (int i = count - 1; i >= 0; i--) {
            ValidatorInfo info = validatorStack.get(i);
            if (!info.isEnabled() || (disabledIds != null && disabledIds.contains(info.getValidatorId()))) {
                if (validatorIds.contains(info.getValidatorId())) {
                    validatorIds.remove(info.getValidatorId());
                }
            } else {
                if (!validatorIds.contains(info.getValidatorId())) {
                    validatorIds.add(info.getValidatorId());
                }
            }
        }

        // add the validators to the EditableValueHolder.
        addValidatorsToComponent(ctx,
                                 validatorIds,
                                 editableValueHolder,
                                 ((validatorStack == null || validatorStack.isEmpty())
                                     ? null
                                     : validatorStack));
        
    }


    /**
     * <p>
     * Pushes the provided <code>ValidatorInfo</code> onto the stack.
     * </p>
     *
     * @param info
     */
    public void pushValidatorInfo(ValidatorInfo info) {

        validatorStack.add(info);

    }


    /**
     * <p>
     * Pops the last <code>ValidatorInfo</code> instance from the stack.
     * </p>
     */
    public void popValidatorInfo() {

        if (validatorStack.size() > 0) {
            validatorStack.removeLast();
        }

    }


    // --------------------------------------------------------- Private Methods


    /**
     * <p>
     * Install the validators, if not already present on the component,
     * using the IDs included in <code>validatorIds</code>.
     * </p>
     *
     * @param ctx the <code>FacesContext</code> for the current request
     * @param validatorIds the validator IDs to be added to the
     *  <code>EditableValueHolder</code>
     * @param editableValueHolder the target component to which the validators
     *  installed
     * @param validatorStack current stack of ValidatorInfo instances
     */
    private static void addValidatorsToComponent(FacesContext ctx,
                                                 Collection<String> validatorIds,
                                                 EditableValueHolder editableValueHolder,
                                                 LinkedList<ValidatorInfo> validatorStack) {

        if (validatorIds == null || validatorIds.isEmpty()) {
            return;
        }

        Application application = ctx.getApplication();
        Map<String,String> defaultValidatorInfo =
              application.getDefaultValidatorInfo();
        Validator[] validators = editableValueHolder.getValidators();
        // check to make sure that Validator instances haven't already
        // been added.
        for (Map.Entry<String,String> defaultValidator : defaultValidatorInfo.entrySet()) {
            for (Validator validator : validators) {
                if (defaultValidator.getValue().equals(validator.getClass().getName())) {
                    validatorIds.remove(defaultValidator.getKey());
                    break;
                }
            }
        }

        // we now have the complete List of Validator IDs to add to the
        // target EditablValueHolder
        for (String id : validatorIds) {
            Validator v = application.createValidator(id);
            // work backwards up the stack of ValidatorInfo to find the
            // nearest matching ValidatorInfo to apply attributes
            if (validatorStack != null) {
                for (int i = validatorStack.size() - 1; i >= 0; i--) {
                    ValidatorInfo info = validatorStack.get(i);
                    if (id.equals(info.getValidatorId())) {
                        info.applyAttributes(v);
                        break;
                    }
                }
            }
            editableValueHolder.addValidator(v);
        }

    }


    // ---------------------------------------------------------- Nested Classes


    /**
     * Generic information container for a validator at a particular
     * nesting Level.
     */
    public static class ValidatorInfo {

        private String validatorId;
        private boolean enabled;
        private ValidatorHandler owner;
        private FaceletContext ctx;


        // ------------------------------------------------------------ Constructors


        public ValidatorInfo(FaceletContext ctx,
                             ValidatorHandler owner) {

            this.owner = owner;
            this.ctx = ctx;
            this.validatorId = owner.getValidatorId(ctx);
            this.enabled = !owner.isDisabled(ctx);

        }


        // -------------------------------------------------------------------------


        public String getValidatorId() {

            return validatorId;

        }


        public boolean isEnabled() {

            return enabled;

        }

        public void applyAttributes(Validator v) {

            owner.setAttributes(ctx, v);
            
        }

    } // END ValidatorInfo
    
}
