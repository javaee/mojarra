/*
 * Copyright 2002, 2003 Sun Microsystems, Inc. All Rights Reserved.
 * 
 * Redistribution and use in source and binary forms, with or
 * without modification, are permitted provided that the following
 * conditions are met:
 * 
 * - Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 * 
 * - Redistribution in binary form must reproduce the above
 *   copyright notice, this list of conditions and the following
 *   disclaimer in the documentation and/or other materials
 *   provided with the distribution.
 *    
 * Neither the name of Sun Microsystems, Inc. or the names of
 * contributors may be used to endorse or promote products derived
 * from this software without specific prior written permission.
 *  
 * This software is provided "AS IS," without a warranty of any
 * kind. ALL EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND
 * WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE OR NON-INFRINGEMENT, ARE HEREBY
 * EXCLUDED. SUN AND ITS LICENSORS SHALL NOT BE LIABLE FOR ANY
 * DAMAGES OR LIABILITIES SUFFERED BY LICENSEE AS A RESULT OF OR
 * RELATING TO USE, MODIFICATION OR DISTRIBUTION OF THIS SOFTWARE OR
 * ITS DERIVATIVES. IN NO EVENT WILL SUN OR ITS LICENSORS BE LIABLE
 * FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT,
 * SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER
 * CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF
 * THE USE OF OR INABILITY TO USE THIS SOFTWARE, EVEN IF SUN HAS
 * BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 *  
 * You acknowledge that this software is not designed, licensed or
 * intended for use in the design, construction, operation or
 * maintenance of any nuclear facility.
 */

package com.sun.faces.systest.lifecycle;

import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;



/**
 *
 * <B>ReloadPhaseListener</B> is a class that looks for a Restore or Render
 * phase. If it finds that a phase has been entered other than a Restore or
 * Render, it sets a system property to false.
 *
 * This listener is used to determine whether a client refresh with no
 * request parameters or save state has occurred.
 *
 * @version $Id: ReloadPhaseListenerImpl.java,v 1.1 2003/09/19 23:54:05 horwat Exp $
 *
 */
public class ReloadPhaseListenerImpl implements PhaseListener {
    PhaseId phaseId = null;
    String pageRefresh;

    public ReloadPhaseListenerImpl(PhaseId newPhaseId) { 
        phaseId = newPhaseId;
        pageRefresh = "true";
    }

    public void afterPhase(PhaseEvent event) {
        System.setProperty("PageRefreshPhases", pageRefresh);
    }

    public void beforePhase(PhaseEvent event) {
        if (event.getPhaseId() == PhaseId.RESTORE_VIEW) {
            //reset System property to true when starting phase processing
            pageRefresh = "true";
            return;
        }
        else if (event.getPhaseId() == PhaseId.RENDER_RESPONSE) {
            //no other phases should be called
            return;
        }

        //phase other than Restore or Render is called
        pageRefresh = "false";
    }

    public PhaseId getPhaseId() {
        return phaseId;
    }

} // end of class ReloadPhaseListenerImpl

