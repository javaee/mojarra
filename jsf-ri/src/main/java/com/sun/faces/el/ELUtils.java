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

package com.sun.faces.el;

import com.sun.faces.RIConstants;
import com.sun.faces.application.ApplicationAssociate;
import com.sun.faces.context.flash.FlashELResolver;
import com.sun.faces.mgbean.BeanManager;
import com.sun.faces.util.MessageUtils;

import com.sun.faces.util.ReflectionUtils;
import java.lang.reflect.Method;
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
import javax.faces.el.EvaluationException;
import javax.faces.el.PropertyResolver;
import javax.faces.el.ReferenceSyntaxException;
import javax.faces.el.VariableResolver;
import javax.faces.component.UIViewRoot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.el.ExpressionFactory;
import javax.servlet.ServletContext;
import javax.servlet.jsp.JspApplicationContext;
import javax.servlet.jsp.JspFactory;

/**
 * <p>Utility class for EL related methods.</p>
 */
public class ELUtils {

    /**
     * Helps to determine if a EL expression represents a composite component
     * EL expression.
     */
    private static final Pattern COMPOSITE_COMPONENT_EXPRESSION =
          Pattern.compile(".(?:[ ]+|[\\[{,(])cc[.].+[}]");

    /**
     * Used to determine if EL method arguments are being passed to a
     * composite component lookup expression.
     *
     * For example:
     *
     *    #{cc.attrs.label('foo')}
     *
     * is illegal, while:
     *
     *    #{cc.attrs.bean.label('foo')}
     *
     * is legal.
     */
    private static final Pattern COMPOSITE_COMPONENT_LOOKUP_WITH_ARGS =
          Pattern.compile("(?:[ ]+|[\\[{,(])cc[.]attrs[.]\\w+[(].+[)]");


    /**
     * Use to determine if an expression being considered as a
     * MethodExpression is a simple lookup (i.e. #{cc.attrs.myaction}).
     */
    private static final Pattern METHOD_EXPRESSION_LOOKUP =
          Pattern.compile(".[{]cc[.]attrs[.]\\w+[}]");

    private static final String APPLICATION_SCOPE = "applicationScope";
    private static final String SESSION_SCOPE = "sessionScope";
    private static final String REQUEST_SCOPE = "requestScope";
    private static final String VIEW_SCOPE = "viewScope";
    private static final String COOKIE_IMPLICIT_OBJ = "cookie";
    private static final String FACES_CONTEXT_IMPLICIT_OBJ = "facesContext";
    private static final String HEADER_IMPLICIT_OBJ = "header";
    private static final String HEADER_VALUES_IMPLICIT_OBJ = "headerValues";
    private static final String INIT_PARAM_IMPLICIT_OBJ = "initParam";
    private static final String PARAM_IMPLICIT_OBJ = "param";
    private static final String PARAM_VALUES_IMPLICIT_OBJ = "paramValues";
    private static final String VIEW_IMPLICIT_OBJ = "view";

    public enum Scope {
        NONE("none"),
        REQUEST("request"),
        VIEW("view"),
        SESSION("session"),
        APPLICATION("application");

        String scope;
        Scope(String scope) {
            this.scope = scope;
        }

        public String toString() {
            return scope;
        }

    }

    public static final ArrayELResolver ARRAY_RESOLVER = new ArrayELResolver();

    public static final BeanELResolver BEAN_RESOLVER = new BeanELResolver();

    public static final FacesResourceBundleELResolver FACES_BUNDLE_RESOLVER =
        new FacesResourceBundleELResolver();

    public static final ImplicitObjectELResolverForJsp IMPLICIT_JSP_RESOLVER =
        new ImplicitObjectELResolverForJsp();

    public static final ImplicitObjectELResolver IMPLICIT_RESOLVER =
        new ImplicitObjectELResolver();
    
    public static final FlashELResolver FLASH_RESOLVER = 
        new FlashELResolver();

    public static final ListELResolver LIST_RESOLVER = new ListELResolver();

    public static final ManagedBeanELResolver MANAGED_BEAN_RESOLVER =
        new ManagedBeanELResolver();

    public static final MapELResolver MAP_RESOLVER = new MapELResolver();

    public static final ResourceBundleELResolver BUNDLE_RESOLVER =
        new ResourceBundleELResolver();

    public static final ScopedAttributeELResolver SCOPED_RESOLVER =
        new ScopedAttributeELResolver();

    public static final ResourceELResolver RESOURCE_RESOLVER =
          new ResourceELResolver();

    public static final CompositeComponentAttributesELResolver COMPOSITE_COMPONENT_ATTRIBUTES_EL_RESOLVER =
          new CompositeComponentAttributesELResolver();



    // ------------------------------------------------------------ Constructors


    private ELUtils() {

        throw new IllegalStateException();

    }


    // ---------------------------------------------------------- Public Methods


    public static boolean isCompositeComponentExpr(String expression) {

        // TODO we should be trying to re-use the Matcher by calling
        // m.reset(expression);
        Matcher m = COMPOSITE_COMPONENT_EXPRESSION.matcher(expression);
        return m.find();

    }


    public static boolean isCompositeComponentMethodExprLookup(String expression) {

        Matcher m = METHOD_EXPRESSION_LOOKUP.matcher(expression);
        return m.matches();

    }


    public static boolean isCompositeComponentLookupWithArgs(String expression) {

        // TODO we should be trying to re-use the Matcher by calling
        // m.reset(expression);
        Matcher m = COMPOSITE_COMPONENT_LOOKUP_WITH_ARGS.matcher(expression);
        return m.find();
        
    }


    /**
     * <p>Create the <code>ELResolver</code> chain for programmatic EL calls.</p>
     * @param composite a <code>CompositeELResolver</code>
     * @param associate our ApplicationAssociate
     */
    public static void buildFacesResolver(FacesCompositeELResolver composite,
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

        composite.addRootELResolver(IMPLICIT_RESOLVER);
        composite.add(FLASH_RESOLVER);
        composite.addPropertyELResolver(COMPOSITE_COMPONENT_ATTRIBUTES_EL_RESOLVER);
        addELResolvers(composite, associate.getELResolversFromFacesConfig());
        addVariableResolvers(composite, FacesCompositeELResolver.ELResolverChainType.Faces,
                associate);
        addPropertyResolvers(composite, associate);
        composite.add(associate.getApplicationELResolvers());
        composite.addRootELResolver(MANAGED_BEAN_RESOLVER);
        composite.addPropertyELResolver(RESOURCE_RESOLVER);
        composite.addPropertyELResolver(BUNDLE_RESOLVER);
        composite.addRootELResolver(FACES_BUNDLE_RESOLVER);
        addEL3_0_Resolvers(composite, associate);
        composite.addPropertyELResolver(MAP_RESOLVER);
        composite.addPropertyELResolver(LIST_RESOLVER);
        composite.addPropertyELResolver(ARRAY_RESOLVER);
        composite.addPropertyELResolver(BEAN_RESOLVER);
        composite.addRootELResolver(SCOPED_RESOLVER);

    }
    
    private static void addEL3_0_Resolvers(FacesCompositeELResolver composite, 
            ApplicationAssociate associate) {
        ExpressionFactory ef = associate.getExpressionFactory();
        Method getStreamELResolverMethod = ReflectionUtils.lookupMethod(ExpressionFactory.class, 
                "getStreamELResolver", RIConstants.EMPTY_CLASS_ARGS);
        if (null != getStreamELResolverMethod) {
            try {
                ELResolver streamELResolver = (ELResolver) 
                        getStreamELResolverMethod.invoke(ef, (Object[]) null);
                composite.addRootELResolver(streamELResolver);
                // Assume that if we have getStreamELResolver, then we must have
                // javax.el.staticFieldELResolver
                composite.addRootELResolver((ELResolver)
                        ReflectionUtils.newInstance("javax.el.StaticFieldELResolver"));
                
            } catch (Throwable t) {
                // This is normal on containers that do not have these ELResolvers
            }
        }
    }


    /**
     * <p>Create the <code>ELResolver</code> chain for JSP.</p>
     * @param composite a <code>CompositeELResolver</code>
     * @param associate our ApplicationAssociate
     */
    public static void buildJSPResolver(FacesCompositeELResolver composite,
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

        composite.addRootELResolver(IMPLICIT_JSP_RESOLVER);
        composite.add(FLASH_RESOLVER);
        composite.addRootELResolver(MANAGED_BEAN_RESOLVER);
        composite.addPropertyELResolver(RESOURCE_RESOLVER);
        composite.addRootELResolver(FACES_BUNDLE_RESOLVER);
        addELResolvers(composite, associate.getELResolversFromFacesConfig());
        addVariableResolvers(composite, FacesCompositeELResolver.ELResolverChainType.JSP,
	                associate);
        addPropertyResolvers(composite, associate);
        composite.add(associate.getApplicationELResolvers());

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
     * @param provideDefault whether or not to return a
     *  <code>DummpyPropertyResolverImpl</code>
     * @return the <code>PropertyResolver</code>s set via
     *  {@link javax.faces.application.Application#setPropertyResolver(javax.faces.el.PropertyResolver)}
     *  or, if that is <code>null</code>, return the <code>PropertyResolver</code>
     *  chain from the parsed configuration resources.  If either of those are
     *  null, and <code>provideDefault</code> is <code>true</code>,
     *  return the <code>DummyPropertyResolverImpl</code>.
     */
    @SuppressWarnings("deprecation")
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
     * @param provideDefault whether or not to return a
     *  <code>DummpyPropertyResolverImpl</code>
     * @return the <code>VariableResolver</code>s set via
     *  {@link javax.faces.application.Application#setVariableResolver(javax.faces.el.VariableResolver)}
     *  or, if that is <code>null</code>, return the <code>VariableResolver</code>
     *  chain from the parsed configuration resources.  If either of those are
     *  null, , and <code>provideDefault</code> is <code>true</code>,
     *  return the <code>ChainAwareVariableResolver</code>.
     */
    @SuppressWarnings("deprecation")
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
     * @throws ReferenceSyntaxException if the expression string is invalid
     */
    @SuppressWarnings("deprecation")
    public static List<String> getExpressionsFromString(String expressionString)
    throws ReferenceSyntaxException {

        if (null == expressionString) {
            return Collections.emptyList();
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
     * @throws ReferenceSyntaxException if valueBinding is syntactically invalid
     */
    @SuppressWarnings("deprecation")
    public static ELUtils.Scope getScope(String valueBinding, String[] outString)
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
        if (REQUEST_SCOPE.equalsIgnoreCase(identifier)) {
            return Scope.REQUEST;
        }
        if (VIEW_SCOPE.equalsIgnoreCase(identifier)) {
            return Scope.VIEW;
        }
        if (SESSION_SCOPE.equalsIgnoreCase(identifier)) {
            return Scope.SESSION;
        }
        if (APPLICATION_SCOPE.equalsIgnoreCase(identifier)) {
            return Scope.APPLICATION;
        }

        // handle implicit objects
        if (INIT_PARAM_IMPLICIT_OBJ.equalsIgnoreCase(identifier)) {
            return Scope.APPLICATION;
        }
        if (COOKIE_IMPLICIT_OBJ.equalsIgnoreCase(identifier)) {
            return Scope.REQUEST;
        }
        if (FACES_CONTEXT_IMPLICIT_OBJ.equalsIgnoreCase(identifier)) {
            return Scope.REQUEST;
        }
        if (HEADER_IMPLICIT_OBJ.equalsIgnoreCase(identifier)) {
            return Scope.REQUEST;
        }
        if (HEADER_VALUES_IMPLICIT_OBJ.equalsIgnoreCase(identifier)) {
            return Scope.REQUEST;
        }
        if (PARAM_IMPLICIT_OBJ.equalsIgnoreCase(identifier)) {
            return Scope.REQUEST;
        }
        if (PARAM_VALUES_IMPLICIT_OBJ.equalsIgnoreCase(identifier)) {
            return Scope.REQUEST;
        }
        if (VIEW_IMPLICIT_OBJ.equalsIgnoreCase(identifier)) {
            return Scope.REQUEST;
        }

        Map<String,Object> requestMap = ec.getRequestMap();
        if (requestMap != null && requestMap.containsKey(identifier)) {
            return Scope.REQUEST;
        }

        UIViewRoot root = context.getViewRoot();
        if (root != null) {
            Map<String, Object> viewMap = root.getViewMap(false);
            if (viewMap != null && viewMap.containsKey(identifier)) {
                return Scope.VIEW;
            }
        }

        Map<String,Object> sessionMap = ec.getSessionMap();
        if (sessionMap != null && sessionMap.containsKey(identifier)) {
            return Scope.SESSION;
        }

        Map<String,Object> appMap = ec.getApplicationMap();
        if (appMap != null && appMap.containsKey(identifier)) {
            return Scope.APPLICATION;
        }
       
        //not present in any scope
        return null;

    }


    /**
     * Create a <code>ValueExpression</code> with the expected type of
     * <code>Object.class</code>
     * @param expression an EL expression
     * @return a new <code>ValueExpression</code> instance based off the
     *  provided <code>valueRef</code>
     */
    public static ValueExpression createValueExpression(String expression) {

       return createValueExpression(expression, Object.class);

    }


    public static ValueExpression createValueExpression(String expression,
                                                        Class<?> expectedType) {
        FacesContext context = FacesContext.getCurrentInstance();
        return context.getApplication().getExpressionFactory().
            createValueExpression(context.getELContext(),
                                  expression,
                                  expectedType);
    }


    public static Object coerce(Object value, Class<?> toType) {

        FacesContext context = FacesContext.getCurrentInstance();
        return context.getApplication().getExpressionFactory().coerceToType(value, toType);

    }


    public static Scope getScope(String scope) {

        for (Scope s : Scope.values()) {
            if (s.toString().equals(scope)) {
                return s;
            }
        }
        return null;
        
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
    @SuppressWarnings("deprecation")
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
    @SuppressWarnings("deprecation")
    private static void addVariableResolvers(FacesCompositeELResolver target,
                                             FacesCompositeELResolver.ELResolverChainType chainType,
                                             ApplicationAssociate associate) {

        VariableResolver vr = getDelegateVR(associate, true);
        if (vr != null) {
            VariableResolverChainWrapper vrChainWrapper = new VariableResolverChainWrapper(vr);
            target.addRootELResolver(vrChainWrapper);
            if (chainType == FacesCompositeELResolver.ELResolverChainType.JSP) {
                associate.setLegacyVRChainHeadWrapperForJsp(vrChainWrapper);
            } else {
                associate.setLegacyVRChainHeadWrapperForFaces(vrChainWrapper);
            }
        }

    }


    /**
     * <p/>
     * The the first segment of a String tokenized by a "." or "["
     *
     * @param valueBinding the expression from which the first segment
     *  will be obtained
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


    public static Scope getScopeForExpression(String expression) {

        if (SharedUtils.isMixedExpression(expression)) {
            return (getNarrowestScopeFromExpression(expression));
        } else {
            return (getScopeForSingleExpression(expression));
        }

    }


    @SuppressWarnings("deprecation")
    public static boolean hasValidLifespan(Scope expressionScope, Scope beanScope)
         throws EvaluationException {

        //if the managed bean's scope is "none" but the scope of the
        //referenced object is not "none", scope is invalid
        if (beanScope == Scope.NONE) {
            return expressionScope == Scope.NONE;
        }

        //if the managed bean's scope is "request" it is able to refer
        //to objects in any scope
        if (beanScope == Scope.REQUEST) {
            return true;
        }
        
        //if the managed bean's scope is "view" it is able to refer to 
        //objects in other "view", "session", "application" or "none" scopes.
        if (beanScope == Scope.VIEW) {
            return expressionScope != Scope.REQUEST;
        }

        //if the managed bean's scope is "session" it is able to refer
        //to objects in other "session", "application", or "none" scopes
        if (beanScope == Scope.SESSION) {
            return !(expressionScope == Scope.REQUEST
                     || expressionScope == Scope.VIEW);
        }

        //if the managed bean's scope is "application" it is able to refer
        //to objects in other "application", or "none" scopes
        if (beanScope == Scope.APPLICATION) {
            return !(expressionScope == Scope.REQUEST
                     || expressionScope == Scope.VIEW
                     || expressionScope == Scope.SESSION);
        }

        //the managed bean is required to be in either "request", "view",
        // "session", "application", or "none" scopes. One of the previous decision
        //statements must be true.
        //noinspection ConstantConditions
        assert (false);
        return false;
    }


    @SuppressWarnings("deprecation")
    public static ELUtils.Scope getScopeForSingleExpression(String value)
         throws EvaluationException {
        String[] firstSegment = new String[1];
        ELUtils.Scope valueScope = ELUtils.getScope(value, firstSegment);

        if (null == valueScope) {
            // Perhaps the bean hasn't been created yet.  See what its
            // scope would be when it is created.
            if (firstSegment[0] != null) {
                BeanManager manager =
                     ApplicationAssociate.getCurrentInstance().getBeanManager();

                if (manager.isManaged(firstSegment[0])) {
                    valueScope = ELUtils.getScope(manager.getBuilder(firstSegment[0]).getScope());
                }
            } else {
                // we are referring to a bean that doesn't exist in the
                // configuration file.  Give it a wide scope...
                valueScope = Scope.APPLICATION;
            }
        }
        return valueScope;
    }


    @SuppressWarnings("deprecation")
    public static Scope getNarrowestScopeFromExpression(String expression)
         throws ReferenceSyntaxException {
        // break the argument expression up into its component
        // expressions, ignoring literals.
        List<String> expressions = ELUtils.getExpressionsFromString(expression);

        int shortestScope = Scope.NONE.ordinal();
        Scope result = Scope.NONE;
        for (String expr : expressions) {
        // loop over the expressions

            Scope lScope = getScopeForSingleExpression(expr);
            // don't consider none
            if (null == lScope || lScope == Scope.NONE) {
                continue;
            }

            int currentScope = lScope.ordinal();

            // if we have no basis for comparison
            if (Scope.NONE.ordinal() == shortestScope) {
                shortestScope = currentScope;
                result = lScope;
            } else {
                // we have a basis for comparison
                if (currentScope < shortestScope) {
                    shortestScope = currentScope;
                    result = lScope;
                }
            }
        }
        return result;
    }


    public static boolean isScopeValid(String scopeName) {
        if (scopeName == null) {
            return false;
        }
        for (Scope scope : Scope.values()) {
            if (scopeName.equals(scope.toString())) {
                return true;
            }
        }
        return false;
    }
    
    /*
     * First look in the ApplicationAssociate.  If that fails, try
     * the Jsp engine.  If that fails, return null;
    
    */
    public static ExpressionFactory getDefaultExpressionFactory(FacesContext facesContext) {
        ExpressionFactory result;
        if (null == facesContext) {
            return null;
        }
        ExternalContext extContext = facesContext.getExternalContext();
        if (null == extContext) {
            return null;
        }
        
        ApplicationAssociate associate = ApplicationAssociate.getInstance(extContext);
        result = getDefaultExpressionFactory(associate, facesContext);
        
        return result;
    }

    public static ExpressionFactory getDefaultExpressionFactory(ApplicationAssociate associate, FacesContext facesContext) {
        ExpressionFactory result = null;
        
        if (null != associate) {
            result = associate.getExpressionFactory();
        }
        
        if (null == result) {
            if (null == facesContext) {
                return null;
            }
            ExternalContext extContext = facesContext.getExternalContext();
            if (null == extContext) {
                return null;
            }
            
            Object servletContext = extContext.getContext();
            if (null != servletContext) {
                if (servletContext instanceof ServletContext) {
                    ServletContext sc = (ServletContext) servletContext;
                    JspApplicationContext jspAppContext = JspFactory.getDefaultFactory().getJspApplicationContext(sc);
                    if (null != jspAppContext) {
                        result = jspAppContext.getExpressionFactory();
                    }
                }
            }
        }
        
        return result;
    }
}
