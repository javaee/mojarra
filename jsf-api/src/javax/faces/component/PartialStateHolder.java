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


package javax.faces.component;

import java.io.Serializable;

/**This interface is implemented
 * by StateHolders which can save
 * a partial state between requests.
 *
 * This interface extends from
 * StateHolder as implementations
 * are expected to also provide
 * implementations of restoreState/saveState.
 * However, these implementations should
 * return a partial state only if applicable.
 *
 * The partial state is the state which
 * has been changed after notifyStoreState
 * has been called. It will be returned only
 * if notifyStoreState has been called - if it
 * has not been called, the full state will be
 * returned.
 *
 * For components created from a PDL-template, notifyStoreState
 * should be called, for dynamically
 * created components (in Java), notifyStoreState
 * should not be called.
 *
 * RELEASE_PENDING (edburns,rogerk) docs
 */
public interface PartialStateHolder extends StateHolder {


    /**Notify this state-holder that it
     * a) should start collecting delta-state
     * b) should return the delta-state only from now own if
     * save-state gets called.
     *
     */
    void markInitialState();

    /**
     * @return <code>true</code> if delta state changes are being tracked,
     *  otherwise <code>false</code>
     */
    boolean initialStateMarked();


    /**
     * Reset the PartialStateHolder to a non-delta tracking state.
     */
    void clearInitialState();

}
