/*
 * $Id: ClientBehavior.java,v 1.0 2009/01/03 18:51:29 rogerk Exp $
 */

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

package javax.faces.component.behavior;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

/**
 * <p class="changed_added_2_0">Contract for objects that add
 * client-side behaviors to UIComponents. Instances of 
 * <code>ClientBehavior</code> may be attached to components 
 * that implement the {@link ClientBehaviorHolder} contract by 
 * calling {@code ClientBehaviorHolder.addClientBehavior()}.  
 * Once a <code>ClientBehavior</code> has been attached to a 
 * ClientStateHolder component, the component
 * calls {@link #getScript} to obtain the behavior's script and wire 
 * this up to the appropriate client-side event handler.
 * </p>
 *
 * @since 2.0
 */
public abstract class ClientBehavior {

    /**
     * <p class="changed_added_2_0">Returns the script that implements this
     * ClientBehavior's client-side logic.</p>
     *
     * @param context the {@link FacesContext} for the current request
     * @param component the component instance that generates event.
     * @param eventName name of the client-side event.  If this argument is
     * <code>null</code> it is assumed the caller will include the 
     * client-side event name with the return value from this method.
     * @return script that provides the client-side behavior
     */      
    public abstract String getScript(FacesContext context,
                                     UIComponent component,
                                     String eventName);
}
