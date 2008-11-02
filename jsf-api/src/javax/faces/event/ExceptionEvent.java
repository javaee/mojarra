/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
 * 
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License. You can obtain
 * a copy of the License at https://glassfish.dev.java.net/public/CDDL+GPL.html
 * or glassfish/bootstrap/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 * 
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at glassfish/bootstrap/legal/LICENSE.txt.
 * Sun designates this particular file as subject to the "Classpath" exception
 * as provided by Sun in the GPL Version 2 section of the License file that
 * accompanied this code.  If applicable, add the following below the License
 * Header, with the fields enclosed by brackets [] replaced by your own
 * identifying information: "Portions Copyrighted [year]
 * [name of copyright owner]"
 * 
 * Contributor(s):
 * 
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */

package javax.faces.event;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.faces.component.UIComponent;
import org.apache.taglibs.standard.tag.common.core.SetSupport;


/**
* Metadata passed to the exception handler
*/
public class ExceptionEvent extends SystemEvent {
    
    public ExceptionEvent(Exception e) {
        super(e);
    }
    
    public ExceptionEvent(Exception e, UIComponent component, PhaseId phaseId) {
        super(e);
        setComponent(component);
        setPhaseId(phaseId);
    }
    
    /**
     * The exception that was thrown
     */
    public Exception getException() {
        return (Exception) this.getSource();
    }

    public void setException(Exception exception) {
        this.source = exception;
    }


    private UIComponent component;
    
    /**
     * The UIComponent which was being processed when the exception was thrown. If none/undetermined, null
     */
    public UIComponent getComponent() {
        return this.component;
    }
    
    public void setComponent(UIComponent component) {
        this.component = component;
    }

    private PhaseId phaseId;

    /**
     * The phase in which the exception occured
     */
    public PhaseId getPhaseId() {
        return this.phaseId;
    }
    
    public void setPhaseId(PhaseId phaseId) {
        this.phaseId = phaseId;
    }


    private Map<Object, Object> attributes;
    
    /**
     * User defined meta-data about the exception handled
     */
 
    public Map<Object, Object> getAttributes() {
        if (null == attributes) {
            attributes = new HashMap<Object,Object>();
        }
        return attributes;
    }

}