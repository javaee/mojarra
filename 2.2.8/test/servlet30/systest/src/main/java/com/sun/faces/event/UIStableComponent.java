package com.sun.faces.event;

import javax.faces.component.FacesComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.component.UIViewRoot;
import javax.faces.component.html.HtmlInputText;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.PreRenderViewEvent;
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;

@FacesComponent( value = "com.sun.faces.event.UIStableComponent" )
public class UIStableComponent
	extends UIComponentBase
	implements SystemEventListener {

	//
	// Constructor
	//

	public UIStableComponent() {

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

		if ( FacesContext.getCurrentInstance().getMaximumSeverity() != null ) {
			return;
		}

		HtmlInputText inputText1 = new HtmlInputText();
		inputText1.setValue( "1" );
		getChildren().add( inputText1 );

		HtmlInputText inputText2 = new HtmlInputText();
		inputText2.setValue( "2" );
		getChildren().add( inputText2 );

		HtmlInputText inputText3 = new HtmlInputText();
		inputText3.setId( "text3" );
		inputText3.setRequired( true );
		getChildren().add( inputText3 );
	}
}
