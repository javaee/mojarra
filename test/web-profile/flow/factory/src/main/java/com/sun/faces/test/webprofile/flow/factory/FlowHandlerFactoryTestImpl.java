package com.sun.faces.test.webprofile.flow.factory;

import javax.enterprise.context.ApplicationScoped;
import javax.faces.context.FacesContext;
import javax.faces.flow.FlowHandler;
import javax.faces.flow.FlowHandlerFactory;
import javax.faces.flow.FlowHandlerFactoryWrapper;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * A simple wrapped flow handler factory.
 */
@Named
@ApplicationScoped
public class FlowHandlerFactoryTestImpl extends FlowHandlerFactoryWrapper {

    public FlowHandlerFactoryTestImpl() {
    }
    
    private FlowHandlerFactory wrapped;
    
    @Inject
    private AppBean appBean;

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
        String id = (null != appBean) ? appBean.getId() : "null";
        FacesContext.getCurrentInstance().getExternalContext().getApplicationMap().put("appBean", id);
        return getWrapped().createFlowHandler(context);
    }
}
