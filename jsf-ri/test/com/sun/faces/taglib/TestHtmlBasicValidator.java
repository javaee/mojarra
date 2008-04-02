package com.sun.faces.taglib;

import com.sun.faces.taglib.html_basic.HtmlBasicValidator;
import org.xml.sax.helpers.DefaultHandler;


public class TestHtmlBasicValidator extends HtmlBasicValidator {

    public  DefaultHandler getSAXHandler() {
        return super.getSAXHandler();
    }
}
