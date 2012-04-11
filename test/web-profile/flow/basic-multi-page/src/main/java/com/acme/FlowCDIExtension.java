package com.acme;

import com.sun.faces.flow.FlowCDIContext;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.spi.Contextual;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AfterBeanDiscovery;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessBean;
import javax.faces.flow.FlowScoped;

public class FlowCDIExtension implements Extension {


   // Log instance for this class
   private static final Logger LOGGER = Logger.getLogger(FlowCDIExtension.class.getName());
   private Map<Contextual<?>, String> flowIds;


   public FlowCDIExtension() {
       if (LOGGER.isLoggable(Level.FINE)) {
           LOGGER.fine("ctor for Flow CDI Extensions called");
       }
       flowIds = new ConcurrentHashMap<Contextual<?>, String>();

   }
   
   public void processBean(@Observes ProcessBean<?> event) {
       FlowScoped flowScoped = event.getAnnotated().getAnnotation(FlowScoped.class);
       if (null != flowScoped) {
           flowIds.put(event.getBean(), flowScoped.id());
       }
       
   }

   void afterBeanDiscovery(@Observes final AfterBeanDiscovery event) {
       event.addContext(new FlowCDIContext(flowIds));
   }

}