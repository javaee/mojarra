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

package javax.faces.component;

import javax.faces.component.behavior.ClientBehavior;
import javax.faces.component.behavior.ClientBehaviorContext;
import javax.faces.component.behavior.ClientBehaviorHint;
import javax.faces.component.behavior.ClientBehaviorHolder;
import javax.faces.context.FacesContext;
import javax.faces.event.BehaviorEvent;
import java.io.Serializable;
import java.util.*;

/**
 * <p class="changed_added_2_0">
 * Test case for component behaviors.
 * </p>
 *
 * @since 2.0
 */
public class UIComponentBaseBehaviorTestCase extends UIComponentTestCase {

    private static final String ONTEST = "ontest";
    private static final String ONCLICK = "onclick";
    private static final String ONCHANGE = "onchange";
    private static final String TEST_FAMILY = "javax.faces.Test";
    private static final Collection<String> EVENTS = set(ONTEST, ONCLICK, ONCHANGE);

    /**
     * @author asmirnov
     */
    public static class BehaviorComponent extends UIComponentBase implements ClientBehaviorHolder {


        /* (non-Javadoc)
           * @see javax.faces.component.UIComponent#getFamily()
           */
        @Override
        public String getFamily() {
            return TEST_FAMILY;
        }

        @Override
        public Collection<String> getEventNames() {
            return EVENTS;
        }

        @Override
        public String getDefaultEventName() {
            return ONTEST;
        }

    }

    @SuppressWarnings("serial")
    public static class TestBehavior implements ClientBehavior, Serializable {

        private static final Set<ClientBehaviorHint> HINTS =
                Collections.unmodifiableSet(EnumSet.of(ClientBehaviorHint.SUBMITTING));

        private static int sequence = 0;

        private final int id;

        public TestBehavior() {
            id = sequence++;
        }

        public String getRendererType() {
            return TEST_FAMILY;
        }

        public Set<ClientBehaviorHint> getHints() {
            return HINTS;
        }

        public void broadcast(BehaviorEvent event) {
        }

        public void decode(FacesContext context, UIComponent component) {
        }

        public String getScript(ClientBehaviorContext bContext) {
            return null;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + id;
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            TestBehavior other = (TestBehavior) obj;
            if (id != other.id)
                return false;
            return true;
        }

        @Override
        public String toString() {
            return "Behavior #" + id;
        }

    }

    public UIComponentBaseBehaviorTestCase(String name) {
        super(name);
    }

    /**
     * Test method for {@link javax.faces.component.UIComponentBase#saveState(javax.faces.context.FacesContext)}.
     */
    public void testSaveState() {
        BehaviorComponent comp = new BehaviorComponent();
        // Cast component to the interface, to be sure about method definition.
        ClientBehaviorHolder holder = (ClientBehaviorHolder) comp;
        TestBehavior behavior = new TestBehavior();
        holder.addClientBehavior(ONCLICK, behavior);
        TestBehavior behavior2 = new TestBehavior();
        holder.addClientBehavior(ONCLICK, behavior2);
        TestBehavior behavior3 = new TestBehavior();
        holder.addClientBehavior(ONCHANGE, behavior3);
        Object state = comp.saveState(facesContext);
        BehaviorComponent restoredComp = new BehaviorComponent();
        restoredComp.restoreState(facesContext, state);
        Map<String, List<ClientBehavior>> behaviors = restoredComp.getClientBehaviors();
        assertFalse(behaviors.isEmpty());
        assertTrue(behaviors.containsKey(ONCLICK));
        assertTrue(behaviors.containsKey(ONCHANGE));
        assertFalse(behaviors.containsKey(ONTEST));
        assertEquals(2, behaviors.entrySet().size());
        assertEquals(2, behaviors.keySet().size());
        assertEquals(2, behaviors.values().size());
        assertEquals(2, behaviors.get(ONCLICK).size());
        assertEquals(1, behaviors.get(ONCHANGE).size());
        assertEquals(behavior3, behaviors.get(ONCHANGE).get(0));
        assertEquals(behavior, behaviors.get(ONCLICK).get(0));
        assertEquals(behavior2, behaviors.get(ONCLICK).get(1));

    }

    public void testNonClientBehaviorHolder() throws Exception {
        UIInput input = new UIInput();
        try {
            input.addClientBehavior(ONTEST, new TestBehavior());
        } catch (IllegalStateException e) {
            return;
        }
        assertFalse(true);
    }

    /**
     * Test method for {@link javax.faces.component.UIComponentBase#addClientBehavior(java.lang.String, javax.faces.component.behavior.Behavior)}.
     */
    public void testAddBehavior() {
        BehaviorComponent comp = new BehaviorComponent();
        // Cast component to the interface, to be sure about method definition.
        ClientBehaviorHolder holder = (ClientBehaviorHolder) comp;
        holder.addClientBehavior(ONCLICK, new TestBehavior());
        holder.addClientBehavior(ONCLICK, new TestBehavior());
        holder.addClientBehavior(ONCHANGE, new TestBehavior());
        try {
            holder.addClientBehavior("foo", new TestBehavior());
        } catch (IllegalArgumentException e) {
            return;
        }
        assertFalse(true);
    }

    /**
     * Test method for {@link javax.faces.component.UIComponentBase#getEventNames()}.
     */
    public void testGetEventNames() {
        BehaviorComponent comp = new BehaviorComponent();
        ClientBehaviorHolder holder = (ClientBehaviorHolder) comp;
        assertEquals(EVENTS, holder.getEventNames());
    }

    /**
     * Test method for {@link javax.faces.component.UIComponentBase#getClientBehaviors()}.
     */
    public void testGetBehaviors() {
        BehaviorComponent comp = new BehaviorComponent();
        // Cast component to the interface, to be sure about method definition.
        ClientBehaviorHolder holder = (ClientBehaviorHolder) comp;
        Map<String, List<ClientBehavior>> behaviors = holder.getClientBehaviors();
        assertTrue(behaviors.isEmpty());
        assertFalse(behaviors.containsKey(ONCLICK));
        assertFalse(behaviors.containsValue(new TestBehavior()));
        assertEquals(0, behaviors.entrySet().size());
        holder.addClientBehavior(ONCLICK, new TestBehavior());
        holder.addClientBehavior(ONCLICK, new TestBehavior());
        holder.addClientBehavior(ONCHANGE, new TestBehavior());
        assertFalse(behaviors.isEmpty());
        assertTrue(behaviors.containsKey(ONCLICK));
        assertTrue(behaviors.containsKey(ONCHANGE));
        assertFalse(behaviors.containsKey(ONTEST));
        assertEquals(2, behaviors.entrySet().size());
        assertEquals(2, behaviors.keySet().size());
        assertEquals(2, behaviors.values().size());
        assertEquals(2, behaviors.get(ONCLICK).size());
        assertEquals(1, behaviors.get(ONCHANGE).size());
    }

    /**
     * Test method for {@link javax.faces.component.UIComponentBase#getDefaultEventName()}.
     */
    public void testGetDefaultEventName() {
        BehaviorComponent comp = new BehaviorComponent();
        // Cast component to the interface, to be sure about method definition.
        ClientBehaviorHolder holder = (ClientBehaviorHolder) comp;
        assertEquals(ONTEST, holder.getDefaultEventName());
    }

    public static <T> Set<T> set(T... ts) {
        return new HashSet<T>(Arrays.asList(ts));
    }


}
