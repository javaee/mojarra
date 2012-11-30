package com.sun.faces.test.webprofile.flow.factory;

import javax.faces.context.FacesContext;
import javax.faces.flow.FlowHandler;
import javax.faces.flow.FlowHandlerFactory;
import javax.faces.flow.FlowHandlerFactoryWrapper;

/**
 * A simple wrapped flow handler factory.
 */
public class FlowHandlerFactoryTestImpl extends FlowHandlerFactoryWrapper {
    
    private FlowHandlerFactory wrapped;

    /**
     * Constructor.
     *
     * @param wrapped the wrapped flow handler factory.
     */
    public FlowHandlerFactoryTestImpl(FlowHandlerFactory wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public FlowHandlerFactory getWrapped() {
        return this.wrapped;
    }

    /**
     * Add a message to the context every time the createFlowHandler method is
     * called so we can verify later that the factory is actually being used.
     *
     * @param context the Faces context.
     * @return the flow handler.
     */
    @Override
    public FlowHandler createFlowHandler(FacesContext context) {
        System.out.println("createFlowHandler");
        FacesContext.getCurrentInstance().getExternalContext().getApplicationMap().put("flowHandlerFactoryWrapped", true);
        return getWrapped().createFlowHandler(context);
    }
}
