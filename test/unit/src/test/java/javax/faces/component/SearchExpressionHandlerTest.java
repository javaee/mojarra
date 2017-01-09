/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2014 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.java.net/public/CDDL+GPL_1_1.html
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
package javax.faces.component;

import com.sun.faces.component.search.CompositeSearchKeywordResolver;
import com.sun.faces.component.search.SearchExpressionContextFactoryImpl;
import com.sun.faces.component.search.SearchExpressionHandlerImpl;
import com.sun.faces.component.search.SearchKeywordResolverImplAll;
import com.sun.faces.component.search.SearchKeywordResolverImplChild;
import com.sun.faces.component.search.SearchKeywordResolverImplComposite;
import com.sun.faces.component.search.SearchKeywordResolverImplForm;
import com.sun.faces.component.search.SearchKeywordResolverImplId;
import com.sun.faces.component.search.SearchKeywordResolverImplNamingContainer;
import com.sun.faces.component.search.SearchKeywordResolverImplNext;
import com.sun.faces.component.search.SearchKeywordResolverImplNone;
import com.sun.faces.component.search.SearchKeywordResolverImplParent;
import com.sun.faces.component.search.SearchKeywordResolverImplPrevious;
import com.sun.faces.component.search.SearchKeywordResolverImplRoot;
import com.sun.faces.component.search.SearchKeywordResolverImplThis;
import com.sun.faces.component.visit.VisitContextFactoryImpl;
import com.sun.faces.junit.JUnitFacesTestCaseBase;
import com.sun.faces.mock.MockApplication;
import com.sun.faces.mock.MockRenderKit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.faces.FactoryFinder;
import javax.faces.component.search.ComponentNotFoundException;
import javax.faces.component.search.SearchExpressionContext;
import javax.faces.component.search.SearchExpressionHandler;
import javax.faces.component.search.SearchExpressionHint;
import javax.faces.component.search.SearchKeywordContext;
import javax.faces.component.search.SearchKeywordResolver;
import javax.faces.context.FacesContext;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;
import org.junit.Assert;

public class SearchExpressionHandlerTest extends JUnitFacesTestCaseBase {

    public SearchExpressionHandlerTest(String name) {
        super(name);
    }
    
    @Override
    public void setUp() throws Exception {
        super.setUp();

        UIViewRoot root = facesContext.getApplication().getViewHandler().createView(facesContext, null);
        root.setViewId("/viewId");
        facesContext.setViewRoot(root);
        
        RenderKitFactory renderKitFactory = (RenderKitFactory) FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
        RenderKit renderKit = new MockRenderKit();
        try {
            renderKitFactory.addRenderKit(RenderKitFactory.HTML_BASIC_RENDER_KIT,
                    renderKit);
        } catch (IllegalArgumentException e) {
        }
        
        FactoryFinder.setFactory(FactoryFinder.SEARCH_EXPRESSION_CONTEXT_FACTORY,
                SearchExpressionContextFactoryImpl.class.getName());
        FactoryFinder.setFactory(FactoryFinder.VISIT_CONTEXT_FACTORY,
                VisitContextFactoryImpl.class.getName());
        
        SearchExpressionHandlerImpl expressionHandlerImpl = new SearchExpressionHandlerImpl();
        ((MockApplication) application).setSearchExpressionHandler(expressionHandlerImpl);
        
        CompositeSearchKeywordResolver searchKeywordResolvers = new CompositeSearchKeywordResolver();
        searchKeywordResolvers.add(new SearchKeywordResolverImplThis());
        searchKeywordResolvers.add(new SearchKeywordResolverImplParent());
        searchKeywordResolvers.add(new SearchKeywordResolverImplForm());
        searchKeywordResolvers.add(new SearchKeywordResolverImplComposite());
        searchKeywordResolvers.add(new SearchKeywordResolverImplNext());
        searchKeywordResolvers.add(new SearchKeywordResolverImplPrevious());
        searchKeywordResolvers.add(new SearchKeywordResolverImplNone());
        searchKeywordResolvers.add(new SearchKeywordResolverImplNamingContainer());
        searchKeywordResolvers.add(new SearchKeywordResolverImplRoot());
        searchKeywordResolvers.add(new SearchKeywordResolverImplId());
        searchKeywordResolvers.add(new SearchKeywordResolverImplChild());
        searchKeywordResolvers.add(new SearchKeywordResolverImplAll());
        application.setSearchKeywordResolver(searchKeywordResolvers);
    }
    
    @Override
    public void tearDown() throws Exception {
        super.tearDown();
    }

	private UIComponent resolveComponent(UIComponent source, String expression, SearchExpressionHint... hints) {
		
        SearchExpressionContext searchContext = SearchExpressionContext.createSearchExpressionContext(facesContext, source, new HashSet<>(Arrays.asList(hints)), null);

        ResolveComponentCallback callback = new ResolveComponentCallback();
        
        SearchExpressionHandler handler = FacesContext.getCurrentInstance().getApplication().getSearchExpressionHandler();
        
        handler.resolveComponent(searchContext, expression, callback);

		return callback.component;
	}

    private static class ResolveComponentCallback implements ContextCallback {
        public UIComponent component;

        @Override
        public void invokeContextCallback(FacesContext context, UIComponent target) {
            component = target;
        }
    }
    
    private String resolveClientId(UIComponent source, String expression) {
        SearchExpressionContext searchContext = SearchExpressionContext.createSearchExpressionContext(facesContext, source);
        SearchExpressionHandler handler = FacesContext.getCurrentInstance().getApplication().getSearchExpressionHandler();
        
        return handler.resolveClientId(searchContext, expression);
    }
    
    private List<UIComponent> resolveComponents(UIComponent source, String expression) {
        SearchExpressionContext searchContext = SearchExpressionContext.createSearchExpressionContext(facesContext, source);
        SearchExpressionHandler handler = FacesContext.getCurrentInstance().getApplication().getSearchExpressionHandler();

        ResolveComponentsCallback callback = new ResolveComponentsCallback();
        
        handler.resolveComponents(searchContext, expression, callback);
        
        return callback.components;
    }
    
    private static class ResolveComponentsCallback implements ContextCallback {
        public List<UIComponent> components = new ArrayList<>();

        @Override
        public void invokeContextCallback(FacesContext context, UIComponent target) {
            components.add(target);
        }
    }
    
    private String resolveClientIds(UIComponent source, String expressions, SearchExpressionHint... hints) {
                
        SearchExpressionContext searchContext = SearchExpressionContext.createSearchExpressionContext(facesContext, source, new HashSet<>(Arrays.asList(hints)), null);
        SearchExpressionHandler handler = FacesContext.getCurrentInstance().getApplication().getSearchExpressionHandler();

        List<String> clientIds = handler.resolveClientIds(searchContext, expressions);
        
        return String.join(" ", clientIds);
    }
    
	public void test_ResolveComponent_Parent() {

		UIComponent root = new UIPanel();

		UIForm form = new UIForm();
		root.getChildren().add(form);

		UINamingContainer outerContainer = new UINamingContainer();
		form.getChildren().add(outerContainer);

		UINamingContainer innerContainer = new UINamingContainer();
		outerContainer.getChildren().add(innerContainer);

		UIComponent component = new UIOutput();
		innerContainer.getChildren().add(component);

		UIComponent source = new UICommand();
		innerContainer.getChildren().add(source);

		assertSame("Failed", innerContainer, resolveComponent(source, "@parent"));
	}

	public void test_ResolveComponent_ParentParent() {

		UIComponent root = new UIPanel();

		UIForm form = new UIForm();
		root.getChildren().add(form);

		UINamingContainer outerContainer = new UINamingContainer();
		form.getChildren().add(outerContainer);

		UINamingContainer innerContainer = new UINamingContainer();
		outerContainer.getChildren().add(innerContainer);

		UIComponent component = new UIOutput();
		innerContainer.getChildren().add(component);

		UIComponent source = new UICommand();
		innerContainer.getChildren().add(source);

		assertSame("Failed", outerContainer, resolveComponent(source, "@parent:@parent"));
	}
    
	public void test_ResolveComponent_Form() {

		UIComponent root = new UIPanel();

		UIForm form = new UIForm();
		root.getChildren().add(form);

		UINamingContainer outerContainer = new UINamingContainer();
		form.getChildren().add(outerContainer);

		UINamingContainer innerContainer = new UINamingContainer();
		outerContainer.getChildren().add(innerContainer);

		UIComponent component = new UIOutput();
		innerContainer.getChildren().add(component);

		UIComponent source = new UICommand();
		innerContainer.getChildren().add(source);

		assertSame("Failed", form, resolveComponent(source, "@form"));
	}

	public void test_ResolveComponent_FormParent() {

		UIComponent root = new UIPanel();

		UIForm form = new UIForm();
		root.getChildren().add(form);

		UINamingContainer outerContainer = new UINamingContainer();
		form.getChildren().add(outerContainer);

		UINamingContainer innerContainer = new UINamingContainer();
		outerContainer.getChildren().add(innerContainer);

		UIComponent component = new UIOutput();
		innerContainer.getChildren().add(component);

		UIComponent source = new UICommand();
		innerContainer.getChildren().add(source);

		assertSame("Failed", root, resolveComponent(source, "@form:@parent"));
	}
    
	public void test_ResolveComponent_All() {

		UIComponent root = new UIPanel();

		UIForm form = new UIForm();
		root.getChildren().add(form);

		UINamingContainer outerContainer = new UINamingContainer();
		form.getChildren().add(outerContainer);

		UINamingContainer innerContainer = new UINamingContainer();
		outerContainer.getChildren().add(innerContainer);

		UIComponent component = new UIOutput();
		innerContainer.getChildren().add(component);

		UIComponent source = new UICommand();
		innerContainer.getChildren().add(source);

		assertSame("Failed", root, resolveComponent(source, "@all"));
	}
    
	public void test_ResolveComponent_This() {

		UIComponent root = new UIPanel();

		UIForm form = new UIForm();
		root.getChildren().add(form);

		UINamingContainer outerContainer = new UINamingContainer();
		form.getChildren().add(outerContainer);

		UINamingContainer innerContainer = new UINamingContainer();
		outerContainer.getChildren().add(innerContainer);

		UIComponent component = new UIOutput();
		innerContainer.getChildren().add(component);

		UIComponent source = new UICommand();
		innerContainer.getChildren().add(source);

		assertSame("Failed", source, resolveComponent(source, "@this"));
	}

	public void test_ResolveComponent_ThisParent() {

		UIComponent root = new UIPanel();

		UIForm form = new UIForm();
		root.getChildren().add(form);

		UINamingContainer outerContainer = new UINamingContainer();
		form.getChildren().add(outerContainer);

		UINamingContainer innerContainer = new UINamingContainer();
		outerContainer.getChildren().add(innerContainer);

		UIComponent component = new UIOutput();
		innerContainer.getChildren().add(component);

		UIComponent source = new UICommand();
		innerContainer.getChildren().add(source);

		assertSame("Failed", innerContainer, resolveComponent(source, "@this:@parent"));
	}

	public void test_ResolveComponent_Namingcontainer() {

		UIComponent root = new UIPanel();

		UIForm form = new UIForm();
		root.getChildren().add(form);

		UINamingContainer outerContainer = new UINamingContainer();
		form.getChildren().add(outerContainer);

		UINamingContainer innerContainer = new UINamingContainer();
		outerContainer.getChildren().add(innerContainer);

		UIComponent component = new UIOutput();
		innerContainer.getChildren().add(component);

		UIComponent source = new UICommand();
		innerContainer.getChildren().add(source);

		assertSame("Failed", innerContainer, resolveComponent(source, "@namingcontainer"));
	}

	public void test_ResolveComponent_Absolute() {

		UIComponent root = new UIPanel();
        FacesContext.getCurrentInstance().getViewRoot().getChildren().add(root);

		UIForm form = new UIForm();
		form.setId("form");
		root.getChildren().add(form);

		UINamingContainer outerContainer = new UINamingContainer();
		outerContainer.setId("outerContainer");
		form.getChildren().add(outerContainer);

		UINamingContainer innerContainer = new UINamingContainer();
		innerContainer.setId("innerContainer");
		outerContainer.getChildren().add(innerContainer);

		UIComponent component = new UIOutput();
		innerContainer.getChildren().add(component);

		UIComponent source = new UICommand();
		source.setId("source");
		innerContainer.getChildren().add(source);

		assertSame("Failed", source, resolveComponent(source, " :form:outerContainer:innerContainer:source "));
	}
    
	public void test_ResolveComponent_Relative() {

		UIComponent root = new UIPanel();

		UIForm form = new UIForm();
		form.setId("form");
		root.getChildren().add(form);

		UINamingContainer outerContainer = new UINamingContainer();
		outerContainer.setId("outerContainer");
		form.getChildren().add(outerContainer);

		UINamingContainer innerContainer = new UINamingContainer();
		innerContainer.setId("innerContainer");
		outerContainer.getChildren().add(innerContainer);

		UIComponent component = new UIOutput();
		component.setId("other");
		innerContainer.getChildren().add(component);

		UIComponent source = new UICommand();
		source.setId("source");
		innerContainer.getChildren().add(source);

		assertSame("Failed", component, resolveComponent(source, " other "));
	}
    
	public void test_ResolveComponent_AbsoluteForm() {

		UIComponent root = new UIPanel();
		root.setId("root");

		UIForm form = new UIForm();
		form.setId("form");
		root.getChildren().add(form);

		UINamingContainer outerContainer = new UINamingContainer();
		outerContainer.setId("outerContainer");
		form.getChildren().add(outerContainer);

		UINamingContainer innerContainer = new UINamingContainer();
		innerContainer.setId("innerContainer");
		outerContainer.getChildren().add(innerContainer);

		UIComponent component = new UIOutput();
		component.setId("other");
		innerContainer.getChildren().add(component);

		UIComponent source = new UICommand();
		source.setId("source");
		innerContainer.getChildren().add(source);

		assertSame("Failed", root, resolveComponent(source, " :form:@parent "));
	}
    
	public void test_ResolveComponent_ParentChild() {

	    UIComponent root = new UIPanel();
	    root.setId("root");

	    UIForm form = new UIForm();
	    form.setId("form");
	    root.getChildren().add(form);

	    UINamingContainer outerContainer = new UINamingContainer();
	    outerContainer.setId("outerContainer");
	    form.getChildren().add(outerContainer);

	    UINamingContainer innerContainer = new UINamingContainer();
	    innerContainer.setId("innerContainer");
	    outerContainer.getChildren().add(innerContainer);

	    UIComponent component = new UIOutput();
	    component.setId("other");
	    innerContainer.getChildren().add(component);

	    UIComponent source = new UICommand();
	    source.setId("source");
	    innerContainer.getChildren().add(source);

	    assertSame("Failed", component, resolveComponent(source, " @parent:@child(0) "));
	    assertSame("Failed", source, resolveComponent(source, " @parent:@child(1) "));
	}
    
	public void test_ResolveComponent_AbsoluteNamingcontainer() {

		UIComponent root = new UIPanel();
		root.setId("root");

		UIForm form = new UIForm();
		form.setId("form");
		root.getChildren().add(form);

		UINamingContainer outerContainer = new UINamingContainer();
		outerContainer.setId("outerContainer");
		form.getChildren().add(outerContainer);

		UINamingContainer innerContainer = new UINamingContainer();
		innerContainer.setId("innerContainer");
		outerContainer.getChildren().add(innerContainer);

		UIComponent component = new UIOutput();
		component.setId("other");
		innerContainer.getChildren().add(component);

		UIComponent source = new UICommand();
		source.setId("source");
		innerContainer.getChildren().add(source);

		assertSame("Failed", form, resolveComponent(source, " :form:outerContainer:@namingcontainer "));
	}
    
	public void test_ResolveClientId_This() {

		UIComponent root = new UIPanel();

		UIForm form = new UIForm();
		form.setId("form");
		root.getChildren().add(form);

		UINamingContainer outerContainer = new UINamingContainer();
		outerContainer.setId("outerContainer");
		form.getChildren().add(outerContainer);

		UINamingContainer innerContainer = new UINamingContainer();
		innerContainer.setId("innerContainer");
		outerContainer.getChildren().add(innerContainer);

		UIComponent component = new UIOutput();
		innerContainer.getChildren().add(component);

		UIComponent source = new UICommand();
		source.setId("source");
		innerContainer.getChildren().add(source);

		assertEquals("Failed", "form:outerContainer:innerContainer:source", resolveClientId(source, " @this "));
	}
    
	public void test_ResolveClientId_Form() {

		UIComponent root = new UIPanel();

		UIForm form = new UIForm();
		form.setId("form");
		root.getChildren().add(form);

		UINamingContainer outerContainer = new UINamingContainer();
		outerContainer.setId("outerContainer");
		form.getChildren().add(outerContainer);

		UINamingContainer innerContainer = new UINamingContainer();
		innerContainer.setId("innerContainer");
		outerContainer.getChildren().add(innerContainer);

		UIComponent component = new UIOutput();
		innerContainer.getChildren().add(component);

		UIComponent source = new UICommand();
		source.setId("source");
		innerContainer.getChildren().add(source);

		assertEquals("Failed", "form", resolveClientId(source, " @form "));
	}
    
	public void test_ResolveClientId_AbsoluteId() {

		UIComponent root = new UIPanel();
        FacesContext.getCurrentInstance().getViewRoot().getChildren().add(root);

		UIForm form = new UIForm();
		form.setId("form");
		root.getChildren().add(form);

		UINamingContainer outerContainer = new UINamingContainer();
		outerContainer.setId("outerContainer");
		form.getChildren().add(outerContainer);

		UINamingContainer innerContainer = new UINamingContainer();
		innerContainer.setId("innerContainer");
		outerContainer.getChildren().add(innerContainer);

		UIComponent component = new UIOutput();
		innerContainer.getChildren().add(component);

		UIComponent source = new UICommand();
		innerContainer.getChildren().add(source);

		assertEquals("Failed", "form", resolveClientId(source, " :form "));
	}
    
	public void test_ResolveClientId_Relative() {

		UIComponent root = new UIPanel();

		UIForm form = new UIForm();
		form.setId("form");
		root.getChildren().add(form);

		UINamingContainer outerContainer = new UINamingContainer();
		outerContainer.setId("outerContainer");
		form.getChildren().add(outerContainer);

		UINamingContainer innerContainer = new UINamingContainer();
		innerContainer.setId("innerContainer");
		outerContainer.getChildren().add(innerContainer);

		UIComponent component = new UIOutput();
		component.setId("other");
		innerContainer.getChildren().add(component);

		UIComponent source = new UICommand();
		source.setId("source");
		innerContainer.getChildren().add(source);

		assertEquals("Failed", "form:outerContainer:innerContainer:other", resolveClientId(source, " other "));
	}
    
	public void test_ResolveComponents_RelativeAndParentParent() {

	    UIComponent root = new UIPanel();

	    UIForm form = new UIForm();
	    form.setId("form");
	    root.getChildren().add(form);

	    UINamingContainer outerContainer = new UINamingContainer();
	    outerContainer.setId("outerContainer");
	    form.getChildren().add(outerContainer);

	    UINamingContainer innerContainer = new UINamingContainer();
	    innerContainer.setId("innerContainer");
	    outerContainer.getChildren().add(innerContainer);

	    UIComponent component = new UIOutput();
	    component.setId("other");
	    innerContainer.getChildren().add(component);

	    UIComponent source = new UICommand();
	    source.setId("source");
	    innerContainer.getChildren().add(source);

        List<UIComponent> resolvedComponents = resolveComponents(source, " other @parent:@parent ");
        assertTrue("Failed", resolvedComponents.contains(component));
        assertTrue("Failed", resolvedComponents.contains(outerContainer));
        assertEquals("Failed", 2, resolvedComponents.size());
	}
    
	public void test_ResolveComponents_RelativeAndThisParent() {

	    UIComponent root = new UIPanel();

	    UIForm form = new UIForm();
	    form.setId("form");
	    root.getChildren().add(form);

	    UINamingContainer outerContainer = new UINamingContainer();
	    outerContainer.setId("outerContainer");
	    form.getChildren().add(outerContainer);

	    UINamingContainer innerContainer = new UINamingContainer();
	    innerContainer.setId("innerContainer");
	    outerContainer.getChildren().add(innerContainer);

	    UIComponent component = new UIOutput();
	    component.setId("other");
	    innerContainer.getChildren().add(component);

	    UIComponent source = new UICommand();
	    source.setId("source");
	    innerContainer.getChildren().add(source);

        List<UIComponent> resolvedComponents = resolveComponents(source, " other,@this:@parent  @none ");
        assertTrue("Failed", resolvedComponents.contains(component));
        assertTrue("Failed", resolvedComponents.contains(innerContainer));
	    assertEquals("Failed", 2, resolvedComponents.size());
	}

	public void test_ResolveClientIdsWithParentFallback() {

	    UIComponent root = new UIPanel();
	    root.setId("test");

	    UIForm form = new UIForm();
	    form.setId("form");
	    root.getChildren().add(form);

	    assertEquals(
	    		"test",
	    		resolveClientIds(form, null, SearchExpressionHint.PARENT_FALLBACK));

	    assertEquals(
	    		"test",
	    		resolveClientIds(form, " ", SearchExpressionHint.PARENT_FALLBACK));
	}
    
	public void test_ResolveComponent_Next() {

	    UIComponent root = new UIPanel();
	    root.setId("root");

	    UIComponent command1 = new UICommand();
	    command1.setId("command1");
	    root.getChildren().add(command1);

	    UIComponent command2 = new UICommand();
	    command2.setId("command2");
	    root.getChildren().add(command2);

	    UIComponent command3 = new UICommand();
	    command3.setId("command3");
	    root.getChildren().add(command3);

	    assertSame("Failed", command2, resolveComponent(command1, " @next "));
	    assertSame("Failed", command3, resolveComponent(command2, " @next "));

		try {
			resolveComponent(command3, " @next");
			fail("This should actually raise an exception");
		} catch (Exception e) {
			assertEquals(ComponentNotFoundException.class, e.getClass());
		}
	}
    
	public void test_ResolveComponent_NextNext() {

	    UIComponent root = new UIPanel();
	    root.setId("root");

	    UIComponent command1 = new UICommand();
	    command1.setId("command1");
	    root.getChildren().add(command1);

	    UIComponent command2 = new UICommand();
	    command2.setId("command2");
	    root.getChildren().add(command2);

	    UIComponent command3 = new UICommand();
	    command3.setId("command3");
	    root.getChildren().add(command3);

	    assertSame("Failed", command3, resolveComponent(command1, " @next:@next "));

		try {
			resolveComponent(command2, " @next:@next");
			fail("This should actually raise an exception");
		} catch (Exception e) {
			assertEquals(ComponentNotFoundException.class, e.getClass());
		}


		try {
			resolveComponent(command3, " @next:@next");
			fail("This should actually raise an exception");
		} catch (Exception e) {
			assertEquals(ComponentNotFoundException.class, e.getClass());
		}
	}
    
	public void test_ResolveComponent_Previous() {

	    UIComponent root = new UIPanel();
	    root.setId("root");

	    UIComponent command1 = new UICommand();
	    command1.setId("command1");
	    root.getChildren().add(command1);

	    UIComponent command2 = new UICommand();
	    command2.setId("command2");
	    root.getChildren().add(command2);

	    UIComponent command3 = new UICommand();
	    command3.setId("command3");
	    root.getChildren().add(command3);

	    assertSame("Failed", command1, resolveComponent(command2, " @previous "));
	    assertSame("Failed", command2, resolveComponent(command3, " @previous "));

		try {
			resolveComponent(command1, " @previous");
			fail("This should actually raise an exception");
		} catch (Exception e) {
			assertEquals(ComponentNotFoundException.class, e.getClass());
		}
	}
    
	public void test_ResolveComponent_FormChildNextNext() {

	    UIForm root = new UIForm();
	    root.setId("form");

	    UIComponent command1 = new UICommand();
	    command1.setId("command1");
	    root.getChildren().add(command1);

	    UIComponent command2 = new UICommand();
	    command2.setId("command2");
	    root.getChildren().add(command2);

	    UIComponent command3 = new UICommand();
	    command3.setId("command3");
	    root.getChildren().add(command3);

	    assertSame("Failed", command3, resolveComponent(command1, " @form:@child(0):@next:@next "));
	}
    
	public void test_ResolveComponent_IgnoreNoResult() {
	    UIForm root = new UIForm();
	    root.setId("form");

	    UIComponent command1 = new UICommand();
	    command1.setId("command1");
	    root.getChildren().add(command1);

	    UIComponent command2 = new UICommand();
	    command2.setId("command2");
	    root.getChildren().add(command2);

	    assertSame("Failed", null,
	    		resolveComponent(command1, " command3 ", SearchExpressionHint.IGNORE_NO_RESULT));
	}
    
	public void test_ResolveClientId_AbsoluteWithFormPrependIdFalse() {

		UIComponent root = new UIPanel();
        FacesContext.getCurrentInstance().getViewRoot().getChildren().add(root);

		UIForm form = new UIForm();
		form.setId("form");
        form.setPrependId(false);
		root.getChildren().add(form);

		UINamingContainer outerContainer = new UINamingContainer();
		outerContainer.setId("outerContainer");
		form.getChildren().add(outerContainer);

		UINamingContainer innerContainer = new UINamingContainer();
		innerContainer.setId("innerContainer");
		outerContainer.getChildren().add(innerContainer);

		UIComponent component = new UIOutput();
		innerContainer.getChildren().add(component);

		UIComponent source = new UICommand();
		source.setId("source");
		innerContainer.getChildren().add(source);

		assertEquals("Failed", "outerContainer:innerContainer:source", resolveClientId(source, " :form:outerContainer:innerContainer:source "));
	}
    
	public void test_ResolveClientId_AbsoluteWithFormPrependIdFalse_InvokeOnComponent() {

		UIComponent root = new UIPanel();
        FacesContext.getCurrentInstance().getViewRoot().getChildren().add(root);

		UIForm form = new UIForm();
		form.setId("form");
        form.setPrependId(false);
		root.getChildren().add(form);

		UINamingContainer outerContainer = new UINamingContainer();
		outerContainer.setId("outerContainer");
		form.getChildren().add(outerContainer);

		UINamingContainer innerContainer = new UINamingContainer();
		innerContainer.setId("innerContainer");
		outerContainer.getChildren().add(innerContainer);

		UIComponent component = new UIOutput();
		innerContainer.getChildren().add(component);

		UIComponent source = new UICommand();
		source.setId("source");
		innerContainer.getChildren().add(source);

		assertEquals("Failed", "outerContainer:innerContainer:source", resolveClientId(source, " outerContainer:innerContainer:source "));
	}
    
    public void test_Passthrough() {
        SearchExpressionHandler handler = facesContext.getApplication().getSearchExpressionHandler();
        
        SearchExpressionContext searchExpressionContext = SearchExpressionContext.createSearchExpressionContext(facesContext, null);

        Assert.assertFalse(handler.isPassthroughExpression(searchExpressionContext, "mainForm:showName"));
        Assert.assertFalse(handler.isPassthroughExpression(searchExpressionContext, "mainForm:table:3:nested:1:nestedText"));
        Assert.assertFalse(handler.isPassthroughExpression(searchExpressionContext, "mainForm:table:3:baseText"));
        Assert.assertFalse(handler.isPassthroughExpression(searchExpressionContext, "mainForm:table:0:baseText"));
        Assert.assertFalse(handler.isPassthroughExpression(searchExpressionContext, "mainForm:table:3:nested:0:nestedText"));
        Assert.assertFalse(handler.isPassthroughExpression(searchExpressionContext, "mainForm:table:3:nested"));
        Assert.assertFalse(handler.isPassthroughExpression(searchExpressionContext, "mainForm:table:1:nested:0:nestedText"));
        
        Assert.assertFalse(handler.isPassthroughExpression(searchExpressionContext, "@this"));
        Assert.assertFalse(handler.isPassthroughExpression(searchExpressionContext, "@this:@parent:showName"));
        Assert.assertFalse(handler.isPassthroughExpression(searchExpressionContext, "@parent:showName:@parent:showName"));
        Assert.assertFalse(handler.isPassthroughExpression(searchExpressionContext, "@form"));
        Assert.assertFalse(handler.isPassthroughExpression(searchExpressionContext, "@form:showName"));
        Assert.assertFalse(handler.isPassthroughExpression(searchExpressionContext, "@namingcontainer:showName"));
        Assert.assertFalse(handler.isPassthroughExpression(searchExpressionContext, "@previous"));
        Assert.assertFalse(handler.isPassthroughExpression(searchExpressionContext, "@next"));
        Assert.assertFalse(handler.isPassthroughExpression(searchExpressionContext, "@parent:@id(msgName)"));
        
        Assert.assertFalse(handler.isPassthroughExpression(searchExpressionContext, "@whoNows"));
        Assert.assertFalse(handler.isPassthroughExpression(searchExpressionContext, "@parent:@whoNows"));
        Assert.assertFalse(handler.isPassthroughExpression(searchExpressionContext, "mainForm:@whoNows"));
        Assert.assertFalse(handler.isPassthroughExpression(searchExpressionContext, "!whoNows"));
        
        
        Set<SearchExpressionHint> hints = new HashSet<>();
        hints.add(SearchExpressionHint.RESOLVE_CLIENT_SIDE);
        searchExpressionContext = SearchExpressionContext.createSearchExpressionContext(facesContext, null, hints, null);
        
        Assert.assertTrue(handler.isPassthroughExpression(searchExpressionContext, "@form"));
        Assert.assertFalse(handler.isPassthroughExpression(searchExpressionContext, "@form:showName"));
        Assert.assertFalse(handler.isPassthroughExpression(searchExpressionContext, "@form:@child(0)"));
    }
    
    public void test_Valid() {
        SearchExpressionHandler handler = facesContext.getApplication().getSearchExpressionHandler();
        
        SearchExpressionContext searchExpressionContext =  SearchExpressionContext.createSearchExpressionContext(facesContext, null);

        Assert.assertTrue(handler.isValidExpression(searchExpressionContext, "mainForm:showName"));
        Assert.assertTrue(handler.isValidExpression(searchExpressionContext, "mainForm:table:3:nested:1:nestedText"));
        Assert.assertTrue(handler.isValidExpression(searchExpressionContext, "mainForm:table:3:baseText"));
        Assert.assertTrue(handler.isValidExpression(searchExpressionContext, "mainForm:table:0:baseText"));
        Assert.assertTrue(handler.isValidExpression(searchExpressionContext, "mainForm:table:3:nested:0:nestedText"));
        Assert.assertTrue(handler.isValidExpression(searchExpressionContext, "mainForm:table:3:nested"));
        Assert.assertTrue(handler.isValidExpression(searchExpressionContext, "mainForm:table:1:nested:0:nestedText"));
        
        Assert.assertTrue(handler.isValidExpression(searchExpressionContext, "@this"));
        Assert.assertTrue(handler.isValidExpression(searchExpressionContext, "@this:@parent:showName"));
        Assert.assertTrue(handler.isValidExpression(searchExpressionContext, "@parent:showName:@parent:showName"));
        Assert.assertTrue(handler.isValidExpression(searchExpressionContext, "@form:showName"));
        Assert.assertTrue(handler.isValidExpression(searchExpressionContext, "@namingcontainer:showName"));
        Assert.assertTrue(handler.isValidExpression(searchExpressionContext, "@previous"));
        Assert.assertTrue(handler.isValidExpression(searchExpressionContext, "@next"));
        Assert.assertTrue(handler.isValidExpression(searchExpressionContext, "@parent:@id(msgName)"));
        
        Assert.assertFalse(handler.isValidExpression(searchExpressionContext, "@whoNows"));
        Assert.assertFalse(handler.isValidExpression(searchExpressionContext, "@parent:@whoNows"));
        Assert.assertFalse(handler.isValidExpression(searchExpressionContext, "mainForm:@whoNows"));
        
        Assert.assertFalse(handler.isValidExpression(searchExpressionContext, "@none:@parent"));
        Assert.assertFalse(handler.isValidExpression(searchExpressionContext, "@all:@parent"));
    }
    
    public void test_ResolveComponents_Id() {
		UIComponent root = new UIPanel();
        FacesContext.getCurrentInstance().getViewRoot().getChildren().add(root);

		UINamingContainer outerContainer = new UINamingContainer();
		outerContainer.setId("myContainer");
		root.getChildren().add(outerContainer);

		UIForm form = new UIForm();
		form.setId("form");
		root.getChildren().add(form);

		UINamingContainer innerContainer = new UINamingContainer();
		innerContainer.setId("myContainer");
		form.getChildren().add(innerContainer);

		UINamingContainer innerContainer2 = new UINamingContainer();
		innerContainer2.setId("myContainer2");
		form.getChildren().add(innerContainer2);

        List<UIComponent> result = resolveComponents(form, " @id(myContainer) ");
        assertTrue(result.size() == 1);
        assertTrue(result.contains(innerContainer));
    }
    
    /**
     * The SearchExpression API was inspired by PrimeFaces.
     * This test only tests, if PFS (PrimeFaces Selectors -> jQuery like selectors; like @(#myId > .myStyle))
     * can be correctly handled by the API+IMPL as a passthrough expression.
     */
    public void test_PFS() {
        CompositeSearchKeywordResolver s = (CompositeSearchKeywordResolver) application.getSearchKeywordResolver();
        s.add(new SearchKeywordResolver() {

            @Override
            public void resolve(SearchKeywordContext searchKeywordContext, UIComponent previous, String keyword) {
                
            }

            @Override
            public boolean isResolverForKeyword(SearchExpressionContext searchExpressionContext, String keyword) {
                return keyword.startsWith("(") && keyword.endsWith(")");
            }
            
            @Override
            public boolean isPassthrough(SearchExpressionContext searchExpressionContext, String keyword) {
                return true;
            }
            
            @Override
            public boolean isLeaf(SearchExpressionContext searchExpressionContext, String keyword) {
                return true;
            }
        });
        
		UIComponent root = new UIPanel();
        FacesContext.getCurrentInstance().getViewRoot().getChildren().add(root);

        assertEquals("@(.myPanel #id)", resolveClientId(root, " @(.myPanel #id)"));
        
        SearchExpressionHandler handler = facesContext.getApplication().getSearchExpressionHandler();
        SearchExpressionContext searchExpressionContext =  SearchExpressionContext.createSearchExpressionContext(facesContext, null);
        Assert.assertTrue(handler.isValidExpression(searchExpressionContext, "@(.myPanel #id)"));
        Assert.assertFalse(handler.isValidExpression(searchExpressionContext, "@(.myPanel #id):test"));
    }
}
