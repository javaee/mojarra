package com.sun.faces.event;

import javax.faces.component.FacesComponent;
import javax.faces.component.UIComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.component.UIViewRoot;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.component.html.HtmlPanelGrid;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.PreRenderViewEvent;
import javax.faces.event.PostRestoreStateEvent;
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;

@FacesComponent( value = "com.sun.faces.event.UIAddComponent1" )
public class UIAddComponent1
	extends UIComponentBase
	implements SystemEventListener {

	//
	// Constructor
	//

	public UIAddComponent1() {

		setRendererType( "testcomponent" );

		FacesContext context = FacesContext.getCurrentInstance();
		UIViewRoot root = context.getViewRoot();

		root.subscribeToViewEvent( PreRenderViewEvent.class, this );
		root.subscribeToViewEvent( PostRestoreStateEvent.class, this );
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
            HtmlPanelGrid component = new HtmlPanelGrid();
            component.setId("PANEL");
            component.setStyle( "border: 1px dashed blue; padding: 5px; margin: 5px" );
            getChildren().add( component );
		} else {
            // Get PanelGrid component
            HtmlPanelGrid component = (HtmlPanelGrid)getChildren().get(0);
            // If the child has not already been added - add it
             String added = (String)component.getAttributes().get("CHILD_ADDED");
            if (null == added) {
                HtmlOutputText output = new HtmlOutputText();
                output.setId("OUTPUT");
                output.setValue("NEW-OUTPUT");
                component.getChildren().add(output);
                component.getAttributes().put("CHILD_ADDED","CHILD_ADDED");
            }
        }
	}
}
