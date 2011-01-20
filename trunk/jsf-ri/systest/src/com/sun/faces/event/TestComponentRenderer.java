package com.sun.faces.event;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.render.FacesRenderer;
import javax.faces.render.Renderer;

@FacesRenderer( componentFamily = "com.sun.faces.event", rendererType = "testcomponent" )
public class TestComponentRenderer
	extends Renderer {

	//
	// Public methods
	//

	@Override
	public void encodeBegin( FacesContext context, UIComponent component )
		throws IOException {

		context.getResponseWriter().write( "<div style=\"border: 1px solid red; margin: 2px\"><div style=\"background-color: #ffc0c0; padding: 2px; margin-bottom: 5px; display:block\">TestComponent::encodeBegin</div>" );

		super.encodeBegin( context, component );
	}

	@Override
	public void encodeEnd( FacesContext context, UIComponent component )
		throws IOException {

		super.encodeEnd( context, component );

		context.getResponseWriter().write( "<div style=\"background-color: #ffc0c0; padding: 2px; margin-top: 5px; display:block\">TestComponent::encodeEnd</div></div>" );
	}
}
