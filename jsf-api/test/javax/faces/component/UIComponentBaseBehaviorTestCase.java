/*
 * $Id:
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

package javax.faces.component;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.faces.component.behavior.Behavior;
import javax.faces.component.behavior.BehaviorHolder;

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
	private static final Collection<String> EVENTS=set(ONTEST,ONCLICK,ONCHANGE);

	/**
	 * @author asmirnov
	 *
	 */
	public static class BehaviorComponent extends UIComponentBase implements BehaviorHolder {

		
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
	public static class TestBehavior extends Behavior implements Serializable {

		private static int sequence = 0;
		
		private final int id;
		
		public TestBehavior() {
			id=sequence++;
		}

		@Override
		public String getRendererType() {
			return TEST_FAMILY;
		}
		
		/* (non-Javadoc)
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + id;
			return result;
		}

		/* (non-Javadoc)
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
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
			return "Behavior #"+id;
		}
		
	}
	
	public UIComponentBaseBehaviorTestCase(String name) {
		super(name);
	}

	/* (non-Javadoc)
	 * @see javax.faces.component.UIComponentTestCase#setUp()
	 */
	public void setUp() {
		super.setUp();
	}

	/* (non-Javadoc)
	 * @see javax.faces.component.UIComponentTestCase#tearDown()
	 */
	public void tearDown() {
		super.tearDown();
	}

	/**
	 * Test method for {@link javax.faces.component.UIComponentBase#saveState(javax.faces.context.FacesContext)}.
	 */
	public void testSaveState() {
		BehaviorComponent comp = new BehaviorComponent();
		// Cast component to the interface, to be sure about method definition.
		BehaviorHolder holder = (BehaviorHolder) comp;
		TestBehavior behavior = new TestBehavior();
		holder.addBehavior(ONCLICK, behavior);
		TestBehavior behavior2 = new TestBehavior();
		holder.addBehavior(ONCLICK, behavior2);
		TestBehavior behavior3 = new TestBehavior();
		holder.addBehavior(ONCHANGE, behavior3);
		Object state = comp.saveState(facesContext);
		BehaviorComponent restoredComp = new BehaviorComponent();
		restoredComp.restoreState(facesContext, state);
		Map<String, List<Behavior>> behaviors = restoredComp.getBehaviors();
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

	public void testNonBehaviorHolder() throws Exception {
		UIInput input = new UIInput();
		try {
			input.addBehavior(ONTEST, new TestBehavior());
		} catch (IllegalStateException e) {
			return;
		}
		assertFalse(true);
	}

	/**
	 * Test method for {@link javax.faces.component.UIComponentBase#addBehavior(java.lang.String, javax.faces.component.behavior.Behavior)}.
	 */
	public void testAddBehavior() {
		BehaviorComponent comp = new BehaviorComponent();
		// Cast component to the interface, to be sure about method definition.
		BehaviorHolder holder = (BehaviorHolder) comp;
		holder.addBehavior(ONCLICK, new TestBehavior());
		holder.addBehavior(ONCLICK, new TestBehavior());
		holder.addBehavior(ONCHANGE, new TestBehavior());
		try {
			holder.addBehavior("foo", new TestBehavior());
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
		BehaviorHolder holder = (BehaviorHolder) comp;
		assertEquals(EVENTS, holder.getEventNames());
	}

	/**
	 * Test method for {@link javax.faces.component.UIComponentBase#getBehaviors()}.
	 */
	public void testGetBehaviors() {
		BehaviorComponent comp = new BehaviorComponent();
		// Cast component to the interface, to be sure about method definition.
		BehaviorHolder holder = (BehaviorHolder) comp;
		Map<String, List<Behavior>> behaviors = holder.getBehaviors();
		assertTrue(behaviors.isEmpty());
		assertFalse(behaviors.containsKey(ONCLICK));
		assertFalse(behaviors.containsValue(new TestBehavior()));
		assertEquals(0, behaviors.entrySet().size());
		holder.addBehavior(ONCLICK, new TestBehavior());
		holder.addBehavior(ONCLICK, new TestBehavior());
		holder.addBehavior(ONCHANGE, new TestBehavior());
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
		BehaviorHolder holder = (BehaviorHolder) comp;
		assertEquals(ONTEST, holder.getDefaultEventName());
	}

	/**
	 * Test method for {@link javax.faces.component.UIComponentBase#getEventsWhatAreSet()}.
	 */
	public void testGetEventsWhatAreSet() {
		BehaviorComponent comp = new BehaviorComponent();
		// Cast component to the interface, to be sure about method definition.
		BehaviorHolder holder = (BehaviorHolder) comp;
		holder.addBehavior(ONCLICK, new TestBehavior());
		holder.addBehavior(ONCLICK, new TestBehavior());
		holder.addBehavior(ONCHANGE, new TestBehavior());
		Collection<String> eventsWhatAreSet = comp.getEventsWhatAreSet();
		assertEquals(2, eventsWhatAreSet.size());
		assertTrue(eventsWhatAreSet.contains(ONCLICK));
		assertTrue(eventsWhatAreSet.contains(ONCHANGE));
	}
	
	public static <T> Set<T> set(T... ts) 
	  {
	    return new HashSet<T>(Arrays.asList(ts));
	  }


}
