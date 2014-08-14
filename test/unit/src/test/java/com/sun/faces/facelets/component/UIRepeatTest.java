package com.sun.faces.facelets.component;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;

import java.lang.reflect.Method;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;

import junit.framework.TestCase;

import org.easymock.EasyMock;
import org.easymock.IAnswer;
import org.junit.Test;

public class UIRepeatTest extends TestCase {

	private FacesContext jsf;

	private FacesMessage.Severity maximumSeverity = FacesMessage.SEVERITY_WARN;

	private Method uiRepeatHasErrorMessages;

	@Test
	public void testHasErrorMessages() throws Exception {
		jsf = EasyMock.createMock(FacesContext.class);
		expect(jsf.getMaximumSeverity()).andAnswer(new IAnswer<Severity>() {
			@Override
			public Severity answer() throws Throwable {
				return maximumSeverity;
			}
		}).anyTimes();
		replay(jsf);

		maximumSeverity = FacesMessage.SEVERITY_WARN;
		assertEquals(false, hasErrorMessages(jsf));
		maximumSeverity = FacesMessage.SEVERITY_INFO;
		assertEquals(false, hasErrorMessages(jsf));
		maximumSeverity = FacesMessage.SEVERITY_ERROR;
		assertEquals(true, hasErrorMessages(jsf));
		maximumSeverity = FacesMessage.SEVERITY_FATAL;
		assertEquals(true, hasErrorMessages(jsf));
	}

	private boolean hasErrorMessages(FacesContext context) throws Exception {
		if (uiRepeatHasErrorMessages == null) {
			Class<?> uiRepeatClass = Class.forName(UIRepeat.class.getName());
			uiRepeatHasErrorMessages = uiRepeatClass.getDeclaredMethod(
					"hasErrorMessages", new Class[] { FacesContext.class });
			uiRepeatHasErrorMessages.setAccessible(true);
		}
		return (Boolean)uiRepeatHasErrorMessages.invoke(new UIRepeat(),
				new Object[] { context });
	}
}
