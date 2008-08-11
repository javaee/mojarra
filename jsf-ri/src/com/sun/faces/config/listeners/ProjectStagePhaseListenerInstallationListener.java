package com.sun.faces.config.listeners;

import java.util.Iterator;

import javax.faces.event.SystemEvent;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.SystemEventListener;
import javax.faces.event.PhaseListener;
import javax.faces.event.ConfigurationCompleteEvent;
import javax.faces.application.Application;
import javax.faces.application.ProjectStage;
import javax.faces.lifecycle.LifecycleFactory;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.FactoryFinder;

import com.sun.faces.lifecycle.ProjectStagePhaseListener;

/**
 * This {@link SystemEventListener} handles the installation of the
 * {@link ProjectStagePhaseListener}.  The PhaseListener will only be installed
 * if the current {@link ProjectStage} is {@link ProjectStage#Development}.
 */
public class ProjectStagePhaseListenerInstallationListener
      implements SystemEventListener {


    // ---------------------------------------- Methods from SystemEventListener


    public void processEvent(SystemEvent event)
          throws AbortProcessingException {

        PhaseListener pl = new ProjectStagePhaseListener();
        LifecycleFactory factory = (LifecycleFactory)
              FactoryFinder.getFactory(FactoryFinder.LIFECYCLE_FACTORY);
        for (Iterator i = factory.getLifecycleIds(); i.hasNext();) {
            Lifecycle l = factory.getLifecycle((String) i.next());
            l.addPhaseListener(pl);
        }

        // uninstall the listener - no need to be included during usual
        // runtime processing.
        Application application = (Application) event.getSource();
        application.unsubscribeFromEvent(ConfigurationCompleteEvent.class,
                                         this);        

    }


    public boolean isListenerForSource(Object source) {

        if (source instanceof Application) {
            Application application = (Application) source;
            return (application.getProjectStage() == ProjectStage.Development);
        }
        return false;

    }

}
