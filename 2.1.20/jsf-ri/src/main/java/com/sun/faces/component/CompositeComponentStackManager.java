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

package com.sun.faces.component;

import javax.faces.context.FacesContext;
import javax.faces.component.UIComponent;
import javax.faces.view.Location;
import javax.faces.application.Resource;
import java.util.Stack;

/**
 * <p>
 * <code>CompositeComponentStackManager</code> is responsible for managing the
 * two different composite component stacks currently used by Mojarra.
 * </p>
 *
 * <p>
 * The stacks are identified by the {@link StackType} enum which has two elements,
 * <code>TreeCreation<code> and <code>Evaluation</code>.
 * </p>
 *
 * <p>
 * The <code>TreeCreation</code> stack represents the composite components that
 * have been pushed by the TagHandlers responsible for building the tree.
 * </p>
 *
 * <p>
 * The <code>Evaluation</code> stack is used by the EL in order to properly
 * resolve nested composite component expressions.
 * </p>
 */
public class CompositeComponentStackManager {


    private static final String MANAGER_KEY =
          CompositeComponentStackManager.class.getName();


    public enum StackType {
        TreeCreation,
        Evaluation
    }

    private StackHandler treeCreation = new TreeCreationStackHandler();
    private StackHandler runtime = new RuntimeStackHandler();

    
    // ------------------------------------------------------------ Constructors


    private CompositeComponentStackManager() {
    }


    // ---------------------------------------------------------- Public Methods


    /**
     * @param ctx the <code>FacesContext</code> for the current request
     * @return the <code>CompositeComponentStackManager</code> for the current
     *  request
     */
    public static CompositeComponentStackManager getManager(FacesContext ctx) {

        CompositeComponentStackManager manager =
              (CompositeComponentStackManager) ctx.getAttributes().get(MANAGER_KEY);
        if (manager == null) {
            manager = new CompositeComponentStackManager();
            ctx.getAttributes().put(MANAGER_KEY, manager);
        }

        return manager;

    }


    /**
     * <p>
     * Pushes the specified composite component to the <code>Evaluation</code>
     * stack.
     * </p>
     *
     * @param compositeComponent the component to push
     * @return <code>true</code> if the component was pushed, otherwise
     *  returns <code>false</code>
     */
    public boolean push(UIComponent compositeComponent) {
        return getStackHandler(StackType.Evaluation).push(compositeComponent);
    }


    /**
     * <p>
     * Pushes the specified composite component to the desired <code>StackType</code>
     * stack.
     * </p>
     *
     * @param compositeComponent the component to push
     * @param stackType the stack to push to the component to
     * @return <code>true</code> if the component was pushed, otherwise
     *  returns <code>false</code>
     */
    public boolean push(UIComponent compositeComponent, StackType stackType) {
        return getStackHandler(stackType).push(compositeComponent);
    }


    /**
     * <p>
     * Pushes a component derived by the push logic to the <code>Evaluation</code>
     * stack.
     * </p>
     *
     * @return <code>true</code> if the component was pushed, otherwise
     *  returns <code>false</code>
     */
    public boolean push() {
        return getStackHandler(StackType.Evaluation).push();
    }


    /**
     * <p>
     * Pushes a component derived by the push logic to the specified stack.
     * </p>
     *
     * @param stackType the stack to push to the component to
     *
     * @return <code>true</code> if the component was pushed, otherwise
     *  returns <code>false</code>
     */
    public boolean push(StackType stackType) {
        return getStackHandler(stackType).push();
    }


    /**
     * <p>
     * Pops the top-level component from the stack.
     * </p>
     *
     * @param stackType the stack to pop the top level component from
     */
    public void pop(StackType stackType) {
        getStackHandler(stackType).pop();
    }


    /**
     * <p>
     * Pops the top-level component from the <code>Evaluation</code> stack.
     * </p>
     */
    public void pop() {
        getStackHandler(StackType.Evaluation).pop();
    }


    /**
     * @return the top-level component from the <code>Evaluation</code> stack
     *  without removing the element
     */
    public UIComponent peek() {
        return getStackHandler(StackType.Evaluation).peek();
    }


    /**
     * @param stackType the stack to push to the component to
     * 
     * @return the top-level component from the specified stack
     *  without removing the element
     */
    public UIComponent peek(StackType stackType) {
        return getStackHandler(stackType).peek();
    }


    public UIComponent getParentCompositeComponent(StackType stackType,
                                                   FacesContext ctx,
                                                   UIComponent forComponent) {
        return getStackHandler(stackType).getParentCompositeComponent(ctx, forComponent);
    }

    public UIComponent findCompositeComponentUsingLocation(FacesContext ctx,
                                                           Location location) {

        StackHandler sh = getStackHandler(StackType.TreeCreation);
        Stack<UIComponent> s = sh.getStack(false);
        if (s != null) {
            String path = location.getPath();
            for (int i = s.size(); i > 0; i--) {
                UIComponent cc = s.get(i - 1);
                Resource r = (Resource) cc.getAttributes().get(Resource.COMPONENT_RESOURCE_KEY);
                if (path.endsWith('/' + r.getResourceName()) && path.contains(r.getLibraryName())) {
                    return cc;
                }
            }
        } else {
            // runtime eval
            String path = location.getPath();
            UIComponent cc = UIComponent.getCurrentCompositeComponent(ctx);
            while (cc != null) {
                Resource r = (Resource) cc.getAttributes().get(Resource.COMPONENT_RESOURCE_KEY);
                if (path.endsWith('/' + r.getResourceName()) && path.contains(r.getLibraryName())) {
                    return cc;
                }
                cc = UIComponent.getCompositeComponentParent(cc);
            }
        }
        return null;
    }


    // --------------------------------------------------------- Private Methods


    private StackHandler getStackHandler(StackType type) {

        StackHandler handler = null;
        switch (type) {
            case TreeCreation: handler = treeCreation; break;
            case Evaluation: handler = runtime; break;
        }
        return handler;

    }


    // ------------------------------------------------------ Private Interfaces


    private interface StackHandler {

        boolean push(UIComponent compositeComponent);
        boolean push();
        void pop();
        UIComponent peek();
        UIComponent getParentCompositeComponent(FacesContext ctx, UIComponent forComponent);
        void delete();
        Stack<UIComponent> getStack(boolean create);

    }


    // ---------------------------------------------------------- Nested Classes


    private abstract class BaseStackHandler implements StackHandler {

        protected Stack<UIComponent> stack;


        // ------------------------------------------- Methods from StackHandler


        public void delete() {

            stack = null;

        }


        public Stack<UIComponent> getStack(boolean create) {

            if (stack == null && create) {
                stack = new Stack<UIComponent>();
            }
            return stack;

        }


        public UIComponent peek() {

            if (stack != null && !stack.isEmpty()) {
                return stack.peek();
            }
            return null;
            
        }

    } // END BaseStackHandler


    private final class RuntimeStackHandler extends BaseStackHandler {


        // ------------------------------------------- Methods from StackHandler


        public void delete() {

            Stack s = getStack(false);
            if (s != null) {
                s.clear();
            }

        }


        public void pop() {

            Stack s = getStack(false);
            if (s != null && !s.isEmpty()) {
                s.pop();
            }

        }


        public boolean push() {

            return push(null);

        }


        public boolean push(UIComponent compositeComponent) {

            Stack<UIComponent> tstack =
                  CompositeComponentStackManager.this.treeCreation.getStack(false);
            Stack<UIComponent> stack = getStack(false);
            UIComponent ccp;
            if (tstack != null) {
                // We have access to the stack of composite components
                // the tree creation process has made available.
                // Since we can' reliably access the parent composite component
                // of the current composite component, use the index of the
                // current composite component within the stack to locate the
                // parent.
                ccp = compositeComponent;
            } else {
                // no tree creation stack available, so use the runtime stack.
                // If the current stack isn't empty, then use the component
                // on the stack as the current composite component.
                stack = getStack(false);

                if (compositeComponent == null) {
                    if (stack != null && !stack.isEmpty()) {
                        ccp = getCompositeParent(stack.peek());
                    } else {
                        ccp = getCompositeParent((UIComponent
                              .getCurrentCompositeComponent(FacesContext.getCurrentInstance())));
                    }
                } else {
                    ccp = compositeComponent;
                }
            }


            if (ccp != null) {
                if (stack == null) {
                    stack = getStack(true);
                }
                stack.push(ccp);
                return true;
            }
            return false;

        }

        public UIComponent getParentCompositeComponent(FacesContext ctx,
                                                       UIComponent forComponent) {

            return getCompositeParent(forComponent);

        }


        // ----------------------------------------------------- Private Methods


        private UIComponent getCompositeParent(UIComponent comp) {

            return UIComponent.getCompositeComponentParent(comp);

        }

    } // END RuntimeStackHandler


    private final class TreeCreationStackHandler extends BaseStackHandler {


        // ------------------------------------------- Methods from StackHandler


        public void pop() {

            Stack s = getStack(false);
            if (s != null && !stack.isEmpty()) {
                stack.pop();
                if (stack.isEmpty()) {
                    delete();
                }
            }

        }


        public boolean push() {

            return false;

        }


        public boolean push(UIComponent compositeComponent) {

            if (compositeComponent != null) {
                assert (UIComponent.isCompositeComponent(compositeComponent));
                Stack<UIComponent> s = getStack(true);
                s.push(compositeComponent);
                return true;
            }
            return false;

        }


        public UIComponent getParentCompositeComponent(FacesContext ctx, UIComponent forComponent) {

            Stack<UIComponent> s = getStack(false);
            if (s == null) {
                return null;
            } else {
                int idx = s.indexOf(forComponent);
                if (idx == 0) { // no parent
                    return null;
                }
                return (s.get(idx - 1));
            }
        }
        
    } // END TreeCreationStackHandler



}
