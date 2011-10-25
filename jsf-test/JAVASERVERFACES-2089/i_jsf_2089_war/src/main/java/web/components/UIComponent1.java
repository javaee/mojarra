/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2011 Oracle and/or its affiliates. All rights reserved.
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

package web.components;

import java.util.Map;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.faces.component.FacesComponent;
import javax.faces.component.NamingContainer;
import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlInputText;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.event.ComponentSystemEventListener;
import javax.faces.event.PreRenderComponentEvent;

@FacesComponent("web.components.component1")
public class UIComponent1 extends ComponentBase implements NamingContainer, ComponentSystemEventListener {

	public static final String COMPONENT_FAMILY = "javax.faces.NamingContainer";
	
	public UIComponent1() {
		
		super();
		
		FacesContext ctx = FacesContext.getCurrentInstance();
                this.subscribeToEvent(PreRenderComponentEvent.class, this);
	}        
	
	@Override
	public String getFamily() {
		return(COMPONENT_FAMILY);
	}

	@Override
	public void processEvent(ComponentSystemEvent e) throws AbortProcessingException {

		if(e instanceof PreRenderComponentEvent)
			this.processPreRenderViewEvent((PreRenderComponentEvent)e);
	}
	
	private void processPreRenderViewEvent(PreRenderComponentEvent e) throws AbortProcessingException  {
		
		FacesContext ctx = FacesContext.getCurrentInstance();
		
		if(!ctx.isPostback()) {
			
			UIComponent parent = this.findComponent("controls");
			ExpressionFactory ef = ctx.getApplication().getExpressionFactory();
			
			HtmlOutputText itemCheck = new HtmlOutputText();
                        Map<String, Object> attrs = this.getAttributes();
                        Object item = attrs.get("item");
                        boolean itemIsNull = (item==null);
			itemCheck.setValue("Item Attribute is null: " + itemIsNull);
				
			HtmlInputText txt =  new HtmlInputText();
			txt.setId("txt");
			
			ValueExpression ve = ef.createValueExpression(ctx.getELContext(), "#{cc.attrs.item.text}", java.lang.String.class);
			txt.setValueExpression("value", ve);			
		
			parent.getChildren().add(txt);
			parent.getChildren().add(itemCheck);
		}
	}

}
