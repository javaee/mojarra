/*
 * $Id: ConfigBase.java,v 1.8 2003/05/05 23:31:31 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.config;


import com.sun.faces.application.ApplicationImpl;
import com.sun.faces.util.Util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.faces.FacesException;
import javax.faces.FactoryFinder;
import javax.faces.application.ApplicationFactory;
import javax.faces.render.Renderer;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;


/**
 * <p>Base bean for parsing configuration information.</p>
 */
public class ConfigBase {


    // ---------------------------------------------------------- <application>


    private String actionListener = null;
    public String getActionListener() {
        return (this.actionListener);
    }
    public void setActionListener(String actionListener) {
        this.actionListener = actionListener;
    }

    private String navigationHandler = null;
    public String getNavigationHandler() {
        return (this.navigationHandler);
    }
    public void setNavigationHandler(String navigationHandler) {
        this.navigationHandler = navigationHandler;
    }

    private String propertyResolver = null;
    public String getPropertyResolver() {
        return (this.propertyResolver);
    }
    public void setPropertyResolver(String propertyResolver) {
        this.propertyResolver = propertyResolver;
    }

    private String variableResolver = null;
    public String getVariableResolver() {
        return (this.variableResolver);
    }
    public void setVariableResolver(String variableResolver) {
        this.variableResolver = variableResolver;
    }


    // ------------------------------------------------------------ <component>


    private Map components = null;
    public void addComponent(ConfigComponent component) {
        if (components == null) {
            components = new HashMap();
        }
        components.put(component.getComponentType(), component);
    }
    public Map getComponents() {
        if (components == null) {
            return (Collections.EMPTY_MAP);
        } else {
            return (this.components);
        }
    }


    // ------------------------------------------------------------ <converter>


    private Map converters = null;
    public void addConverter(ConfigConverter converter) {
        if (converters == null) {
            converters = new HashMap();
        }
        converters.put(converter.getConverterId(), converter);
    }
    public Map getConverters() {
        if (converters == null) {
            return (Collections.EMPTY_MAP);
        } else {
            return (this.converters);
        }
    }

    // ------------------------------------------------------------ <message-resources>

    private Map messageResources = null;
    public void addMessageResources(ConfigMessageResources newResource) {
        if (messageResources == null) {
            messageResources = new HashMap();
        }
        messageResources.put(newResource.getMessageResourcesId(), 
			     newResource);
    }
    public Map getMessageResources() {
        if (messageResources == null) {
            return (Collections.EMPTY_MAP);
        } else {
            return (this.messageResources);
        }
    }

    // ------------------------------------------------------------ <validator>


    private Map validators = null;
    public void addValidator(ConfigValidator validator) {
        if (validators == null) {
            validators = new HashMap();
        }
        validators.put(validator.getValidatorId(), validator);
    }
    public Map getValidators() {
        if (validators == null) {
            return (Collections.EMPTY_MAP);
        } else {
            return (this.validators);
        }
    }

    // ------------------------------------------------------------ <object manager>

    private Map managedBeans = null;
    public void addManagedBean(ConfigManagedBean managedBean) {
        if (managedBeans == null) {
            managedBeans = new HashMap();
        }
        managedBeans.put(managedBean.getManagedBeanId(), managedBean);

        ApplicationFactory aFactory = (ApplicationFactory)FactoryFinder.getFactory(
            FactoryFinder.APPLICATION_FACTORY);
        ApplicationImpl application = (ApplicationImpl)aFactory.getApplication();
        ManagedBeanFactory mbf = new ManagedBeanFactory(managedBean);
        application.getAppConfig().addManagedBeanFactory(managedBean.getManagedBeanId(), mbf);
    }
    public Map getManagedBeans() {
        if (managedBeans == null) {
            return (Collections.EMPTY_MAP);
        } else {
            return (this.managedBeans);
        }
    }    

    // ------------------------------------------------------------ <navigation>

    private String fromTreeId;
    public void setFromTreeId(String fromTreeId) {
        this.fromTreeId = fromTreeId;
    }
    public String getFromTreeId() {
        return fromTreeId;
    }

    private List navigationCases = null;
    public void addNavigationCase(ConfigNavigationCase navigationCase) {
        if (navigationCases == null) {
            navigationCases = new ArrayList();
        }
        navigationCase.setFromTreeId(getFromTreeId());
        navigationCases.add(navigationCase);
    }
    public List getNavigationCases() {
        if (navigationCases == null) {
            return (Collections.EMPTY_LIST);
        } else {
            return (this.navigationCases);
        }
    }

    // ------------------------------------------------------------ <render-kit>


    private Map renderKits = null;
    public void addRenderKit(ConfigRenderKit renderKit) {
        if (renderKits == null) {
            renderKits = new HashMap();
        }
        renderKits.put(renderKit.getRenderKitId(), renderKit);
    }
    public Map getRenderKits() {
        if (renderKits == null) {
            return (Collections.EMPTY_MAP);
        } else {
            return (this.renderKits);
        }
    }

    public void updateRenderKits() {
        if (renderKits == null) {
            return;
        }
        RenderKitFactory renderKitFactory = (RenderKitFactory)
            FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
        Iterator renderKitIds = renderKits.keySet().iterator();
        while (renderKitIds.hasNext()) {
            String renderKitId = (String) renderKitIds.next();
            ConfigRenderKit configRenderKit = (ConfigRenderKit)
                renderKits.get(renderKitId);
            RenderKit renderKit =
                renderKitFactory.getRenderKit(renderKitId);
            // PENDING(craigmcc) -- Looks like this wil never return null,
            // but it also looks like we still try to load the old
            // RenderKitConfig.xml file???
            Map renderersMap = configRenderKit.getRenderers();
            Iterator rendererIds = renderersMap.keySet().iterator();
            while (rendererIds.hasNext()) {
                String rendererId = (String) rendererIds.next();
                ConfigRenderer configRenderer = (ConfigRenderer)
                    renderersMap.get(rendererId);
                String rendererClass = configRenderer.getRendererClass();
                try {
                    Class rendererClazz =
                        Util.loadClass(rendererClass, this);
                    Renderer renderer = (Renderer)
                        rendererClazz.newInstance();
                    renderKit.addRenderer(rendererId, renderer);
                } catch (Exception e) {
                    throw new FacesException(e);
                }
            }
        }
    }

}
