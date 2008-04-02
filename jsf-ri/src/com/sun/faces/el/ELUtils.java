/*
 * $Id: ELUtils.java,v 1.1 2007/02/27 23:10:23 rlubke Exp $
 */

/*
 * The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the License). You may not use this file except in
 * compliance with the License.
 *
 * You can obtain a copy of the License at
 * https://javaserverfaces.dev.java.net/CDDL.html or
 * legal/CDDLv1.0.txt.
 * See the License for the specific language governing
 * permission and limitations under the License.
 *
 * When distributing Covered Code, include this CDDL
 * Header Notice in each file and include the License file
 * at legal/CDDLv1.0.txt.
 * If applicable, add the following below the CDDL Header,
 * with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 *
 * [Name of File] [ver.__] [Date]
 *
 * Copyright 2007 Sun Microsystems Inc. All Rights Reserved
 */

package com.sun.faces.el;

import com.sun.faces.RIConstants;
import com.sun.faces.application.ApplicationAssociate;
import com.sun.faces.spi.ManagedBeanFactory;
import com.sun.faces.util.MessageUtils;

import javax.el.ArrayELResolver;
import javax.el.BeanELResolver;
import javax.el.CompositeELResolver;
import javax.el.ELContext;
import javax.el.ELResolver;
import javax.el.ListELResolver;
import javax.el.MapELResolver;
import javax.el.ResourceBundleELResolver;
import javax.el.ValueExpression;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.el.PropertyResolver;
import javax.faces.el.ReferenceSyntaxException;
import javax.faces.el.VariableResolver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * <p>Utility class for EL related methods.</p>
 */
public class ELUtils {


    public static final ArrayELResolver ARRAY_RESOLVER = new ArrayELResolver();

    public static final BeanELResolver BEAN_RESOLVER = new BeanELResolver();

    public static final FacesResourceBundleELResolver FACES_BUNDLE_RESOLVER =
        new FacesResourceBundleELResolver();

    public static final ImplicitObjectELResolverForJsp IMPLICIT_JSP_RESOLVER =
        new ImplicitObjectELResolverForJsp();

    public static final ImplicitObjectELResolver IMPLICIT_RESOLVER =
        new ImplicitObjectELResolver();

    public static final ListELResolver LIST_RESOLVER = new ListELResolver();

    public static final ManagedBeanELResolver MANAGED_BEAN_RESOLVER =
        new ManagedBeanELResolver();

    public static final MapELResolver MAP_RESOLVER = new MapELResolver();

    public static final ResourceBundleELResolver BUNDLE_RESOLVER =
        new ResourceBundleELResolver();

    public static final ScopedAttributeELResolver SCOPED_RESOLVER =
        new ScopedAttributeELResolver();


    // ------------------------------------------------------------ Constructors


    private ELUtils() {

        throw new IllegalStateException();

    }


    // ---------------------------------------------------------- Public Methods


    /**
     * <p>Create the <code>ELResolver</code> chain for programmatic EL calls.</p>
     * @param composite a <code>CompositeELResolver</code>
     * @param associate our ApplicationAssociate
     */
    public static void buildFacesResolver(CompositeELResolver composite,
                                          ApplicationAssociate associate) {

        if (associate == null) {
            String message = MessageUtils.getExceptionMessageString
                 (MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID, "associate");
            throw new NullPointerException(message);
        }

        if (composite == null) {
            String message = MessageUtils.getExceptionMessageString
                 (MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID, "composite");
            throw new NullPointerException(message);
        }

        composite.add(IMPLICIT_RESOLVER);
        addELResolvers(composite, associate.getELResolversFromFacesConfig());
        addVariableResolvers(composite, associate);
        addPropertyResolvers(composite, associate);
        addELResolvers(composite, associate.getApplicationELResolvers());
        composite.add(MANAGED_BEAN_RESOLVER);
        composite.add(BUNDLE_RESOLVER);
        composite.add(FACES_BUNDLE_RESOLVER);
        composite.add(MAP_RESOLVER);
        composite.add(LIST_RESOLVER);
        composite.add(ARRAY_RESOLVER);
        composite.add(BEAN_RESOLVER);
        composite.add(SCOPED_RESOLVER);

    }


    /**
     * <p>Create the <code>ELResolver</code> chain for JSP.</p>
     * @param composite a <code>CompositeELResolver</code>
     * @param associate our ApplicationAssociate
     */
    public static void buildJSPResolver(CompositeELResolver composite,
                                        ApplicationAssociate associate) {

        if (associate == null) {
            String message = MessageUtils.getExceptionMessageString
                 (MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID, "associate");
            throw new NullPointerException(message);
        }

        if (composite == null) {
            String message = MessageUtils.getExceptionMessageString
                 (MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID, "composite");
            throw new NullPointerException(message);
        }

        composite.add(IMPLICIT_JSP_RESOLVER);
        composite.add(MANAGED_BEAN_RESOLVER);
        composite.add(FACES_BUNDLE_RESOLVER);
        addELResolvers(composite, associate.getELResolversFromFacesConfig());
        addVariableResolvers(composite, associate);
        addPropertyResolvers(composite, associate);
        addELResolvers(composite, associate.getApplicationELResolvers());

    }


    public static Object evaluateValueExpression(ValueExpression expression,
                                                 ELContext elContext) {

           if (expression.isLiteralText()) {
               return expression.getExpressionString();
           } else {
               return expression.getValue(elContext);
           }

       }


    /**
     * @param associate the <code>ApplicationAssociate</code>
     * @return the <code>PropertyResolver</code>s set via
     *  {@link javax.faces.application.Application#setPropertyResolver(javax.faces.el.PropertyResolver)}
     *  or, if that is <code>null</code>, return the <code>PropertyResolver</code>
     *  chain from the parsed configuration resources.  If either of those are
     *  null, and <code>provideDefault</code> is <code>true</code>,
     *  return the <code>DummyPropertyResolverImpl</code>.
     */
    public static PropertyResolver getDelegatePR(ApplicationAssociate associate,
                                                 boolean provideDefault)  {

        PropertyResolver pr = associate.getLegacyPropertyResolver();
        if (pr == null) {
            pr = associate.getLegacyPRChainHead();
            if (pr == null && provideDefault) {
                pr = new DummyPropertyResolverImpl();
            }
        }

        return pr;

    }


    /**
     * @param associate the <code>ApplicationAssociate</code>
     * @return the <code>VariableResolver</code>s set via
     *  {@link javax.faces.application.Application#setVariableResolver(javax.faces.el.VariableResolver)}
     *  or, if that is <code>null</code>, return the <code>VariableResolver</code>
     *  chain from the parsed configuration resources.  If either of those are
     *  null, , and <code>provideDefault</code> is <code>true</code>,
     *  return the <code>ChainAwareVariableResolver</code>.
     */
    public static VariableResolver getDelegateVR(ApplicationAssociate associate,
                                                 boolean provideDefault) {

        VariableResolver vr = associate.getLegacyVariableResolver();
        if (vr == null) {
            vr = associate.getLegacyVRChainHead();
            if (vr == null && provideDefault) {
                vr = new ChainAwareVariableResolver();
            }
        }

        return vr;

    }


    /**
     * @param expressionString the expression string, with delimiters
     *                         intact.
     * @return a List of expressions from the expressionString
     */
    @SuppressWarnings("deprecation")
    public static List getExpressionsFromString(String expressionString)
    throws ReferenceSyntaxException {

        if (null == expressionString) {
            return Collections.EMPTY_LIST;
        }
        //noinspection CollectionWithoutInitialCapacity
        List<String> result = new ArrayList<String>();
        int i, j, len = expressionString.length(), cur = 0;
        while (cur < len &&
             -1 != (i = expressionString.indexOf("#{", cur))) {
            if (-1 == (j = expressionString.indexOf('}', i + 2))) {
                throw new ReferenceSyntaxException(
                     MessageUtils.getExceptionMessageString(
                          MessageUtils.INVALID_EXPRESSION_ID,
                          expressionString));
            }
            cur = j + 1;
            result.add(expressionString.substring(i, cur));
        }
        return result;

    }
        

    /**
     * <p>This method is used by the ManagedBeanFactory to ensure that
     * properties set by an expression point to an object with an
     * accepted lifespan.</p>
     *
     * <p>get the scope of the expression. Return <code>null</code> if
     * it isn't scoped</p>
     *
     * <p>For example, the expression:
     * <code>sessionScope.TestBean.one</code> should return "session"
     * as the scope.</p>
     *
     * @param valueBinding the expression
     *
     * @param outString an allocated String Array into which we put the
     * first segment.
     *
     * @return the scope of the expression
     *
     * PENDING: This should be private to ManagedBeanFactoryImpl.  Keep it
     *  here for now as ManagedBeanFactoryImpl is going to be changed for
     *  _05
     */
    @SuppressWarnings("deprecation")
    public static ManagedBeanFactory.Scope getScope(String valueBinding,
                                                    String[] outString)
    throws ReferenceSyntaxException {

        if (valueBinding == null || 0 == valueBinding.length()) {
            return null;
        }
        valueBinding = stripBracketsIfNecessary(valueBinding);

        int segmentIndex = getFirstSegmentIndex(valueBinding);

        //examine first segment and see if it is a scope
        String identifier = valueBinding;

        if (segmentIndex > 0) {
            //get first segment designated by a "." or "["
            identifier = valueBinding.substring(0, segmentIndex);
        }

        //check to see if the identifier is a named scope.

        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext ec = context.getExternalContext();

        if (null != outString) {
            outString[0] = identifier;
        }
        if (RIConstants.REQUEST_SCOPE.equalsIgnoreCase(identifier)) {
            return ManagedBeanFactory.Scope.REQUEST;
        }
        if (RIConstants.SESSION_SCOPE.equalsIgnoreCase(identifier)) {
            return ManagedBeanFactory.Scope.SESSION;
        }
        if (RIConstants.APPLICATION_SCOPE.equalsIgnoreCase(identifier)) {
            return ManagedBeanFactory.Scope.APPLICATION;
        }

        // handle implicit objects
        if (RIConstants.INIT_PARAM_IMPLICIT_OBJ.equalsIgnoreCase(identifier)) {
            return ManagedBeanFactory.Scope.APPLICATION;
        }
        if (RIConstants.COOKIE_IMPLICIT_OBJ.equalsIgnoreCase(identifier)) {
            return ManagedBeanFactory.Scope.REQUEST;
        }
        if (RIConstants.FACES_CONTEXT_IMPLICIT_OBJ.equalsIgnoreCase(identifier)) {
            return ManagedBeanFactory.Scope.REQUEST;
        }
        if (RIConstants.HEADER_IMPLICIT_OBJ.equalsIgnoreCase(identifier)) {
            return ManagedBeanFactory.Scope.REQUEST;
        }
        if (RIConstants.HEADER_VALUES_IMPLICIT_OBJ.equalsIgnoreCase(identifier)) {
            return ManagedBeanFactory.Scope.REQUEST;
        }
        if (RIConstants.PARAM_IMPLICIT_OBJ.equalsIgnoreCase(identifier)) {
            return ManagedBeanFactory.Scope.REQUEST;
        }
        if (RIConstants.PARAM_VALUES_IMPLICIT_OBJ.equalsIgnoreCase(identifier)) {
            return ManagedBeanFactory.Scope.REQUEST;
        }
        if (RIConstants.VIEW_IMPLICIT_OBJ.equalsIgnoreCase(identifier)) {
            return ManagedBeanFactory.Scope.REQUEST;
        }

        //No scope was provided in the expression so check for the
        //expression in all of the scopes. The expression is the first
        //segment.

        if (ec.getRequestMap().get(identifier) != null) {
            return ManagedBeanFactory.Scope.REQUEST;
        }
        if (ec.getSessionMap().get(identifier) != null) {
            return ManagedBeanFactory.Scope.SESSION;
        }
        if (ec.getApplicationMap().get(identifier) != null) {
            return ManagedBeanFactory.Scope.APPLICATION;
        }

        //not present in any scope
        return null;

    }


    /**
     * Create a <code>ValueExpression</code> with the expected type of
     * <code>Object.class</code>
     * @param valueRef a value expression
     * @return a new <code>ValueExpression</code> instance based off the
     *  provided <code>valueRef</code>
     */
    public static ValueExpression getValueExpression(String valueRef) {

        FacesContext context = FacesContext.getCurrentInstance();
        return context.getApplication().getExpressionFactory().
            createValueExpression(context.getELContext(),
                                  valueRef,
                                  Object.class);

    }


    // --------------------------------------------------------- Private Methods


    /**
     * <p>Add the <code>ELResolvers</code> from the provided list
     * to the target <code>CompositeELResolver</code>.</p>
     *
     * @param target the <code>CompositeELResolver</code> to which
     *  the <code>ELResolver</code>s will be added.
     * @param resolvers a <code>List</code> of <code>ELResolver</code>s
     */
    private static void addELResolvers(CompositeELResolver target,
                                       List<ELResolver> resolvers) {

        if (resolvers != null && !resolvers.isEmpty()) {
            for (ELResolver resolver : resolvers) {
                target.add(resolver);
            }
        }

    }


    /**
     * <p>Add any <code>PropertyResolver</code>s to the specified
     * <code>CompositeELResolver</code>.</p>
     * @param target the <code>CompositeELResolver</code> to which
     *  the <code>PropertyResolver</code>s will be added.
     * @param associate our ApplicationAssociate
     */
    private static void addPropertyResolvers(CompositeELResolver target,
                                             ApplicationAssociate associate) {

        PropertyResolver pr = getDelegatePR(associate, false);
        if (pr != null) {
            target.add(new PropertyResolverChainWrapper(pr));
        }

    }


    /**
     * <p>Add any <code>VariableResolver</code>s to the specified
     * <code>CompositeELResolver</code>.</p>
     * @param target the <code>CompositeELResolver</code> to which
     *  the <code>VariableResolver</code>s will be added.
     * @param associate our ApplicationAssociate
     */
    private static void addVariableResolvers(CompositeELResolver target,
                                             ApplicationAssociate associate) {

        VariableResolver vr = getDelegateVR(associate, false);
        if (vr != null) {
            target.add(new VariableResolverChainWrapper(vr));
        }

    }


    /**
     * <p/>
     * The the first segment of a String tokenized by a "." or "["
     *
     * @return index of the first occurrence of . or [
     */
    private static int getFirstSegmentIndex(String valueBinding) {

        int segmentIndex = valueBinding.indexOf('.');
        int bracketIndex = valueBinding.indexOf('[');

        //there is no "." in the valueBinding so take the bracket value
        if (segmentIndex < 0) {
            segmentIndex = bracketIndex;
        } else {
            //if there is a bracket proceed
            if (bracketIndex > 0) {
                //if the bracket index is before the "." then
                //get the bracket index
                if (segmentIndex > bracketIndex) {
                    segmentIndex = bracketIndex;
                }
            }
        }
        return segmentIndex;

    }


    @SuppressWarnings("deprecation")
    private static String stripBracketsIfNecessary(String expression)
    throws ReferenceSyntaxException {

        assert (null != expression);

        // look for invalid expressions
        if (expression.charAt(0) == '#') {
            if (expression.charAt(1) != '{') {
                throw new ReferenceSyntaxException(MessageUtils.getExceptionMessageString(
                    MessageUtils.INVALID_EXPRESSION_ID,
                    expression));
            }
            int len = expression.length();
            if (expression.charAt(len - 1) != '}') {
                throw new ReferenceSyntaxException(MessageUtils.getExceptionMessageString(
                    MessageUtils.INVALID_EXPRESSION_ID,
                    expression));
            }
            expression = expression.substring(2, len - 1);
        }

        return expression;

    }

}
