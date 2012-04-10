package com.acme;

import com.sun.faces.flow.FlowCDIContext;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AfterBeanDiscovery;
import javax.enterprise.inject.spi.Extension;

public class FlowCDIExtension implements Extension {


   // Log instance for this class
   private static final Logger LOGGER = Logger.getLogger(FlowCDIExtension.class.getName());


   public FlowCDIExtension() {
       if (LOGGER.isLoggable(Level.FINE)) {
           LOGGER.fine("ctor for Flow CDI Extensions called");
       }

   }

   void afterBeanDiscovery(@Observes final AfterBeanDiscovery event) {
       event.addContext(new FlowCDIContext());
   }

}