package com.sun.faces.event;

import javax.faces.component.FacesComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.component.UIViewRoot;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.PreRenderViewEvent;
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;

@FacesComponent( value = "com.sun.faces.event.UIAddComponent" )
public class UIAddComponent
	extends UIComponentBase
	implements SystemEventListener {

	//
	// Constructor
	//

	public UIAddComponent() {

		setRendererType( "testcomponent" );

		FacesContext context = FacesContext.getCurrentInstance();
		UIViewRoot root = context.getViewRoot();

		root.subscribeToViewEvent( PreRenderViewEvent.class, this );
	}

	//
	// Public methods
	//

	@Override
	public String getFamily() {

		return "com.sun.faces.event";
	}

	public boolean isListenerForSource( Object source ) {

		return ( source instanceof UIViewRoot );
	}

	@Override
	public void processEvent( SystemEvent event )
		throws AbortProcessingException {

		if ( !FacesContext.getCurrentInstance().isPostback() ) {

			HtmlOutputText component = new HtmlOutputText();
			component.setValue( "Dynamically added child" );
			getChildren().add( component );
		}
	}
}
