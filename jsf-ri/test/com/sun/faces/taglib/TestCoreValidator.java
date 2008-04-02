package com.sun.faces.taglib;

import com.sun.faces.taglib.jsf_core.CoreValidator;
import org.xml.sax.helpers.DefaultHandler;


public class TestCoreValidator extends CoreValidator {

    public  DefaultHandler getSAXHandler() {
        return super.getSAXHandler();
    }
}
