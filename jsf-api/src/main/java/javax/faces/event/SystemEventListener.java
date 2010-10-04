/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2010 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
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


/**
 * <p class="changed_added_2_0">By implementing this class, an object
 * indicates that it is a listener for one or more kinds of {@link
 * SystemEvent}s.  The exact type of event that will cause the
 * implementing class's {@link #processEvent} method to be called is
 * indicated by the <code>facesEventClass</code> argument passed when
 * the listener is installed using {@link
 * javax.faces.application.Application#subscribeToEvent}.</p>
 *
 * @since 2.0
 */
public interface SystemEventListener extends FacesListener {


    /**
     * <p>When called, the listener can assume that any guarantees given
     * in the javadoc for the specific {@link SystemEvent}
     * subclass are true.</p>
     *
     * @param event the <code>SystemEvent</code> instance that
     * is being processed.
     *
     * @throws AbortProcessingException if lifecycle processing should
     * cease for this request.
     */
    public void processEvent(SystemEvent event) throws AbortProcessingException;

    /**
     * <p>This method must return <code>true</code> if and only if this
     * listener instance is interested in receiving events from the
     * instance referenced by the <code>source</code> parameter.</p>
     *
     * @param source the source that is inquiring about the
     * appropriateness of sending an event to this listener instance.  
     */ 
    public boolean isListenerForSource(Object source);

}
